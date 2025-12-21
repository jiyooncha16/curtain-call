package com.ssafy.curtaincall;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(Authentication authentication) {
        CustomUserDetails user =
            (CustomUserDetails) authentication.getPrincipal();

        return "userId = " + user.getUserId();
    }
}

