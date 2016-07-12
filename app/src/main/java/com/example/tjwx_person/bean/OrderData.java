package com.example.tjwx_person.bean;

import java.util.List;

public class OrderData extends ImModelData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1176695895898833012L;

	String comment;
	List<String> pictures;
	String audio;
	int imgNumber;//几张图片
    int timeData;
    
    
	public int getTimeData() {
		return timeData;
	}

	public void setTimeData(int timeData) {
		this.timeData = timeData;
	}

	public int getImgNumber() {
		return imgNumber;
	}

	public void setImgNumber(int imgNumber) {
		this.imgNumber = imgNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

}
