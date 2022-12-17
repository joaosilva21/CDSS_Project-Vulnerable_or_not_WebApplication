package com.example.app.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Index {
    @GetMapping("/index")
    public String index(@CookieValue(name = "user", required = false) String user,
            @CookieValue(name = "error_index", required = false) String error_index,
            Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }
        if (error_index != null) {
            model.addAttribute("error_index", error_index);
        }

        return "index";
    }

    @PostMapping("/logout_")
    public String logout(HttpServletResponse response) {
        Cookie user = new Cookie("user", null);
        Cookie error_index = new Cookie("error_index", null);

        user.setSecure(true);
        user.setMaxAge(0);
        error_index.setSecure(true);
        error_index.setMaxAge(0);

        response.addCookie(user);
        response.addCookie(error_index);
        return "redirect:/index";
    }
}