package com.example.tjwx_person.bean;

import java.io.Serializable;

public class skillData extends ImModelData implements Serializable {
	String description;
	String id;
	boolean emergency;
	boolean haveNode;
	String fatherId;
	public String name;

	public String getFatherId() {
		return fatherId;
	}

	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEmergency() {
		return emergency;
	}

	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	public boolean isHaveNode() {
		return haveNode;
	}

	public void setHaveNode(boolean haveNode) {
		this.haveNode = haveNode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
