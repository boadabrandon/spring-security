package com.spring_security.config.security;

import com.spring_security.config.security.filter.JwtAuthenticationFilter;
import com.spring_security.util.Permission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public HttpSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement( sessionMangConfig -> 
                    sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests( authConfig -> {

                   authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                   authConfig.requestMatchers(HttpMethod.GET, "/auth/public-access").permitAll();
                   authConfig.requestMatchers("/error").permitAll();

                    authConfig.requestMatchers(HttpMethod.GET, "/product")
                        .hasAuthority(Permission.READ_ALL_PRODUCTS.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/product")
                        .hasAuthority(Permission.SAVE_ONE_PRODUCT.name());

                    authConfig.anyRequest().denyAll();
                });

        return http.build();
    }
}
