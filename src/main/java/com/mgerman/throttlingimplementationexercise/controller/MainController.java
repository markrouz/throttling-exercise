package com.mgerman.throttlingimplementationexercise.controller;

import com.mgerman.throttlingimplementationexercise.ratelimiter.ApplyRateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    @ApplyRateLimiter
    public ResponseEntity<Void> getResponse() {
        return ResponseEntity.ok().build();
    }

}
