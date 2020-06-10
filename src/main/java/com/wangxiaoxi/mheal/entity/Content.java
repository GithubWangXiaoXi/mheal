package com.wangxiaoxi.mheal.entity;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-27 00:30
 **/
public class Content {

    private Student student;
    private Doctor doctor;
    private String content;
    private String date;
    private String time;
    private Integer from_id;  //0表示学生，1表示医生

    @Override
    public String toString() {
        return "Content{" +
                "student=" + student +
                ", doctor=" + doctor +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", from_id=" + from_id +
                '}';
    }

    public Integer getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Integer from_id) {
        this.from_id = from_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
