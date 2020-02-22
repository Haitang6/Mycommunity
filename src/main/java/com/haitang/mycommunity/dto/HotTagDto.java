package com.haitang.mycommunity.dto;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class HotTagDto implements Comparable {
    private String name;
    private Integer priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDto) o).getPriority();
    }
}
