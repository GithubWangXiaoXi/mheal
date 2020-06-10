package com.wangxiaoxi.mheal.entity;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-22 18:37
 **/
public class Tag {

    private String id;
    private String tagName;

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
