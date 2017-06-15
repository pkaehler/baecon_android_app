package com.baecon.rockpaperscissorsapp.rest;

import com.baecon.rockpaperscissorsapp.model.GameResult;
import com.baecon.rockpaperscissorsapp.model.Move;
import com.baecon.rockpaperscissorsapp.model.Stats;
import com.baecon.rockpaperscissorsapp.model.User;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {


    @GET
    Call startBackend();

    @GET("stats/{playerId}")
    Call<Stats> getStats(@Path("playerId") int playerId);

    @GET("game/{gameId}/{playerId}")
    Call<GameResult> getGameOutcome(@Path("gameId") int gameId, @Path("playerId") int playerId);

    @FormUrlEncoded
    @POST("move")
    Call<Move> setMove(@Field("beaconId") String beaconId, @Field("playerId") int playerId, @Field("option") String option);

    @FormUrlEncoded
    @POST("registration")
    Call<User> createPlayer(@Field("name") String playerName);

    @GET("isvalidbeacon/{beaconId}")
    Call<String> isvalidbeacon(@Path("beaconId") String beaconId);

    @GET("allgamesforplayer/{playerId}")
    Call<List<GameResult>> getAllGamesForPlayer(@Path("playerId") int playerId);

}
