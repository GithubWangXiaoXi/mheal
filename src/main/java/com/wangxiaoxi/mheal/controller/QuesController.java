package com.wangxiaoxi.mheal.controller;

import com.wangxiaoxi.mheal.entity.PageData;
import com.wangxiaoxi.mheal.entity.Question;
import com.wangxiaoxi.mheal.entity.Tag;
import com.wangxiaoxi.mheal.service.QuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-08 13:27
 **/
@Controller
public class QuesController {

    @Autowired
    private QuesService quesService;

//    @Autowired
//    private PageData<Question> questionPageData;

    /**
     * 跳转到问题社区页面
     * @param
     * @param
     * @return
     */
    @GetMapping(value = "/question/toQuesHoodPage")
    public String toQuesHood(HttpServletRequest servletRequest){
        System.out.println("toQuesHood");
        return "/doctor/questionHood";
    }

    /**
    * @Description: 返回pageData的json数据
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/12 0012
    */
    @GetMapping(value = "/question/pageData")
    @ResponseBody
    public PageData<Question> getPageData(PageData<Question> pageData,HttpServletRequest servletRequest){
        System.out.println("getPageData");

        //设置每页数据条数
        pageData.setPageSize(5);

        pageData = quesService.getPageData(pageData);
        System.out.println(pageData);

        //注意limit语法：select * from table limit (start-1)*pageSize,pageSize
        int begin = (pageData.getPageNo() - 1) * pageData.getPageSize();
        int end  = pageData.getPageSize();
        List<Question> questions = quesService.getQuestions(pageData,begin,end);

        pageData.setResult(questions);
        System.out.println(pageData);
        return pageData;
    }

    //自定义标签
    @PostMapping("/question/submitTag")
    public void submitTag(HttpServletRequest request){
        Tag tag = new Tag();
        String tagName = request.getParameter("tagName");
        tag.setTagName(tagName);
        quesService.insertTag(tag);
    }

    @GetMapping("/question/getTags")
    @ResponseBody
    public List<Tag> getTags(){
        List<Tag>tags = quesService.getTags();
        System.out.println(tags);
        return tags;
    }

    @GetMapping("/question/getTagByName")
    @ResponseBody
    public String getTagByName(HttpServletRequest request){
        String tagName = request.getParameter("tagName");
        System.out.println(tagName);
        tagName = quesService.getTagByName(tagName);
        System.out.println(tagName);
        return tagName;
    }
}
