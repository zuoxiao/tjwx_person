package com.example.tjwx_person.bean;

import java.util.List;

public class selectOrders {
	List<publishedData>	content;
	String first;
	String last;
	String number;
	String numberOfElements;
	String size;
	String totalElements;
	String totalPages;
	public List<publishedData> getContent() {
		return content;
	}
	public void setContent(List<publishedData> content) {
		this.content = content;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
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
	
	
	
}
