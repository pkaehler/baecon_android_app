package com.baecon.rockpaperscissorsapp.rest;

import com.baecon.rockpaperscissorsapp.model.Move;
import com.baecon.rockpaperscissorsapp.model.Stats;
import com.baecon.rockpaperscissorsapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("stats/{id_player}")
    Call<Stats> getStats(@Path("id_player") int id);

    @POST("move")
    Call<Move> setMove(@Body Move move);

    @FormUrlEncoded
    @POST("registration")
    Call<User> createPlayer(@Field("name") String playername);

}
