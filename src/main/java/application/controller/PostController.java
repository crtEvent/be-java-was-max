package application.controller;

import java.util.ArrayList;

import application.db.PostDatabase;
import application.model.Post;
import application.model.PostWriteFormRequest;
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

		modelAndView.setView("/index.html");
		modelAndView.addAttribute("posts", new ArrayList<>(PostDatabase.findAll()));
		return modelAndView;
	}

	@MyRequestMapping(value = "/posts", method = HttpMethod.POST)
	public ModelAndView write(HttpRequestMessage httpRequestMessage) {

		PostWriteFormRequest postWriteFormRequest = new PostWriteFormRequest(httpRequestMessage.getQueryParam("title")
			, httpRequestMessage.getQueryParam("content")
			, httpRequestMessage.getQueryParam("userId"));

		PostDatabase.addPost(postWriteFormRequest);

		return new ModelAndView("/");
	}

	@MyRequestMapping(value="/posts/view-page",method = HttpMethod.GET)
	public ModelAndView selectOne(HttpRequestMessage httpRequestMessage){
		ModelAndView modelAndView = new ModelAndView("/qna/show.html");

		User user = (User)SessionController.getSessionValue(httpRequestMessage, "LOGIN_SID");

		if (user == null) {
			return new ModelAndView("/user/login.html");
		}

		modelAndView.addAttribute("userId", user.getUserId());
		modelAndView.addAttribute("password", user.getPassword());

		try {
			int postNumber = Integer.parseInt(httpRequestMessage.getQueryParam("postNumber"));
			Post post = PostDatabase.findPostBy(postNumber-1);
			modelAndView.addAttribute("title", post.getTitle());
			modelAndView.addAttribute("content", post.getContent());
			modelAndView.addAttribute("writer", post.getWriter());

			return modelAndView;
		} catch (NumberFormatException e) {
			modelAndView.setView("/index.html");
			modelAndView.addAttribute("posts", new ArrayList<>(PostDatabase.findAll()));
			return modelAndView;
		}

	}

}
