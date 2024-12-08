package net.fullstack7.studyShare.config;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import net.fullstack7.studyShare.security.JwtAuthenticationEntryPoint;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /*
    배포시
    */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico",
                    "/assets/**"
                ).permitAll()
                // 공개 페이지
                .requestMatchers(
                    "/",
                    "/member/login",
                    "/member/register",
                    "/member/find-password",
                    "/member/reset-password"
                ).permitAll()
                // 실제 존재하는 URL 패턴에 대해서만 인증 체크
                .requestMatchers(
                    "/post/**",
                    "/chat/**",
                    "/friend/**",
                    "/member/modify",
                    "/member/delete"
                ).authenticated()
                // 나머지는 모두 허용 (404 처리를 위해)
                .anyRequest().permitAll()
            )
            .addFilterBefore(
                jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class
            );
            
        return http.build();
    }
    
    /*
    개발시
    */
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())
    //         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    //         .sessionManagement(session -> session
    //             .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .exceptionHandling(exception -> exception
    //             .authenticationEntryPoint(jwtAuthenticationEntryPoint))
    //         .authorizeHttpRequests(auth -> auth
    //             // 개발 중에는 모든 요청 허용
    //             .anyRequest().permitAll()
                
    //             /* 운영 설정은 주석처리
    //             .requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**").permitAll()
    //             .requestMatchers("/", "/member/login", "/member/register", "/member/find-password").permitAll()
    //             .requestMatchers("/error").permitAll()
    //             .anyRequest().authenticated()
    //             */
    //         )
    //         .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                
    //     return http.build();
    // }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 허용할 origin 설정
        configuration.setAllowedOrigins(Arrays.asList(
            "https://www.gyeongminiya.asia",
            "https://api.gyeongminiya.asia"
        ));
        
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 허용할 헤더
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type", 
            "X-Requested-With"
        ));
        
        // 인증 정보 포함 허용
        configuration.setAllowCredentials(true);
        
        // 노출할 헤더
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
} 