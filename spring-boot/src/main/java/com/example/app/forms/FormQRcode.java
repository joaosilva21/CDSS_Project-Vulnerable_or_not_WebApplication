package com.example.app.forms;

public class FormQRcode {
    private String code;
    private String qrcode;

    public FormQRcode(String qrcode){
        this.qrcode = qrcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    
}