package com.example.app.controllers;

import com.example.app.entities.*;
import com.example.app.services.*;
//import com.example.app.forms.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Part1_1_Controller {
    @Autowired
    UsersService usersService;

    @Autowired
    MessagesService messagesService;

    @Autowired
    BookService bookService;

    @GetMapping("/part1_1_vulnerable")
    public String part1_1_vuln() {

        return "part1_1_vulnerable";
    }

    @GetMapping("/part1_1_non_vulnerable")
    public String part1_1_non_vuln() {

        return "part1_1_non_vulnerable";
    }
}