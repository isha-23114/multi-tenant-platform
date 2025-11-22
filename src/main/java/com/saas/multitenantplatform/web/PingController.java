package com.saas.multitenantplatform.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/ping")
    public String ping() {
        return "Multi-Tenant Platform SaaS platform - running!";
    }
}
