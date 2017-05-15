package com.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<?> getAccountCashIns() {
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
}
