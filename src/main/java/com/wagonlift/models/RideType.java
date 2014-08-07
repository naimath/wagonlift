package com.wagonlift.models;

public enum RideType {

	CAR("car"), BIKE("bike");
    
	private String value;

    private RideType(String value) {
            this.value = value;
    }

    public String value(){
    	return value;
    }
    
    public static RideType fromString(String text) {
        if (text != null) {
          for (RideType b : RideType.values()) {
            if (text.equalsIgnoreCase(b.value)) {
              return b;
            }
          }
        }
        return null;
      }
    
}
