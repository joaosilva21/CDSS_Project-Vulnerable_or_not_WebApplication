package com.example.app.controllers;

import com.example.app.entities.*;
import com.example.app.services.*;


import com.example.app.forms.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;


@Controller
public class Part1_1_Controller {
    @Autowired
    UsersService usersService;

    @Autowired
    MessagesService messagesService;

    @Autowired
    BookService bookService;

    @GetMapping("/part1_1_vulnerable")
    public String part1_1_vuln(@CookieValue(name = "cookie", required = false)String cookie, @RequestParam(name = "error", required = false) String error, Model model) {
        if(cookie != null){
            return "redirect:/index";
        }
  
        if(error != null){
            model.addAttribute("error", error);
        }
        model.addAttribute("formlogin", new FormLogin());

        return "part1_1_vulnerable";
    }

    @PostMapping("/part1_1_vulnerable_post")
    public String part1_1_vulnerable_post(@ModelAttribute FormLogin formLogin, HttpServletResponse response){
        int error;
        List<Users> users = usersService.vulnerable_authentication(formLogin);
        System.out.println(users.toString().substring(1, users.toString().length()-1));
        if (users.size() == 0){
            return "redirect:/part1_1_vulnerable?error=invalid credentials";
  
        }else{
            Cookie cookie = new Cookie("cookie", users.get(0).getUsername());
            if (formLogin.getRemember()){
                cookie.setMaxAge(60*60);
            }else{
                cookie.setMaxAge(60);
            }
            response.addCookie(cookie);
            return "redirect:/index";
        }
    }

    @GetMapping("/part1_1_non_vulnerable")
    public String part1_1_non_vuln() {

        return "part1_1_non_vulnerable";
    }
}