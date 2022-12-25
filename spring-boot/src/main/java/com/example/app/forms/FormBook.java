package com.example.app.forms;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class FormBook {
    private String title;
    private String authors;
    private String category;
    private BigDecimal price_more;
    private BigDecimal price_less;
    private String search_for;
    private String within;
    private String match;
    private String date;
    private Integer date_range;
    private Integer from_day, from_month;
    private Integer to_day, to_month;
    private Integer from_year, to_year;
    private Integer show_results;
    private Integer show_summaries; //Boolean
    private String sort_by;
    
    public FormBook() {
        
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }
    
    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice_more() {
        return price_more;
    }

    public void setPrice_more(String price_more) {
        try{
            this.price_more = new BigDecimal(Float.parseFloat(price_more));
            this.price_more = new BigDecimal(Integer.parseInt(price_more));
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.price_more = new BigDecimal(-2);
        }

        if(price_more.compareTo("") == 0){
            this.price_more = new BigDecimal(-1);
        }
    }

    public BigDecimal getPrice_less() {
        return price_less;
    }

    public void setPrice_less(String price_less) {
        try{
            this.price_less = new BigDecimal(Float.parseFloat(price_less));
            this.price_less = new BigDecimal(Integer.parseInt(price_less));
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.price_less = new BigDecimal(-3);
        }

        if(price_less.compareTo("") == 0){
            this.price_less = new BigDecimal(-1);
        }
    }

    public String getSearch_for() {
        return search_for;
    }
    
    public void setSearch_for(String search_for) {
        this.search_for = search_for;
    }

    public String getWithin() {
        return within;
    }

    public void setWithin(String within) {
        this.within = within;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    public Integer getDate_range() {
        return date_range;
    }

    public void setDate_range(String date_range) {
        try{
            this.date_range = Integer.parseInt(date_range);
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.date_range = Integer.valueOf(-45);
        }
    }

    public Integer getFrom_day() {
        return from_day;
    }

    public void setFrom_day(String from_day) {
        try{
            this.from_day = Integer.parseInt(from_day);

            if(this.from_day == 0){
                this.from_day = Integer.valueOf(-4);
            }
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.from_day = Integer.valueOf(-4);
        }
    }

    public Integer getFrom_month() {
        return from_month;
    }

    public void setFrom_month(String from_month) {
        try{
            this.from_month = Integer.parseInt(from_month);

            if(this.from_month == 0){
                this.from_month = Integer.valueOf(-4);
            }
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.from_month = Integer.valueOf(-4);
        }
    }

    public Integer getFrom_year() {
        return from_year;
    }

    public void setFrom_year(String from_year) {
        try{
            this.from_year = Integer.parseInt(from_year);
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.from_year = Integer.valueOf(-4);
        }
    }

    public Integer getTo_day() {
        return to_day;
    }

    public void setTo_day(String to_day) {
        try{
            this.to_day = Integer.parseInt(to_day);

            if(this.to_day == 0){
                this.to_day = Integer.valueOf(-5);
            }
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.to_day = Integer.valueOf(-5);
        }
    }

    public Integer getTo_month() {
        return to_month;
    }

    public void setTo_month(String to_month) {
        try{
            this.to_month = Integer.parseInt(to_month);

            if(this.to_month == 0){
                this.to_month = Integer.valueOf(-5);
            }
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.to_month = Integer.valueOf(-5);
        }
    }

    public Integer getTo_year() {
        return to_year;
    }

    public void setTo_year(String to_year) {
        try{
            this.to_year = Integer.parseInt(to_year);
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.to_year = Integer.valueOf(-5);
        }
    }

    public Integer getShow_results() {
        return show_results;
    }
    
    public void setShow_results(String show_results) {
        try{
            this.show_results = Integer.parseInt(show_results);
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.show_results = Integer.valueOf(-6);
        }
    }

    public Integer getShow_summaries() {
        return show_summaries;
    }

    public void setShow_summaries(String show_summaries) {
        try{
            this.show_summaries = Integer.parseInt(show_summaries);
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.show_summaries = Integer.valueOf(-7);
        }

        if(this.show_summaries != 0 && this.show_summaries != 1){
            this.show_summaries = Integer.valueOf(-7);
        }
    }

    public String getSort_by() {
        return sort_by;
    }
    
    public void setSort_by(String sort_by) {
        this.sort_by = sort_by;
    }

    public Boolean getNullable() throws IllegalAccessException{
        for (Field f : getClass().getDeclaredFields())
            if (!(f.toString().compareTo("private java.lang.String com.example.app.forms.FormBook.match") == 0 ||
                f.toString().compareTo("private java.lang.String com.example.app.forms.FormBook.date") == 0) && 
                f.get(this) == null){
                System.out.println(f.toString());
                return true;
            }

        return false;
    }

    @Override
    public String toString() {
        return "FormBook [title=" + title + ", author=" + authors + ", category=" + category + ", price_more="
                + price_more + ", price_less=" + price_less + ", search_for=" + search_for + ", within=" + within
                + ", match=" + match + ", date=" + date + ", date_range=" + date_range + ", from_day=" + from_day
                + ", from_month=" + from_month + ", from_year=" + from_year + ", to_day=" + to_day + ", to_month="
                + to_month + ", to_year=" + to_year + ", show_results=" + show_results + ", show_summaries="
                + show_summaries + ", sort_by=" + sort_by + "]";
    }
}
