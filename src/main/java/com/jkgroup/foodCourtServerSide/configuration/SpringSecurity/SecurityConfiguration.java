package com.jkgroup.foodCourtServerSide.configuration.SpringSecurity;

import com.jkgroup.foodCourtServerSide.configuration.JWT.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true) // allows using the @RolesAllowed annotation
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors().and() // Enable CORS
                .authorizeHttpRequests()

                .requestMatchers(
                        "/swagger-ui-custom.html/**",
                        "/swagger-ui/**",
                        "/api-docs/**",
                        "/api/user/user-info/**",
                        "/api/auth/**",
                        "/api/dish",
                        "/api/dish/category-list",
                        "/api/dish/dishes-by-category",
                        "/api/dish/{id:\\d+}", // Only allow paths like /api/dish/1, /api/dish/2, etc.
                        "/api/reset-password/**"
                )
                .permitAll()

                .requestMatchers(
                        "/api/user/find-by-email",
                        "/api/user/find-by-email",
                        "/api/user/upload-avatar",
                        "/api/basket/**",
                        "/api/order/**"
                ).hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN", "ROLE_CUSTOMER")

                .requestMatchers(
                        "/api/dish/**",
                        "/api/order-management/**"
                ).hasAuthority("ROLE_MANAGER")
                .requestMatchers(
                        "/api/demo/**",
                        "/api/user/**"
                ).hasAuthority("ROLE_ADMIN")


                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://chicchicchic.github.io")); // Specify your allowed origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}