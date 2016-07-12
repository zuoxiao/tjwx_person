package com.example.tjwx_person.bean;

import java.io.Serializable;
import java.util.List;

public class publishedData extends ImModelData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7918022953409586324L;

    String address;
    String amount;
    // audioData audio;
    audioData audio;
    String comment;
    boolean emergency;
    String id;
    double latitude;
    double longitude;
    // picturesData pictures;
    //List<String> pictures;
    String publishTime;


    // String publisherId;
    // String publisherMobilePhone;
    // String publisherName;
    processorHeadPortrait processorHeadPortrait;
    String processorId;
    String processorName;
    String processorMobilePhone;
    String cancelType;
    String cancelReason;
    String state;
    String refusalReason;
    String refusalType;

    boolean commentState;
    boolean audioOrder;

    public String getRefusalReason() {
        return refusalReason;
    }

    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    public String getRefusalType() {
        return refusalType;
    }

    public void setRefusalType(String refusalType) {
        this.refusalType = refusalType;
    }

    public boolean isAudioOrder() {
        return audioOrder;
    }

    public void setAudioOrder(boolean audioOrder) {
        this.audioOrder = audioOrder;
    }

    public boolean isCommentState() {
        return commentState;
    }

    public void setCommentState(boolean commentState) {
        this.commentState = commentState;
    }


    public List<skillData> skills;
    List<positionsData> positions;
    String orderNo;
    int processorEvaluateCount;
    int processorEvaluateScope;
    String processorHeadPortraitUrl;

    public String getProcessorHeadPortraitUrl() {
        return processorHeadPortraitUrl;
    }

    public void setProcessorHeadPortraitUrl(String processorHeadPortraitUrl) {
        this.processorHeadPortraitUrl = processorHeadPortraitUrl;
    }

    public com.example.tjwx_person.bean.processorHeadPortrait getProcessorHeadPortrait() {
        return processorHeadPortrait;
    }

    public void setProcessorHeadPortrait(com.example.tjwx_person.bean.processorHeadPortrait processorHeadPortrait) {
        this.processorHeadPortrait = processorHeadPortrait;
    }

    public int getProcessorEvaluateCount() {
        return processorEvaluateCount;
    }

    public void setProcessorEvaluateCount(int processorEvaluateCount) {
        this.processorEvaluateCount = processorEvaluateCount;
    }

    public int getProcessorEvaluateScope() {
        return processorEvaluateScope;
    }

    public void setProcessorEvaluateScope(int processorEvaluateScope) {
        this.processorEvaluateScope = processorEvaluateScope;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

//	public List<String> getPictures() {
//		return pictures;
//	}
//
//	public void setPictures(List<String> pictures) {
//		this.pictures = pictures;
//	}

    public List<positionsData> getPositions() {
        return positions;
    }

    public void setPositions(List<positionsData> positions) {
        this.positions = positions;
    }


    public audioData getAudio() {
        return audio;
    }

    public void setAudio(audioData audio) {
        this.audio = audio;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    // public audioData getAudio() {
    // return audio;
    // }
    //
    // public void setAudio(audioData audio) {
    // this.audio = audio;
    // }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<skillData> getSkills() {
        return skills;
    }

    public void setSkills(List<skillData> skills) {
        this.skills = skills;
    }

    public String getProcessorId() {
        return processorId;
    }

    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public String getProcessorMobilePhone() {
        return processorMobilePhone;
    }

    public void setProcessorMobilePhone(String processorMobilePhone) {
        this.processorMobilePhone = processorMobilePhone;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
