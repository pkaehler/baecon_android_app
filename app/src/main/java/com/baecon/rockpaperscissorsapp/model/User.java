package com.baecon.rockpaperscissorsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("is_active")
    @Expose
    private int is_active;

    public User(){}

    public User(int id, String name){
        this.id = id;
        this.name = name;
        this.is_active = 0;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }
}
