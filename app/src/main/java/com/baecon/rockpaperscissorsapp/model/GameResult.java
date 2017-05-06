package com.baecon.rockpaperscissorsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GameResult {
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("result")
    @Expose
    private String result;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
