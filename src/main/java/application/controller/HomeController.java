package application.controller;

import java.util.ArrayList;

import application.db.PostDatabase;
import application.model.User;
import webserver.web.annotation.MyController;
import webserver.web.annotation.MyRequestMapping;
import webserver.http.component.HttpMethod;
import webserver.web.session.SessionController;
import webserver.http.request.HttpRequestMessage;
import webserver.web.model.ModelAndView;

@MyController
public class HomeController {

	@MyRequestMapping(value = "/", method = HttpMethod.GET)
	public ModelAndView home(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = new ModelAndView("/index.html");

		User user = (User) SessionController.getSessionValue(httpRequestMessage, "LOGIN_SID");

		if(user != null) {
			modelAndView.addAttribute("userId", user.getUserId());
			modelAndView.addAttribute("password", user.getPassword());
		}

		modelAndView.addAttribute("posts", new ArrayList<>(PostDatabase.findAll()));

		return modelAndView;
	}
}
