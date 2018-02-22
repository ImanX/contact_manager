package com.imansoft.contact.manager.models;

/**
 * Created by ImanX.
 * ContactManager | Copyrights 2017 ZarinPal Crop.
 */

public class Group {
    private int id;
    private String name;
    private int countOfMember;

    public Group(int id, String name, int countOfMember) {
        this.id = id;
        this.name = name;
        this.countOfMember = countOfMember;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCountOfMember() {
        return countOfMember;
    }
}
