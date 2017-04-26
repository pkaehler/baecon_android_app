package com.baecon.rockpaperscissorsapp.rest;

import com.baecon.rockpaperscissorsapp.model.Move;
import com.baecon.rockpaperscissorsapp.model.Stats;
import com.baecon.rockpaperscissorsapp.model.User;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("stats/{id_player}")
    Call<Stats> getStats(@Path("id_player") int id_player);

    @GET("game/{id_game}/{id_player}")
    String getGameOutcome(@Path("id_game") int id_game, @Path("id_player") int id_player);

    @FormUrlEncoded
    @POST("move")
    Call<Move> setMove(@Field("beaconId") String beaconId, @Field("playerId") int playerId, @Field("option") String option);

    @FormUrlEncoded
    @POST("registration")
    Call<User> createPlayer(@Field("name") String playerName);

    @GET("isValidBeacon/{id_beacon}")
    Call<String> isValidBeacon(@Path("id_beacon") String id_beacon);



}
