package com.baecon.rockpaperscissorsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Move {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("firstUser")
    @Expose
    private User firstUser;
    @SerializedName("secondUser")
    @Expose
    private User secondUser;
    @SerializedName("firstFigure")
    @Expose
    private String firstFigure;
    @SerializedName("secondFigure")
    @Expose
    private Object secondFigure;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }

    public String getFirstFigure() {
        return firstFigure;
    }

    public void setFirstFigure(String firstFigure) {
        this.firstFigure = firstFigure;
    }

    public Object getSecondFigure() {
        return secondFigure;
    }

    public void setSecondFigure(Object secondFigure) {
        this.secondFigure = secondFigure;
    }

}