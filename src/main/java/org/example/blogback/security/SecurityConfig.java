package org.example.blogback.security;

import org.example.blogback.jwt.AuthEntryPointJwt;
import org.example.blogback.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig  {
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors->cors.configure(http))
                .csrf(csrf->csrf.disable())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/user/register").permitAll()
                                .requestMatchers("/user/login").permitAll()
                                .requestMatchers("post/all").permitAll()
                                .requestMatchers("post/add").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("post/edit").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("post/my-posts").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/user/my-profile").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/post/search").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/post/search/post/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/comment/add").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/post/delete/all").hasAnyRole( "ADMIN")
                                .requestMatchers("/comment/delete/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/comment/post/").permitAll()
                                .requestMatchers("/uploads/**").permitAll()
                                .requestMatchers("/answer/add/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/answer/allAnswer/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/daily/addDailyQuestion/").hasAnyRole( "ADMIN")
                                .requestMatchers("/daily/question/").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/admin/allUsers").hasAnyRole( "ADMIN")
                                .requestMatchers("/admin/edit-profile").hasAnyRole( "ADMIN")
                                .requestMatchers("/admin/users/**").hasAnyRole( "ADMIN")

                                .anyRequest().authenticated()

                );
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
