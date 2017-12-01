package com.example.acer.movieandroidkejar.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ACER on 01/12/2017.
 */

public class ApiClient {

    public static ApiRequestInterface service(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiRequestInterface.class);

    }
}
