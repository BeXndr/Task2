package com.example.task2;

public class Email {
    
    private String address;
    private String title;
    private String text;

    public Email(String address, String title, String text) {
        this.address = address;
        this.title = title;
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
