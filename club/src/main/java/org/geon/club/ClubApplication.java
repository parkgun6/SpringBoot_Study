package org.geon.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//BaseEntity 사용
@EnableJpaAuditing
public class ClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubApplication.class, args);
    }

}
