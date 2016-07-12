package com.example.tjwx_person.bean;

/**
 * Created by zuo on 2016/6/24.
 */
public class UpdateDown extends ImModelData {

    String url;
    String updateLog;
    boolean update;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
