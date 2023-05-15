package application.controller;

import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
import webserver.http.MyRequestMethod;
import webserver.request.MyHttpRequest;

@MyController
public class HomeController {

	@MyRequestMapping(value = "/", method = MyRequestMethod.GET)
	public String home(MyHttpRequest myHttpRequest) {
		return "/index.html";
	}
}
