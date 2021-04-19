package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoards() {

        //random user idx 생성
        IntStream.rangeClosed(1,100).forEach(i -> {

            int idx = (int)(Math.random() * 100) + 1;

            Member member = Member.builder()
                    .email("user" + idx + "@aaa.com")
                    .build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer(member)
                    .build();

            boardRepository.save(board);
        });
    }

    @Test
    public void testRead() {

        Optional<Board> result = boardRepository.findById(100L);

        //Board와 Member가 leftjoin을 자동으로 했다.
        //join이 일어나면 안된다.
        log.info(result);

        //isPresent = 존재여부
        if(result.isPresent()){
            Board board = result.get();
            log.info("-------------------------------------");
            log.info(board);
        }

    }


}
