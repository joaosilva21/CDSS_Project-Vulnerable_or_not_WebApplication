package com.example.app.controllers;

import com.example.app.entities.Book;
import com.example.app.forms.FormBook;
import com.example.app.services.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

@Controller
public class Part1_3_Controller {
    @Autowired
    private BookService bookService;

    @GetMapping("/part1_3_vulnerable")
    public String part1_3_vuln(@CookieValue(name = "user", required = false)String user,
                                @RequestParam(name = "books", required = false)List<Book> books, 
                                @RequestParam(name = "show_description", required = false) Boolean show_description, 
                                Model model, HttpServletResponse response) {
        if(user == null){
            Cookie error_index = new Cookie("error_index", "12");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            response.addCookie(error_index);

            return "redirect:/index";
        }
                                    
        model.addAttribute("formbook", new FormBook());
        model.addAttribute("show_description", show_description);
                            
        if(books != null){
            model.addAttribute("books", books);
        }


        return "part1_3_vulnerable";
    }

    @GetMapping("/part1_3_vulnerable_post")
    public String part1_3_vuln_post(@ModelAttribute FormBook formBook, RedirectAttributes model){
        if(formBook.getPrice_more().compareTo(new BigDecimal(-2)) == 0){
            System.out.println("Price More invalid");
            return "redirect:/index";
        }

        if(formBook.getPrice_less().compareTo(new BigDecimal(-3)) == 0){
            System.out.println("Price Less invalid");

            return "redirect:/index";
        }

        if(formBook.getDate() != null && formBook.getDate().compareTo("specific") == 0){
            if(formBook.getFrom_day() == 0 || formBook.getFrom_month() == 0 || formBook.getFrom_year() == -4){
                System.out.println("From Date invalid");
                return "redirect:/index";
            }

            if(formBook.getTo_day() == 0 || formBook.getTo_month() == 0 || formBook.getTo_year() == -5){
                System.out.println("To Date invalid");
                return "redirect:/index";
            }
        }

        List<Book> books = bookService.part1_3_vuln(formBook);
        
        model.addAttribute("show_description", formBook.getShow_summaries());
        model.addAttribute("books", books);


        return "redirect:/part1_3_vulnerable";
    }

    @GetMapping("/part1_3_non_vulnerable")
    public String part1_3_non_vuln(@CookieValue(name = "user", required = false)String user,
                                   @RequestParam(name = "books", required = false)List<Book> books, 
                                   @RequestParam(name = "show_description", required = false) Boolean show_description,  
                                   Model model, HttpServletResponse response) {
        if(user == null){
            Cookie error_index = new Cookie("error_index", "12");
            error_index.setSecure(true);
            error_index.setMaxAge(1);
            error_index.setHttpOnly(true);
            response.addCookie(error_index);

            return "redirect:/index";
        }
            
        model.addAttribute("formbook", new FormBook());
        model.addAttribute("show_description", show_description);
        
        if(books != null){
            model.addAttribute("books", books);
        }

        return "part1_3_non_vulnerable";
    }

    @PostMapping("/part1_3_non_vulnerable_post")
    public String part1_3_non_vuln_post(@ModelAttribute FormBook formBook, RedirectAttributes model) {
        if(formBook.getPrice_more().compareTo(new BigDecimal(-2)) == 0){
            return "redirect:/index";
        }

        if(formBook.getPrice_less().compareTo(new BigDecimal(-3)) == 0){
            return "redirect:/index";
        }

        if(formBook.getDate() != null && formBook.getDate().compareTo("specific") == 0){
            if(formBook.getFrom_day() == 0 || formBook.getFrom_month() == 0 ||formBook.getFrom_year() == -4){
                return "redirect:/index";
            }
            
            if(formBook.getTo_day() == 0 || formBook.getTo_month() == 0 || formBook.getTo_year() == -5){
                return "redirect:/index";
            }
        }

        List<Book> books = this.bookService.part1_3_non_vuln(formBook);
        
        model.addAttribute("show_description", formBook.getShow_summaries());
        model.addAttribute("books", books);

        return "redirect:/part1_3_non_vulnerable";
    }
}
