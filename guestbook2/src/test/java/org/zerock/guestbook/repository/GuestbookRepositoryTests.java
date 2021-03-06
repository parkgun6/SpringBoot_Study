package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Arrays;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,300).asLongStream().forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer("user" + (i % 10))
                    .build();
            log.info(guestbookRepository.save(guestbook));
        });

    }

    @Test
    public void testUpdate(){

        Guestbook guestbook = guestbookRepository.findById(300L).get();

        log.info("BEFORE..............");
        log.info(guestbook);

        guestbook.changeTitle("Update 300 Title........");
        guestbook.changeContent("Update 300 Content..........");

        log.info("After..................");
        log.info(guestbook);

        guestbookRepository.save(guestbook);

    }

    @Test
    public void testQuery1(){

        //동적쿼리를 만드는 메서드

        Pageable pageable =
                PageRequest.of(0,10, Sort.by("gno").descending());

        //QDomain을 사용한다.
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //title like ?
        BooleanExpression expression =
                qGuestbook.title.contains(keyword);

        booleanBuilder.and(expression);

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.get().forEach(guestbook -> log.info(guestbook));
    }


    //동적쿼리(검색) ~150p
    @Test
    public void testSearch() {

        Pageable pageable =
                PageRequest.of(0,10, Sort.by("gno").descending());

        String keyword = "1";

        String[] arr = null;//{"t","c"};

        //Qdomian 필드값으로 사용
        QGuestbook qGuestbook = QGuestbook.guestbook;

        //total
        BooleanBuilder total = new BooleanBuilder();

        //gno
        BooleanBuilder gno = new BooleanBuilder();

        //condition
        BooleanBuilder condition = new BooleanBuilder();

        if(arr != null && arr.length > 0) {
            Arrays.stream(arr).forEach(type -> {
                log.info(type);

                switch (type) {
                    case "t":
                        condition.or(qGuestbook.title.contains(keyword));
                        break;
                    case "c":
                        condition.or(qGuestbook.content.contains(keyword));
                        break;
                    case "w":
                        condition.or(qGuestbook.writer.contains(keyword));
                        break;
                }//end switch
            });//end loop
        }//end if

        //total에 condition 포함
        total.and(condition);

        //gt(greater than) = '>'
        gno.and(qGuestbook.gno.gt(0L));

        //total에 gno 포함
        total.and(gno);

        guestbookRepository.findAll(total,pageable);

    }
}
