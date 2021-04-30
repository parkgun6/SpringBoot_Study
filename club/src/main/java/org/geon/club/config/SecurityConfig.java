package org.geon.club.config;

import lombok.extern.log4j.Log4j2;
import org.geon.club.security.filter.ApiCheckFilter;
import org.geon.club.security.filter.ApiLoginFilter;
import org.geon.club.security.handler.ApiLoginFailHandler;
import org.geon.club.security.handler.ClubLoginSuccessHandler;
import org.geon.club.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    //ClubUserDetailsService를 스프링부트가 별도의 설정없이 자동으로 인식한다.
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user00")
//                .password("$2a$10$8u40GAoOik6OvAUDZack9uzImuk2IAL7GODXYsByEK9Hfd9KuIKWe")
//                .roles("USER");
//    }

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
        //구글 소셜로그인
        http.oauth2Login().successHandler(successHandler());

        http.addFilterBefore(apiCheckFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiLoginFilter apiLoginFilter()throws Exception{

        ApiLoginFilter apiLoginFilter =
                new ApiLoginFilter("/api/login",jwtUtil());

        apiLoginFilter.setAuthenticationManager(authenticationManager());

        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/notes/**/*",jwtUtil());
    }

    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

}
