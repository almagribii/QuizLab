// src/main/java/com/quizlab/config/SecurityConfig.java
package com.quizlab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Import ini
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Import ini
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Import ini (untuk Spring Boot 3+)
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; // Import ini

@Configuration
@EnableWebSecurity // Mengaktifkan konfigurasi keamanan web Spring Security
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean ini yang akan mengonfigurasi aturan keamanan HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Menonaktifkan CSRF (Cross-Site Request Forgery) untuk API stateless (penting untuk REST API)
                .csrf(AbstractHttpConfigurer::disable)
                // Mengatur otorisasi untuk permintaan HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Mengizinkan semua permintaan ke endpoint autentikasi tanpa otentikasi
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/categories/**").permitAll()
                        // Mengizinkan akses ke Swagger UI
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // Untuk endpoint Health Check kita
                        .requestMatchers("/api/health/**").permitAll()
                        // Semua permintaan lainnya memerlukan otentikasi
                        .anyRequest().authenticated()
                );

        // Nanti di sini kita akan tambahkan konfigurasi untuk JWT
        // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}