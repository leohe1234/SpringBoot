package com.leo.jwts.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leo.jwts.bean.BasePageRequest;
import com.leo.jwts.bean.BaseResponse;
import com.leo.jwts.security.JwtUtils;

@RestController
public class UserControler {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/user/login")
	public String Login(@RequestBody User user)
	{
		System.out.println(" user login ...");
		String token = null;
		User u = userRepository.findByUsername(user.getUsername());
		
		if(u == null)
			return "user not exist";
		
		if(BCrypt.checkpw(user.getPassword(), u.getPassword()))
		{	
			token= jwtUtils.generateToken(u.getUsername(), user.getPassword());
			return token;
		}
			else
				return " password incorrect! ";
	}
	
	
	@PostMapping("/user/register")
	public String register(@RequestBody User user)
	{
		System.out.println(" user register ...");

		User u = userRepository.findByUsername(user.getUsername());
		
		if(u != null)
			return "user existed";
		else
		{
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			
			user.setRole("ROLE_USER");
			userRepository.save(user);
		}
		return "user registed";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/manager/list")
	@ResponseBody
	public BaseResponse queryUser(@RequestBody BasePageRequest request)
	{
		System.out.println(" queryUser");
		
		int page = request.getPage();
		int size = request.getSize();
	    Sort sort = new Sort(Sort.Direction.DESC, "id");
	    Pageable pageable = new PageRequest(page, size, sort);
	
	    
		Page<User> users = userRepository.findAll(pageable);
		BaseResponse rsp = new BaseResponse();
		rsp.setData(users);
		return rsp;
	}
}
