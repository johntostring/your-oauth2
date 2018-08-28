package com.millinch.oauth2.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@RestController
public class UserApi {

    @GetMapping("/user-detail")
    public Object getUserDetail(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @GetMapping("/ping")
    public String ping() {
        return "pang";
    }
}
