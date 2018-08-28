package com.millinch.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@SpringBootApplication
@EnableOAuth2Sso
public class OAuth2AdminApplication /*extends WebSecurityConfigurerAdapter*/ {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2AdminApplication.class, args);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .logout().and()
                .authorizeExchange()
                .matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .pathMatchers("/index.html", "/", "/login").permitAll()
                .anyExchange().authenticated().and()
                .csrf().and()
                .build();
    }
    /*@Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .logout().logoutSuccessUrl("/").and()
                .authorizeRequests()
                .antMatchers("/index.html", "/", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // @formatter:on
    }*/
}