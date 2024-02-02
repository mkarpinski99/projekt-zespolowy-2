package com.blynder.blynder.configuration.security;

import com.blynder.blynder.exception.UserIsBannedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests().antMatchers("/").permitAll();
//        httpSecurity.cors();
//}
//

import com.blynder.blynder.repository.UserRepository;
import com.blynder.blynder.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, ObjectMapper objectMapper,
                             UserRepository userRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RequestBodyReaderAuthenticationFilter authenticationFilter() throws Exception {
        RequestBodyReaderAuthenticationFilter authenticationFilter = new RequestBodyReaderAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(successHandler());
        authenticationFilter.setAuthenticationFailureHandler(failureHandler());
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.cors();
        http.sessionManagement().maximumSessions(-1).sessionRegistry(sessionRegistry());
        http.csrf().disable() //todo wlaczyc
                .authorizeRequests().antMatchers("/h2-console/**").permitAll().antMatchers("/username/*").permitAll().antMatchers("/users/register").permitAll().antMatchers("/streams").permitAll().antMatchers("/categories").permitAll().antMatchers("/categories/**").permitAll().antMatchers("/actuator/**").permitAll().antMatchers("/users/**").permitAll().antMatchers("/search/**").permitAll().anyRequest().authenticated()

                .and().addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class).logout().clearAuthentication(true).logoutUrl("/logout").deleteCookies("JSESSIONID").logoutSuccessHandler(this::logoutSuccessHandler).invalidateHttpSession(true)

                .and().exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration" +
                "/security", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    public AuthenticationSuccessHandler successHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("authority", authentication.getAuthorities().iterator().next().getAuthority());
            map.put("username", authentication.getName());
            httpServletResponse.getWriter().append(objectMapper.writeValueAsString(map));
            httpServletResponse.setStatus(200);
        };
    }

    public AuthenticationFailureHandler failureHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            if (e.getCause() instanceof UserIsBannedException) {
                httpServletResponse.getWriter().append(e.getMessage());
                httpServletResponse.setStatus(403);
            } else {
                httpServletResponse.getWriter().append("Authentication failure");
                httpServletResponse.setStatus(401);
            }
        };
    }

    private void logoutSuccessHandler(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), "Bye!");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedOrigins(
                        "http://localhost:4200").allowCredentials(true).allowedHeaders("*").exposedHeaders("Set" +
                        "-Cookie");
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
//
