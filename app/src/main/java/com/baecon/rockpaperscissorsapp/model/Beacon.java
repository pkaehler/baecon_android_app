package com.baecon.rockpaperscissorsapp.model;


public class Beacon {
    private int id;

    private boolean isValid;

    public Beacon(int id) {

        this.id = id;
        this.isValid = true;
    }

    public int getId() {
        return id;
    }

    public boolean isValid() {
        return isValid;
    }
}
