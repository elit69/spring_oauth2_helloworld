package com.app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.Response;
import com.app.dto.UserLoginDTO;
import com.app.util.Debug;

@RestController
@RequestMapping(value = "/api")
public class ApiController {
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Response userLogin(@RequestBody UserLoginDTO usr, HttpServletRequest request) throws ServletException {
		Debug.request("login", usr, request);
		Response response = new Response();
		try {
			request.login(usr.getUsername(), usr.getPassword());
			response.setErrorCode("200");
			response.setStatus("T");
			response.setErrorMessage("Login Success");
		} catch (Exception e) {
			response.setErrorCode("400");
			response.setStatus("F");
			response.setErrorMessage(e.getMessage());
		}
		Debug.response("login", response, request);
		return response;
	}
	
	@RequestMapping(value = "/login/status", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Map<String, Object>> status(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.OK;
		map.put("MESSAGE", "CURRENT USER STATUS");
		map.put("STATUS", "200");
		map.put("IS_LOGIN", isAuthenticated());
		map.put("ROLE", getRole());
		map.put("USERNAME", getUsername());
		return new ResponseEntity<Map<String, Object>>(map, status);
	}
	
	public boolean isAuthenticated() {
		return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
	}

	private String getRole() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().toString();
	}

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
