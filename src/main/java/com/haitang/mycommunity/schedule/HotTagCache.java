package com.haitang.mycommunity.schedule;

import com.haitang.mycommunity.dto.HotTagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HotTagCache {

    List<String> hots=new ArrayList<>();

    @Autowired
    HotTagDto hotTagDto;

    public void updateTags(Map<String,Integer> tags){

        int max=5;
        PriorityQueue<HotTagDto> priorityQueue = new PriorityQueue<>(max);
        tags.forEach((name,priority)->{
            HotTagDto hotTagDto = new HotTagDto();
            hotTagDto.setName(name);
            hotTagDto.setPriority(priority);

            if (priorityQueue.size()<max){
                priorityQueue.add(hotTagDto);
            }else {
//              拿到堆中最小的元素
                HotTagDto minHot = priorityQueue.peek();
                if (hotTagDto.compareTo(minHot) >0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDto);
                }
            }
        });
        List<String> newHots=new ArrayList<>();
        HotTagDto poll=priorityQueue.poll();
        while (poll !=null){
            newHots.add(0,poll.getName());
            poll=priorityQueue.poll();
        }
        hots=newHots;
        System.out.println(hots);
    }

    public List<String> getHots() {
        return hots;
    }
    public void setHots(List<String> hots) {
        this.hots = hots;
    }
}
