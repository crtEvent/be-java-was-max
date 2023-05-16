package application.controller;

import application.db.Database;
import application.model.User;
import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
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
}
