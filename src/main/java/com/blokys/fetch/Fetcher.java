package com.blokys.fetch;

import com.blokys.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class Fetcher implements Callback<List<Emote>>
{
    public Fetcher()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://twitch-emotes-warehouse.blokhuis.dev/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Service call = retrofit.create(Service.class);

        Call<List<Emote>> async = call.loadEmotes();

        async.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Emote>> call, Response<List<Emote>> response)
    {
        if (response.body() == null)
        {
            System.out.println("Couldn't fetch emotes because body is empty");
            return;
        }

        Store.setEmotes(
                new ArrayList<>(response.body())
        );
    }

    @Override
    public void onFailure(Call<List<Emote>> call, Throwable t)
    {
        System.out.println("Couldn't fetch emotes because request failed");
    }
}
