package com.baecon.rockpaperscissorsapp.model;


public class Beacon {
    private static int counter = 0;
    public final int id;
    public final String id_beacon;

    public Beacon(String id_beacon) {
        this.id = counter++;
        this.id_beacon = id_beacon;
    }

    public int getId() {
        return id;
    }
    public String getId_beacon() {return id_beacon;}


}
