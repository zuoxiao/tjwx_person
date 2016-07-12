package com.example.tjwx_person.bean;

import java.util.ArrayList;
import java.util.List;

public class publishData extends ImModelData{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2971085210613474293L;
	
	public List<String> listImg ;
	public	String textData;
	public	String voiceData;
	public List<String> getListImg() {
		return listImg;
	}
	public void setListImg(List<String> listImg) {
		this.listImg = listImg;
	}
	public String getTextData() {
		return textData;
	}
	public void setTextData(String textData) {
		this.textData = textData;
	}
	public String getVoiceData() {
		return voiceData;
	}
	public void setVoiceData(String voiceData) {
		this.voiceData = voiceData;
	}
	
}
