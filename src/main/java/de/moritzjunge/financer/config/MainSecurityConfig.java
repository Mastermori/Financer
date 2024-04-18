package de.moritzjunge.financer.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class MainSecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //noinspection Convert2MethodRef
        http
                .userDetailsService(userDetailsService)
                // Necessary to redirect from login. (https://twin.sh/articles/21/spring-security-prevent-authenticated-users-from-accessing-login-page)
                .addFilterBefore(this::doFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf((csrf) -> csrf.disable())
                .headers((headers) -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/login", "/register")
                        .permitAll()
                        .requestMatchers("/css/**", "/js/**", "/webfonts/**", "/favicon.ico")
                        .permitAll()
                        .requestMatchers("/")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin((login) -> login
                        .defaultSuccessUrl("/dashboard", true)
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }

    private void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && ((HttpServletRequest) request).getRequestURI().equals("/login")) {
//                System.out.println("user is authenticated but trying to access login page, redirecting to /");
            ((HttpServletResponse) response).sendRedirect("/dashboard");
        }
        chain.doFilter(request, response);
    }

}
