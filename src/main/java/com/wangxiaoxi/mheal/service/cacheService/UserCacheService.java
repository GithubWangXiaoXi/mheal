package com.wangxiaoxi.mheal.service.cacheService;

import com.wangxiaoxi.mheal.entity.*;
import com.wangxiaoxi.mheal.service.DoctorService;
import com.wangxiaoxi.mheal.service.StuService;
import com.wangxiaoxi.mheal.util.CompareContentByTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-08 11:24
 **/
@Service
public class UserCacheService {

    @Autowired
    private StringRedisTemplate redisUserTemplate; //加载个人在线信息，聊天信息进redis缓存

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private StuService stuService;

    //格式：user:stu,1614010822
    public void insertStu(String id){
        redisUserTemplate.opsForList().leftPush("user:stu",id);
        redisUserTemplate.expire("user:stu",5,TimeUnit.MINUTES);
    }

    //格式：user:doctor,161401080801
    public void insertDoctor(String id){
        redisUserTemplate.opsForList().leftPush("user:doctor",id);
        redisUserTemplate.expire("user:doctor",5,TimeUnit.MINUTES);
    }

    //检查该学生是否在线
    public Boolean isStuExist(String id){
        List<String> stu_ids = redisUserTemplate.opsForList().range("user:stu", 0, -1);
        if(stu_ids.contains(id)){
            return true;
        }else{
            return false;
        }
    }

    //检查该医生是否在线
    public Boolean isDoctorExist(String id){
        List<String> doctor_ids = redisUserTemplate.opsForList().range("user:doctor", 0, -1);
        if(doctor_ids.contains(id)){
            return true;
        }else{
            return false;
        }
    }

    //得到所有在线医生的id
    public List<String> getDoctorsIds(){
        List<String> doctor_ids = redisUserTemplate.opsForList().range("user:doctor", 0, -1);
        return doctor_ids;
    }

    //得到所有在线学生的id
    public List<String> getStudentsIds(){
        List<String> student_ids = redisUserTemplate.opsForList().range("user:stu", 0, -1);
        return student_ids;
    }

    //得到在线医生
    public List<Doctor> getDoctorsOnline() {
        List<String> doctor_ids = getDoctorsIds();
        List<Doctor> doctors = new ArrayList<>();
        for (String id: doctor_ids) {
            doctors.add(doctorService.getDoctorById(id));
        }
        return doctors;
    }

    /**
    * @Description: 学生发送聊天记录，格式为hash1 + hash2：如果用一个hash来存储一条聊天记录有点费劲，eg 日期，from-to关系,时间1+内容1
    *                                                                                                            时间2+内容1
    *                                                                                                           时间2+内容1
    *               但好像不能将hash的值改为list，为了解决这个问题只能用两个hash来存
    *               hash1:   stu:id-doctor:id, 2020-03-17, chat:对话编号
    *               hash2:   chat:对话编号, 对话时间, 对话内容
    *              这里为了方便，就不将聊天内容写入数据库了
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/27 0027
    */
    public void insertStuContent(String s_id, String d_id,String content){
        //获取当前时间
        Calendar cal = Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m=cal.get(Calendar.MONTH) + 1;
        int d=cal.get(Calendar.DATE);
        int h=cal.get(Calendar.HOUR_OF_DAY);
        int mi=cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

//        String date = y + "-" + m + "-" + d;
        String date = "2020-3-27";
        String time = h + ":" + mi + ":" + second;

        //获取当天，该学生和医生的对话id
        //先判断hash1是否为空
        String c_id = (String) redisUserTemplate.opsForHash().get("stu:" + s_id + "-doctor:" + d_id, date);
        if( c_id == null){
            c_id = "chat:" + UUID.randomUUID().toString();
            //hash1  stu:id-doctor:id, 2020-03-17(date), 对话编号
            redisUserTemplate.opsForHash().put("stu:" + s_id + "-doctor:" + d_id, date,c_id);
        }

        //hash2 对话编号, 对话时间(time), 对话内容
        redisUserTemplate.opsForHash().put(c_id,time,content);
    }

