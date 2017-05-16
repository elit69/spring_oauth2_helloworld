package com.app.util;

import javax.servlet.http.HttpServletRequest;

import com.app.dto.UserLoginDTO;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Debug {
	
	public static final ObjectMapper OBJMAPPER = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
	
	public static void request(String functionName, Object obj, HttpServletRequest request) {
		try {
			String requestStr = "";
			if (obj instanceof UserLoginDTO) {
				UserLoginDTO user = (UserLoginDTO) obj;
				String password = user.getPassword();
				user.setPassword("xxx");
				requestStr = OBJMAPPER.writeValueAsString(user);
				user.setPassword(password);
			} else {
				requestStr = OBJMAPPER.writeValueAsString(obj);
			}
			System.out.println("----------Request(" + functionName + ") By IP:" + request.getRemoteAddr() + "-----------------\r\n" + requestStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void response(String functionName, Object obj, HttpServletRequest request) {
		try {			
			System.out.println("----------Response(" + functionName + ") By IP:" + request.getRemoteAddr() + "-----------------\r\n" + OBJMAPPER.writeValueAsString(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// #####################################################debug	
	public static void debugJson(Object obj) {
		try {
			System.out.println("######" + obj.getClass());
			System.out.println(OBJMAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
			System.out.println("###############################################");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}