package com.example.app.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.app.entities.Users;

import java.util.List;

public interface UsersRepository extends CrudRepository<Users, Integer> {

}