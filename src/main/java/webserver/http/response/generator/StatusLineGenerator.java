package webserver.http.response.generator;

import webserver.http.component.StatusCodeType;
import webserver.http.component.start_line.StatusLine;
import webserver.http.request.HttpRequestMessage;
import webserver.web.model.ModelAndView;

public class StatusLineGenerator {

	private StatusLineGenerator() {}

	protected static StatusLine generateBy(HttpRequestMessage httpRequestMessage, ModelAndView modelAndView) {
		StatusCodeType statusCodeType;

		if(modelAndView.isRedirect()) {
			statusCodeType = StatusCodeType.FOUND_302;
		} else {
			statusCodeType = StatusCodeType.OK_200;
		}

		return new StatusLine(httpRequestMessage.getHttpVersion(), statusCodeType);
	}
}
