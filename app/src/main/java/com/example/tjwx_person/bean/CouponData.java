package com.example.tjwx_person.bean;

import java.util.List;

public class CouponData extends ImModelData {

	/**
	 * 优惠券list
	 */
	private static final long serialVersionUID = -1295374836071149830L;

	boolean first;
	boolean last;
	String number;
	String numberOfElements;
	String totalElements;
	String totalPages;
	List<CouponDetailData> content;

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(String numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public String getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(String totalElements) {
		this.totalElements = totalElements;
	}

	public String getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}

	public List<CouponDetailData> getContent() {
		return content;
	}

	public void setContent(List<CouponDetailData> content) {
		this.content = content;
	}

}
