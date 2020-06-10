package com.wangxiaoxi.mheal.mapper;

import com.wangxiaoxi.mheal.entity.Activity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ActivityMapper {

    @Select("select * from activity where id = #{id}")
    public Activity getActById(Integer id);

    @Insert("insert into activity(id, name, startTime, endTime, place, requirement) values (#{id},#{name},#{startTime},#{endTime},#{place}, #{requirement})")
    public void insertAct(Activity activity);

    @Update("update activity set place = #{place} where id = #{id}")
    public void updateAct(Activity activity);

    @Delete("delete from activity where id = #{id}")
    public void deleAct(Integer id);
}
