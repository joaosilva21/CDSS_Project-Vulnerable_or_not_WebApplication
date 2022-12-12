package com.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Index {
    @GetMapping("/index")
    public String index(@CookieValue(name = "error_index", required = false) String error_index, Model model) {
        if(error_index != null){
            model.addAttribute("error_index", error_index);
        }

        return "index";
    }
}