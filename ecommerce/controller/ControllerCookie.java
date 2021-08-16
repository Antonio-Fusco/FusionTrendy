package org.generation.italy.ecommerce.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cookie")
public class ControllerCookie {

	@GetMapping()
	public String readAllCookies(HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			return Arrays.stream(cookies).map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(","));
		}

		return null;
	}

	@GetMapping(value = "/{providedSessionId}")
	public String getUserAndSessionId(UsernamePasswordAuthenticationToken activeUser,
			HttpServletRequest httpServletRequest, @PathVariable("providedSessionId") String sessionID) {
		System.out.println(httpServletRequest.getRequestedSessionId());
		// Session ID
		String sessionId = httpServletRequest.getRequestedSessionId();

		if (sessionId != null && sessionId.equals(sessionID) && activeUser != null) {
			// Username
			return activeUser.toString();
		}
		return null;

	}

}
