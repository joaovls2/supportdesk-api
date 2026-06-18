package com.supportdesk.config;

import com.supportdesk.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // 1. Garante que o Spring aplique a segurança web corretamente
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 2. Aponta para o Bean de configuração de CORS isolado ali embaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Libera as requisições de teste (Preflight OPTIONS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rotas públicas do sistema
                        .requestMatchers("/auth/**", "/empresas/cadastrar").permitAll()

                        // 3. O SEGREDO: Libera a rota de erro global do Spring.
                        // Se der 404 ou 500, o CORS não vai quebrar e mostrará o erro real no front!
                        .requestMatchers("/error").permitAll()

                        // Rotas restritas por permissão (Hierarquia do mais específico para o mais geral)
                        .requestMatchers("/dashboard/**").hasRole("ADMIN")
                        .requestMatchers("/administradores/**").hasRole("ADMIN")
                        .requestMatchers("/tecnicos/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios").hasRole("ADMIN")

                        .requestMatchers("/usuarios/*/chamados").hasAnyRole("ADMIN", "USUARIO")
                        .requestMatchers("/tecnicos/*/chamados").hasAnyRole("ADMIN", "TECNICO")
                        .requestMatchers("/usuarios/**").hasAnyRole("ADMIN", "USUARIO")
                        .requestMatchers("/chamados/**").hasAnyRole("ADMIN", "TECNICO", "USUARIO")

                        // Configuração correta: regras específicas de empresa ANTES da regra geral de admin
                        .requestMatchers("/empresas/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // 4. Configuração do CORS isolada e limpa
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "https://supportdesk-frontend-eight.vercel.app",
                "https://*.vercel.app"
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}