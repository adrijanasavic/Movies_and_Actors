package com.example.movies_and_actors.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService1 {

    public static Retrofit getRetrofitInstance() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyServiceContract.BASE_URL1)
                .addConverterFactory( GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static MyApiEndpointInterface apiInterface() {
        MyApiEndpointInterface apiService = getRetrofitInstance().create(MyApiEndpointInterface.class);

        return apiService;
    }
}
