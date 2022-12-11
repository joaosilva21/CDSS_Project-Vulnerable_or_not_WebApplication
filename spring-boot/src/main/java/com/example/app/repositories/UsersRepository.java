package com.example.app.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.app.entities.Users;

import java.util.List;

public interface UsersRepository extends CrudRepository<Users, Integer> {
    @Query("select u from Users u where u.username like %?1 and u.password like %?2")
    public Users ValidateUser(String username, String password);

    @Query("select u.salt from Users u where u.username like %?1")
    public String findSaltByUsername(String username);
    
    @Query("select u from Users u where u.username like %?1")
    public Users findUserByUsername(String username);

}