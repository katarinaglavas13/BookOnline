package com.bookonline.model;


public class BookAPI {
    private String title;
    private String imageUrl;

    public BookAPI(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
