package application.controller;

import java.util.UUID;

import application.db.Database;
import application.model.LoginRequest;
import application.model.User;
import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
import webserver.http.factor.Cookie;
import webserver.http.factor.HttpMethod;
import webserver.http.request.HttpRequestMessage;

@MyController
public class UserController {

	@MyRequestMapping(value = "/user/create", method= HttpMethod.GET)
	public String join(HttpRequestMessage httpRequestMessage) {

		User user = new User(httpRequestMessage.getQueryParam("userId")
			, httpRequestMessage.getQueryParam("password")
			, httpRequestMessage.getQueryParam("name")
			, httpRequestMessage.getQueryParam("email"));

		Database.addUser(user);

		return "/index.html";
	}

	@MyRequestMapping(value="/user/login", method = HttpMethod.POST)
	public String login(HttpRequestMessage httpRequestMessage) {
		LoginRequest loginRequest = new LoginRequest(httpRequestMessage.getQueryParam("userId")
			, httpRequestMessage.getQueryParam("password"));

		User user = Database.findUserById(loginRequest.getUserId());
		if(user != null && user.getPassword().equals(loginRequest.getPassword())) {
			Cookie cookie = new Cookie();
			cookie.putCookieValue("LOGIN_SID", UUID.randomUUID().toString());
			cookie.putCookieValue("userId", loginRequest.getUserId());

			httpRequestMessage.setCookie(cookie);

			return "/index.html";
		}

		return "/user/login_failed.html";
	}
}
