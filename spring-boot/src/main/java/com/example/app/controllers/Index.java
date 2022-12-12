package com.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class Index {
    @GetMapping("/index")
    public String index(HttpServletResponse response) {

        return "index";
    }
}