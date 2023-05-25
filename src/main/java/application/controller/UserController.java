package application.controller;

import java.util.ArrayList;

import application.db.UserDatabase;
import application.model.LoginRequest;
import application.model.User;
import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
import webserver.http.factor.HttpMethod;
import webserver.http.factor.session.SessionController;
import webserver.http.factor.session.SessionMap;
import webserver.http.request.HttpRequestMessage;
import webserver.model.ModelAndView;

@MyController
public class UserController {

	@MyRequestMapping(value="/users/join-page", method = HttpMethod.GET)
	public ModelAndView joinPage(HttpRequestMessage httpRequestMessage) {
		return new ModelAndView("/user/form.html");
	}

	@MyRequestMapping(value = "/users", method= HttpMethod.POST)
	public ModelAndView join(HttpRequestMessage httpRequestMessage) {

		User user = new User(httpRequestMessage.getQueryParam("userId")
			, httpRequestMessage.getQueryParam("password")
			, httpRequestMessage.getQueryParam("name")
			, httpRequestMessage.getQueryParam("email"));

		UserDatabase.addUser(user);

		return new ModelAndView("/index.html");
	}

	@MyRequestMapping(value="/users/login-page", method = HttpMethod.GET)
	public ModelAndView loginPage(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = new ModelAndView("/index.html");

		User user = (User) SessionController.getSessionValue(httpRequestMessage, "LOGIN_SID");

		if(user != null) {
			modelAndView.addAttribute("userId", user.getUserId());
			modelAndView.addAttribute("password", user.getPassword());
			return modelAndView;
		}

		return new ModelAndView("/user/login.html");
	}

	@MyRequestMapping(value="/users/login", method = HttpMethod.POST)
	public ModelAndView login(HttpRequestMessage httpRequestMessage) {
		LoginRequest loginRequest = new LoginRequest(httpRequestMessage.getQueryParam("userId")
			, httpRequestMessage.getQueryParam("password"));

		User user = UserDatabase.findUserById(loginRequest.getUserId());
		if(user != null && user.getPassword().equals(loginRequest.getPassword())) {
			SessionMap.addSession("LOGIN_SID", user, httpRequestMessage);

			return new ModelAndView("/");
		}

		return new ModelAndView("/user/login_failed.html");
	}

	@MyRequestMapping(value="/users", method=HttpMethod.GET)
	public ModelAndView list(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = new ModelAndView("/user/list.html");

		User user = (User) SessionController.getSessionValue(httpRequestMessage, "LOGIN_SID");

		if(user != null) {
			modelAndView.addAttribute("userId", user.getUserId());
			modelAndView.addAttribute("password", user.getPassword());
			modelAndView.addAttribute("users", new ArrayList<>(UserDatabase.findAll()));
			return modelAndView;
		}

		return new ModelAndView("/index.html");
	}
}
