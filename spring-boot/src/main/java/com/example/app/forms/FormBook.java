package com.example.app.forms;
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
    private int date_range;
    private int from_day, from_month;
    private int to_day, to_month;
    private Integer from_year, to_year;
    private int show_results;
    private Boolean show_summaries;
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
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.price_more = new BigDecimal(-2);
        }

        if(price_more.compareTo("") == 0){
            this.price_more = new BigDecimal(0);
        }
    }

    public BigDecimal getPrice_less() {
        return price_less;
    }

    public void setPrice_less(String price_less) {
        try{
            this.price_less = new BigDecimal(Float.parseFloat(price_less));
        }
        catch(NumberFormatException e){
            System.out.println(e);
            this.price_less = new BigDecimal(-3);
        }

        if(price_less.compareTo("") == 0){
            this.price_less = new BigDecimal(0);
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
    
    public int getDate_range() {
        return date_range;
    }

    public void setDate_range(int date_range) {
        this.date_range = date_range;
    }

    public int getFrom_day() {
        return from_day;
    }

    public void setFrom_day(int from_day) {
        this.from_day = from_day;
    }

    public int getFrom_month() {
        return from_month;
    }

    public void setFrom_month(int from_month) {
        this.from_month = from_month;
    }

    public Integer getFrom_year() {
        return from_year;
    }

    public void setFrom_year(Integer from_year) {
        this.from_year = from_year == null ? -4 : from_year;
    }

    public int getTo_day() {
        return to_day;
    }

    public void setTo_day(int to_day) {
        this.to_day = to_day;
    }

    public int getTo_month() {
        return to_month;
    }

    public void setTo_month(int to_month) {
        this.to_month = to_month;
    }

    public Integer getTo_year() {
        return to_year;
    }

    public void setTo_year(Integer to_year) {
        this.to_year = to_year == null ? -5 : to_year;
    }

    public int getShow_results() {
        return show_results;
    }
    
    public void setShow_results(int show_results) {
        this.show_results = show_results;
    }

    public Boolean getShow_summaries() {
        return show_summaries;
    }

    public void setShow_summaries(Boolean show_summaries) {
        this.show_summaries = show_summaries;
    }

    public String getSort_by() {
        return sort_by;
    }
    
    public void setSort_by(String sort_by) {
        this.sort_by = sort_by;
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
