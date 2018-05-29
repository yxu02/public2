package edu.sjsu.xuy87.myapplication;

import android.os.Parcelable;

import java.io.Serializable;

public class TravelDeal implements Serializable{
    private String id;
    private String title;
    private String price;
    private String desc;
    private String imageUrl;

    public TravelDeal() {
    }

    public TravelDeal(String title, String price, String desc, String imageUrl) {
        this.title = title;
        this.price = price;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
