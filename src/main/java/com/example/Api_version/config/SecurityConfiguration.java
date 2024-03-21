package com.example.Api_version.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Gestion de la sécurité de l'application
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    public final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeHttpRequests(
                        (request)
                                -> {
                            try {
                                request.requestMatchers("/utilisateur/authentication", "/utilisateur/changer" , "/utilisateur/forgot",
                                        "/api_licence/institution/**",
                                        "/api_licence/agence/**",
                                        "/api_licence/produit/**",
                                        "/api_licence/module/**",
                                        "/api_licence/parametre/**",
                                        "/api_licence/parametre-vie/**",
                                        "/api_licence/contrat/**",
                                        "/api_licence/licence/**",
                                        "/api_licence/licenceServeur/**",
                                        "/api_licence/habilitation/**",
                                        "/api_licence/profil/**",
                                        "/api_licence/soushabilitation/**",

                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"
                                        )
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                                        .and()
                                        .sessionManagement()
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // .cors()
        ;

        /*
        http
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("utilisateur/authentication"
                                )
                        .permitAll().requestMatchers(HttpMethod.POST,"/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
*/
        return http.build();

    }


}
