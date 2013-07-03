package com.quesity.models;

public class Account extends JSONModel {
	
	private String email;
	private String password;
	private Name name;
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
	public String getFirstName() {
		return name.first;
	}
	public void setFirstName(String first_name) {
		this.name.first = first_name;
	}
	public String getLastName() {
		return name.last;
	}
	public void setLastName(String last_name) {
		this.name.last= last_name;
	}
	
	private class Name {
		public String first;
		public String last;
	}
}
