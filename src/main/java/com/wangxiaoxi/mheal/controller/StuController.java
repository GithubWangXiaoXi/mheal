package com.wangxiaoxi.mheal.controller;

import com.wangxiaoxi.mheal.bean.ChatFriends;
import com.wangxiaoxi.mheal.bean.Login;
import com.wangxiaoxi.mheal.entity.*;
import com.wangxiaoxi.mheal.service.*;
import com.wangxiaoxi.mheal.service.cacheService.UserCacheService;
import com.wangxiaoxi.mheal.util.exception.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class StuController {

    @Autowired
    private QuesService quesService;

    @Autowired
    private StuService stuService;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ChatFriendsService chatFriendsService;

    /**
     * 跳转到学生主页
     * @param student
     * @return
     */
    @GetMapping(value = "/stu/toHomePage")
    public String toHomePage(Student student,HttpServletRequest request){
        System.out.println("主页");
        student = stuService.getStuById(student.getId());

        //往session中存入学生聊天帐号
        Login login = loginService.getLoginFromStu(student);
        String userid = loginService.justLogin(login);
        request.getSession().setAttribute("userid",userid);

        request.getSession().setAttribute("student",student);
        System.out.println(student);

        //个人信息注册到redis中
        if(!userCacheService.isStuExist(student.getId())){
            userCacheService.insertStu(student.getId());
        }

        //获取在线医生
        List<Doctor> doctorsOnline = userCacheService.getDoctorsOnline();
        request.setAttribute("doctorsOnline",doctorsOnline);

        //获取离线医生
        List<Doctor> doctorsOffline = doctorService.getDoctorsOffline();
        request.setAttribute("doctorsOffline",doctorsOffline);

        return "/stu/home";
    }

    /**
     * 跳转到登录页面
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/stu")
    public String loginPage(HttpServletRequest httpServletRequest){
        String s = httpServletRequest.getParameter("success");
        httpServletRequest.setAttribute("success",s);
        return "/user/sign-in-stu";
    }

    /**
     * 跳转到注册页面
     * @return
     */
    @GetMapping(value = "/stu1")
    public String registerPage(){
        return "/user/sign-up-stu";
    }

    /**
     * 重定向到注册页面
     * @return
     */
    @GetMapping(value = "/stu/toReg")
    public String toRegisterPage(){
        return "redirect:/stu1";
    }

    /**
     * 完成注册，跳转到登录页面
     * @param student
     * @return
     */
    @PostMapping(value = "/stu/register")
    public void register(Student student){
        System.out.println("提交数据" + student);
        //插入学生登录帐号
        stuService.insertStu(student);
        //插入学生聊天帐号
        stuService.insertLogin(student);
    }

    /**
     * 校验学生身份（id）
     * @return
     */
    @GetMapping(value = "/stu/stuChecked1")
    @ResponseBody
    public Student stuChecked1(Student student){
        System.out.println(student.getId());
        return stuService.stuChecked1(student);
    }

    /**
     * 校验学生身份（id + password）
     * @return
     */
    @GetMapping(value = "/stu/stuChecked")
    @ResponseBody
    public Student stuChecked(Student student){
        System.out.println(student.getId() + " ------- " + student.getPassword());
        return stuService.stuChecked(student);
    }


    /**
     * 跳转到我的问题页面
     * @return
     */
    @GetMapping(value = "/stu/toQuesPage")
    public String toQuesPage(HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("student");
        System.out.println("toQuesPage " + student);
//        request.setAttribute("student",student);

        List<Question> questions = quesService.getQuestionByStu(student);
        request.setAttribute("questions",questions);
        System.out.println(questions);
        return "/stu/question";
    }


    /**
     * 提交问题
     * @param question
     * @param request
     * @return
     */
    @PostMapping(value = "/stu/question")
    public String submitQuestion(Question question, HttpServletRequest request){
        System.out.println(question);
        Student student = (Student) request.getSession().getAttribute("student");
//        request.setAttribute("student",student);
        quesService.insertQues(question,student);
        request.setAttribute("success","success");
        return "redirect:/stu/toQuesPage";
    }


    /** 
    * @Description: 跳转到问答社区 
    * @Param:  
    * @return:  
    * @Author: wangxiaoxi
    * @Date: 2020/3/1 0001 
    */
    @GetMapping(value = "/stu/toAnsHood")
    public String toAnsHood(HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("student");
        System.out.println("toAnsHood " + student);
//        request.setAttribute("student",student);
        //问题日期
        List<String> dates = quesService.getDates();
        request.setAttribute("dates",dates);

        //标签
        List<Tag> tags = quesService.getTags();
        request.setAttribute("tags",tags);

        return "/stu/answerHood";
    }
    
    /** 
    * @Description: 跳转到活动社区
    * @Param:  
    * @return:  
    * @Author: wangxiaoxi
    * @Date: 2020/3/1 0001 
    */
    @GetMapping(value = "/stu/toAcitvities")
    public String toActivityPage(HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("student");
        System.out.println("toAcitvities " + student);
//        request.setAttribute("student",student);
        return "/stu/activities";
    }

    /**
    * @Description: 跳转到在线聊天页面
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/2 0002
    */
    @GetMapping(value = "/stu/toChatPage")
    public String toChatPage(HttpServletRequest request){
        Student student = (Student) request.getSession().getAttribute("student");
        System.out.println("toChatPage " + student);
//        request.setAttribute("student",student);

        //获取聊天医生id 和 图像编号
        String doctorId = request.getParameter("doctorId");
        Doctor doctor = doctorService.getDoctorById(doctorId);
        request.setAttribute("doctor",doctor);
        request.setAttribute("iconNum",request.getParameter("iconNum"));

        //得到学生和所有医生的聊天关系
        List<String> chatFriendsId = chatFriendsService.getFriendsId(student.getId());
        //判断该医生是否和学生已经建立聊天关系
        for (String id :chatFriendsId) {
            if(id.equals(doctorId)){
                return "/chat/chats";
            }
        }
        //建立学生和医生之间的聊天关系
        chatFriendsService.setChatFriends(student.getId(),doctorId);

//        return "/stu/chat";
        return "/chat/chats";
    }

    @GetMapping("/stu/toEvent")
    public String toEvent(){
        return "/stu/events";
    }

//    /**
//    * @Description: 返回到学生登录页面
//    * @Param:
//    * @return:
//    * @Author: wangxiaoxi
//    * @Date: 2020/3/7 0007
//    */
//    @GetMapping(value = "/stu/return")
//    public String returnPage(HttpServletRequest request){
//        request.getSession().setAttribute("student",null);
//        return "redirect:/stu";
//    }

    //获取对应日期的已回答的问题
    @GetMapping("/stu/getQuesByDate")
    @ResponseBody
    public List<Question> getQuesByDate(HttpServletRequest request){
        String dateTime = request.getParameter("datetime");
        List<Question> questionByDate = quesService.getQuestionByDate(dateTime);
        System.out.println(questionByDate);
        return questionByDate;
    }

    //获取对应标签的已回答的问题
    @GetMapping("/stu/getQuesByTag")
    @ResponseBody
    public List<Question> getQuesByTag(HttpServletRequest request){
        String t_id = request.getParameter("tag");
        Tag tag = new Tag();
        tag.setId(t_id);
        List<Question> questions = quesService.getQuestionByTag(tag);
        System.out.println(questions);
        return questions;
    }

    /**
     * @Description: 跳转浏览问题页面
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/2 0002
     */
    @GetMapping(value = "/stu/toViewPage")
    public ModelAndView toAnsPage(Question question, HttpServletRequest request){
        System.out.println("toViewPage");

        ModelAndView modelAndView = new ModelAndView();
        question = quesService.getQuestionById(question.getId());
        System.out.println(question);

        modelAndView.addObject("question",question);
        modelAndView.addObject("student",question.getStudent());

        modelAndView.addObject("askAndAns_s",question.getAskAndAnsList());
        modelAndView.addObject("questionAndTags",question.getQuestionAndTags());
        modelAndView.setViewName("/stu/viewQuestion");

        return modelAndView;
    }

    /**
    * @Description: 获取在线医生
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/26 0026
    */
    @GetMapping("/stu/getDoctorsOnline")
    @ResponseBody
    public List<Doctor> getDoctorsOnline(HttpServletRequest request){
        //查询在线医生
        List<Doctor> doctorsOnline = userCacheService.getDoctorsOnline();
        String doctorId = request.getParameter("doctorId");

        //在遍历list的过程中，如果修改了元素，会导致list中索引与对应的值不同，因此抛出java.util.ConcurrentModificationException 异常
//        for(Doctor doctor : doctorsOnline){
//            if(doctor.getId().equals(doctorId)){
//                doctorsOnline.remove(doctor);
//            }
//        }
        for (int i = 0; i < doctorsOnline.size(); i++) {
            if(doctorsOnline.get(i).getId().equals(doctorId)){
                doctorsOnline.remove(i);
                i--;
            }
        }

        System.out.println("在线" + doctorsOnline);
        return doctorsOnline;
    }

    @GetMapping("/stu/getDoctorsOffline")
    @ResponseBody
    public List<Doctor> getDoctorsOffline(HttpServletRequest request){
        //查询离线医生
        List<Doctor> doctorsOffline = doctorService.getDoctorsOffline();
        System.out.println("离线" + doctorsOffline);
        return doctorsOffline;
    }

    @GetMapping("/stu/getContentList")
    @ResponseBody
    public List<Content> getContentList(HttpServletRequest request){

        //查询聊天记录
        String date = "2020-3-27";
        String s_id = request.getParameter("s_id");
        String d_id = request.getParameter("d_id");
        return userCacheService.getContentList(date,s_id,d_id);
    }

    @PostMapping("/stu/submitContent")
    public void submitContent(HttpServletRequest request){
        String s_id = request.getParameter("s_id");
        String d_id = request.getParameter("d_id");
        String content = request.getParameter("content");
        userCacheService.insertStuContent(s_id,d_id,content);
    }
}
