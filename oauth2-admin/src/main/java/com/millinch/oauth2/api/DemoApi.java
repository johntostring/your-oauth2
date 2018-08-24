package com.millinch.oauth2.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@RestController
public class DemoApi {

    @GetMapping("/hi")
    public String hi() {
        return "Hey";
    }
}
