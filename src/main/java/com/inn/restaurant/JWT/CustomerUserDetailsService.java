package com.inn.restaurant.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inn.restaurant.dao.UserDao;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {

	
	@Autowired
	UserDao userDao;
	
	private com.inn.restaurant.pojo.User userDetails;
	
	
	public void setUserDetails(com.inn.restaurant.pojo.User userDetails) {
		this.userDetails = userDetails;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		userDetails = userDao.findByEmailId(username);
		if(!Objects.isNull(userDetails))
				{
			return new User(userDetails.getEmail(),userDetails.getPassword(), new ArrayList<>());
		}
		else
			throw new UsernameNotFoundException("user not found");
	}
	public com.inn.restaurant.pojo.User getUserDetails(){
		com.inn.restaurant.pojo.User user = userDetails;
		user.setPassword(null);
		
		return userDetails;
	}
	
	

}
