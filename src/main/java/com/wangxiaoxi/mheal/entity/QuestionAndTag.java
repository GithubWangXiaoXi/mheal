package com.wangxiaoxi.mheal.entity;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-23 00:26
 **/
//一个问题对应多个标签
public class QuestionAndTag {

    private int id;
    private String q_id;
    private Tag tag;

    @Override
    public String toString() {
        return "QuestionAndTag{" +
                "id=" + id +
                ", q_id='" + q_id + '\'' +
                ", tag=" + tag +
                '}';
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }
}
