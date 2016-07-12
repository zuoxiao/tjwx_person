package com.example.tjwx_person.bean;

public class RegistData extends ImModelData {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7386226306336224904L;
	public String clientId;
	public String clientSecret;
    public String mobilePhone;
    
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

}
