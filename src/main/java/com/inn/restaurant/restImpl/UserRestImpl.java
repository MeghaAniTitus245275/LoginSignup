package com.inn.restaurant.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.web.bind.annotation.RestController;

import com.inn.restaurant.constents.RestaurantConstents;
import com.inn.restaurant.rest.UserRest;
//import com.inn.restaurant.rest.Userwrapper;
//import com.inn.restaurant.rest.list;
import com.inn.restaurant.service.UserService;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.wrapper.UserWrapper;

@RestController
public class UserRestImpl  implements UserRest{

	@Autowired
	UserService userservice;
	
	@Override
	public ResponseEntity<String> signup(Map<String, String> requestMap) {
		
		try {
			
			return userservice.signup(requestMap);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return RestaurantUtils.getResponseEntity(RestaurantConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			return userservice.login(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return RestaurantUtils.getResponseEntity(RestaurantConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		
		try {
			return userservice.getAllUser();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return userservice.update(requestMap);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return RestaurantUtils.getResponseEntity(RestaurantConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
