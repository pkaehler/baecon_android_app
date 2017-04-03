package com.baecon.rockpaperscissorsapp.model;

import com.baecon.rockpaperscissorsapp.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("alpha2_code")
    @Expose
    private String alpha2Code;
    @SerializedName("alpha3_code")
    @Expose
    private String alpha3Code;

    private int imageResourceId;

    public Result(String name, String alpha2Code, String alpha3Code){
        this.name = name;
        this.alpha2Code = alpha2Code;
        this.alpha3Code = alpha3Code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public int getImageResourceId(String alpha2Code){
        if (alpha2Code.equals("DE")) {
            imageResourceId = R.drawable.rock;
        } else if (alpha2Code.equals("DZ")) {
            imageResourceId = R.drawable.paper;
        } else if (alpha2Code.equals("NE")) {
            imageResourceId = R.drawable.scissors;
        } else
            imageResourceId = R.drawable.defaulticon;
        return imageResourceId;
    }

}