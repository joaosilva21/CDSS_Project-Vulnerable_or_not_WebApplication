package com.example.app.services;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.entities.Messages;
import com.example.app.repositories.MessagesRepository;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

@Service
public class MessagesService {
    @Autowired
    private MessagesRepository messagesRepository;

    public void insertMessage(Messages message, SecretKey key){
        
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            message.setMessage(new String(cipher.doFinal(Base64.getDecoder().decode(message.getMessage()))));
        } 
        catch (Exception e) {
            System.out.println(e);
        }

        messagesRepository.save( message);
    }

    public List<Messages> findMessages(){
        return messagesRepository.findMessages();
    }


}