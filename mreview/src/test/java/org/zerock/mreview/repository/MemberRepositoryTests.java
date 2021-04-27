package org.zerock.mreview.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.entity.Member;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMember(){

        IntStream.rangeClosed(1,100).forEach(i -> {

            Member member = Member.builder()
                    .email("user" + i + "@aaa.com")
                    .pw("1111")
                    .nickname("reviewer" + i)
                    .build();

            memberRepository.save(member);
        });
    }

    @Test
    @Transactional
    @Commit
    public void testDelete(){

        Long mid = 1L;
        Member member = Member.builder().mid(mid).build();

        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);
    }
}
