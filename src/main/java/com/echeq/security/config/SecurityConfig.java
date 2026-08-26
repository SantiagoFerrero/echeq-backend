package com.echeq.security.config;

import com.echeq.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // ==========================================
                // CSRF
                // ==========================================

                .csrf(csrf -> csrf.disable())

                // ==========================================
                // CORS
                // ==========================================

                .cors(Customizer.withDefaults())

                // ==========================================
                // SESIONES
                // ==========================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                // ==========================================
                // ERRORES DE AUTENTICACION / AUTORIZACION
                // ==========================================

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                authenticationEntryPoint
                        )
                        .accessDeniedHandler(
                                accessDeniedHandler
                        )
                )

                // ==========================================
                // AUTORIZACIÓN
                // ==========================================

                .authorizeHttpRequests(auth -> auth

                        // ==========================================
                        // AUTENTICACIÓN
                        // ==========================================

                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // ==========================================
                        // SWAGGER
                        // ==========================================

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        // ==========================================
                        // USUARIOS
                        // ==========================================

                        .requestMatchers("/api/usuarios/clientes")

                                .hasAnyRole("ADMIN", "OPERADOR")


                        .requestMatchers("/api/usuarios/**")

                                .hasRole("ADMIN")

                        // ==========================================
                        // ROLES
                        // ==========================================

                        .requestMatchers("/api/roles/**")
                        .hasRole("ADMIN")

                        // ==========================================
                        // BANCOS
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/bancos/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "CLIENTE",
                                "OPERADOR",
                                "AUDITOR"
                        )

                        // Crear banco
                        // ADMIN / OPERADOR / CLIENTE
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/bancos"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR",
                                "CLIENTE"
                        )
                        .requestMatchers("/api/bancos/**")
                        .hasRole("ADMIN")

                        // ==========================================
                        // CUENTAS
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cuentas/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "CLIENTE",
                                "OPERADOR",
                                "AUDITOR"
                        )

                        // Cliente: crear únicamente una cuenta propia
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/cuentas/mis-cuentas"
                        )
                        .hasRole("CLIENTE")
                        .requestMatchers("/api/cuentas/**")
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR"
                        )

                        // ==========================================
                        // CUENTA BANCO
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cuentas-banco/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "CLIENTE",
                                "OPERADOR",
                                "AUDITOR"
                        )

                        // Cliente: crear únicamente una Cuenta Banco propia
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/cuentas-banco/mis-cuentas-banco"
                        )
                        .hasRole("CLIENTE")
                        .requestMatchers(
                                "/api/cuentas-banco/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR"
                        )

                        // ==========================================
                        // CUENTA CORRIENTE
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cuentas-corrientes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "CLIENTE",
                                "OPERADOR",
                                "AUDITOR"
                        )

                        // Cliente: crear únicamente una Cuenta Corriente propia
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/cuentas-corrientes/mis-cuentas-corrientes"
                        )
                        .hasRole("CLIENTE")
                        .requestMatchers(
                                "/api/cuentas-corrientes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR"
                        )

                        // ==========================================
                        // SOLICITUDES ECHEQ
                        // ==========================================

                        // Consultar solicitudes
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/solicitudes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "CLIENTE",
                                "OPERADOR",
                                "AUDITOR"
                        )

                        // Crear solicitudes
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/solicitudes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR",
                                "CLIENTE"
                        )

                        // Modificar datos de una solicitud
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/solicitudes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR",
                                "CLIENTE"
                        )

                        // Cambiar estado
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/solicitudes/*/estado"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR"
                        )

                        // Eliminar solicitudes
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/solicitudes/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR"
                        )

                        // ==========================================
                        // APROBACIONES
                        // ==========================================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/aprobaciones/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR",
                                "AUDITOR"
                        )

                        .requestMatchers(
                                "/api/aprobaciones/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "OPERADOR"
                        )

                        // ==========================================
                        // NOTIFICACIONES
                        // ==========================================

                        .requestMatchers(
                                "/api/notificaciones/**"
                        )
                        .authenticated()

                        // ==========================================
                        // AUDITORÍA
                        // ==========================================

                        .requestMatchers(
                                "/api/auditorias/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AUDITOR"
                        )

                        // ==========================================
                        // RESTO
                        // ==========================================

                        .anyRequest()
                        .authenticated()
                )

                // ==========================================
                // JWT AUTHENTICATION FILTER
                // ==========================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ==========================================
    // CORS
    // ==========================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOriginPatterns(
                List.of(
                        "http://localhost:*",
                        "http://127.0.0.1:*"
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
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // ==========================================
    // PASSWORD ENCODER
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ==========================================
    // AUTHENTICATION MANAGER
    // ==========================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}