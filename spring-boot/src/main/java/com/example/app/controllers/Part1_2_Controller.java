package com.example.app.controllers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import com.example.app.entities.Messages;
import com.example.app.services.*;

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
public class Part1_2_Controller {
    @Autowired
    UsersService usersService;

    @Autowired
    MessagesService messagesService;

    @Autowired
    BookService bookService;

    private PublicKey public_key;
    private PrivateKey private_key;

    @GetMapping("/part1_2_vulnerable")
    public String part1_2_vuln() {

        return "part1_2_vulnerable";
    }

    @GetMapping("/part1_2_non_vulnerable")
    public String part1_2_non_vuln(@CookieValue(name = "user", required = false)String user, Model model) {
        if(user == null){
            return "redirect:/index";
        }

        model.addAttribute("message", new Messages());
        model.addAttribute("listMessages", this.messagesService.findMessages() );

        try {
            KeyPairGenerator kgrsa = KeyPairGenerator.getInstance("RSA");
            kgrsa.initialize(1024);
            KeyPair pair = kgrsa.generateKeyPair();
            this.private_key = pair.getPrivate();
            this.public_key = pair.getPublic();
        }catch(Exception e){
            System.out.println(e);
        }

        model.addAttribute("mykey", new String(Base64.getEncoder().encodeToString(this.public_key.getEncoded())));

        return "part1_2_non_vulnerable";
    }

    @PostMapping("/part1_2_non_vulnerable_post")
    public String part1_2_non_vuln_post(@CookieValue(name = "user", required = false)String user, @ModelAttribute Messages message){
        message.setAuthor(user);
        this.messagesService.insertMessage(message, this.private_key);
        
        return "redirect:index";
    }
}