package com.swd.smk.security;

import com.swd.smk.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JWTAuthFilter jWTAuthFilter;

    @Autowired
    private JWTUtils jwtUtils;

    private final String[] EMAIL_LIST = {"/api/emails/**"};
    private final String[] ADMIN_LIST = {"/api/admin/**"};
    private final String[] MEMBER_LIST = {"/api/member/**"};
    private final String[] COACH_LIST = {"/api/coach/**"};
    private final String[] PUBLIC_LIST = {"/api/public/**"};
    private final String[] SWAGGERUI = {"/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html"};
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                       .requestMatchers(EMAIL_LIST).permitAll()
                       .requestMatchers(SWAGGERUI).permitAll()
                       .requestMatchers(PUBLIC_LIST).permitAll()
                       .requestMatchers(ADMIN_LIST).hasAuthority("ADMIN")
                       .requestMatchers(COACH_LIST).hasAuthority("COACH")
                       .requestMatchers(MEMBER_LIST).hasAuthority("MEMBER")
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jWTAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();        
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
