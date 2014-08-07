package com.wagonlift.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class Ride {

	@Id
	private String id;
	private String rideMessages;
	private Date startTime;
	private String OTP;
	private int seats;
	private RideStatus status;
	private Date created;

	private String offerId;
	private List<String> rideRequests;

	public Ride() {
		this.id = UUID.randomUUID().toString();
		this.created = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRideMessages() {
		return rideMessages;
	}

	public void setRideMessages(String rideMessages) {
		this.rideMessages = rideMessages;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getOTP() {
		return OTP;
	}

	public void setOTP(String oTP) {
		OTP = oTP;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public RideStatus getStatus() {
		return status;
	}

	public void setStatus(RideStatus status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public List<String> getRideRequests() {
		return rideRequests;
	}

	public void setRideRequests(List<String> rideRequests) {
		this.rideRequests = rideRequests;
	}

}
