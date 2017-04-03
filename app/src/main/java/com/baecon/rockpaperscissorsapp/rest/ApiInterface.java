package com.baecon.rockpaperscissorsapp.rest;

import com.baecon.rockpaperscissorsapp.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("get/iso2code/{alpha2_code}")
    Call<Example> getData(@Path("alpha2_code") String alpha2_code);

    @GET("search")
    Call<Example> getCountriesWithCode(@Query("text") String code);

}
