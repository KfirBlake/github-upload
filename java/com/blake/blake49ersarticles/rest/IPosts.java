package com.blake.blake49ersarticles.rest;

import com.blake.blake49ersarticles.rest.model.*;

//import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kfir Blake on 08/10/2020.
 */
public interface IPosts {

    //@GET("/rsn-api/team/recent-posts/127")
    //Call<List<Posts>> getArticleList();

    @GET("/rsn-api/team/recent-posts/127")
    Call<PostsBody> getPosts();

}
