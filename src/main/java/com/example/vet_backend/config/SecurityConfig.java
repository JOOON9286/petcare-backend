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
                        // 공개 경로
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/users/register",
                                "/api/users/login",
                                "/api/community/**",
                                "/api/reviews/**",
                                "/api/users/**",
                                "/api/users",
                                "/api/users/doctors",
                                "/api/consult/**"
                        ).permitAll()

                        // 사용자 전용 경로
                        .requestMatchers(
                                "/api/reservation/**",
                                "/api/mypage/**",
                                "/api/pet/**",
                                "/api/vets",
                                "/api/appointments/find"
                        ).hasRole("USER")

                        // 의사 전용 경로
                        .requestMatchers(
                                "/api/doctor/**",
                                "/api/appointment/**",
                                "/api/doctor/mypage/**",
                                "/api/vets/me"
                        ).hasRole("DOCTOR")

                        // 관리자 전용
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 그 외는 인증만 필요
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
