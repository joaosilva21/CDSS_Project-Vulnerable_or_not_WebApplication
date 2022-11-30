package com.example.app.services;

import java.util.*;

import javax.transaction.Transactional;
import javax.persistence.Query;
import javax.persistence.EntityManager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.entities.Users;
import com.example.app.forms.FormLogin;
import com.example.app.repositories.UsersRepository;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EntityManager em;

    public List<Users> vulnerable_authentication(FormLogin formLogin){
        String sql = "SELECT * FROM users WHERE username = '" + formLogin.getUsername() + "' AND password = '" + formLogin.getPassword() + "'";
        System.out.println(sql);
        Query query = em.createNativeQuery(sql);
        List<Object[]> o = query.getResultList();
        List<Users> users = new ArrayList();
        for(Object[] obj : o){
            users.add(new Users((String)obj[0], null, null));
        }

        return users;
        
    }

}