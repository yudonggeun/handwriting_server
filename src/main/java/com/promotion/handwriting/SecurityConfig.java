package com.promotion.handwriting;

import com.promotion.handwriting.security.JwtFilter;
import com.promotion.handwriting.security.JwtService;
import com.promotion.handwriting.security.oauth.CustomAuthenticationSuccessHandler;
import com.promotion.handwriting.service.business.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class  SecurityConfig implements WebSecurityCustomizer {

    @Override
    public void customize(WebSecurity web) {
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtService jwtService,
                                           AuthenticationService authorizationService,
                                           CustomAuthenticationSuccessHandler successHandler
                                           ) throws Exception {
        return http.csrf().disable()
                .addFilterBefore(new JwtFilter(jwtService, authorizationService), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeRequests()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/admin/**").permitAll()
                .antMatchers("/data/**").access("request.method == 'GET' ? permitAll : hasRole('OWNER')")
                .and()

                .logout()
                .logoutUrl("/admin/logout")
                .and()

                .oauth2Login()
                .defaultSuccessUrl("/processing_login.html")
                .successHandler(successHandler)
                .failureUrl("/")
                .userInfoEndpoint()
                .userService(authorizationService)
                .and()
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
