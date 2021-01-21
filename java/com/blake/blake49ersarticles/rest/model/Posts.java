package com.blake.blake49ersarticles.rest.model;

/**
 * Created by Kfir Blake on 08/10/2020.
 */
public class Posts {
    String nid;
    String title;
    String image;
    Long published;
    String type;
    String path;

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPublished() {
        return published;
    }

    public void setPublished(Long published) {
        this.published = published;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
