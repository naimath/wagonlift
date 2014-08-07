package com.wagonlift.models;

public enum RideStatus {

	NEW("new"), RUNNING("running"), COMPLETED("completed");
    
	private String value;

    private RideStatus(String value) {
            this.value = value;
    }

    public String value(){
    	return value;
    }
    
}
