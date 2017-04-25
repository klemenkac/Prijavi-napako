package com.example;

import java.util.ArrayList;

/**
 * Created by xklem on 13. 03. 2017.
 */

public class TagList {
    private ArrayList<Tag> list;

    public TagList() {
        list = new ArrayList<>();
        list.add(new Tag("Elektro"));
        list.add(new Tag("Oprema"));
        list.add(new Tag("Vodovod"));
        list.add(new Tag("Ogrevanje"));
        list.add(new Tag("Internet"));
        list.add(new Tag("Požarne naprave"));
        list.add(new Tag("Drugo"));
    }

    @Override
    public String toString() {
        return "TagList{" +
                "list=" + list +
                '}';
    }

    public Tag getPrvi() {
        return list.get(0);
    }
}
