package org.geon.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    {
        log.info("security config");
    }

    //스프링부트는 기본적으로 패스워드 인코딩을 해야지만 인증이 가능하다.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user00")
                .password("$2a$10$8u40GAoOik6OvAUDZack9uzImuk2IAL7GODXYsByEK9Hfd9KuIKWe")
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //기본 설정은 모든 url을 시큐리티 처리한다.
        //super.configure(http);

        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();
        http.csrf().disable();
        http.logout();

    }
}
