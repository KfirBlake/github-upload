package com.blake.blake49ersarticles.rest.model;

import java.util.List;

/**
 * Created by Kfir Blake on 08/10/2020.
 */
public class PostsBody {
    private List<Posts> data;

    public List<Posts> getData() {
        return data;
    }

    public void setData(List<Posts> data) {
        this.data = data;
    }
}
