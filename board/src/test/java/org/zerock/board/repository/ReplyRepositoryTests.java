package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReplies() {

        IntStream.rangeClosed(1,300).forEach(i -> {
            //인덱스 번호 1~100
            long idx = (long)(Math.random() * 100 ) + 1;

            Board board = Board.builder().bno(idx).build();

            Reply reply = Reply.builder()
                    .replyer("replyer" + (i % 10))
                    .text("댓글..." + idx)
                    .board(board)
                    .build();

            replyRepository.save(reply);
        });

    }

}
