package com.millinch.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@EnableResourceServer
@SpringBootApplication
public class OAuth2AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2AdminApplication.class, args);
    }
}
