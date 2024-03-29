package com.example.app.controllers;
import com.example.app.forms.FormQRcode;
import com.example.app.forms.FormRegister;
import com.example.app.services.*;

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

import java.util.Base64;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import java.net.URLEncoder;

@Controller
public class Part1_4_Controller {
    @Autowired
    private UsersService usersService;

    private PublicKey public_key;
    private PrivateKey private_key;

	@GetMapping("/part1_4_vulnerable")
    public String part1_4_vuln(@CookieValue(name = "user", required = false)String user, @RequestParam(name = "error", required = false) String error, Model model, HttpServletResponse response){
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
        model.addAttribute("formregister", new FormRegister());

        return "part1_4_vulnerable";
    }

    @GetMapping("/part1_4_vulnerable_post")
    public String part1_4_vuln_post(@ModelAttribute FormRegister formRegister){
        String command =  "grep " + formRegister.getPassword() + " rockyou.txt";
        //System.out.println(command);
        
        try{
            BufferedReader in =new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(new String[]{ "/bin/sh", "-c", command }).getInputStream()));
            String line = in.readLine();
            if(line != null){
                System.out.println(line);
                return "redirect:/part1_4_vulnerable?error=Weak Password";
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        if(!usersService.part1_4_vuln(formRegister)){
            return "redirect:/part1_4_vulnerable?error=User already exists";
        }
        
        return "redirect:/index";
    }

    @GetMapping("/part1_4_non_vulnerable")
    public String part1_4_non_vuln(@CookieValue(name = "error_register", required = false) String error, @CookieValue(name = "user", required = false)String user, Model model, HttpServletResponse response){
        if(user != null){
            Cookie error_index = new Cookie("error_index", "11");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            error_index.setDomain("localhost");
            error_index.setPath("/");
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

        model.addAttribute("formregister", new FormRegister());
        model.addAttribute("mykey", String.valueOf(Base64.getEncoder().encodeToString(this.public_key.getEncoded())));

        return "part1_4_non_vulnerable";
    }

    @PostMapping("/part1_4_non_vulnerable_post")
    public ModelAndView part1_4_non_vuln_post(@ModelAttribute FormRegister formRegister, HttpServletResponse response, RedirectAttributes model){
        if(formRegister.getUsername() == null || formRegister.getPassword() == null || formRegister.getConfirm_password() == null){
            Cookie error = new Cookie("error_register", "1");
            error.setSecure(true);
            error.setMaxAge(1);
            error.setHttpOnly(true);
            error.setDomain("localhost");
            error.setPath("/part1_4_non_vulnerable");
            response.addCookie(error);

            return new ModelAndView("redirect:/part1_4_non_vulnerable");
        }
        else{
            int error_value = this.usersService.part1_4_non_vuln_verify(formRegister, this.private_key);
            
            Cookie error = new Cookie("error_register", String.valueOf(error_value));
            error.setSecure(true);
            error.setMaxAge(1);
            error.setHttpOnly(true);
            error.setDomain("localhost");
            error.setPath("/part1_4_non_vulnerable");
            response.addCookie(error);

            if(error_value != 0){
                return new ModelAndView("redirect:/part1_4_non_vulnerable");
            }

            model.addFlashAttribute("formregister", formRegister);
        }
        
        return new ModelAndView("redirect:/qrcode");
    }

    
    @GetMapping("/qrcode")
    public String qrcode(@CookieValue(name = "error_register", required = false) String error_register, 
                          Model model, HttpServletResponse response) throws UnsupportedEncodingException{
                          FormRegister formRegister;

        if((formRegister = (FormRegister) (model.asMap().get("formregister"))) == null){
            formRegister = (FormRegister) (model.asMap().get("nice_try"));
        }

        if(formRegister == null){
            Cookie error_index = new Cookie("error_index", "13");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            error_index.setDomain("localhost");
            error_index.setPath("/index");
            response.addCookie(error_index);

            return "redirect:/index";
        }

        if(error_register != null){
            model.addAttribute("error_register", error_register);
        }

        String secret = OTP.randomBase32(20); 
        String otpUrl = OTP.getURL(secret, 6, Type.TOTP, "spring-boot-2fa-demo", formRegister.getUsername());

        String twoFaQrUrl = String.format(
            "https://chart.googleapis.com/chart?cht=qr&chs=200x200&chl=%s",
            URLEncoder.encode(otpUrl, "UTF-8"));

        model.addAttribute("twoFaQrUrl", twoFaQrUrl);
        model.addAttribute("formqrcode", new FormQRcode(secret));
        model.addAttribute("formregister", formRegister);

        return "qrcode";
    }

    @PostMapping("/qrcode_post")
    public ModelAndView qrcode_post(@ModelAttribute("formqrcode") FormQRcode formQRcode, @ModelAttribute("formregister") FormRegister formRegister,
                              RedirectAttributes model, HttpServletResponse response) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException{
        if(formQRcode.getCode() == null || formQRcode.getQrcode() == null){
            model.addFlashAttribute("nice_try", formRegister);
            Cookie error_register = new Cookie("error_register", "21");
            error_register.setSecure(true);
            error_register.setMaxAge(1);
            error_register.setHttpOnly(true);
            error_register.setDomain("localhost");
            error_register.setPath("/qrcode");
            response.addCookie(error_register);
            
            return new ModelAndView("redirect:/qrcode");
        }
        
        if(!(OTP.create(formQRcode.getQrcode(), OTP.timeInHex(System.currentTimeMillis(), 30), 6, Type.TOTP).compareTo(formQRcode.getCode()) == 0)){
            model.addFlashAttribute("nice_try", formRegister);
            Cookie error_register = new Cookie("error_register", "22");
            error_register.setSecure(true);
            error_register.setMaxAge(1);
            error_register.setHttpOnly(true);
            error_register.setDomain("localhost");
            error_register.setPath("/qrcode");
            response.addCookie(error_register);
            
            return new ModelAndView("redirect:/qrcode");
        }

        this.usersService.part1_4_non_vuln(formRegister, formQRcode.getQrcode());

        return new ModelAndView("redirect:/index");
    }
}