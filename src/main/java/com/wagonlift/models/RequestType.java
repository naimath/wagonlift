package com.wagonlift.models;

public enum RequestType {

	SINGLE("single"), SHARING("sharing");
    
	private String value;

    private RequestType(String value) {
            this.value = value;
    }

    public String getValue(){
    	return value;
    }
    
    public static RequestType fromString(String text) {
        if (text != null) {
          for (RequestType b : RequestType.values()) {
            if (text.equalsIgnoreCase(b.value)) {
              return b;
            }
          }
        }
        return null;
      }
}