package com.example.app.controllers;

import com.example.app.services.UsersService;
import com.example.app.entities.Users;
import com.example.app.forms.FormLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;
import java.util.List;

import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

@Controller
public class Part1_1_Controller {
    @Autowired
    private UsersService usersService;

    private PublicKey public_key;
    private PrivateKey private_key;

    
    @GetMapping("/part1_1_vulnerable")
    public String part1_1_vuln(@CookieValue(name = "user", required = false)String user, @RequestParam(name = "error", required = false) String error, Model model, HttpServletResponse response) {
        if(user != null){
            Cookie error_index = new Cookie("error_index", "11");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            error_index.setDomain("localhost");
            error_index.setPath("/index");
            response.addCookie(error_index);

            return "redirect:/index";
        }
  
        if(error != null){
            model.addAttribute("error", error);
        }
        model.addAttribute("formlogin", new FormLogin());

        return "part1_1_vulnerable";
    }
    
    @GetMapping("/part1_1_vulnerable_post")
    public String part1_1_vuln_post(@ModelAttribute FormLogin formLogin, HttpServletResponse response){
        List<Object> returns = usersService.part1_1_vuln(formLogin);
        List<Users> users = (List<Users>)returns.get(0);

        if (users.size() == 0){
            if((Integer)returns.get(1) == 1){
                return "redirect:/part1_1_vulnerable?error=This username doesn't exist";
            }else{
                return "redirect:/part1_1_vulnerable?error=The password is incorrect";
            }

        }
        else{
            Cookie user = new Cookie("user", users.get(0).getUsername());
            user.setSecure(true);
            user.setHttpOnly(true);
            user.setDomain("localhost");

            if (formLogin.getRemember()){
                user.setMaxAge(30*24*60*60);
            }
            else{
                user.setMaxAge(60);
            }
            response.addCookie(user);
            return "redirect:/index";
        }
    }

    @GetMapping("/part1_1_non_vulnerable")
    public String part1_1_non_vuln(@CookieValue(name = "error_login", required = false) String error, @CookieValue(name = "user", required = false) String user, Model model, HttpServletResponse response) {        
        if(user != null){
            Cookie error_index = new Cookie("error_index", "11");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            error_index.setDomain("localhost"); 
            error_index.setPath("/index"); 
            response.addCookie(error_index);

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

        model.addAttribute("formlogin", new FormLogin());
        model.addAttribute("mykey", String.valueOf(Base64.getEncoder().encodeToString(this.public_key.getEncoded())));
       
        return "part1_1_non_vulnerable";
    }

    @PostMapping("/part1_1_non_vulnerable_post")
    public String part1_1_non_vuln_post(@ModelAttribute FormLogin formlogin, RedirectAttributes model, HttpServletResponse response) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException{
        if(formlogin.getUsername() == null || formlogin.getPassword() == null || formlogin.getQrcode() == null){
            Cookie error = new Cookie("error_login", "1");
            error.setSecure(true);
            error.setMaxAge(1);
            error.setHttpOnly(true);
            error.setDomain("localhost");
            error.setPath("/part1_1_non_vulnerable");
            response.addCookie(error);
        }
        else{
            if(this.usersService.part1_1_non_vuln(formlogin, this.private_key)){  
                Cookie user = new Cookie("user", formlogin.getUsername());
                user.setSecure(true);
                user.setHttpOnly(true);
                user.setDomain("localhost"); 
    
                if(formlogin.getRemember()){            
                    user.setMaxAge(60*60);
                }
                else{            
                    user.setMaxAge(60);
                }
    
                response.addCookie(user);
    
                return "redirect:/index";
            }
    
            Cookie error = new Cookie("error_login", "2");
            error.setSecure(true);
            error.setMaxAge(1);
            error.setHttpOnly(true);
            error.setDomain("localhost");
            error.setPath("/part1_1_non_vulnerable");
            response.addCookie(error);
        }

        return "redirect:/part1_1_non_vulnerable";
    }
}
