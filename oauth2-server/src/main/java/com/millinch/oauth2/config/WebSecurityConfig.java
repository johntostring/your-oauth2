package com.millinch.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.sql.DataSource;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@Order(2)
@Configuration
@SessionAttributes("authorizationRequest")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public YourUserDetailsManager userDetailsManager() {
        return new YourUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
//            .csrf()
//            .ignoringAntMatchers("/logout/**")
//        .and()
            .formLogin().loginPage("/login").permitAll()
        .and().logout().logoutSuccessUrl("/")
        .and()
            .requestMatchers()
                .antMatchers("/api/**")
                .antMatchers("/", "/login", "/logout", "/oauth/authorize", "/oauth/confirm_access")
        .and()
            .authorizeRequests().anyRequest().authenticated();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsManager())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
