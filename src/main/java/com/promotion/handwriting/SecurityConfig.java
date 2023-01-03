package com.promotion.handwriting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig implements WebSecurityCustomizer {
    @Override
    public void customize(WebSecurity web) {

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/admin/**").permitAll()
                .antMatchers("/data/**").access("request.method == 'GET' ? permitAll : hasAnyRole('MEMBER', 'ADMIN')")
                .and().build();
    }

}
