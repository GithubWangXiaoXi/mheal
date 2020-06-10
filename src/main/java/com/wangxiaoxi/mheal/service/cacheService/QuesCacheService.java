package com.wangxiaoxi.mheal.service.cacheService;

import com.wangxiaoxi.mheal.entity.PageData;
import com.wangxiaoxi.mheal.entity.Question;
import com.wangxiaoxi.mheal.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangxiaoxi
 * @create: 2020-03-08 11:24
 **/
@Service
public class QuesCacheService {

    @Autowired
    private RedisTemplate<String,Question> redisQuesTemplate; //问题按日期划分

    @Autowired
    private StringRedisTemplate redisDateTemplate; //加载进redis缓存的日期列表

    @Autowired
    private RedisTemplate<String,Tag> tagRedisTemplate; //加载tag列表

    private static int count = 0; //记录缓存的数据

    //将数据库中查到的日期保存在缓存中
    public void insertDates(List<String> dates){
        for(String date : dates){
            redisDateTemplate.opsForList().leftPush("ques:dateList",date);
        }
    }

    //设置"ques:dateList"的过期时间
    public void setDateListExpire(){
        redisDateTemplate.expire("ques:dateList",1,TimeUnit.MINUTES);
    }

    //得到dateList
    public List<String> getDateList(){
        return redisDateTemplate.opsForList().range("ques:dateList",0,-1);
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        QuesCacheService.count = count;
    }

    //问题按日期插入，用redis的set保存
    public void insertQuesByDate(String date,Question question){
        redisQuesTemplate.opsForSet().add( date, question);
    }

    /**
     * 将分页数据，用redis的hash保存
     * 1)判断该问题是否之前遍历过，并计算size
     * 2）如果未超页，则count:indexEnd 为 size:-1。(注意size为本来问题的数目，不是遍历后剩余的size)
     * 3)如果超页，则count:indexEnd 为 size:前一个indexEnd + pageSize (注意size为本来问题的数目，不是遍历后剩余的size)
     *   并将下一个页号保存在pageData中，将其返回
     * @param pageData
     * @param date
     * @param questions
     * @return
     */
    public PageData<Question> insertQuesPage(PageData<Question>pageData, String date, Set<Question> questions){

        int leftOverSize;
        int indexBegin = pageData.getIndexEnd();
        int indexEnd;
        //indexBegin == -1, 上一个集合遍历完毕，leftOverSize为新的问题的size，
        // 此时需要判断leftOverSize + count是否超页，不超页，直接加；若超页，还需要修改leftOverSize
        if(indexBegin == -1) {
            leftOverSize = questions.size();
        }
        //上一个集合还有元素未遍历完，leftOverSize是旧问题的size，由于之前遍历过，size需要减去之前的元素个数才能变成leftOverSize
        else{
            leftOverSize = questions.size() - indexBegin - 1;
        }

        int temp = (pageData.getPageSize() * pageData.getPageNo());
        //加上该日期下的剩下的所有问题，未超页
        if(count + leftOverSize <= temp){
            count = count + leftOverSize;

            //如果上一次遍历完（indexBegin == -1），则hv为 0 :-1，
            if(indexBegin == -1){
                redisQuesTemplate.opsForHash().put("question:" + (pageData.getPageNo() - 1), date, 0 + ":-1");
            }
            // 如果上一次未遍历完indexBegin ！= -1，则hv还是 (indexBegin + 1): -1
            else{
                redisQuesTemplate.opsForHash().put("question:" + (pageData.getPageNo() - 1), date, (indexBegin + 1) + ":-1");
            }

            pageData.setPageNo(pageData.getPageNo());
            pageData.setIndexEnd(-1);

            //如果刚好满页，则将pageNo设置为下一页
            if(count == temp){
                pageData.setPageNo(pageData.getPageNo() + 1);
            }

            return pageData;
        }
        /**
         * 超页，hv为indexBegin: indexEnd, 新的indexEnd = indexBegin + min（pageSize ，leftOverSize）
         * 需要返回新的页号，和该问题集合的问题下标，方便下次重新加载问题集合
         */
        else{
            //上一个问题遍历完毕，且这个问题若全部加入会超页，需要修改leftOverSize
            if(indexBegin == -1){
                indexBegin = 0;
                leftOverSize = pageData.getPageSize() * pageData.getPageNo() - count;
            }else{
                indexBegin = indexBegin + 1;
            }
            indexEnd = indexBegin + Math.min(pageData.getPageSize() - 1,leftOverSize - 1);
            redisQuesTemplate.opsForHash().put("question:" + (pageData.getPageNo() - 1),date,indexBegin  + ":" + indexEnd);
            pageData.setPageNo(pageData.getPageNo() + 1);
            pageData.setIndexEnd(indexEnd);

            count = temp;

            return pageData;
        }
    }

    //编辑日期列表
    public void insertQuesDate(String date){
        redisDateTemplate.opsForList().rightPush( "ques:date", date);
    }

    //设置日期list过期时间
    public void setDateExpire(String date,Integer minutes){
        redisDateTemplate.expire(date,minutes, TimeUnit.MINUTES);
    }

    //设置问题set的过期时间
    public void setQuesExpire(String date,Integer minutes){
        redisQuesTemplate.expire(date,minutes, TimeUnit.MINUTES);
    }

    //设置页面hash的过期时间
    public void setPageExpire(String page,Integer minutes){
        redisQuesTemplate.expire(page,minutes, TimeUnit.MINUTES);
    }

    //日期list是否为空
    public boolean isQuesDateEmpty(String date){
        List<String> list = getQuesDate();
        return list.contains(date);
    }

    //得到日期list
    public List<String> getQuesDate(){
        return redisDateTemplate.opsForList().range("ques:date",0,-1);
    }

    //得到问题set
    public Set<Question> getQuesByDate(String date){
        return redisQuesTemplate.opsForSet().members(date);
    }


    //根据页号查出指定的问题集合
    public List<Question> getQuesByPage(int pageNum) {
        List<Question> questions = new ArrayList<>();
        //从redis中得到日期list
        Set dates = redisQuesTemplate.opsForHash().keys("question:" + pageNum);

        for(Object date : dates) {
            String countAndIndex = (String) redisQuesTemplate.opsForHash().get("question:" + pageNum, date);
            Integer indexBegin = Integer.valueOf(countAndIndex.split(":")[0]);
            Integer indexEnd = Integer.valueOf(countAndIndex.split(":")[1]);


            //通过日期获取问题set
            Set<Question> questionSet = getQuesByDate((String) date);

            int index = 0;
            //从indexBegin开始，indexEnd结束读取问题
            for (Question q : questionSet) {
                if (indexEnd != -1) {
                    if (indexBegin <= index && index <= indexEnd) {
                        questions.add(q);
                    } else if (index > indexEnd) {
                        break;
                    }
                } else {
                    //indexEnd == -1  加至最后
                    if (indexBegin <= index) {
                        questions.add(q);
                    }
                }
                index++;
            }
        }
        return questions;
    }

    //日期list是否为空
    public boolean isQuesEmpty(){
        if(redisDateTemplate.opsForList().range("ques:date",0,-1).size() == 0){
            return true;
        }
        return false;
    }

    //在缓存中获取tags
    public List<Tag> getTags() {
        return tagRedisTemplate.opsForList().range("tags",0,-1);
    }

    //将tags插入缓存
    public void insertTags(List<Tag> tags){

        for (Tag tag:tags) {
            tagRedisTemplate.opsForList().leftPush("tags",tag);
        }
    }
}
