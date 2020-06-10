package com.wangxiaoxi.mheal.service;

import com.wangxiaoxi.mheal.entity.*;
import com.wangxiaoxi.mheal.mapper.QuestionMapper;
import com.wangxiaoxi.mheal.service.cacheService.QuesCacheService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QuesService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private RedisTemplate<String,Question> redisTemplate;

    //更新数据库中问题的数目，不必每次都到数据库中查找问题数目
    private static Integer count;

    @Autowired
    private QuesCacheService quesCacheService;
    private PageData<Question> pageData;

    public Question getQuestionById(String id){
        return questionMapper.getQuestionById(id);
    }

    /**
    * @Description: 先将数据放在阻塞队列中，然后一一计数，并写入数据库   （待更新）
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/13 0013
    */
    public static Integer getCount() {
        return count;
    }

    public static void setCount(Integer count) {
        QuesService.count = count;
    }

    @Transactional
    public void insertQues(Question question,Student student){
        question.setId(UUID.randomUUID().toString());
        //System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
        question.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
        question.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
        question.setViewCount(0);
        question.setLikes(0);

        //插入问题
        questionMapper.insertQues(question);

        //插入中间表
        questionMapper.insertQuesWithStu(question.getId(),student.getId());

        //检查dateList中是否有该日期
        List<String> dateList = quesCacheService.getDateList();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp = dateFormat.format(new Date(System.currentTimeMillis()));
        //设置dateList的过期时间
        if(dateList.size() != 0){
            if(!dateList.contains(temp)){
                quesCacheService.setDateListExpire();
                //插入时间到dates表
                questionMapper.insertdateTime(new Date(System.currentTimeMillis()));
            }
        }

    }

    /**
     * @Description: 查找问题社区中问题总数
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/8 0008
     */
    public Integer getQuesCount(){
        return questionMapper.getQuesCount();
    }

    /**
    * @Description: 分页查询得到问题列表，并将问题按日期保存在redis中。
    *               方便在redis中按日期查询。
    * @Param: begin,end
    * @return: List<Question>
    * @Author: wangxiaoxi
    * @Date: 2020/3/8 0008
    */
    public List<Question> getQuestions(PageData<Question> pageData, Integer begin, Integer end){

         Integer pageNo = pageData.getPageNo();
        //若查找的是前4页数据，并且该数据在缓存中，则从缓存中取,并返回指定页的问题集合
        if(!quesCacheService.isQuesEmpty() && begin < 4 * pageData.getPageSize()){
            System.out.println("redis");
            int pageNum = pageData.getPageNo();

            List<Question> questions = quesCacheService.getQuesByPage(pageNum - 1);

            return questions;
        }
        //先到数据库中，按日期先取4页数据
        else if(begin < 4 * pageData.getPageSize()){
            System.out.println("sql");
            List<Question> questions = questionMapper.getQuestions(0, 4 * pageData.getPageSize());

            String time;
            for (Question question: questions) {
                time = question.getUpdateTime().split(" ")[0];

                time = "ques:" + time;
                /**------------------------------将问题按日期插入到redis,数据类型为set，并设置过期时间------------------------------------*/
                quesCacheService.insertQuesByDate(time,question);

                /**------------------------------将日期插入到redis，数据类型为list，并设置过期时间------------------------------------*/
//                System.out.println(!quesCacheService.isQuesDateEmpty(time) + "  " + time);
                if(!quesCacheService.isQuesDateEmpty(time)){
                    quesCacheService.insertQuesDate(time);
                    quesCacheService.setQuesExpire(time,1);
                }
            }

            quesCacheService.setDateExpire("ques:date",1);

            /**------------------------------根据问题集合生成分页表，数据类型是hash，并设置过期时间 ------------------------------------*/
            List<String> dates = quesCacheService.getQuesDate();

            for (String date: dates) {
                Set<Question> quesSet = quesCacheService.getQuesByDate(date);
                pageData = quesCacheService.insertQuesPage(pageData,date,quesSet);

                //若pageData的indexEnd不是-1，则该集合还有元素未遍历，需要重新再来
                while(pageData.getIndexEnd() != -1){
                    pageData = quesCacheService.insertQuesPage(pageData,date,quesSet);
                }
            }

            //hash表生成成功,将缓存数据个数count置0
            QuesCacheService.setCount(0);

            //设置页面过期时间
            setPagesExpire(pageData);

            return quesCacheService.getQuesByPage(pageNo - 1);
        }
        else{
            System.out.println("sql");
            List<Question> questions = questionMapper.getQuestions(begin, end);
            return questions;
        }
    }


    /** 
    * @Description: 得到pageData的基本信息 
    * @Param:  
    * @return:  
    * @Author: wangxiaoxi
    * @Date: 2020/3/13 0013 
    */
    public PageData<Question> getPageData(PageData<Question> pageData) {

        //count每次需要到数据库中取数据，保证数据的时效性
        count = questionMapper.getQuesCount();
        pageData.setTotalCount(count);
        if(count % pageData.getPageSize() == 0){
            pageData.setPageCount(count / pageData.getPageSize());
        }else{
            pageData.setPageCount(count / pageData.getPageSize() + 1);
        }
        if(pageData.getPageCount() == 0){
            pageData.setPageCount(1);
        }
        return pageData;
    }


    public void setPagesExpire(PageData<Question> pageData){
        //设置页面过期时间
        for (int i = 0; i < pageData.getPageCount(); i++) {
            quesCacheService.setPageExpire("question:" + (i),1);
        }
    }

    public void updateAns(AskAndAns askAndAns, QuestionAndTag questionAndTag) {

        String dId = questionMapper.isDidEmpty(askAndAns);

        askAndAns.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));

        //dId为空，更新即可
        if(dId == null){
            questionMapper.updateAns(askAndAns);
        }
        //dId不空，需要插入
        else{
            questionMapper.insertAns(askAndAns);
        }

        //插入问题和标签映射
        questionMapper.insertQuesWithTag(questionAndTag.getQ_id(),questionAndTag.getTag().getId());
    }

    public void insertTag(Tag tag) {
        String s = UUID.randomUUID().toString();
        tag.setId(s);
        questionMapper.insertTag(tag);
    }

    public List<Tag> getTags() {

        //先到redis中查询
//        List<Tag> tags = quesCacheService.getTags();
//        if(tags.size() != 0){
//            System.out.println("redis");
//            return tags;
//        }else{
        List<Tag> tags = quesCacheService.getTags();
        tags = questionMapper.getTags();
        System.out.println("sql");
        quesCacheService.insertTags(tags);
        return tags;
//        }
    }

    public String getTagByName(String tagName) {
        return questionMapper.getTagByName(tagName);
    }

    //给没有标签的问题，在中间表上加上标签-问题映射
    public void updateQuesWithTag(){
        List<Question> questions = questionMapper.getQuestions(0, 40);
        for(Question question : questions){
            questionMapper.updateQuesWithTag(question);
        }
    }

    public List<Question> getQuestionByStu(Student student) {
        List<Question> questions = new ArrayList<>();
        List<String> questionsId = questionMapper.getQuestionByStu(student.getId());
        for(String id : questionsId){
            questions.add(questionMapper.getQuestionById(id));
        }
        return questions;
    }

    public void setQuesStatus(String q_id) {
        questionMapper.setQuesStatus(q_id);
    }

    //得到所有问题的时间范围
    public List<String> getDates(){

        //先从缓存中取
        List<String> dateList = quesCacheService.getDateList();
        if(dateList.size() != 0){
            System.out.println("redis");
            return dateList;
        }else{
            System.out.println("sql");
            List<String> dates = questionMapper.getDates();
            //将其写入到缓存中
            quesCacheService.insertDates(dates);
            return dates;
        }
    }

    //按日期查询问题
    public List<Question> getQuestionByDate(String datetime){
        return questionMapper.getQuestionByDate(datetime);
    }

    //按标签查询问题
    public List<Question> getQuestionByTag(Tag tag){
        List<Question> questions = new ArrayList<>();
        List<String> q_ids = questionMapper.getQuestionByTag(tag);
        for (String q_id : q_ids) {
            Question que = questionMapper.getQuestionById1(q_id);
            if(que != null){
                questions.add(que);
            }
        }
        return questions;
    }
}
