package com.inn.restaurant.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.inn.restaurant.JWT.CustomerUserDetailsService;
import com.inn.restaurant.JWT.JwtFilter;
import com.inn.restaurant.JWT.JwtUtil;
import com.inn.restaurant.constents.RestaurantConstents;
import com.inn.restaurant.dao.UserDao;
import com.inn.restaurant.pojo.User;
import com.inn.restaurant.service.UserService;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	CustomerUserDetailsService customerUserDetailsService;
	
	@Override
	public ResponseEntity<String> signup(Map<String, String> requestMap)
	{
		
		log.info("inside signup{}",requestMap);
		try 
		{
			
		if(validateSignUpMap(requestMap))
		{
			
			User user = userDao.findByEmailId(requestMap.get("email"));
			if(Objects.isNull(user))
			{
				userDao.save(getUserFromMap(requestMap));
				return RestaurantUtils.getResponseEntity("successfully registered", HttpStatus.OK);
				
				
			}
			else
			{
				return RestaurantUtils.getResponseEntity("email already exits", HttpStatus.BAD_REQUEST);
			}
			
		}
		else 
		{
			return RestaurantUtils.getResponseEntity(RestaurantConstents.INVALID_DATA,HttpStatus.BAD_REQUEST);
		
		}
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
			// TODO: handle exception
		}
		return RestaurantUtils.getResponseEntity(RestaurantUtils.SOMETHING_WENT_WRONG,HttpStatus.BAD_REQUEST);
	
	}

	private boolean validateSignUpMap (Map<String, String> requestMap) {
		if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") &&
		requestMap.containsKey("password")) {
			return true;
		}
		return false;
		
	}
	private User getUserFromMap (Map<String,String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		return user;
		
	}
	@Override
	public ResponseEntity<String> login (Map<String, String> requestMap) {
		log.info("inside the login");
		try {
			Authentication auth =authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
			);
			if(auth.isAuthenticated())
			{
				if(customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true"))
				{
					return new ResponseEntity<String>("{\"token\":\""+ jwtUtil.generateToken(customerUserDetailsService
							.getUserDetails().getEmail(),customerUserDetailsService
							.getUserDetails().getRole())+"\"}",HttpStatus.OK);
				}
				else
				{
					return new ResponseEntity<String>("{\"message\":\""+"wait for admin to approvel"+"\"}",
							HttpStatus.BAD_REQUEST);
							
				}
			}
			
		}catch(Exception e) {
			log.error("{}",e);
		}
		return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials"+"\"}",
				HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		
		try {
			if(jwtFilter.isAdmin())
			{
				return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try
		{
			
			if(jwtFilter.isUser())
			{
				//return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
				Optional <User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				if(optional.isEmpty())
				{
					userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					return RestaurantUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
				}
				else
				{
					return RestaurantUtils.getResponseEntity("User id does not exist", HttpStatus.OK);
				}
			
			
			
			}
			else
			{
				return RestaurantUtils.getResponseEntity(RestaurantConstents.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
				}
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return RestaurantUtils.getResponseEntity(RestaurantConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	
	
	


