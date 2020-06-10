package com.wangxiaoxi.mheal;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.wangxiaoxi.mheal.entity.*;
import com.wangxiaoxi.mheal.mapper.ActivityMapper;
import com.wangxiaoxi.mheal.mapper.DoctorMapper;
import com.wangxiaoxi.mheal.mapper.QuestionMapper;
import com.wangxiaoxi.mheal.mapper.StudentMapper;
import com.wangxiaoxi.mheal.service.*;
import com.wangxiaoxi.mheal.service.cacheService.QuesCacheService;
import com.wangxiaoxi.mheal.service.cacheService.UserCacheService;
import com.wangxiaoxi.mheal.util.UrlUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class MhealApplicationTests {

	@Autowired
	StudentMapper studentMapper;

	@Autowired
	DoctorMapper doctorMapper;

	@Autowired
	QuestionMapper questionMapper;

	@Autowired
	ActivityMapper activityMapper;

	@Autowired
	DoctorService doctorService;

	@Autowired
	UserCacheService userCacheService;

	@Autowired
	QuesService quesService;

	@Autowired
	StuService stuService;

	@Autowired
	ResourcesService resourcesService;

	@Test
	public void test(){
		LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap(16,0.75f,true){
			@Override
			protected boolean removeEldestEntry(Map.Entry eldest) {
				//父类的size对于子孙类可见，对其他package不可见
				return size() > 3;
			}
		};

		linkedHashMap.put("project1","world");
		linkedHashMap.put("project2","world");
		linkedHashMap.put("project3","world");
		linkedHashMap.forEach((k,v) ->{
			System.out.println(k + ' ' + v);
		});
		System.out.println("-----------------------------------");

		//模拟访问
		linkedHashMap.get("project1");
		linkedHashMap.get("project1");
		linkedHashMap.get("project2");
		linkedHashMap.forEach((k,v) ->{
			System.out.println(k + ' ' + v);
		});

		System.out.println("-----------------------------------");
		//触发删除
		linkedHashMap.put("project4","Mission control");
		System.out.println("oldest entry should ne removed:");
		linkedHashMap.forEach((k,v) ->{
			System.out.println(k + ' ' + v);
		});
	}

	@Test
	void contextLoads() throws Exception {

		String data = UrlUtils.getRedirectUrl("https://music.163.com/song/media/outer/url?id=619579");
		System.out.println(data);
		//		UrlUtils.testUrlWithTimeOut("http://player.bilibili.com/player.html?aid=93733260&bvid=BV1KE411x7oc&cid=160036375&page=1",2000);

//		// Endpoint以杭州为例，其它Region请按实际情况填写。
//		String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
//		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
//		String accessKeyId = "LTAI4FdNm5gpKwUHwiHGj6eD";
//		String accessKeySecret = "JWHsM9Z1YLG4rt1CYlFxsMbkMOP7aD";
//
//		// 创建OSSClient实例。
//		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//		// 上传文件流。
//		InputStream inputStream = new FileInputStream(new File("C://Users//Administrator//Desktop//test.txt"));
//		ossClient.putObject("wangxiaoxi-20200401", "doc", inputStream);
//
//		// 关闭OSSClient。
//		ossClient.shutdown();

//		Video video = new Video();
//		video.setTitle("总是情绪低落,该怎么办？");
//		video.setHref("//player.bilibili.com/player.html?aid=45305425&bvid=BV1ob411B7Nq&cid=79321057&page=1&high_quality=1");
//		resourcesService.insertVideo(video);
//
//
//		video.setTitle("意志消沉该怎么办？");
//		video.setHref("//player.bilibili.com/player.html?aid=51259059&bvid=BV1K441147hn&cid=89735461&page=1&high_quality=1");
//		resourcesService.insertVideo(video);
//
//		video.setTitle("如何走出自卑的牢笼？");
//		video.setHref("//player.bilibili.com/player.html?aid=54335481&bvid=BV1p4411n7gK&cid=95040995&page=1&high_quality=1");
//		resourcesService.insertVideo(video);
//		music = adminService.getMusicByName(music);
//		System.out.println(music.getBgm());

//		Document document = new Document();
//		document.setTitle("再见抑郁");
//		List<String> list = FileUtils.readLines(new File("C://Users//Administrator//Desktop//test.txt"),"utf-8");
//		StringBuffer stringBuffer = new StringBuffer();
//		for (String c:list){
//			stringBuffer.append(c);
//		}
//		document.setContent(stringBuffer.toString());
//		adminService.insertDocument(document);
//		String content = adminService.getDocumentByName(document).getContent();
//		System.out.println(content);


		//对学生，老师创建聊天帐号
//		List<Student> stuAll = studentMapper.getStuAll();
//		for (Student stu: stuAll) {
//			stuService.insertLogin(stu);
//		}
//
//		List<Doctor> all = doctorService.findAll();
//		for(Doctor doctor : all){
//			doctorService.insertLogin(doctor);
//		}

//		Thread.sleep(2000);
//		userCacheService.insertStuContent("1614010822","161401080801","你好，医生。");
//		Thread.sleep(2000);
//		userCacheService.insertDoctorContent("161401080801","1614010822","请问我有什么可以帮你的吗？");
//		Thread.sleep(2000);
//		userCacheService.insertStuContent("1614010822","161401080801","我最近压力很大，什么都学不下");
//		Thread.sleep(2000);
//		userCacheService.insertStuContent("1614010822","161401080801","我是不是得了抑郁症了");
//		Thread.sleep(2000);
//		userCacheService.insertDoctorContent("161401080801","1614010822","没事的，放轻松，多出去走走，找人聊聊天就能缓解了");

//		List<Content> stuContentList = userCacheService.getStuContentList("2020-3-27", "1614010822", "161401080801");

//		List<Content> doctorContentList = userCacheService.getDoctorContentList("2020-3-27","1614010822","161401080801");
//		stuContentList.addAll(doctorContentList);
//		List<Content> contents = stuContentList;
//		List<Content> contents = userCacheService.getContentList("2020-3-27", "1614010822", "161401080801");
//		System.out.println(stuContentList);

//		List<Doctor> doctorsOffline = doctorService.getDoctorsOffline();
//		List<Doctor> doctorsOnline = userCacheService.getDoctorsOnline();
//		System.out.println(doctorsOnline);
//		System.out.println("---------------------------");
//		System.out.println(doctorsOffline);
	}
}
