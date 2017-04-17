package com.baecon.rockpaperscissorsapp.model;

import com.baecon.rockpaperscissorsapp.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("drawns")
    @Expose
    private Integer drawns;
    @SerializedName("losses")
    @Expose
    private Integer losses;
    @SerializedName("wins")
    @Expose
    private Integer wins;
    @SerializedName("rockCount")
    @Expose
    private Integer rockCount;
    @SerializedName("rockWinCount")
    @Expose
    private Integer rockWinCount;
    @SerializedName("scissorCount")
    @Expose
    private Integer scissorCount;
    @SerializedName("scissorWinCount")
    @Expose
    private Integer scissorWinCount;
    @SerializedName("paperCount")
    @Expose
    private Integer paperCount;
    @SerializedName("paperWinCount")
    @Expose
    private Integer paperWinCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getDrawns() {
        return drawns;
    }

    public void setDrawns(Integer drawns) {
        this.drawns = drawns;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getRockCount() {
        return rockCount;
    }

    public void setRockCount(Integer rockCount) {
        this.rockCount = rockCount;
    }

    public Integer getRockWinCount() {
        return rockWinCount;
    }

    public void setRockWinCount(Integer rockWinCount) {
        this.rockWinCount = rockWinCount;
    }

    public Integer getScissorCount() {
        return scissorCount;
    }

    public void setScissorCount(Integer scissorCount) {
        this.scissorCount = scissorCount;
    }

    public Integer getScissorWinCount() {
        return scissorWinCount;
    }

    public void setScissorWinCount(Integer scissorWinCount) {
        this.scissorWinCount = scissorWinCount;
    }

    public Integer getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(Integer paperCount) {
        this.paperCount = paperCount;
    }

    public Integer getPaperWinCount() {
        return paperWinCount;
    }

    public void setPaperWinCount(Integer paperWinCount) {
        this.paperWinCount = paperWinCount;
    }

}