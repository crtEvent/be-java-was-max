package application.controller;

import java.util.ArrayList;

import application.db.PostDatabase;
import application.model.User;
import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
import webserver.http.factor.HttpMethod;
import webserver.http.factor.session.SessionController;
import webserver.http.request.HttpRequestMessage;
import webserver.model.ModelAndView;

@MyController
public class PostController {

	@MyRequestMapping(value = "/posts", method = HttpMethod.GET)
	public ModelAndView list(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = new ModelAndView("/index.html");

		User user = (User)SessionController.getSessionValue(httpRequestMessage, "LOGIN_SID");

		if (user != null) {
			modelAndView.addAttribute("userId", user.getUserId());
			modelAndView.addAttribute("password", user.getPassword());
		}

		modelAndView.addAttribute("posts", new ArrayList<>(PostDatabase.findAll()));

		return modelAndView;
	}

	@MyRequestMapping(value = "/posts/write-page", method = HttpMethod.GET)
	public ModelAndView writePage(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = new ModelAndView("/qna/form.html");

		User user = (User)SessionController.getSessionValue(httpRequestMessage, "LOGIN_SID");

		if (user != null) {
			modelAndView.addAttribute("userId", user.getUserId());
			modelAndView.addAttribute("password", user.getPassword());
			return modelAndView;
		}

		return new ModelAndView("/index.html");
	}

	@MyRequestMapping(value = "/posts", method = HttpMethod.POST)
	public ModelAndView write(HttpRequestMessage httpRequestMessage) {
		return new ModelAndView("/");
	}
}
