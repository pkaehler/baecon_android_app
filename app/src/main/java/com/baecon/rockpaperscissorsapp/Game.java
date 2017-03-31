package com.baecon.rockpaperscissorsapp;

/**
 * Created by p.kaehler on 31/03/17.
 */

public class Game {

    // ENUMS sind anscheind relativ speicher intensiv, daher Konstanten
    private static String ROCK = "rock";
    private static String PAPER = "paper";
    private static String SCISSORS = "scissors";

    private String id;
    private String idPlayerOne;
    private String idPlayerTwo;

    private String optionPlayerOne;
    private String optionPlayerTwo;

    public Game(String id) {
        this.id = id;
    }


    public String getOptionPlayerOne() {
        return optionPlayerOne;
    }

    public String getOptionPlayerTwo() {
        return optionPlayerTwo;
    }
}
