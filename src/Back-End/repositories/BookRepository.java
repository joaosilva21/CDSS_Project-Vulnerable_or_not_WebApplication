package com.example.app.repositories;

import com.example.app.entities.Book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BookRepository extends CrudRepository<Book, Integer>{
    @Query(nativeQuery = true, value = 
           " select distinct b.*" +
           " from book as b," + 
           "      (select unnest(string_to_array(?11, ' ')) as parts) as q," +
           "      (select concat('(?=.*', string_agg(concat.parts, ')(?=.*'), ')') as regex" + 
           "       from (select unnest(string_to_array(?11, ' ')) as parts) as concat) as reg" +
           " where (b.title = ?1 or True = ?2) and" + 
           "       (b.authors = ?3 or True = ?4) and" +
           "       (b.category = ?5 or True = ?6) and" +
           "       (b.price < ?7 or True = ?8) and" +
           "       (b.price > ?9 or True = ?10) and" +
           "       ((((b.title like concat('%',q.parts,'%') and True = ?12) or" +
           "        (b.authors like concat('%',q.parts,'%') and True = ?13) or" +
           "        (b.description like concat('%',q.parts,'%') and True = ?14) or" +
           "        (b.keywords like concat('%',q.parts,'%') and True = ?15) or" +
           "        (b.notes like concat('%',q.parts,'%') and True = ?16)) and 'any' = ?17) or" +
           "       (((b.title ~ reg.regex and True = ?12) or" +
           "        (b.authors ~ reg.regex and True = ?13) or" +
           "        (b.description ~ reg.regex and True = ?14) or" +
           "        (b.keywords ~ reg.regex and True = ?15) or" +
           "        (b.notes ~ reg.regex and True = ?16)) and 'all' = ?17) or" +
           "       (((b.title like concat('%',?11,'%') and True = ?12) or" +
           "        (b.authors like concat('%',?11,'%') and True = ?13) or" +
           "        (b.description like concat('%',?11,'%') and True = ?14) or" +
           "        (b.keywords like concat('%',?11,'%') and True = ?15) or" +
           "        (b.notes like concat('%',?11,'%') and True = ?16)) and 'phrase' = ?17) or True = ?18) and" +
           "       (b.book_date <= ?19) and" +
           "       (b.book_date >= ?20 or True = ?21)")
    public List<Book> searchBooks_non_vuln(String title, Boolean t_use,
                                String authors, Boolean a_use,
                                String category, Boolean c_use,
                                BigDecimal price_less, Boolean pl_use,
                                BigDecimal price_more, Boolean pm_use,
                                String search_for,
                                Boolean adv_title, Boolean adv_authors, Boolean adv_description, Boolean adv_keywords, Boolean adv_notes,
                                String adv_math,
                                Boolean sf_use,
                                LocalDate date_less, LocalDate date_more, Boolean d_use,
                                Pageable sort_by);
                                
} 
