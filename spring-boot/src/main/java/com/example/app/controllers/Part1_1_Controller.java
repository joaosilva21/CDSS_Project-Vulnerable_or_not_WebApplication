package com.example.app.controllers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import com.example.app.entities.*;
import com.example.app.services.*;
import com.example.app.forms.FormLogin;

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

    private PublicKey public_key;
    private PrivateKey private_key;

    @GetMapping("/part1_1_vulnerable")
    public String part1_1_vuln(@CookieValue(name = "user", required = false)String user, @RequestParam(name = "error", required = false) String error, Model model) {
        if(user != null){
            return "redirect:/index";
        }
  
        if(error != null){
            model.addAttribute("error", error);
        }
        model.addAttribute("formlogin", new FormLogin());

        return "part1_1_vulnerable";
    }

    @PostMapping("/part1_1_vulnerable_post")
    public String part1_1_vuln_post(@ModelAttribute FormLogin formLogin, HttpServletResponse response){
        List<Users> users = usersService.part1_1_vuln(formLogin);
        
        if (users.size() == 0){
            return "redirect:/part1_1_vulnerable?error=Invalid credentials";
        }
        else{
            Cookie cookie = new Cookie("user", users.get(0).getUsername());
            if (formLogin.getRemember()){
                cookie.setMaxAge(60*60);
            }
            else{
                cookie.setMaxAge(60);
            }
            response.addCookie(cookie);
            return "redirect:/index";
        }
    }

    @GetMapping("/part1_1_non_vulnerable")
    public String part1_1_non_vuln(@CookieValue(name = "error", required = false) String error, @CookieValue(name = "user", required = false) String user, Model model) {
        if(user != null){
            return "redirect:/index";
        }
        
        if(error != null && error.compareTo("1") == 0){
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

        model.addAttribute("formlogin", new FormLogin());
        model.addAttribute("mykey", new String(Base64.getEncoder().encodeToString(this.public_key.getEncoded())));
       
        return "part1_1_non_vulnerable";
    }

    @PostMapping("/part1_1_non_vulnerable_post")
    public String part1_1_non_vuln_post(@ModelAttribute FormLogin formlogin, RedirectAttributes model, HttpServletResponse response) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException{
        if(usersService.part1_1_non_vuln(formlogin, this.private_key)){
            Cookie error = new Cookie("error", "0");        
            Cookie user = new Cookie("user", formlogin.getUsername());
            error.setSecure(true);
            user.setSecure(true);

            if(formlogin.getRemember()){            
                user.setMaxAge(60*60);
            }
            else{            
                user.setMaxAge(60);
            }

            response.addCookie(error);
            response.addCookie(user);

            return "redirect:/index";
        }

        Cookie error = new Cookie("error", "1");
        error.setSecure(true);
        error.setMaxAge(1);
        response.addCookie(error);

        return "redirect:/part1_1_non_vulnerable";
    }
}
