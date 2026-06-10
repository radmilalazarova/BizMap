package com.radmila.businessdirectory.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "companies")
public class CompanyEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "website")
    private String website;


    @ColumnInfo(name = "logo_url")
    private String logoUrl;

    @ColumnInfo(name = "categories")
    private String categories;


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
    public void setAddress(String addr)     { this.address = addr; }
    public void setLatitude(double lat)     { this.latitude = lat; }
    public void setLongitude(double lon)    { this.longitude = lon; }
    public void setEmail(String email)      { this.email = email; }
    public void setPhone(String phone)      { this.phone = phone; }
    public void setWebsite(String website)  { this.website = website; }
    public void setLogoUrl(String url)      { this.logoUrl = url; }
    public void setCategories(String cats)  { this.categories = cats; }
}