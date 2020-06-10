package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.entity.Document;
import com.wangxiaoxi.mheal.entity.Music;
import com.wangxiaoxi.mheal.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResourcesMapper {

    @Insert("insert into document(id, title, href, content, createTime, updateTime, viewCount, likes) " +
            "values (#{id}, #{title},  #{href}, #{content}, #{createTime}, #{updateTime}, #{viewCount}, #{likes})")
    void insertDocument(Document document);

    @Select("select * from document where title = #{title}")
    Document getDocumentByName(String title);

    //根据tag查询专栏
    @Select("select * from document where tag = #{tag}")
    Document getDocumentByTag(String tag);

    @Insert("insert into music(id, title, author, href, bgm, createTime, updateTime, viewCount, likes) " +
            "values (#{id}, #{title}, #{author}, #{href}, #{bgm}, #{createTime}, #{updateTime}, #{viewCount}, #{likes})")
    void insertMusic(Music music);

    @Insert("insert into video(id, title, author, href, createTime, updateTime, viewCount, likes) " +
            "values (#{id}, #{title}, #{author}, #{href}, #{createTime}, #{updateTime}, #{viewCount}, #{likes})")
    void insertVideo(Video video);

    @Select("select * from music where title = #{title}")
    Music getMusicByName(String title);

    @Select("select * from video where title = #{title}")
    Video getVideoByName(String title);

    @Select("select * from music")
    List<Music> getMusicAll();

    @Select("select * from video")
    List<Video> getVideoAll();

    @Select("select * from video where id = #{id}")
    Video getVideoById(String id);

    @Insert("insert into ResourceAndTag(r_id, t_id) values (#{arg0}, #{arg1})")
    void insertResourceWithTag(String r_id, String t_id);
}
