package com.haitang.mycommunity.schedule;

import com.haitang.mycommunity.mapper.QuestionExtMapper;
import com.haitang.mycommunity.mapper.QuestionMapper;
import com.haitang.mycommunity.model.Question;
import com.haitang.mycommunity.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TagCache {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    HotTagCache hotTagCache;


    @Scheduled(fixedRate = 50000)
//    @Scheduled(cron = "0 0 1 * * *")
    public void reportCurrentTime(){
        Map<String, Integer> tagMap = new HashMap<>();
        List<Question> questions = questionMapper.selectByExample( new QuestionExample());
//        遍历每个问题的每个标签，把每个标签的权重存储到map集合中
        for (Question question : questions) {
            String[] tags = question.getTag().split(",");
            for (String tag : tags) {
                Integer priority = tagMap.get(tag);
                if (priority == null){
                    tagMap.put(tag,10+question.getCommentCount());
                }else {
                    tagMap.put(tag,priority+5+question.getCommentCount());
                }
            }
        }
//        找出堆中前几个热门标签
        hotTagCache.updateTags(tagMap);
        System.out.println(new Date());
    }
}
