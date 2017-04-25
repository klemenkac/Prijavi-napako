package com.example;

import java.util.UUID;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class Lokacija {
    String id;
    String dom;
    String soba;
    String idUser; //idUser
    String fileName;
    String opis;
    String tipNapake;
    long date;
    public static final String NODATA="_NA";

    public Lokacija(String dom, String soba, String idUser, long date, String opis, String fileName, String tipNapake) {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.dom = dom;
        this.soba = soba;
        this.idUser = idUser;
        this.date = date;
        this.opis=opis;
        this.fileName=fileName;
        this.tipNapake=tipNapake;
    }

    @Override
    public String toString() {
        return "Lokacija{" +
                "id='" + id + '\'' +
                ", dom='" + dom + '\'' +
                ", soba='" + soba + '\'' +
                ", vzdevek='" + idUser + '\'' +
                ", date=" + date + '\'' +
                ", fileName='" + fileName + '\'' +
                ", tipNapake=" + tipNapake + '\'' +
                ", opis=" + opis +
                '}';
    }

    public String getTipNapake() {
        return tipNapake;
    }

    public void setTipNapake(String tipNapake) {
        this.tipNapake = tipNapake;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDom() {
        return dom;
    }

    public String getOpis() {
        return opis;
    }

    public String getSoba() {
        return soba;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setSoba(String soba) {
        this.soba = soba;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean hasImage() {
        if (fileName==null) return false;
        else if (fileName.equals(NODATA)) return false;
        return true;
    }
}
