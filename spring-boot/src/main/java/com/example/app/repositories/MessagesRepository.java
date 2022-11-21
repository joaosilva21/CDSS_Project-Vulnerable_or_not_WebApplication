package com.example.app.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.app.entities.Messages;

import java.util.List;

public interface MessagesRepository extends CrudRepository<Messages, Integer>{

} 