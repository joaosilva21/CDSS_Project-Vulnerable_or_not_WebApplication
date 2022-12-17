package com.example.app.repositories;

import com.example.app.entities.Messages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessagesRepository extends CrudRepository<Messages, Integer>{
    @Query("select m from Messages m")
    public List<Messages> findMessages_non_vuln();

} 