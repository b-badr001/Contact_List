package com.example.contact_list;

public class Contact {

    private int ID;

    private String Name;

    public Contact(int id) {
        this.ID = id;
    }

    public Contact(String name) {
        Name = name;
    }

    public Contact(int id, String name) {
        this.ID = id;
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
