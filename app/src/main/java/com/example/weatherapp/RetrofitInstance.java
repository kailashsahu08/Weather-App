package com.example.weatherapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static RetrofitInstance ret;
    public ApiService api;
    public RetrofitInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("put your own open weather url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiService.class);
    }
    public static RetrofitInstance getInstance(){
        if(ret==null){
            ret = new RetrofitInstance();
        }
        return ret;
    }
}
