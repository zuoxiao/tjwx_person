package com.example.tjwx_person.bean;

public class positionsData extends ImModelData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2020631406663595534L;

	double latitude;
	double longitude;
	String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
