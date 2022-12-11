package com.example.app.controllers;

import com.example.app.entities.*;
import com.example.app.forms.FormRegister;
import com.example.app.services.*;

import java.security.KeyPair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Part1_4_Controller {
    @Autowired
    UsersService usersService;

    @Autowired
    MessagesService messagesService;

    @Autowired
    BookService bookService;

    private PublicKey public_key;
    private PrivateKey private_key;

    @GetMapping("/part1_4_non_vulnerable")
    public String part1_4_non_vuln(@CookieValue(name = "error", required = false) String error, @CookieValue(name = "user", required = false)String user, Model model){
        if(user != null){
            return "redirect:/index";
        }

        if(error != null){
            model.addAttribute("error", error);
        }

        try {
            KeyPairGenerator kgrsa = KeyPairGenerator.getInstance("RSA");
            kgrsa.initialize(1024);
            KeyPair pair = kgrsa.generateKeyPair();
            this.private_key = pair.getPrivate();
            this.public_key = pair.getPublic();
        }catch(Exception e){
            System.out.println(e);
        }

        model.addAttribute("formregister", new FormRegister());
        model.addAttribute("mykey", new String(Base64.getEncoder().encodeToString(this.public_key.getEncoded())));

        return "part1_4_non_vulnerable";
    }

    @PostMapping("/part1_4_non_vulnerable_post")
    public String part1_4_non_vuln_post(@ModelAttribute FormRegister formRegister, HttpServletResponse response){

        int error_value = usersService.part1_4_non_vuln(formRegister, this.private_key);
        Cookie error = new Cookie("error", String.valueOf(error_value));
        error.setSecure(true);
        error.setMaxAge(1);
        response.addCookie(error);

        if(error_value != 0){
            return "redirect:/part1_4_non_vulnerable";
        }
        
        return "redirect:/index";
    }
}

