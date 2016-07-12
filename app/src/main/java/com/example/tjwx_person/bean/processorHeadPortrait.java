package com.example.tjwx_person.bean;

/**
 * Created by zuo on 2016/5/29.
 */
public class processorHeadPortrait extends  ImModelData{

    String name;
    String contentType;
    String length;
    String md5;
    String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }


    public processorHeadPortrait() {
    }

    public String getMd5() {

        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }


}
