package com.wangxiaoxi.mheal.controller;

import com.wangxiaoxi.mheal.bean.Login;
import com.wangxiaoxi.mheal.entity.*;
import com.wangxiaoxi.mheal.service.DoctorService;
import com.wangxiaoxi.mheal.service.LoginService;
import com.wangxiaoxi.mheal.service.QuesService;
import com.wangxiaoxi.mheal.service.StuService;
import com.wangxiaoxi.mheal.service.cacheService.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-02 12:41
 **/
@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private QuesService quesService;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private StuService stuService;

    @Autowired
    private LoginService loginService;

    /**
    * @Description: 跳转到doctor登录页面
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/2 0002
    */
    @GetMapping("/doctor")
    public String loginPage(){
        System.out.println("sign-in");
        return "/user/sign-in-doctor";
    }

    /**
    * @Description: 跳转到doctor注册页面
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/2 0002
    */
    @GetMapping("/doctor1")
    public String registerPage(){
        System.out.println("sign-up");
        return "/user/sign-up-doctor";
    }

    /**
     * 校验医生身份（id）
     * @return
     */
    @GetMapping(value = "/doctor/doctorChecked1")
    @ResponseBody
    public Doctor stuChecked1(Doctor doctor){
        System.out.println(doctor.getId());
        return doctorService.doctorChecked1(doctor);
    }

    /**
     * 校验医生身份（id + password）
     * @return
     */
    @GetMapping(value = "/doctor/doctorChecked")
    @ResponseBody
    public Doctor stuChecked(Doctor doctor){
        System.out.println(doctor.getId() + " ------- " + doctor.getPassword());
        return doctorService.doctorChecked(doctor);
    }


    /**
     * 跳转到主页
     * @param doctor
     * @param request
     * @return
     */
    @GetMapping(value = "/doctor/toHomePage")
    public String toHomePage(Doctor doctor,HttpServletRequest request){
        System.out.println("主页");
        doctor = doctorService.getDoctorById(doctor.getId());
        request.getSession().setAttribute("doctor",doctor);
        System.out.println(doctor);

        //往session中存入医生聊天帐号
        Login login = loginService.getLoginFromDoctor(doctor);
        String userid = loginService.justLogin(login);
        request.getSession().setAttribute("userid",userid);

        //个人信息注册到redis中
        if(!userCacheService.isDoctorExist(doctor.getId())){
            userCacheService.insertDoctor(doctor.getId());
        }

        //获取在线学生
        List<Student> studentsOnline = userCacheService.getStudentsOnline();
        System.out.println("studentOnline" + studentsOnline);
        request.setAttribute("studentsOnline",studentsOnline);

        //获取离线学生
        List<Student> studentsOffline = doctorService.getStudentsOffline();
        System.out.println("studentsOffline" + studentsOffline);
        request.setAttribute("studentsOffline",studentsOffline);

        return "/doctor/home";
    }

    /**
     * @Description: 跳转到在线聊天页面
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/2 0002
     */
    @GetMapping(value = "/doctor/toChatPage")
    public String toChatPage(HttpServletRequest request){
        Doctor doctor = (Doctor) request.getSession().getAttribute("doctor");
        System.out.println("toChatPage " + doctor);
//        request.setAttribute("student",student);

        //获取聊天医生id 和 图像编号
        String studentId = request.getParameter("studentId");
        request.setAttribute("student",stuService.getStuById(studentId));
        request.setAttribute("iconNum",request.getParameter("iconNum"));

//        //获取在线医生
//        List<Doctor> doctorsOnline = userCacheService.getDoctorsOnline();
//        for(Doctor doctor : doctorsOnline){
//            if(doctor.getId().equals(doctorId)){
//                doctorsOnline.remove(doctor);
//            }
//        }
//        request.setAttribute("doctorsOnline",doctorsOnline);
//
//        //获取离线医生
//        List<Doctor> doctorsOffline = doctorService.getDoctorsOffline();
//        request.setAttribute("doctorsOffline",doctorsOffline);

        return "/chat/chats";

//        return "/doctor/chat";
    }

    /**
     * 完成注册，跳转到登录页面
     * @param doctor
     * @return
     */
    @PostMapping(value = "/doctor/register")
    public void register(Doctor doctor){
        System.out.println("提交数据" + doctor);
        doctorService.insertDoctor(doctor);
    }

    /**
     * 跳转到我的回答页面
     * @param doctor
     * @param request
     * @return
     */
    @GetMapping(value = "/doctor/toContactPage")
    public String toContactPage(Doctor doctor,HttpServletRequest request){
        System.out.println("toContactPage");
        doctor = doctorService.getDoctorById(doctor.getId());
        return "/doctor/contact";
    }

    /**
     * 跳转到问题社区页面
     * @param doctor
     * @param request
     * @return
     */
    @GetMapping(value = "/doctor/toQuesHood")
    public String toQuesHood(Doctor doctor,HttpServletRequest request){
        System.out.println("toQuesHood");
        doctor = doctorService.getDoctorById(doctor.getId());
        return "/doctor/questionHood";
    }

    /**
     * 跳转到问答页面
     * @param doctor
     * @param request
     * @return
     */
    @GetMapping(value = "/doctor/toAnsHood")
    public String toAnsHood(Doctor doctor,HttpServletRequest request){
        System.out.println("toAnsHood");
        doctor = doctorService.getDoctorById(doctor.getId());

        //问题日期
        List<String> dates = quesService.getDates();
        request.setAttribute("dates",dates);

        //标签
        List<Tag> tags = quesService.getTags();
        request.setAttribute("tags",tags);

        return "/doctor/answerHood";
    }

    /**
    * @Description: 跳转到回答页面
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/2 0002
    */
    @GetMapping(value = "/doctor/toAnsPage")
    public ModelAndView toAnsPage(Question question, HttpServletRequest request){
        System.out.println("toAnsPage");

        ModelAndView modelAndView = new ModelAndView();
        question = quesService.getQuestionById(question.getId());
        System.out.println(question);

        modelAndView.addObject("question",question);
        modelAndView.addObject("student",question.getStudent());

        modelAndView.addObject("askAndAns_s",question.getAskAndAnsList());
        modelAndView.addObject("questionAndTags",question.getQuestionAndTags());
        modelAndView.setViewName("/doctor/answer");

        return modelAndView;
    }

    /**
    * @Description: 跳转到学习社区
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/3 0003
    */
    @GetMapping(value = "/doctor/toStudyPage")
    public String toStudyPage(HttpServletRequest request){
        Doctor doctor = (Doctor) request.getSession().getAttribute("doctor");
        System.out.println("toStudyPage " + doctor);
        return "/study/study";
    }

    /**
    * @Description: 跳转到文章区
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/3 0003
    */
    @GetMapping(value = "/doctor/toDocPage")
    public String toDocPage(HttpServletRequest request){
        Doctor doctor = (Doctor) request.getSession().getAttribute("doctor");
        System.out.println("toDocPage " + doctor);
        return "/stu/document";
    }

    /**
     * @Description: 返回到医生登录页面
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/7 0007
     */
    @GetMapping(value = "/doctor/return")
    public String returnPage(HttpServletRequest request){
        request.getSession().setAttribute("doctor",null);
        return "redirect:/doctor";
    }


    /**
    * @Description: 提交回答，将回答写入中间表
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/19 0019
    */
    @PostMapping(value = "/doctor/submitAns")
    public String submitAns(HttpServletRequest request){

        String yourAnswer = (String) request.getParameter("yourAnswer");
        String question_id = (String) request.getParameter("question_id");

        String student_id = (String) request.getParameter("student_id");
        Student student = new Student();
        student.setId(student_id);

        Doctor doctor = (Doctor)request.getSession().getAttribute("doctor");
        String doctor_id = doctor.getId();

        //设置问题和医生回答映射
        AskAndAns askAndAns = new AskAndAns();
        askAndAns.setAnswer(yourAnswer);
        askAndAns.setDoctor(doctor);
        askAndAns.setQ_id(question_id);
        askAndAns.setStudent(student);

        //将该问题设置为已回答
        quesService.setQuesStatus(question_id);

        //设置问题和标签映射
        String t_id = request.getParameter("tag");
        QuestionAndTag questionAndTag = new QuestionAndTag();
        questionAndTag.setQ_id(question_id);
        Tag tag = new Tag();
        tag.setId(t_id);
        questionAndTag.setTag(tag);

        //更新问题和医生的映射
        quesService.updateAns(askAndAns,questionAndTag);

        System.out.println(yourAnswer + " " + question_id + " " + doctor_id);
        return null;
    }

    /**
     * @Description: 获取在线学生
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/26 0026
     */
    @GetMapping("/doctor/getStudentsOnline")
    @ResponseBody
    public List<Student> getDoctorsOnline(HttpServletRequest request){
        //查询在线医生
        List<Student> studentsOnline = userCacheService.getStudentsOnline();
        String studentId = request.getParameter("studentId");

        for (int i = 0; i < studentsOnline.size(); i++) {
            if(studentsOnline.get(i).getId().equals(studentId)){
                studentsOnline.remove(i);
                i--;
            }
        }

        System.out.println("在线" + studentsOnline);
        return studentsOnline;
    }

    //获取离线学生
    @GetMapping("/doctor/getStudentsOffline")
    @ResponseBody
    public List<Student> getDoctorsOffline(HttpServletRequest request){
        //查询离线医生
        List<Student> studentsOffline = doctorService.getStudentsOffline();
        System.out.println("离线" + studentsOffline);
        return studentsOffline;
    }


    @GetMapping("/doctor/getContentList")
    @ResponseBody
    public List<Content> getContentList(HttpServletRequest request){

        //查询聊天记录
        String date = "2020-3-27";
        String s_id = request.getParameter("s_id");
        String d_id = request.getParameter("d_id");
        List<Content> contentList = userCacheService.getContentList(date, s_id, d_id);
        System.out.println("聊天记录" + contentList);
        return contentList;
    }
}
