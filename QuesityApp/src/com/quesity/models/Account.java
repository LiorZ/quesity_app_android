package com.quesity.models;

public class Account extends JSONModel {
	private String email;
	private String password;
	
	public String getUsername() {
		return email;
	}
	public void setUsername(String username) {
		this.email = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
