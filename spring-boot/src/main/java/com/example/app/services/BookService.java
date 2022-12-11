package com.example.app.services;


import java.util.*;
import java.time.LocalDate;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.app.entities.Book;
import com.example.app.forms.FormBook;
import com.example.app.repositories.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> searchBooks(FormBook formBook){
        Boolean adv_title = false, adv_authors = false, adv_description = false, adv_keywords = false, adv_notes = false;
        LocalDate date_more = LocalDate.now(), date_less = LocalDate.now();

        if(formBook.getSearch_for().compareTo("") == 0){
            formBook.setSearch_for(" ");
        }
        else{
            if(formBook.getWithin().compareTo("any") == 0){
                adv_title = adv_authors = adv_description = adv_keywords = adv_notes = true;
            }
            else if(formBook.getWithin().compareTo("title") == 0){
                adv_title = true;
            }
            else if(formBook.getWithin().compareTo("authors") == 0){
                adv_authors = true;
            }
            else if(formBook.getWithin().compareTo("desc") == 0){
                adv_description = true;
            }
            else if(formBook.getWithin().compareTo("keys") == 0){
                adv_keywords = true;
            }
            else if(formBook.getWithin().compareTo("notes") == 0){
                adv_notes = true;
            }
        }

        if(formBook.getDate() != null){   
            if(formBook.getDate().compareTo("custom") == 0){
                date_less = LocalDate.now();
                date_more = LocalDate.now().minusDays(formBook.getDate_range());
                if(formBook.getDate_range() == 0){
                    formBook.setDate(null);
                }
            }
            else if(formBook.getDate().compareTo("specific") == 0){
                date_less = LocalDate.of(formBook.getTo_year(), formBook.getTo_month(), formBook.getTo_day());
                date_more = LocalDate.of(formBook.getFrom_year(), formBook.getFrom_month(), formBook.getFrom_day());
            }   
        }

        return this.bookRepository.searchBooks(formBook.getTitle(), formBook.getTitle().compareTo("") == 0, 
                                               formBook.getAuthors(), formBook.getAuthors().compareTo("") == 0, 
                                               formBook.getCategory(), formBook.getCategory().compareTo("All") == 0, 
                                               formBook.getPrice_less() , formBook.getPrice_less() == null || formBook.getPrice_less().compareTo(new BigDecimal(0)) == 0, 
                                               formBook.getPrice_more(), formBook.getPrice_more() == null || formBook.getPrice_more().compareTo(new BigDecimal(0)) == 0,
                                               formBook.getSearch_for(),
                                               adv_title, adv_authors, adv_description, adv_keywords, adv_notes,
                                               formBook.getMatch(), 
                                               formBook.getSearch_for().compareTo(" ") == 0,
                                               date_less, date_more, formBook.getDate() == null,
                                               PageRequest.of(0, formBook.getShow_results(), Sort.Direction.DESC, formBook.getSort_by()));
    }
}