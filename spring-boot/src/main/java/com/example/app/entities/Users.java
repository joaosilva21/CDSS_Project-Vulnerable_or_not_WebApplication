package com.example.app.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Users {
    @Id
    @Size(max = 32)
    private String username;
    @NotNull
    @Size(max = 512)
    private String password, salt;
    @NotNull
    private String qrcode;

    public Users() {

    }

    public Users(@Size(max = 32) String username, @NotNull @Size(max = 512) String password,
            @NotNull @Size(max = 512) String salt, String qrcode) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.qrcode = qrcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    

}
