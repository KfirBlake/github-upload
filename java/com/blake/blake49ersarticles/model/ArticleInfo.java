package com.blake.blake49ersarticles.model;

/**
 * Created by Kfir Blake on 08/10/2020.
 */
public class ArticleInfo {
    String id;
    String title;
    String description;
    Long time;
    String linkImage;
    String linkArticle;
    String site;
    boolean read;
    boolean favorite;

    public ArticleInfo() { }

    public ArticleInfo(String id, String title, String description, Long time, String linkImage, String linkArticle, String site, boolean read, boolean favorite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.linkImage = linkImage;
        this.linkArticle = linkArticle;
        this.site = site;
        this.read = read;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getLinkArticle() {
        return linkArticle;
    }

    public void setLinkArticle(String linkArticle) {
        this.linkArticle = linkArticle;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    @Override
    public String toString() {
        return "ArticleInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time.toString() + '\'' +
                ", linkImage='" + linkImage + '\'' +
                ", linkArticle='" + linkArticle + '\'' +
                ", site='" + site + '\'' +
                ", read=" + read +
                '}';
    }
}
