package com.example.app.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.lang.String;
import java.time.LocalDate;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String category;
    private String price;
    private LocalDate book_date;
    private String description;
    private String keywords;
    private String notes;
    private Integer recomendation;

    public Book() {

    }

    public Book(String title, String category, String price, LocalDate book_date, String description,
            String keywords, String notes, Integer recomendation) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.book_date = book_date;
        this.description = description;
        this.keywords = keywords;
        this.notes = notes;
        this.recomendation = recomendation;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public LocalDate getBook_date() {
        return this.book_date;
    }

    public void setBook_date(LocalDate book_date) {
        this.book_date = book_date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getRecomendation() {
        return this.recomendation;
    }

    public void setRecomendation(Integer recomendation) {
        this.recomendation = recomendation;
    }

}
