package com.inovarka.myormawa.models;

public class BannerModel {
    private int imageResource;
    private String imageUrl;
    private String title;
    private String actionUrl;

    // Constructor untuk drawable resource
    public BannerModel(int imageResource) {
        this.imageResource = imageResource;
        this.imageUrl = null;
    }

    // Constructor untuk URL
    public BannerModel(String imageUrl) {
        this.imageUrl = imageUrl;
        this.imageResource = 0;
    }

    // Constructor lengkap
    public BannerModel(int imageResource, String title, String actionUrl) {
        this.imageResource = imageResource;
        this.title = title;
        this.actionUrl = actionUrl;
        this.imageUrl = null;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public boolean isFromUrl() {
        return imageUrl != null && !imageUrl.isEmpty();
    }
}