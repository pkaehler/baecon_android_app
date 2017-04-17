package com.baecon.rockpaperscissorsapp.model;

public class Move {
    private String playerId;
    private String beaconId;
    private String option;

    public Move(String mPlayerId, String mBaeconId, String mOption){
        playerId = mPlayerId;
        beaconId = mBaeconId;
        option = mOption;

    }


    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
