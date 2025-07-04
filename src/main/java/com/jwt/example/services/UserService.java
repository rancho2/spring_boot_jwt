package com.jwt.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.example.entities.User;
import com.jwt.example.repositories.UserRepository;
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEnocoder;
	//private List<User> store=new ArrayList();

	/*public UserService() {
		store.add(new User(UUID.randomUUID().toString(),"Durgesh Tiwari","durgesh@dev.in"));
		store.add(new User(UUID.randomUUID().toString(),"Harsh Tiwari","harsh@dev.in"));
		store.add(new User(UUID.randomUUID().toString(),"Ankit Tiwari","ankit@dev.in"));
		store.add(new User(UUID.randomUUID().toString(),"Gautam Tiwari","gautam@dev.in"));
	}*/
	public List<User> getUsers()
	{
		return userRepository.findAll();
	}
	public User createUser(User user)
	{
		user.setUserId(UUID.randomUUID().toString());
		user.setPassword(passwordEnocoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
