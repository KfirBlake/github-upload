package com.blake.blake49ersarticles.rest;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kfir Blake on 08/10/2020.
 */
public class RestManager {
    private static final RestManager instance = new RestManager();

    private IPosts postsResult;

    public static RestManager getInstance()
    {
        return instance;
    }
    private RestManager()
    {
        postsResult = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(new Gson()))
                .baseUrl("https://www.nbcsports.com").build().create(IPosts.class);
    }

    public IPosts getPostsResult()
    {
        return postsResult;
    }
}
