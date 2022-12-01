package com.example.app.services;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import com.example.app.entities.Users;
import com.example.app.forms.FormLogin;
import com.example.app.repositories.UsersRepository;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public Boolean part1_1_non_vuln(FormLogin formlogin, SecretKey key){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            formlogin.setUsername(new String(cipher.doFinal(Base64.getDecoder().decode(formlogin.getUsername()))));
            formlogin.setPassword(new String(cipher.doFinal(Base64.getDecoder().decode(formlogin.getPassword()))));
        } 
        catch (Exception e) {
            System.out.println(e);
        }

        String salt = usersRepository.findSaltByUsername(formlogin.getUsername());
        Users user = usersRepository.ValidateUser(formlogin.getUsername(), DigestUtils.sha256Hex(formlogin.getPassword() + salt));

        if(user == null){
            return false;
        }

        return true;
    }

}