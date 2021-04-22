package org.zerock.board.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        Board board = dtoTOEntity(dto);

        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("bno").descending());

        //Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
                );

        //Function< T : 입력값, R : 리턴값 >
        Function<Object[], BoardDTO> fn = (arr -> entityToDTO(
                (Board)arr[0],
                (Member)arr[1],
                (Long)arr[2])
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = boardRepository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board)arr[0],(Member)arr[1],(Long)arr[2]);
    }

    @Transactional
    @Override
    public void modify(BoardDTO dto) {

        //getOne은 해당하는 Entity가 없을 때 먼저 예외를 던지기 때문에 if문이 필요없다.
        Board board = boardRepository.getOne(dto.getBno());

        board.changeTitle(dto.getTitle());
        board.changeContent(dto.getContent());

        boardRepository.save(board);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        //댓글 삭제 후 게시글 삭제(트랜잭션 처리)
        replyRepository.deleteByBno(bno);

        boardRepository.deleteById(bno);
    }


}
