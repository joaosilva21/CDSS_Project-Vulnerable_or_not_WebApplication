package com.example.app.controllers;

import com.example.app.entities.Messages;
import com.example.app.services.MessagesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.net.http.HttpRequest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.PrivateKey;

@Controller
public class Part1_2_Controller {
    @Autowired
    private MessagesService messagesService;

    private PublicKey public_key;
    private PrivateKey private_key;

    @GetMapping("/part1_2_vulnerable")
    public String part1_2_vuln(@CookieValue(name = "user", required = false)String user, Model model, HttpServletResponse response) {
        if(user == null){
            Cookie error_index = new Cookie("error_index", "12");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            response.addCookie(error_index);

            return "redirect:/index";
        }

        model.addAttribute("allMessages", messagesService.findMessages_vuln());

        return "part1_2_vulnerable";
    }

    @GetMapping("/part1_2_vulnerable_post")
    public String part1_2_vuln_post(@CookieValue(name = "user", required = false)String user, HttpServletRequest httpRequest) {
        Messages message = new Messages();

        message.setMessage(httpRequest.getParameter("v_text"));
        message.setAuthor(user);
        messagesService.part1_2_vuln(message);

        return "redirect:/index";
    }
    /*</td>
    <td>
      <script th:inline="javascript">
        var objs = document.getElementsByTagName("button")
        for (let item of objs) { item.onclick = function(){
          document.getElementById("form_change").action = "http://localhost:8081/index";
          document.getElementById("form_change").method = "get";
          }
      }
      </script>*/

    @GetMapping("/part1_2_non_vulnerable")
    public String part1_2_non_vuln(@CookieValue(name = "user", required = false)String user, Model model, HttpServletResponse response) {
        if(user == null){
            Cookie error_index = new Cookie("error_index", "12");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            response.addCookie(error_index);

            return "redirect:/index";
        }

        model.addAttribute("message", new Messages());
        model.addAttribute("listMessages", this.messagesService.findMessages_non_vuln() );

        try {
            KeyPairGenerator kgrsa = KeyPairGenerator.getInstance("RSA");
            kgrsa.initialize(1024);
            KeyPair pair = kgrsa.generateKeyPair();
            this.private_key = pair.getPrivate();
            this.public_key = pair.getPublic();
        }catch(Exception e){
            System.out.println(e);
        }

        model.addAttribute("mykey", String.valueOf(Base64.getEncoder().encodeToString(this.public_key.getEncoded())));

        return "part1_2_non_vulnerable";
    }

    @PostMapping("/part1_2_non_vulnerable_post")
    public String part1_2_non_vuln_post(@CookieValue(name = "user", required = false)String user, @ModelAttribute Messages message){
        message.setAuthor(user);
        this.messagesService.part1_2_non_vuln(message, this.private_key);
        
        return "redirect:index";
    }
}