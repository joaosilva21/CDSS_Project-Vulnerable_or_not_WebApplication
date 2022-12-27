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

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;

import org.apache.commons.codec.binary.Base64;
import java.math.BigDecimal;

@Controller
public class Part1_3_Controller {
    @Autowired
    private BookService bookService;

    private Key key;

    @GetMapping("/part1_3_vulnerable")
    public String part1_3_vuln(@CookieValue(name = "user", required = false)String user,
                                @RequestParam(name = "books", required = false)List<Book> books, 
                                @RequestParam(name = "show_description", required = false) Boolean show_description,
                                @RequestParam(name = "error", required = false) String error, 
                                Model model, HttpServletResponse response) {
        if(user == null){
            Cookie error_index = new Cookie("error_index", "12");
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

        model.addAttribute("formbook", new FormBook());
        model.addAttribute("show_description", show_description);
                            
        if(books != null){
            model.addAttribute("books", books);
        }


        return "part1_3_vulnerable";
    }
    
    @GetMapping("/part1_3_vulnerable_post")
    public String part1_3_vuln_post(@ModelAttribute FormBook formBook, RedirectAttributes model) throws IllegalAccessException{
        int error_book;
        if((error_book = verifyInputs(formBook)) < 0){
            return "redirect:/part1_3_vulnerable?error=An error ocorred";
        }

        List<Book> books = bookService.part1_3_vuln(formBook);
        
        model.addAttribute("show_description", formBook.getShow_summaries());
        model.addAttribute("books", books);
        
        return "redirect:/part1_3_vulnerable";
    }
    
    @GetMapping("/part1_3_non_vulnerable")
    public String part1_3_non_vuln(@CookieValue(name = "error_book", required = false) String error,
                                   @CookieValue(name = "user", required = false)String user,
                                   @RequestParam(name = "books", required = false)String books, 
                                   @RequestParam(name = "show_description", required = false)String show_description,  
                                   Model model, HttpServletResponse response) {
        if(user == null){
            Cookie error_index = new Cookie("error_index", "12");
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

        model.addAttribute("formbook", new FormBook());
        model.addAttribute("show_description", show_description);
        
        if(books != null){
            model.addAttribute("books", Decrypt(books));
        }

        return "part1_3_non_vulnerable";
    }

    @PostMapping("/part1_3_non_vulnerable_post")
    public String part1_3_non_vuln_post(@ModelAttribute FormBook formBook, RedirectAttributes model, HttpServletResponse response) throws IllegalAccessException {
        int error_book;
        if((error_book = verifyInputs(formBook)) < 0){
            Cookie error = new Cookie("error_book", String.valueOf(error_book));  
            error.setSecure(true);
            error.setMaxAge(1);
            error.setHttpOnly(true);
            error.setDomain("localhost");
            error.setPath("/part1_3_non_vulnerable"); 
            response.addCookie(error);
        }
        else{
            List<Book> books = this.bookService.part1_3_non_vuln(formBook);
        
            model.addAttribute("show_description", String.valueOf(formBook.getShow_summaries()));
            model.addAttribute("books", Encrypt(books));
        }

        return "redirect:/part1_3_non_vulnerable";
    }
    
    public int verifyInputs(FormBook formBook) throws IllegalAccessException{
        if(formBook.getNullable()){
            return -1;
        }

        if(formBook.getPrice_more().compareTo(new BigDecimal(-2)) == 0){
            return -2;
        }

        if(formBook.getPrice_less().compareTo(new BigDecimal(-3)) == 0){
            return -3;
        }

        if(formBook.getDate() != null && formBook.getDate().compareTo("custom") == 0){
            if(formBook.getDate_range() == -45){
                return -45;
            }
        }

        if(formBook.getDate() != null && formBook.getDate().compareTo("specific") == 0){
            if(formBook.getFrom_day() == -4 || formBook.getFrom_month() == -4 || formBook.getFrom_year() == -4){
                return -4;
            }
            
            if(formBook.getTo_day() == -5 || formBook.getTo_month() == -5 || formBook.getTo_year() == -5){
                return -5;
            }
        }
        
        if(!(formBook.getShow_results() == 5 || formBook.getShow_results() == 10 || formBook.getShow_results() == 25
             || formBook.getShow_results() == 50 || formBook.getShow_results() == 100)){
                return -6;
        }
        
        if(formBook.getShow_summaries() == -7){
            return -7;
        }

        if(!(formBook.getCategory().compareTo("All") == 0|| formBook.getCategory().compareTo("Databases") == 0|| 
             formBook.getCategory().compareTo("HTML & Web design") == 0 || formBook.getCategory().compareTo("Programming") == 0)){
        
            return -8;
        }

        if(formBook.getMatch() != null && !(formBook.getMatch().compareTo("any") == 0 ||
           formBook.getMatch().compareTo("all") == 0 || formBook.getMatch().compareTo("phrase") == 0)){

            return -9;
        }

        if(!(formBook.getWithin().compareTo("any") == 0|| formBook.getWithin().compareTo("title") == 0 || 
             formBook.getWithin().compareTo("authors") == 0 || formBook.getWithin().compareTo("desc") == 0) ||
             formBook.getWithin().compareTo("keys") == 0 || formBook.getWithin().compareTo("notes") == 0){
        
            return -10;
        }

        if(!(formBook.getSort_by().compareTo("recomendation") == 0 || formBook.getSort_by().compareTo("recomendation") == 0)){
            return -11;
        }

        if(!(formBook.getSearch_for().matches("[a-zA-Z0-9_]*"))){
            return -12;
        }

        return 0;
    }

    public String Encrypt(Object object){
        try{
            //ENCRYPT
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(stream);
            out.writeObject(object);
            byte[] serialized = stream.toByteArray();

            Cipher cipher = Cipher.getInstance("AES");
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(192);
            this.key = kg.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, this.key);

            return new String(Base64.encodeBase64(cipher.doFinal(serialized)));    
        }
        catch(Exception e){
            System.out.println(e);
        }

        return null;
    }

    public Object Decrypt(String object){
        try{
            byte[] encryptByte = Base64.decodeBase64(object.getBytes());

            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, this.key);

            byte[] decryptByte = cipher2.doFinal(encryptByte);

            ByteArrayInputStream stream2 = new ByteArrayInputStream(decryptByte);
            ObjectInput in = new ObjectInputStream(stream2);

            stream2.close();
            in.close();
            return in.readObject();        
        }
        catch(Exception e){
            System.out.println(e);
        }

        return null;
    }
}
