package com.wangxiaoxi.mheal.service;

import com.wangxiaoxi.mheal.entity.Document;
import com.wangxiaoxi.mheal.entity.Music;
import com.wangxiaoxi.mheal.entity.Tag;
import com.wangxiaoxi.mheal.entity.Video;
import com.wangxiaoxi.mheal.mapper.ResourcesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: wangxiaoxi
 * @create: 2020-04-03 13:40
 **/
@Service
public class ResourcesService {

    @Autowired
    ResourcesMapper resourcesMapper;
    
    //插入专栏
    public void insertDocument(Document document) {

        document.setId(UUID.randomUUID().toString());
        document.setCreateTime(new Date(System.currentTimeMillis()));
        document.setUpdateTime(new Date(System.currentTimeMillis()));
        document.setViewCount(0);
        document.setLikes(0);
        resourcesMapper.insertDocument(document);
    }

    //得到专栏
    public Document getDocumentByName(Document document){
        return resourcesMapper.getDocumentByName(document.getTitle());
    }

    //插入音频
    public void insertMusic(Music music) {

        String id = UUID.randomUUID().toString();
        music.setId(id);
        music.setCreateTime(new Date(System.currentTimeMillis()));
        music.setUpdateTime(new Date(System.currentTimeMillis()));
        music.setViewCount(0);
        music.setLikes(0);

        List<Tag> tags = music.getTags();
        for(Tag tag : tags){
            resourcesMapper.insertResourceWithTag(id,tag.getId());
        }

        resourcesMapper.insertMusic(music);
    }

    //得到音频
    public Music getMusicByName(Music music){
        return resourcesMapper.getMusicByName(music.getTitle());
    }

    //得到所有音频
    public List<Music> getMusicAll(){
        return resourcesMapper.getMusicAll();
    }

    //插入视频
    public void insertVideo(Video video) {

        video.setId(UUID.randomUUID().toString());
        video.setCreateTime(new Date(System.currentTimeMillis()));
        video.setUpdateTime(new Date(System.currentTimeMillis()));
        video.setViewCount(0);
        video.setLikes(0);
        resourcesMapper.insertVideo(video);
    }

    //得到视频
    public Video getVideoByName(Video video){
        return resourcesMapper.getVideoByName(video.getTitle());
    }

    //得到所有视频
    public List<Video> getVideoAll(){
        return resourcesMapper.getVideoAll();
    }

    public Video getVideoById(String id) {
        return resourcesMapper.getVideoById(id);
    }
}
