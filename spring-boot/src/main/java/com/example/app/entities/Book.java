package com.example.app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_id;
    @Size(max = 128)
    private String title;
    @Size(max = 256)
    private String authors;
    @Size(max = 128)
    private String category;
    @Column(precision = 8, scale = 2)
    private BigDecimal price;
    private LocalDate book_date;
    @Size(max = 1024)
    private String description;
    @Size(max = 256)
    private String keywords;
    @Size(max = 256)
    private String notes;
    private int recomendation;

    public Book() {

    }

    public Book(int book_id, String title, String authors, String category, BigDecimal price, LocalDate book_date, String description, String keywords, String notes, int recomendation) {
        this.book_id = book_id;
        this.title = title;
        this.authors = authors;
        this.category = category;
        this.price = price;
        this.book_date = book_date;
        this.description = description;
        this.keywords = keywords;
        this.notes = notes;
        this.recomendation = recomendation;
    }

    public int getBook_id() {
        return this.book_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return this.authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
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

    public int getRecomendation() {
        return this.recomendation;
    }

    public void setRecomendation(int recomendation) {
        this.recomendation = recomendation;
    }
    

}
