package com.wangxiaoxi.mheal.entity;

import java.util.HashMap;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-19 18:14
 **/
public class AskAndAns {

    private Integer id;
    private String q_id;
    private Doctor doctor;
    private Student student;
    private String answer;
    private String updateTime;

    @Override
    public String toString() {
        return "AskAndAns{" +
                "id=" + id +
                ", q_id='" + q_id + '\'' +
                ", doctor=" + doctor +
                ", student=" + student +
                ", answer='" + answer + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
