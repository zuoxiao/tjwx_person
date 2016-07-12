package com.example.tjwx_person.bean;

import java.util.List;

/**
 * Created by zuo on 2016/6/28.
 */
public class NoticeDatas extends ImModelData{

    List<NoticeDate> content;

    public List<NoticeDate> getContent() {
        return content;
    }

    public void setContent(List<NoticeDate> content) {
        this.content = content;
    }
}
