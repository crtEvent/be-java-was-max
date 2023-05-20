package application.controller;

import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
import webserver.http.factor.HttpMethod;
import webserver.http.request.HttpRequestMessage;

@MyController
public class HomeController {

	@MyRequestMapping(value = "/", method = HttpMethod.GET)
	public String home(HttpRequestMessage httpRequestMessage) {
		return "/index.html";
	}
}
