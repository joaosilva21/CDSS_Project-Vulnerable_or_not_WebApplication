package com.example.app.controllers;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import com.example.app.entities.*;
import com.example.app.forms.FormRegister;
import com.example.app.services.*;

import java.security.KeyPair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.springframework.web.servlet.ModelAndView;
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

    @GetMapping("/qrcode")
    public String qrcode(Model model) throws UnsupportedEncodingException{
        FormRegister formRegister = (FormRegister) (model.asMap().get("formregister"));

        String secret = this.usersService.findQRCodeByUsername(formRegister.getUsername());
        String otpUrl = OTP.getURL(secret, 6, Type.TOTP, "spring-boot-2fa-demo", formRegister.getUsername());

        String twoFaQrUrl = String.format(
            "https://chart.googleapis.com/chart?cht=qr&chs=200x200&chl=%s",
            URLEncoder.encode(otpUrl, "UTF-8")); // "UTF-8" shouldn't be unsupported but it's a checked exception :(

        model.addAttribute("twoFaQrUrl", twoFaQrUrl);
        model.addAttribute("username", formRegister.getUsername());

        return "qrcode";
    }
    

    @PostMapping("/part1_4_non_vulnerable_post")
    public ModelAndView part1_4_non_vuln_post(@ModelAttribute FormRegister formRegister, HttpServletResponse response, RedirectAttributes model){
        int error_value = usersService.part1_4_non_vuln(formRegister, this.private_key);
        Cookie error = new Cookie("error", String.valueOf(error_value));
        error.setSecure(true);
        error.setMaxAge(1);
        response.addCookie(error);

        if(error_value != 0){
            return new ModelAndView("redirect:/part1_4_non_vulnerable");
        }

        model.addFlashAttribute("formregister", formRegister);
        
        return new ModelAndView("redirect:/qrcode");
    }
}
