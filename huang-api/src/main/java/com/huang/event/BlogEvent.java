package com.huang.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Time 2022-05-06 16:50
 * Created by Huang
 * className: BlogEvent
 * Description:
 */
public class BlogEvent extends ApplicationEvent {
    private final Object object;

    public BlogEvent(Object source,Object object) {
        super(source);
        this.object = object;
    }

    public Object getObject(){
        return this.object;
    }
}
