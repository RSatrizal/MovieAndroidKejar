package com.example.acer.movieandroidkejar.main;

/**
 * Created by ACER on 01/12/2017.
 */

public class MainDao {

    public MainDao(String title, String imageUrl) {
        this.title = title;
        ImageUrl = imageUrl;
    }

    private String title;
    private String ImageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
