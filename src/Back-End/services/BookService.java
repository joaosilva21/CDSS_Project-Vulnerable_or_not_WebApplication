package com.example.app.services;

import com.example.app.repositories.BookRepository;
import com.example.app.entities.Book;
import com.example.app.forms.FormBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
	
    @Autowired
    private EntityManager em;

	public List<Book> part1_3_vuln(FormBook formBook){
        String arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
	 arg13, arg14, arg15, arg16, arg17, arg18, arg19 = LocalDate.now().toString(), arg20 = LocalDate.now().toString(), arg21, arg22, arg23;
        
        Boolean adv_title = false, adv_authors = false, adv_description = false, adv_keywords = false, adv_notes = false;

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
                arg19 = LocalDate.now().toString();
                arg20 = LocalDate.now().minusDays(formBook.getDate_range()).toString();
                if(formBook.getDate_range() == 0){
                    formBook.setDate(null);
                }
            }
            else if(formBook.getDate().compareTo("specific") == 0){
                arg19 = LocalDate.of(formBook.getTo_year(), formBook.getTo_month(), formBook.getTo_day()).toString();
                arg20 = LocalDate.of(formBook.getFrom_year(), formBook.getFrom_month(), formBook.getFrom_day()).toString();
            }   
        }
		
		arg1 = formBook.getTitle();
        arg2 = String.valueOf(formBook.getTitle().compareTo("") == 0);
        arg3 = formBook.getAuthors();
        arg4 = String.valueOf(formBook.getAuthors().compareTo("") == 0);
        arg5 = formBook.getCategory();
        arg6 = String.valueOf(formBook.getCategory().compareTo("All") == 0);
        arg7 = formBook.getPrice_less().toString();
        arg8 = String.valueOf(formBook.getPrice_less() == null || formBook.getPrice_less().compareTo(new BigDecimal(-1)) == 0);
        arg9 = formBook.getPrice_more().toString();
        arg10 = String.valueOf(formBook.getPrice_more() == null || formBook.getPrice_more().compareTo(new BigDecimal(-1)) == 0);
        arg11 = formBook.getSearch_for();
        arg12 = String.valueOf(adv_title);
        arg13 = String.valueOf(adv_authors);
        arg14 = String.valueOf(adv_description);
        arg15 = String.valueOf(adv_keywords);
        arg16 = String.valueOf(adv_notes);
        arg17 = formBook.getMatch(); //any, all, phrase
        arg18 = String.valueOf(formBook.getSearch_for().compareTo(" ") == 0);
        arg21 = String.valueOf(formBook.getDate() == null);
        arg22 = formBook.getSort_by();
        arg23 = String.valueOf(formBook.getShow_results());
        
        String sql_books =
        " select distinct b.*" +
        " from book as b," + 
        "      (select unnest(string_to_array('" + arg11 + "', ' ')) as parts) as q," +
        "      (select concat('(?=.*', string_agg(concat.parts, ')(?=.*'), ')') as regex" + 
        "       from (select unnest(string_to_array('" + arg11 + "', ' ')) as parts) as concat) as reg" +
        " where (title = '" + arg1 + "' or True = '" + arg2 + "') and" + 
        "       (authors = '" + arg3 + "' or True = '" + arg4 + "') and" +
        "       (category = '" + arg5 + "' or True = '" + arg6 + "') and" +
        "       (price < '" + arg7 + "' or True = '" + arg8 + "') and" +
        "       (price > '" + arg9 + "' or True = '" + arg10 + "') and" +
        "       ((((b.title like concat('% ', q.parts, ' %') and True = '" + arg12 + "') or" +
        "        (b.authors like concat('% ', q.parts, ' %') and True = '" + arg13 + "') or" +
        "        (b.description like concat('% ', q.parts, ' %') and True = '" + arg14 + "') or" +
        "        (b.keywords like concat('% ', q.parts, ' %') and True = '" + arg15 + "') or" +
        "        (b.notes like concat('% ', q.parts, ' %') and True = '" + arg16 + "')) and 'any' = '" + arg17 + "') or" +
        "       (((b.title ~ reg.regex and True = '" + arg12 + "') or" +
        "        (b.authors ~ reg.regex and True = '" + arg13 + "') or" +
        "        (b.description ~ reg.regex and True = '" + arg14 + "') or" +
        "        (b.keywords ~ reg.regex and True = '" + arg15 + "') or" +
        "        (b.notes ~ reg.regex and True = '" + arg16 + "')) and 'all' = '" + arg17 + "') or" +
        "       (((b.title LIKE concat('%', '" + arg11 + "', '%') and True = '" + arg12 + "') or" +
        "        (b.authors LIKE concat('%', '" + arg11 + "', '%') and True = '" + arg13 + "') or" +
        "        (b.description LIKE concat('%', '" + arg11 + "', '%') and True = '" + arg14 + "') or" +
        "        (b.keywords LIKE concat('%', '" + arg11 + "', '%') and True = '" + arg15 + "') or" +
        "        (b.notes LIKE concat('%', '" + arg11 + "', '%') and True = '" + arg16 + "')) and 'phrase' = '" + arg17 + "') or True = '" + arg18 + "') and" +
        "       (b.book_date <= '" + arg19 + "') and" +
        "       (b.book_date >= '" + arg20 + "' or True = '" + arg21 + "')" +
        "       order by b." + arg22 + " desc" + 
        "       limit " + arg23;

        Query query_books = em.createNativeQuery(sql_books);
        List<Object[]> o_books = query_books.getResultList();
        List<Book> books = new ArrayList<>();
        for(Object[] obj : o_books){
            books.add(new Book((int)obj[0], (String)obj[9], (String)obj[1], (String)obj[3], (BigDecimal)obj[7], Instant.ofEpochMilli(((Date)obj[2]).getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
             (String)obj[4], (String)obj[5],(String)obj[6], (Integer)obj[8]));
        }

        return books;
	}

    public List<Book> part1_3_non_vuln(FormBook formBook){
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
		
		return this.bookRepository.searchBooks_non_vuln(formBook.getTitle(), formBook.getTitle().compareTo("") == 0, 
                                               formBook.getAuthors(), formBook.getAuthors().compareTo("") == 0, 
                                               formBook.getCategory(), formBook.getCategory().compareTo("All") == 0, 
                                               formBook.getPrice_less() , formBook.getPrice_less() == null || formBook.getPrice_less().compareTo(new BigDecimal(-1)) == 0, 
                                               formBook.getPrice_more(), formBook.getPrice_more() == null || formBook.getPrice_more().compareTo(new BigDecimal(-1)) == 0,
                                               formBook.getSearch_for(),
                                               adv_title, adv_authors, adv_description, adv_keywords, adv_notes,
                                               formBook.getMatch(), 
                                               formBook.getSearch_for().compareTo(" ") == 0,
                                               date_less, date_more, formBook.getDate() == null,
                                               PageRequest.of(0, formBook.getShow_results(), Sort.Direction.DESC, formBook.getSort_by()));
	}

}