package com.deathstudio.marcos.demoooooo.retrofitservice;

import com.deathstudio.marcos.demoooooo.pojo.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface PokemonService {

    @GET("pokemon")
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit") int limit, @Query("offset") int offset);
}

