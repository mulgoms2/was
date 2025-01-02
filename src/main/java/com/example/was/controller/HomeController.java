package com.example.was.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @ResponseBody
    @GetMapping("/")
    public String home () {
        return "it's working now";
    }

    @ResponseBody
    @GetMapping("/api/v1/healthCheck")
    public Health healthCheck() {
        return Health.up().build();
    }

    @ResponseBody
    @GetMapping("/api/v1/test")
    public String test() {
        return "test";
    }

}