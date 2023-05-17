package application.controller;

import java.util.UUID;

import application.db.Database;
import application.model.LoginRequest;
import application.model.User;
import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
import webserver.http.MyCookie;
import webserver.http.MyRequestMethod;
import webserver.http.request.MyHttpRequest;

@MyController
public class UserController {

	@MyRequestMapping(value = "/user/create", method= MyRequestMethod.GET)
	public String join(MyHttpRequest myHttpRequest) {

		User user = new User(myHttpRequest.getQueryParam("userId")
			, myHttpRequest.getQueryParam("password")
			, myHttpRequest.getQueryParam("name")
			, myHttpRequest.getQueryParam("email"));

		Database.addUser(user);

		return "/index.html";
	}

	@MyRequestMapping(value="/user/login", method = MyRequestMethod.POST)
	public String login(MyHttpRequest myHttpRequest) {
		LoginRequest loginRequest = new LoginRequest(myHttpRequest.getQueryParam("userId")
			, myHttpRequest.getQueryParam("password"));

		User user = Database.findUserById(loginRequest.getUserId());
		if(user != null && user.getPassword().equals(loginRequest.getPassword())) {
			MyCookie myCookie = new MyCookie();
			myCookie.putCookieValue("LOGIN_SID", UUID.randomUUID().toString());
			myCookie.putCookieValue("userId", loginRequest.getUserId());

			myHttpRequest.setCookie(myCookie);

			return "/index.html";
		}

		return "/user/login_failed.html";
	}
}