    /**
    * @Description: 医生发送聊天记录，格式为hash1 + hash2：
    *               hash1:   doctor:id-stu:id, 2020-03-17, 对话编号
    *               hash2:   对话编号, 对话时间, 对话内容
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/27 0027
    */
    public void insertDoctorContent(String d_id, String s_id, String content){
        //获取当前时间
        Calendar cal = Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m=cal.get(Calendar.MONTH) + 1;
        int d=cal.get(Calendar.DATE);
        int h=cal.get(Calendar.HOUR_OF_DAY);
        int mi=cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

//        String date = y + "-" + m + "-" + d;
        String date = "2020-3-27";
        String time = h + ":" + mi + ":" + second;

        //获取当天，该医生和学生的对话id
        String c_id = (String) redisUserTemplate.opsForHash().get("doctor:" + d_id + "-stu:" + s_id, date);
        if(c_id == null){
            c_id = "chat:" + UUID.randomUUID().toString();

            //hash1  doctor:id-stu:id, 2020-03-17(date), 对话编号
            redisUserTemplate.opsForHash().put("doctor:" + d_id +"-stu:" + s_id, date,c_id);
        }


        //hash2  对话编号, 对话时间(time), 对话内容
        redisUserTemplate.opsForHash().put(c_id,time,content);
    }

    /**
    * @Description: 根据日期，获取学生的聊天记录,并封装到Content对象中
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/27 0027
    */
    public List<Content> getStuContentList(String date,String s_id,String d_id){

        String key = "stu:" + s_id + "-doctor:" + d_id;
        String c_id = (String) redisUserTemplate.opsForHash().get(key, date);

        if(c_id == null){
            return null;
        }else{
            //获取当天该学生给医生发的所有时间
            Set<Object> times = redisUserTemplate.opsForHash().keys(c_id);

            Doctor doctor = doctorService.getDoctorByIdWithName(d_id);
            Student student = stuService.getStuByIdWithName(s_id);
            List<Content> contents = new ArrayList<>();
            for (Object time: times) {
                Content content = new Content();
                String c = (String) redisUserTemplate.opsForHash().get(c_id, time);

                content.setDoctor(doctor);
                content.setStudent(student);
                content.setContent(c);
                content.setTime((String) time);
                content.setDate(date);
                content.setFrom_id(0);

                contents.add(content);
            }

//            Collections.sort(contents,new CompareContentByTime());
            return contents;
        }
    }

    /**
     * @Description: 根据日期，获取医生的聊天记录,并封装到Content对象中
     * @Param:
     * @return:
     * @Author: wangxiaoxi
     * @Date: 2020/3/27 0027
     */
    public List<Content> getDoctorContentList(String date,String s_id,String d_id){

        String key = "doctor:" + d_id + "-stu:" + s_id;
        String c_id = (String) redisUserTemplate.opsForHash().get(key, date);

        if(c_id == null){
            return null;
        }

        //获取当天该医生给学生发的所有时间
        Set<Object> times = redisUserTemplate.opsForHash().keys(c_id);

        Doctor doctor = doctorService.getDoctorByIdWithName(d_id);
        Student student = stuService.getStuByIdWithName(s_id);
        List<Content> contents = new ArrayList<>();
        for (Object time: times) {
            Content content = new Content();
            String c = (String) redisUserTemplate.opsForHash().get(c_id, time);

            content.setDoctor(doctor);
            content.setStudent(student);
            content.setContent(c);
            content.setTime((String) time);
            content.setDate(date);
            content.setFrom_id(1);

            contents.add(content);
        }

        return contents;
    }
    
    /** 
    * @Description: 根据双方聊天记录，按time从小到大排序（目前只能规定双方在同一天交流，跨天作废）
    * @Param:  
    * @return:  
    * @Author: wangxiaoxi
    * @Date: 2020/3/27 0027 
    */
    public List<Content> getContentList(String date,String s_id,String d_id){
        List<Content> stuContentList = getStuContentList(date, s_id, d_id);
        List<Content> doctorContentList = getDoctorContentList(date, s_id, d_id);

        if(stuContentList == null || doctorContentList == null){
            return null;
        }

        stuContentList.addAll(doctorContentList);
        Collections.sort(stuContentList,new CompareContentByTime());
        System.out.println("聊天记录" + stuContentList);
        return stuContentList;
    }

    /** 
    * @Description: 得到在线学生 
    * @Param:  
    * @return:  
    * @Author: wangxiaoxi
    * @Date: 2020/3/28 0028 
    */
    public List<Student> getStudentsOnline() {
        List<String> student_ids = getStudentsIds();
        List<Student> students = new ArrayList<>();
        for (String id: student_ids) {
            students.add(stuService.getStuById(id));
        }
        return students;
    }
}
