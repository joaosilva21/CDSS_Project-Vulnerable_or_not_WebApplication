package com.example.app.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        model.addAttribute("stackTrace", sw.toString());
        return "error";
    }
}
