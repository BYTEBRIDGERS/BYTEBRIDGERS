package com.example.BYTEBRIDGERS.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    /*
     * Password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Persist Spring Security authentication in the HTTP session.
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /*
     * Allow requests from the P1 frontend.
     *
     * Frontend: http://127.0.0.1:5500
     * Backend:  http://localhost:8080
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://127.0.0.1:5500",
                        "http://localhost:5500"
                )

        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "Accept",
                        "Authorization"
                )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /*
     * Spring Security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Enable CORS using the configuration above.
                .cors(cors -> {})

                /*
                 * CSRF is disabled for the current project setup.
                 * Revisit this before production deployment because
                 * authentication uses session cookies.
                 */
                .csrf(csrf -> csrf.disable())

                // Use the configured HTTP session security context.
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(
                                securityContextRepository()
                        )
                )

                // Authorization rules.
                .authorizeHttpRequests(auth -> auth

                        // Allow browser CORS preflight requests.
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // Public authentication endpoints.
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register"
                        ).permitAll()
                        
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/problems",
                                "/api/problems/**"
                        ).permitAll()
                        /*
                         * Existing internal endpoints.
                         * Restrict these appropriately before deployment.
                         */
                        .requestMatchers("/internal/**")
                        .permitAll()

                        // All remaining endpoints require authentication.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}