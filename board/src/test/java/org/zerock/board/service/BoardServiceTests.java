package org.zerock.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

import javax.transaction.Transactional;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    BoardService service;

    @Test
    public void testRegister(){

        BoardDTO dto = BoardDTO.builder()
                .title("테스트...")
                .content("Test...")
                .writerEmail("user100@aaa.com")
                .build();

        Long bno = service.register(dto);

        log.info("BNO : " + bno);
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = service.getList(pageRequestDTO);

        result.getDtoList().forEach(boardDTO -> log.info(boardDTO));
    }

    @Test
    public void testGet(){

        log.info(service.get(100L));
    }

    @Commit
    @Transactional
    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(100L)
                .title("Update100수정.....")
                .content("Content100수정.....")
                .build();

        service.modify(boardDTO);
    }

    @Test
    public void testDelete(){

        service.removeWithReplies(97L);

    }

}
