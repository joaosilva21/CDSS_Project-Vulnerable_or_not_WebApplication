package com.example.app.entities;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

@Entity
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Size(max = 16)
    private String author;
    @NotNull
    @Size(max = 256)
    private String message;

    public Messages() {

    }

    public Messages(@Size(max = 16) String author, @NotNull @Size(max = 256) String message) {
        this.author = author;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    };

}