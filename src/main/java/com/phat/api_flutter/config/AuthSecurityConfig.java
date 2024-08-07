package com.phat.api_flutter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class AuthSecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http.
                authorizeHttpRequests((authorizeHttp) -> authorizeHttp
                                .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/sign-up/**")).permitAll()
                                .anyRequest().permitAll()
//						.anyRequest().authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/swagger-ui/index.html#/")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable); //Disabled cai nay, sau khi hoan tat log in
//		.csrf((csrf) -> csrf
//	            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//	            .csrfTokenRequestHandler(requestHandler)
//	    );

        return http.build();
    }



}
