package org.geon.club.repository;

import lombok.extern.log4j.Log4j2;
import org.geon.club.entity.ClubMember;
import org.geon.club.entity.ClubMemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ClubMemberRepositoryTests {

    @Autowired
    ClubMemberRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {

            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@geon.com")
                    .name("사용자" + i)
                    .password(passwordEncoder.encode("1234"))
                    .fromSocial(false)
                    .build();

            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i > 80){
                clubMember.addMemberRole(ClubMemberRole.MEMBER);
            }

            if(i > 90){
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            repository.save(clubMember);
        });
    }
}
