package by.egorivanov.cloudstorage.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**", "/api/directory/**", "/api/resource/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .securityContext((securityContext) -> securityContext.securityContextRepository(securityContextRepository()))
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .maximumSessions(1)
                                .expiredUrl("/api/auth/sign-in")
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/sign-out")
                        .deleteCookies("SESSION")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/api/auth/sign-in")
                        .logoutSuccessHandler(logoutSuccessHandler()))
                .build();
    }


    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response,
                authentication) -> response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }


    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:8080",
                "http://31.56.48.72:3000",
                "http://31.56.48.72:8080"
        ));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityContextHolderStrategy securityContextHolderStrategy() {
        return SecurityContextHolder.getContextHolderStrategy();
    }
}
