package com.terrideboer.bookbase.configuration;

import com.terrideboer.bookbase.filters.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                        auth
                                // Auth
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/user").authenticated()

                                // Authors & books
                                .requestMatchers(HttpMethod.GET,
                                        "/authors",
                                        "/authors/**",
                                        "/books",
                                        "/books/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/authors",
                                        "/authors/**",
                                        "/books",
                                        "/books/**"
                                ).hasRole("LIBRARIAN")

                                // Book-copies
                                .requestMatchers(HttpMethod.GET,
                                        "/book-copies",
                                        "/book-copies/**"
                                ).permitAll()

                                .requestMatchers(HttpMethod.POST, "/books/*/book-copies").hasAnyRole("LIBRARIAN")
                                .requestMatchers(HttpMethod.PATCH, "/book-copies/**").hasAnyRole("LIBRARIAN")
                                .requestMatchers(HttpMethod.DELETE, "/book-copies/**").hasRole("LIBRARIAN")

                                // Fines
                                .requestMatchers(HttpMethod.GET, "/fines").hasAnyRole("EMPLOYEE", "LIBRARIAN")
                                .requestMatchers(HttpMethod.GET, "/fines/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/fines/**").hasAnyRole("EMPLOYEE", "LIBRARIAN")
                                .requestMatchers(HttpMethod.PATCH, "/fines/*/pay").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/fines/**").hasRole("LIBRARIAN")

                                // Loans
                                .requestMatchers(HttpMethod.GET, "/loans").hasAnyRole("EMPLOYEE", "LIBRARIAN")
                                .requestMatchers(HttpMethod.GET, "/loans/**").authenticated()

                                .requestMatchers(HttpMethod.POST,
                                        "/loans",
                                        "/loans/*/fines"
                                ).hasAnyRole("EMPLOYEE", "LIBRARIAN")

                                .requestMatchers(HttpMethod.PATCH,
                                        "/loans/**",
                                        "/loans/*/return",
                                        "/loans/*/extend"
                                ).hasAnyRole("EMPLOYEE", "LIBRARIAN")

                                .requestMatchers(HttpMethod.DELETE, "/loans/**").hasRole("LIBRARIAN")

                                // Reservations
                                .requestMatchers(HttpMethod.GET,
                                        "/reservations",
                                        "reservations/pdf"
                                ).hasAnyRole("EMPLOYEE", "LIBRARIAN")

                                .requestMatchers(HttpMethod.GET, "/reservations/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/reservations").authenticated()

                                .requestMatchers(HttpMethod.PATCH,
                                        "/reservations/**",
                                        "/reservations/*/ready-for-pickup",
                                        "/reservations/*/collect"
                                ).hasAnyRole("LIBRARIAN", "EMPLOYEE")

                                .requestMatchers(HttpMethod.PATCH, "/reservations/*/cancel").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/reservations/**").hasRole("LIBRARIAN")

                                // Users
                                .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("LIBRARIAN", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()

                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("LIBRARIAN")
                                .requestMatchers(HttpMethod.PATCH, "/users/**").authenticated()

                                .requestMatchers(HttpMethod.POST, "/users/*/roles/**").hasRole("LIBRARIAN")
                                .requestMatchers(HttpMethod.DELETE, "/users/*/roles/**").hasRole("LIBRARIAN")

                                .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
