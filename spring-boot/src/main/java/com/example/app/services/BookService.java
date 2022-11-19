package com.example.app.services;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.entities.Book;
import com.example.app.repositories.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

}