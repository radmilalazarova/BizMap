package com.radmila.businessdirectory.model;

import com.google.gson.annotations.SerializedName;

public class Company {



    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("website")
    private String website;

    @SerializedName("logo_url")
    private String logoUrl;

    @SerializedName("categories")
    private String categories;


    public Company() {}

    public Company(String name, String address, double latitude,
                   double longitude, String email, String phone,
                   String website, String categories) {
        this.name       = name;
        this.address    = address;
        this.latitude   = latitude;
        this.longitude  = longitude;
        this.email      = email;
        this.phone      = phone;
        this.website    = website;
        this.categories = categories;
    }


    public int    getId()         { return id; }
    public String getName()       { return name; }
    public String getAddress()    { return address; }
    public double getLatitude()   { return latitude; }
    public double getLongitude()  { return longitude; }
    public String getEmail()      { return email; }
    public String getPhone()      { return phone; }
    public String getWebsite()    { return website; }
    public String getLogoUrl()    { return logoUrl; }
    public String getCategories() { return categories; }


    public void setId(int id)               { this.id = id; }
    public void setName(String name)        { this.name = name; }
    public void setAddress(String address)  { this.address = address; }
    public void setLatitude(double lat)     { this.latitude = lat; }
    public void setLongitude(double lon)    { this.longitude = lon; }
    public void setEmail(String email)      { this.email = email; }
    public void setPhone(String phone)      { this.phone = phone; }
    public void setWebsite(String website)  { this.website = website; }
    public void setLogoUrl(String url)      { this.logoUrl = url; }
    public void setCategories(String cats)  { this.categories = cats; }


    @Override
    public String toString() {
        return "Company{id=" + id + ", name='" + name + "'}";
    }
}