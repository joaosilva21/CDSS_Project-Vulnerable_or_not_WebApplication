package com.example.app.services;
import com.example.app.entities.Users;
import com.example.app.forms.FormLogin;
import com.example.app.forms.FormRegister;
import com.example.app.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import javax.crypto.Cipher;
import org.apache.commons.codec.digest.DigestUtils;
import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;


@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private EntityManager em;

    public List<Users> part1_1_vuln(FormLogin formlogin){
        List<Users> users = new ArrayList<>();

        String sql_salt = "SELECT * FROM users WHERE username = '" + formlogin.getUsername() + "'";
        Query query_salt = em.createNativeQuery(sql_salt);
        List<Object[]> o_salt = query_salt.getResultList();
        List<String> salts = new ArrayList();
        for(Object[] obj : o_salt){
            salts.add((String)obj[3]);
        }

        if(salts.size() == 0){
            return users;
        }
        
        String sql_login = "SELECT * FROM users WHERE username = '" + formlogin.getUsername() + "' AND password = '" + DigestUtils.sha256Hex(formlogin.getPassword() + salts.get(0)) + "'";
        Query query_login = em.createNativeQuery(sql_login);
        List<Object[]> o_login = query_login.getResultList();
        for(Object[]  obj : o_login){
            users.add(new Users((String)obj[0], null, null, null));
        }

        return users;
    }

    @Transactional
    public Boolean part1_4_vuln(FormRegister formRegister){
        String sql_register_user = "SELECT * FROM users WHERE username = '" + formRegister.getUsername() + "'";
        Query query_register_user = em.createNativeQuery(sql_register_user);
        List<Object[]> o_register_user = query_register_user.getResultList();
        for(Object[] obj : o_register_user){
            if(formRegister.getUsername().compareTo((String)obj[0])==0){
                return false;
            }
        }

        String sql_insert_user = "INSERT INTO users (username, password, salt, qrcode) VALUES('" + formRegister.getUsername() + "', " + 
                                 "'" + DigestUtils.sha256Hex(formRegister.getPassword()) + "', '', '" + OTP.randomBase32(20) + "')"; 
        Query query = em.createNativeQuery(sql_insert_user);
        query.executeUpdate();

        return true;
    }

    public Boolean part1_1_non_vuln(FormLogin formlogin, PrivateKey private_key) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException{
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, private_key);
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

        if(!(OTP.create(user.getQrcode(), OTP.timeInHex(System.currentTimeMillis(), 30), 6, Type.TOTP).compareTo(formlogin.getQrcode()) == 0)){
            return false;
        }

        return true;
    }
    
    public int part1_4_non_vuln_verify(FormRegister formRegister, PrivateKey private_key){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, private_key);
            formRegister.setUsername(new String(cipher.doFinal(Base64.getDecoder().decode(formRegister.getUsername()))));
            formRegister.setPassword(new String(cipher.doFinal(Base64.getDecoder().decode(formRegister.getPassword()))));
        } 
        catch (Exception e) {
            System.out.println(e);
        }

        if(formRegister.getPassword().compareTo(formRegister.getUsername()) == 0){
            return 8;
        }

        if(!formRegister.getPassword().matches("(.*[0-9].*)*")){
            return 7;
        }

        if(!formRegister.getPassword().matches("(.*[a-z].*)*") || !formRegister.getPassword().matches("(.*[A-Z].*)*")){
            return 6;
        }


        if(formRegister.getPassword().length() < 8){
            return 5;
        }

        if(!formRegister.getPassword().matches("(.*[!\"#$%&'\\(\\)\\*+,-\\./:;<=>?@\\[\\]\\^_\\{\\}|~].*)*")){
            return 4;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("rockyou.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.compareTo(formRegister.getPassword()) == 0){
                    return 3; 
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }

        Users user = usersRepository.findUserByUsername(formRegister.getUsername());
        if(user != null){
            return 2;
        }
              
        return 0;
    }

    public void part1_4_non_vuln(FormRegister formRegister, String secret){
        byte[] salt_bytes = new byte[32];
        new SecureRandom().nextBytes(salt_bytes);
        String salt = Base64.getEncoder().encodeToString(salt_bytes);

        usersRepository.save(new Users(formRegister.getUsername(), DigestUtils.sha256Hex(formRegister.getPassword() + salt), salt, secret));
    }

    public String findQRCodeByUsername(String username){
        return this.usersRepository.findQRCodeByUsername(username);
    }

}