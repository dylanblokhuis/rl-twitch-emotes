package com.blokys.fetch;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface Service
{
    @GET("v1/emotes")
    Call<List<Emote>> loadEmotes();
}