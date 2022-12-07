package com.example.app.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.app.entities.Messages;

import java.util.List;

import javax.transaction.Transactional;

public interface MessagesRepository extends CrudRepository<Messages, Integer>{
    @Query("select m from Messages m")
    public List<Messages> findMessages();

} 