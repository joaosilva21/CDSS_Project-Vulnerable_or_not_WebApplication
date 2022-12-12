package com.example.app.services;

import com.example.app.repositories.MessagesRepository;
import com.example.app.entities.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.security.PrivateKey;

import javax.crypto.Cipher;

@Service
public class MessagesService {
    @Autowired
    private MessagesRepository messagesRepository;
	
	
    @Autowired
    private EntityManager em;
	
	@Transactional
    public void part1_2_vuln(Messages message){
        String sql_insert_message = "INSERT INTO messages (author, message) VALUES('" + message.getAuthor() + "', '" + message.getMessage() + "')"; 
        Query query = em.createNativeQuery(sql_insert_message);
        query.executeUpdate();
    }

    public List<Messages> findMessages_vuln(){
        String sql_message = "SELECT * FROM messages";
        Query query = em.createNativeQuery(sql_message);
        List<Object[]> o = query.getResultList();
        List<Messages> messages = new ArrayList<>();
        for(Object[] obj : o){
            messages.add(new Messages((String)obj[1], (String)obj[2]));
        }

        return messages;
    }

    public void part1_2_non_vuln(Messages message, PrivateKey private_key){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, private_key);
            message.setMessage(new String(cipher.doFinal(Base64.getDecoder().decode(message.getMessage()))));
        } 
        catch (Exception e) {
            System.out.println(e);
        }

        messagesRepository.save( message);
    }

    public List<Messages> findMessages_non_vuln(){
        return messagesRepository.findMessages_non_vuln();
    }

}