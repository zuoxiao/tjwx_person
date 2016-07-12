package com.example.tjwx_person.bean;

public class TypeData extends ImModelData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -98241666932471513L;
	public String id;
	public String name;
	public String description;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
