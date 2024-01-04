package com.likelion.news.config;

import com.likelion.news.filter.ExceptionHandlerFilter;
import com.likelion.news.filter.JwtAuthenticationFilter;
import com.likelion.news.service.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService oauth2UserService;
    private final AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors-> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests((authorizeHttpRequest)->{
                    authorizeHttpRequest
                            .requestMatchers(HttpMethod.OPTIONS, "/**/*").permitAll()
                            .requestMatchers("/api/login/**").permitAll()
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**" ).permitAll()
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll();

                })
                .oauth2Login(oauth2Login->{
                    oauth2Login
                            .authorizationEndpoint(config->config.baseUri("/oauth2/authorization"))
                            .userInfoEndpoint(userInfo->userInfo.userService(oauth2UserService))
                            .successHandler(oAuth2AuthenticationSuccessHandler);
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement->{
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                .logout(config->config.clearAuthentication(true).deleteCookies("JSESSIONID"))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, jwtAuthenticationFilter.getClass())
                .build();
    }



}