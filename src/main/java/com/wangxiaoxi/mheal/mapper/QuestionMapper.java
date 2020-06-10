package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import javax.annotation.Generated;
import java.util.Date;
import java.util.List;

@Mapper
public interface QuestionMapper {

    @Select("select * from question where id = #{id}")
    public Question getQuesById(Integer id);

    @Insert("insert into question(id,createTime,updateTime,content,viewCount,likes,anonymous,status) values (#{id},#{createTime}, #{updateTime}, #{content},#{viewCount},#{likes},#{anonymous},#{status})")
    public void insertQues(Question question);

    @Update("update question set viewCount = #{viewCount}, updateTime = #{updateTime} where id = #{id}")
    public void updateQues(Question question);

    @Delete("delete from question where id = #{id}")
    public void deleteQues(Integer id);

    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "student",one = @One(select = "com.wangxiaoxi.mheal.mapper.StudentMapper.getStuByQuesId",fetchType=FetchType.DEFAULT)),
            @Result(column = "id",property = "questionAndTags",many = @Many(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getQuestionAndTagByQid",fetchType=FetchType.DEFAULT)),
    })
    @Select("select * from question order by updateTime DESC limit #{arg0},#{arg1}")
    public List<Question> getQuestions(Integer begin, Integer end);

    @Select("select count(*) from question")
    public Integer getQuesCount();

    @Insert("insert into askAndAns (q_id, s_id) values (#{arg0}, #{arg1})")
    void insertQuesWithStu(String q_id, String s_id);

    /**
    * @Description: 根据问题id查找问题相关信息
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/19 0019
    */
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "student",one = @One(select = "com.wangxiaoxi.mheal.mapper.StudentMapper.getStuByQuesId",fetchType=FetchType.DEFAULT)),
            @Result(column = "id",property = "askAndAnsList",many = @Many(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getAskAndAns",fetchType=FetchType.DEFAULT)),
            @Result(column = "id",property = "questionAndTags",many = @Many(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getQuestionAndTagByQid",fetchType=FetchType.DEFAULT)),
    })
    @Select("select * from question where id = #{id}")
    Question getQuestionById(String id);

    /**
    * @Description: 根据问题id查找对应的askAndAns集合,并通过子查询封装doctor
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/19 0019
    */
    @Results({
          @Result(column = "d_id",property = "doctor",one = @One(select = "com.wangxiaoxi.mheal.mapper.DoctorMapper.getDoctorById1",fetchType=FetchType.DEFAULT))
    })
    @Select("select * from askAndAns where q_id = #{q_id}")
    List<AskAndAns> getAskAndAns(String q_id);

    @Select("select distinct q_id, d_id from askAndAns where q_id = #{q_id}")
    String isDidEmpty(AskAndAns askAndAns);

    //如果该问题在中间表是否存在，但d_id为空，则更新中间表
    @Update("update askAndAns set d_id = #{doctor.id},answer = #{answer}, updateTime = #{updateTime} where q_id = #{q_id}")
    void updateAns(AskAndAns askAndAns);

    //如果该问题在中间表是否存在，但d_id不空，则插入到中间表
    @Insert("insert into askAndAns (q_id, s_id, d_id, answer, updateTime) values (#{q_id}, #{student.id}, #{doctor.id}, #{answer}, #{updateTime})")
    void insertAns(AskAndAns askAndAns);

    @Insert("insert into tag(id,tagName) values (#{id}, #{tagName})")
    void insertTag(Tag tag);

    //得到不含"无"的标签
    @Select("select * from tag where tagName != '无'")
    List<Tag> getTags();

    //根据标签名查询标签
    @Select("select tagName from tag where tagName = #{tagName}")
    String getTagByName(String tagName);

    //根据标签id查询标签
    @Select("select * from tag where id = #{id}")
    Tag getTagById(String id);

    //根据q_id和t_id查询该问题是否与该标签绑定
    @Select("select * from tag where q_id = #{arg0} and #{arg1}")
    String getTagByQIdAndTId(String q_id, String t_id);

    @Insert("insert into questionAndTag (q_id, t_id) values (#{id}, 'a84a1d1f-73b0-4304-8e6e-5a1bc239f496')")
    void updateQuesWithTag(Question question);

    //查询QuestionAndTag
    @Results({
        @Result(column = "t_id",property = "tag",one = @One(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getTagById",fetchType=FetchType.DEFAULT)),
    })
    @Select("select * from questionAndTag where q_id = #{q_id}")
    List<QuestionAndTag> getQuestionAndTagByQid(String q_id);

    @Insert("insert into questionAndTag(q_id, t_id) values (#{arg0}, #{arg1})")
    void insertQuesWithTag(String q_id, String t_id);

    @Select("select distinct q_id from askAndAns where s_id = #{id}")
    List<String> getQuestionByStu(String id);

    @Update("update question set status = 1 where id = #{q_id}")
    void setQuesStatus(String q_id);

    @Insert("insert into dates(datetime) values (#{dateTime})")
    public void insertdateTime(Date dateTime);

    @Select("select datetime from dates")
    List<String> getDates();

    /**
    * @Description: 根据日期查询已回答的问题
    * @Param:
    * @return:
    * @Author: wangxiaoxi
    * @Date: 2020/3/24 0024
    */
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "student",one = @One(select = "com.wangxiaoxi.mheal.mapper.StudentMapper.getStuByQuesId",fetchType=FetchType.DEFAULT)),
            @Result(column = "id",property = "askAndAnsList",many = @Many(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getAskAndAns",fetchType=FetchType.DEFAULT)),
            @Result(column = "id",property = "questionAndTags",many = @Many(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getQuestionAndTagByQid",fetchType=FetchType.DEFAULT)),
    })
    @Select("select * from question where updateTime like '${datetime}%' and status = 1")
    List<Question> getQuestionByDate(String datetime);


    //根据t_id找出所有的q_id集合
    @Select("select q_id from questionAndTag where t_id = #{id}")
    List<String> getQuestionByTag(Tag tag);

    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "student",one = @One(select = "com.wangxiaoxi.mheal.mapper.StudentMapper.getStuByQuesId",fetchType=FetchType.DEFAULT)),
            @Result(column = "id",property = "askAndAnsList",many = @Many(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getAskAndAns",fetchType=FetchType.DEFAULT)),
            @Result(column = "id",property = "questionAndTags",many = @Many(select = "com.wangxiaoxi.mheal.mapper.QuestionMapper.getQuestionAndTagByQid",fetchType=FetchType.DEFAULT)),
    })
    @Select("select * from question where id = #{q_id} and status = 1")
    Question getQuestionById1(String q_id);
}
