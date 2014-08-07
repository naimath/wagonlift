package com.wagonlift.models;

import java.util.UUID;

import com.google.code.morphia.annotations.Id;

public class UserBlockList {

	@Id
	private String id;
	private String userId;
	private String blockedId;

	
	public UserBlockList(){
		id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBlockedId() {
		return blockedId;
	}

	public void setBlockedId(String blockedId) {
		this.blockedId = blockedId;
	}

}
