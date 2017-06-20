package com.nucleuslife.restaurantreview.structures;

public class Restaurants
{
    private String name;
    private String category;
    private String phone;
    private int rating;
    private String url;
    private long latitude;
    private long longitude;
    private long distance;
    private String address;


    public Restaurants(String name, String category, String phone, int rating, String url, long latitude, long longitude, long distance, String address)
    {
        this.name = name;
        this.category = category;
        this.phone = phone;
        this.rating = rating;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.address = address;
    }

}
