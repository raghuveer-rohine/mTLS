package com.example.mTLS_Server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/secure")
    public String secure() {
        return "Hello, mTLS Client!";
    }
}
