package com.wagonlift.models;

public enum FuelType {

	PETROL("petrol"), DIESEL("diesel");
    
	private String value;

    private FuelType(String value) {
            this.value = value;
    }

    public String getValue(){
    	return value;
    }
    
}
