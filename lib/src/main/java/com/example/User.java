package com.example;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class User {
    private String idUser;
    private String ime_priimek;
    //private String nacinVpisa;

    public User(String idUser, String vzdevek) {
        this.idUser = idUser;
        this.ime_priimek = ime_priimek;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIme_priimek() {
        return ime_priimek;
    }
    public void setIme_priimek(String ime_priimek) {
        this.ime_priimek = ime_priimek;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser='" + idUser + '\'' +
                ", ime_priimek='" + ime_priimek + '\'' +
                '}';
    }
}
