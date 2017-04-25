package com.example;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class Tag {
    private String ime;

    public String idTag() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Tag(String ime) {
        this.ime = ime;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "ime='" + ime + '\'' +
                '}';
    }
}
