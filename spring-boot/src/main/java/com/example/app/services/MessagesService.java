package com.example.app.services;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.entities.Messages;
import com.example.app.repositories.MessagesRepository;

@Service
public class MessagesService {
    @Autowired
    private MessagesRepository messagesRepository;

}