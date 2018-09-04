package com.example.salalith.placessearch;

public class GoogleReviewsData {
    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public GoogleReviewsData(String author_name, String author_url, String photo_url, String text, int rating, String time) {
        this.author_name = author_name;
        this.author_url = author_url;
        this.photo_url = photo_url;
        this.text = text;
        this.rating = rating;
        this.time = time;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String author_name,author_url,photo_url,text,time;
    private int rating;

}
