package com.example.app.controllers;

import com.example.app.entities.*;
import com.example.app.forms.FormRegister;
import com.example.app.services.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Base64;

import org.apache.tomcat.jni.Buffer;
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

    @GetMapping("/part1_4_vulnerable")
    public String part1_4_vuln(@CookieValue(name = "user", required = false)String user, @RequestParam(name = "error", required = false) String error, Model model){
        if(user != null){
            return "redirect:/index";
        }
  
        if(error != null){
            model.addAttribute("error", error);
        }
        model.addAttribute("formregister", new FormRegister());

        return "part1_4_vulnerable";
    }

    @PostMapping("/part1_4_vulnerable_post")
    public String part1_4_vuln_post(@ModelAttribute FormRegister formRegister){
        String command = "cmd.exe /c findstr /m \"" + formRegister.getPassword() + "\" rockyou.txt";
        
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream()));
            String line = in.readLine();
            if(line != null){
                return "redirect:/part1_4_vulnerable?error=Weak Password";
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        if(!usersService.vulnerable_register(formRegister)){
            return "redirect:/part1_4_vulnerable?error=User already exists";
        }
        
        return "redirect:/index";
    }
}
