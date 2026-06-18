package com.supportdesk.config;

import com.supportdesk.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOriginPatterns(List.of(
                            "http://localhost:5173",
                            "https://supportdesk-frontend-eight.vercel.app",
                            "https://*.vercel.app"
                    ));

                    configuration.setAllowedMethods(List.of(
                            "GET",
                            "POST",
                            "PUT",
                            "DELETE",
                            "OPTIONS"
                    ));

                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setExposedHeaders(List.of("Authorization"));
                    configuration.setAllowCredentials(true);

                    return configuration;
                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        .requestMatchers(
                                "/auth/**",
                                "/empresas/cadastrar",
                                "/error"
                        )
                        .permitAll()

                        .requestMatchers("/dashboard/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/usuarios/*/chamados")
                        .hasAnyRole("ADMIN", "USUARIO")

                        .requestMatchers("/tecnicos/*/chamados")
                        .hasAnyRole("ADMIN", "TECNICO")

                        .requestMatchers("/administradores/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/tecnicos/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/usuarios")
                        .hasRole("ADMIN")

                        .requestMatchers("/usuarios/**")
                        .hasAnyRole("ADMIN", "USUARIO")

                        .requestMatchers("/chamados/**")
                        .hasAnyRole("ADMIN", "TECNICO", "USUARIO")

                        .requestMatchers("/empresas/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}