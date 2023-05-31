package webserver.http.response.generator;

import webserver.http.component.StatusCodeType;
import webserver.http.component.body.ResponseBody;
import webserver.http.component.header.ResponseHeader;
import webserver.http.component.header.type.EntityHeaderType;
import webserver.http.component.header.type.ResponseHeaderType;
import webserver.http.component.start_line.StatusLine;
import webserver.http.request.HttpRequestMessage;
import webserver.web.model.ModelAndView;

public class ResponseHeaderGenerator {

	private ResponseHeaderGenerator() {}

	protected static ResponseHeader generateBy(HttpRequestMessage httpRequestMessage, StatusLine statusLine, ResponseBody body, ModelAndView modelAndView) {
		String mimeType = httpRequestMessage.getMimeType();

		ResponseHeader header = new ResponseHeader();

		if(statusLine.isStatusCodeTypeMatch(StatusCodeType.OK_200)) {
			header.put(EntityHeaderType.CONTENT_TYPE.getFieldName(), mimeType + ";charset=utf-8");
			header.put(EntityHeaderType.CONTENT_LENGTH.getFieldName(), String.valueOf(body.getContentLength()));
		} else if(statusLine.isStatusCodeTypeMatch(StatusCodeType.FOUND_302)) {
			header.put(ResponseHeaderType.LOCATION.getFieldName(), modelAndView.getView());
			if(httpRequestMessage.getNewCookie() != null) {
				header.put(ResponseHeaderType.SET_COOKIE.getFieldName(), httpRequestMessage.getNewCookie().getSetCookieResponseHeaderValue());
			}
		}

		return header;
	}
}
