package com.millinch.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@EnableZuulProxy
@EnableOAuth2Sso
@SpringBootApplication
public class OAuth2AdminApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2AdminApplication.class, args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .logout().logoutSuccessUrl("/").and()
                .authorizeRequests()
                .antMatchers("/", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // @formatter:on
    }

    @Configuration
    static class MvcConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/").setViewName("index");
            registry.addViewController("/welcome").setViewName("welcome");
        }
    }
}
