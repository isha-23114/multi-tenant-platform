package com.saas.multitenantplatform.security;

import com.saas.multitenantplatform.tenant.TenantFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.core.env.Environment;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final TenantFilter tenantFilter;
    private final Environment env;

    // Note: JwtUtil is NOT injected here; it's created as a @Bean below.
    public SecurityConfig(UserDetailsServiceImpl uds, TenantFilter tenantFilter, Environment env) {
        this.userDetailsService = uds;
        this.tenantFilter = tenantFilter;
        this.env = env;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder encoder) throws Exception {
        AuthenticationManagerBuilder amb = http.getSharedObject(AuthenticationManagerBuilder.class);
        amb.userDetailsService(userDetailsService).passwordEncoder(encoder);
        return amb.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil() {
        String secret = env.getProperty("app.jwt.secret", "dev-secret-key-change-me");
        long expiry = Long.parseLong(env.getProperty("app.jwt.expiry-ms", "86400000")); // 1 day default
        return new JwtUtil(secret, expiry);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/tenants/**", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole("TENANT_ADMIN","SUPER_ADMIN")
                        .anyRequest().authenticated()
                );

        // add jwt filter before username/password filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
