package com.wagonlift.models;

import java.util.UUID;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class VehicleMaster {

	@Id
	private String id;
	private RideType type;
	private String brand;
	private int pricePerKm;
	private int noOfSeats;
	private FuelType fuelType;

	
	public VehicleMaster(){
		id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RideType getType() {
		return type;
	}

	public void setType(RideType type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getPricePerKm() {
		return pricePerKm;
	}

	public void setPricePerKm(int pricePerKm) {
		this.pricePerKm = pricePerKm;
	}

	public int getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

}
