package com.jwt.example.controllers;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.example.entities.User;
import com.jwt.example.models.JwtRequest;
import com.jwt.example.models.JwtResponse;
import com.jwt.example.repositories.UserRepository;
import com.jwt.example.security.JwtHelper;
import com.jwt.example.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private JwtHelper helper;
	private Logger logger=LoggerFactory.getLogger(AuthController.class);
	@Autowired
	private UserService userService;
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
	{
		this.doAuthenticate(request.getEmail(),request.getPassword());
		UserDetails userDetails=userDetailsService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);
		/*JwtResponse response=JwtResponse.builder().jwtToken(token)
		.username(userDetails.getUsername()).build();*/
		JwtResponse response = new JwtResponse(token, userDetails.getUsername());
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	private void doAuthenticate(String email, String password) {
		UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);
		}catch(BadCredentialsException e)
		{
			throw new BadCredentialsException("Invalid username or password !! ");
		}
	}
	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler()
	{
		return "Credentials Invalid !!";
	}
	@PostMapping("/create-user")
	public User createUser(@RequestBody User user)
	{
		return userService.createUser(user);
	}
}
