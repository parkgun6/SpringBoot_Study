package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
        implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {

        log.warn("search1..............");

        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        //select b from board b
        JPQLQuery<Board> jpqlQuery = from(board);
        //board에 member, reply join
        //jpqlQuery.leftJoin(board.writer);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //where(Predicate...) = BooleanExpression
        BooleanExpression ex1 = board.bno.eq(100L);
        //select()의 파라미터 값은 DB마다 호환성이 다르다.
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count()).where(ex1).groupBy(board);

        log.warn("------------------------------");
        log.warn(jpqlQuery);
        log.warn("------------------------------");

        //다 만들어놓고 tuple에서 fetch를 한다.
        List<Tuple> result = tuple.fetch();
        log.warn("------------------------------");
        log.warn(result);
        //join을 많이해서 count날리는건 성능저하를 불러온다.
        Long count = jpqlQuery.fetchCount();

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.warn("------------------------------");

        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        //select b from board b
        JPQLQuery<Board> jpqlQuery = from(board);
        //board에 member, reply join
        //jpqlQuery.leftJoin(board.writer);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression booleanExpression = board.bno.gt(0L);
        //and bno > 0 (pk scan)
        booleanBuilder.and(booleanExpression);
        //where ( board.title like xxx OR member.name like xxx OR reply.text like xxx )

        if(type != null){
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            String[] typeArr = type.split("");

            for (String t:typeArr) {

                switch (t){
                    case "t" :
                        //like를 쓰려면 %를 붙여줘야한다.
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w" :
                        conditionBuilder.or(member.name.contains(keyword));
                        break;
                    case "c" :
                        conditionBuilder.or(reply.text.contains(keyword));
                        break;
                }//end switch
            }//end for
            booleanBuilder.and(conditionBuilder);
        }//end if

        //------------------------Order by---------------------------
        Sort sort = pageable.getSort();

        //stream을 이용해서 여러조건을 붙여준다.
        sort.stream().forEach(order -> {
            //bno
            String prop = order.getProperty();
            log.warn("sort prop : " + prop);

            Order direction = order.isAscending()? Order.ASC : Order.DESC;

            PathBuilder<Board> orderByExpression =
                    new PathBuilder<Board>(Board.class, "board");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });//end forEach

        tuple.where(booleanBuilder);
        tuple.groupBy(board);
        //페이징처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());
        
        log.warn(tuple);

        List<Tuple> result = tuple.fetch();

        log.warn(result);

        Long count = tuple.fetchCount();

        //메서드의 리턴타입이 Page이기 때문에 Page타입으로 변환
        List<Object[]> resultList =
                result.stream().map(tuple1 -> tuple1.toArray()).collect(Collectors.toList());

        return new PageImpl<Object[]>(resultList, pageable, count);
    }
}
