package com.blokys.bttv;

import com.blokys.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.stream.Collectors;

public class BetterTwitchTV implements Callback<List<ServiceItem>> {
    public BetterTwitchTV() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.betterttv.net/3/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Service call = retrofit.create(Service.class);

        Call<List<ServiceItem>> async = call.loadEmotes();

        async.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<ServiceItem>> call, Response<List<ServiceItem>> response) {
        if (response.body() == null) {
            System.out.println("Couldn't fetch emotes because body is empty");
            return;
        }

        Store.setBttvEmotes(
            response.body().stream().map(item -> item.emote).collect(Collectors.toList())
        );
    }

    @Override
    public void onFailure(Call<List<ServiceItem>> call, Throwable t) {
        System.out.println("Couldn't fetch emotes because request failed");
    }
}
