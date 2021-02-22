package com.blokys.bttv;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface Service {
    @GET("emotes/shared/top?offset=0&limit=100")
    Call<List<ServiceItem>> loadEmotes();
}