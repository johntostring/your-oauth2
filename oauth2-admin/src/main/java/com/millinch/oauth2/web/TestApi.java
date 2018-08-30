package com.millinch.oauth2.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This guy is busy, nothing left.
 *
 * @author John Zhang
 */
@RestController
public class TestApi {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/check")
    public String requireRole() {

        return "Your role is ROLE_USER";
    }

    @GetMapping("/hi")
    public String test() {

        return "Hey";
    }
}
