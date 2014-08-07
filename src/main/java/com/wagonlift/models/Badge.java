package com.wagonlift.models;

import java.util.UUID;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class Badge {

	@Id
	private String id;
	private String badgeName;
	private Attachment badgeFile;

	public Badge() {
		id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBadgeName() {
		return badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	public Attachment getBadgeFile() {
		return badgeFile;
	}

	public void setBadgeFile(Attachment badgeFile) {
		this.badgeFile = badgeFile;
	}

}
