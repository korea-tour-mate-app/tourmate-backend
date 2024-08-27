package com.snowflake_guide.tourmate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll() // Swagger UI와 API 문서에 대한 접근 허용
                                .requestMatchers("/api/auth/signup","/api/auth/login").permitAll() // /api/auth/signup URL에 대한 접근 허용
                                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
                )
                .csrf(csrf -> csrf.disable()); // CSRF 보호 비활성화

        return http.build();
    }
}
