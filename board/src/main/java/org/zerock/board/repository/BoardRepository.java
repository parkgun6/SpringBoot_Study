package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board,Long> {

//    //myBatis는 #{}을 쓰고 JPQL은 :를 쓴다.
//    @Query("select b, count(r) from Board b left join Reply r on r.board = b " +
//            "group by b")
//    Page<Object[]> getBoardWithReplyCount(Pageable pageable);



    //오라클DB도 동일한 쿼리를 사용한다.
    @Query(value = "select b, w, count(r) from Board b " +
            "inner join b.writer w " +
            "left join Reply r on r.board = b " +
            "group by b"
            ,
            countQuery = "select count(b) from Board b")
    //count의 성능을 높여주기 위해서 countQuery를 따로 만들어준다.
    //Pageable이기 때문에 Page타입으로 리턴한다. 파라미터는 Object[]을 준다.
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);
}
