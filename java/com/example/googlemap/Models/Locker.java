package com.example.googlemap.Models;

public class Locker
{
    private int id;
    private boolean isAvailable = true;
    private String size;
    private int price;


    public Locker(int id, int price, String size) {
        this.id = id;
        this.price = price;
        this.size = size;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Locker()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
