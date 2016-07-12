package com.example.tjwx_person.bean;

import android.content.Intent;

public class AddressData extends ImModelData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465019606063525154L;

	public String name;
	public String address;
	public Double longitude;
	public Double latitude;
	public String city;
	public String district;
	public String province;
	public String street;
	public String streetNumber;
	public String comment;// 放置楼号和门牌号

	public String town;
	public String county;
	public String road;
	public String buildingNumber;
	public String unit;
	public String number;
	public String id;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean fixed;
	public String clientId;

	
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}



}
