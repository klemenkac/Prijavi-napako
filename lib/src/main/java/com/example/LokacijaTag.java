package com.example;

import java.util.UUID;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class LokacijaTag {
    String idLokacija;
    Tag tag;
    long datum;
    String idUser;
    String id;

    public LokacijaTag(String idLokacija, Tag tag, long datum, String idUser) {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.idLokacija = idLokacija;
        this.tag = tag;
        this.datum = datum;
        this.idUser = idUser;
    }

    public String getIdLokacija() {
        return idLokacija;
    }

    public void setIdLokacija(String idLokacija) {
        this.idLokacija = idLokacija;
    }

    public Tag getTagID() {
        return tag;
    }

    public void setTagID(String tagID) {
        this.tag = tag;
    }

    public long getDatum() {
        return datum;
    }

    public void setDatum(long datum) {
        this.datum = datum;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LokacijaTag{" +
                "idLokacija='" + idLokacija + '\'' +
                ", tag='" + tag + '\'' +
                ", datum=" + datum +
                ", idUser='" + idUser + '\'' +
                '}';
    }
}