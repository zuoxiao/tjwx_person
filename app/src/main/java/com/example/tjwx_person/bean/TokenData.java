package com.example.tjwx_person.bean;

public class TokenData extends ImModelData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2324705054945883350L;
	public String tokenType;
	public String value;

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
