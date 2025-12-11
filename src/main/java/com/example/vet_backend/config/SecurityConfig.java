package com.example.vet_backend.config;

import com.example.vet_backend.filter.JwtAuthenticationFilter;
import com.example.vet_backend.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. 공개 경로 (로그인 없이 접근 가능)
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/users/register",
                                "/api/users/login",
                                "/api/users/doctors",
                                "/api/community/**",
                                "/api/reviews/**",
                                "/api/consult/**",
                                "/api/prescription/**",
                                "/api/appointments/**"
                        ).permitAll()

                        // 2. 사용자 전용 경로
                        .requestMatchers(
                                "/api/reservation/**",
                                "/api/mypage/**",
                                "/api/pet/**",
                                "/api/vets",
                                "/api/payment/**"
                        ).hasRole("USER")

                        // 3. 의사 전용 경로
                        .requestMatchers(
                                "/api/doctor/**",
                                "/api/doctor/mypage/**",
                                "/api/vets/me"
                        ).hasRole("DOCTOR")

                        // 4. 관리자 전용
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 5. 그 외는 인증만 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}