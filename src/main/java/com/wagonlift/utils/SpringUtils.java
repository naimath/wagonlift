package com.wagonlift.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class contains various (static) utility methods that you'll end up using regularly. 
 *
 */
public class SpringUtils {

	/**
	 * Create a text/plain response entity with the given message.
	 * @param message
	 * @return
	 */
	public static ResponseEntity<String> textResponse(String message) {
		return responseOfType(message, "text/plain");
	}
	
	/**
	 * Create a application/json response entity with the given message.
	 * @param message
	 * @return
	 */
	public static ResponseEntity<String> jsonResponse(String message) {
		return responseOfType(message, "application/json");
	}
	
	public static ResponseEntity<String> responseOfType(String body, String contentType) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", contentType);
		return new ResponseEntity<String>(body, headers, HttpStatus.OK);
	}
	
}
