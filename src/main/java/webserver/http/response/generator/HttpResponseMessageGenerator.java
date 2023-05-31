package webserver.http.response.generator;

import webserver.http.component.body.ResponseBody;
import webserver.http.component.header.ResponseHeader;
import webserver.http.component.start_line.StatusLine;
import webserver.http.request.HttpRequestMessage;
import webserver.http.response.HttpResponseMessage;
import webserver.web.model.ModelAndView;
import webserver.web.utill.ControllerMapper;

public class HttpResponseMessageGenerator {

	private HttpResponseMessageGenerator() {}

	public static HttpResponseMessage generateHttpResponseMessage(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = selectRealTargetPath(httpRequestMessage);

		StatusLine statusLine = StatusLineGenerator.generateBy(httpRequestMessage, modelAndView);

		ResponseBody body = ResponseBodyGenerator.generateBy(httpRequestMessage, modelAndView);

		ResponseHeader header = ResponseHeaderGenerator.generateBy(httpRequestMessage, statusLine, body, modelAndView);

		return new HttpResponseMessage(statusLine, header, body);
	}

	private static ModelAndView selectRealTargetPath(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = ControllerMapper.runRequestMappingMethod(httpRequestMessage);

		if(modelAndView == null) {
			modelAndView = new ModelAndView(httpRequestMessage.getRequestTargetWithoutQueryString());
		}
		return modelAndView;
	}

}
