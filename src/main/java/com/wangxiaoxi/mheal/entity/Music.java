package com.wangxiaoxi.mheal.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-30 17:32
 **/
public class Music implements Serializable {

    private String id;
    private String title;
    private String author;
    private String href;
    private String bgm;
    private Date createTime;
    private Date updateTime;
    private Integer viewCount;
    private Integer likes;
    private List<Tag> tags;

    @Override
    public String toString() {
        return "Music{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", href='" + href + '\'' +
                ", bgm='" + bgm + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", viewCount=" + viewCount +
                ", likes=" + likes +
                ", tags=" + tags +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public String getBgm() {
        return bgm;
    }

    public void setBgm(String bgm) {
        this.bgm = bgm;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
