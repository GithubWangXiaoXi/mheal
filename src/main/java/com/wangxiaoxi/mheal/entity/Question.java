package com.wangxiaoxi.mheal.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Question implements Serializable{

    private String id;
    private String createTime;
    private String updateTime;
    private String content;
    private Integer viewCount;
    private Integer likes;
    private boolean anonymous;
    private boolean status;
    private Student student;
    private List<AskAndAns> askAndAnsList;
    private List<QuestionAndTag> questionAndTags;

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", content='" + content + '\'' +
                ", viewCount=" + viewCount +
                ", likes=" + likes +
                ", anonymous=" + anonymous +
                ", status=" + status +
                ", student=" + student +
                ", askAndAnsList=" + askAndAnsList +
                ", questionAndTags=" + questionAndTags +
                '}';
    }

    public List<QuestionAndTag> getQuestionAndTags() {
        return questionAndTags;
    }

    public void setQuestionAndTags(List<QuestionAndTag> questionAndTags) {
        this.questionAndTags = questionAndTags;
    }

    public List<AskAndAns> getAskAndAnsList() {
        return askAndAnsList;
    }

    public void setAskAndAnsList(List<AskAndAns> askAndAnsList) {
        this.askAndAnsList = askAndAnsList;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
