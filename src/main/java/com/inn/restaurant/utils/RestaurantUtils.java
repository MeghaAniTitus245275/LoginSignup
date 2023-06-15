package com.inn.restaurant.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestaurantUtils {
	public  final static String SOMETHING_WENT_WRONG = null;
	private RestaurantUtils() {
		
	}
	public static ResponseEntity<String> getResponseEntity(String responseMessage , HttpStatus httpStatus){
		return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
	}

}
