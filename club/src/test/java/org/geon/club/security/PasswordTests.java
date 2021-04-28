package org.geon.club.security;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class PasswordTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {

        String password = "1111";
        //$2a$10$8u40GAoOik6OvAUDZack9uzImuk2IAL7GODXYsByEK9Hfd9KuIKWe
        String enPw = passwordEncoder.encode(password);

        log.info("enPw: " + enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);

        log.info("matchResult: " + matchResult);
    }
}
