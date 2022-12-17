package com.example.app.repositories;

import com.example.app.entities.Users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Integer> {
    @Query("select u from Users u where u.username = ?1 and u.password = ?2")
    public Users ValidateUser(String username, String password);

    @Query("select u.salt from Users u where u.username = ?1")
    public String findSaltByUsername(String username);
    
    @Query("select u from Users u where u.username = ?1")
    public Users findUserByUsername(String username);

    @Query("select u.qrcode from Users u where u.username = ?1")
    public String findQRCodeByUsername(String username);
}