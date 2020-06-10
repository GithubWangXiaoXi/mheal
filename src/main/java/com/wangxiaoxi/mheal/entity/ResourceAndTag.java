package com.wangxiaoxi.mheal.entity;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-30 22:50
 **/
public class ResourceAndTag {

    private Integer id;
    private String r_id;
    private String t_id;

    @Override
    public String toString() {
        return "ResourceAndTag{" +
                "id=" + id +
                ", r_id='" + r_id + '\'' +
                ", t_id='" + t_id + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }
}
