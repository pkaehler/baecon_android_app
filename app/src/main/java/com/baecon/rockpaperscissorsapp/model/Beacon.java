package com.baecon.rockpaperscissorsapp.model;


public class Beacon {
    private static int counter = 0;
    public final int id;
    public final String beaconId;

    public Beacon(String beaconId) {
        this.id = counter++;
        this.beaconId = beaconId;
    }

    public int getId() {
        return id;
    }
    public String getBeaconId() {return beaconId;}


}
