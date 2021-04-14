package org.zerock.b3.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b3.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemoRepositoryTests {

    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void test1(){
        log.info(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect(){
        Long mno = 100L;

        //Optional로 반환
        Optional<Memo> result = memoRepository.findById(mno);
        log.info("----------------------------");

        //Null체크를 해준다.
        if(result.isPresent()){
            log.info(result.get());
        }
    }

    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);
        log.info("----------------------------");

        log.info(memo);
    }

    @Test
    public void testUpdate(){

        Optional<Memo> result = memoRepository.findById(100L);

        Memo memo = result.get();
        //시간문제 등 의 여러문제에서 사용용
       memo.changeText("Update.........100");

        memoRepository.save(memo);
    }

    @Test
    public void testPageDefault(){

        //정렬 DESC
        Sort sort1 = Sort.by("mno").descending();

        //페이지는 0페이지부터 시작한다 (주의)
        //사이즈가 데이터보다 높을시에 카운트 쿼리를 날리지않는다.
        //정렬을 위해 sort1을 추가했다.
        Pageable pageable = PageRequest.of(0,10,sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        log.info(result);

        result.getContent().forEach(memo -> log.info(memo));

        Pageable prev = result.previousPageable();

        log.info("prev : " + prev);
    }

    @Test
    public void testQuery1(){

        List<Memo> list = memoRepository.findByMemoTextContaining("1");

        list.forEach(m -> log.info(m));
    }

    @Test
    public void testQuery2(){

        //페이지는 0부터 시작한다.
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMemoTextContaining("1",pageable);

        result.get().forEach(m -> log.info(m));
    }

    @Test
    public void testQuery3(){

        memoRepository.getList1("1").forEach(m -> log.info(m));
    }
}
