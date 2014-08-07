package com.wagonlift.models;

import java.util.UUID;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class UserVehicle {

	@Id
	private String id;
	private String userId;
	private String number;
	private Attachment pictureFile;
	private RideType rideType;

	public UserVehicle(){
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Attachment getPictureFile() {
		return pictureFile;
	}

	public void setPictureFile(Attachment pictureFile) {
		this.pictureFile = pictureFile;
	}

	public RideType getRideType() {
		return rideType;
	}

	public void setRideType(RideType rideType) {
		this.rideType = rideType;
	}

}
