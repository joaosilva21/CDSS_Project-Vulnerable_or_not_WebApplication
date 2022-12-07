package com.example.app.services;

import java.util.*;

import javax.transaction.Transactional;
import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import com.example.app.entities.Users;
import com.example.app.forms.FormLogin;
import com.example.app.repositories.UsersRepository;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private EntityManager em;

    public List<Users> vulnerable_authentication(FormLogin formlogin){
        List<Users> users = new ArrayList();

        String sql_salt = "SELECT * FROM users WHERE username = '" + formlogin.getUsername() + "'";
        Query query_salt = em.createNativeQuery(sql_salt);
        List<Object[]> o_salt = query_salt.getResultList();
        List<String> salts = new ArrayList();
        for(Object[] obj : o_salt){
            salts.add((String)obj[2]);
        }

        if(salts.size() == 0){
            return users;
        }

        String sql_login = "SELECT * FROM users WHERE username = '" + formlogin.getUsername() + "' AND password = '" + DigestUtils.sha256Hex(formlogin.getPassword() + salts.get(0)) + "'";
        Query query_login = em.createNativeQuery(sql_login);
        List<Object[]> o_login = query_login.getResultList();
        for(Object[] obj : o_login){
            users.add(new Users((String)obj[0], null, null));
        }

        return users;
    }

}