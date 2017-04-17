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

    private int victories;
    private int draws;
    private int losses;

    private int wonWithRock;
    private int totalPlayedRocks;
    private int wonWithPaper;
    private int totalPlayedPapers;
    private int wonWithScissors;
    private int totalPlayedScissors;


    public User(int id, String name){
        this.id = id;
        this.name = name;

        victories = 0;
        draws = 0;
        losses = 0;

        //Die Stats werden auch durch Backend bereit gestellt
        //werden aber auch lokal gespeichert, damit keine Inet Verbindung nötig ist
        wonWithPaper = 0;
        wonWithRock = 0;
        wonWithScissors = 0;

        totalPlayedPapers = 0;
        totalPlayedRocks = 0;
        totalPlayedScissors = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getWonWithRock() {
        return wonWithRock;
    }

    public int getTotalPlayedRocks() {
        return totalPlayedRocks;
    }

    public int getWonWithPaper() {
        return wonWithPaper;
    }

    public int getTotalPlayedPapers() {
        return totalPlayedPapers;
    }

    public int getWonWithScissors() {
        return wonWithScissors;
    }

    public int getTotalPlayedScissors() {
        return totalPlayedScissors;
    }

    public int getVictories() {
        return victories;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }
}
