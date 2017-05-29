package com.example;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class MyMain {
    public static void main(String args[]){
        System.out.println("Prijavi Napako!");
        Tag a = new Tag("Elektro");
        TagList l = new TagList();
        DataAll dataAll = DataAll.scenarijA("");
        System.out.println(dataAll);
    }
}
