package webserver.http.utill;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.config.WebConfig;
import webserver.http.factor.HttpMethod;
import webserver.http.factor.MimeType;
import webserver.http.factor.StatusCodeType;
import webserver.http.factor.body.ResponseBody;
import webserver.http.factor.header.ResponseHeader;
import webserver.http.factor.header.type.EntityHeaderType;
import webserver.http.factor.header.type.ResponseHeaderType;
import webserver.http.factor.start_line.StatusLine;
import webserver.http.request.HttpRequestMessage;
import webserver.http.response.HttpResponseMessage;

public class HttpResponseMessageGenerator {

	private static final Logger logger = LoggerFactory.getLogger(HttpResponseMessageGenerator.class);

	private HttpResponseMessageGenerator() {}

	public static HttpResponseMessage generateHttpResponseMessage(HttpRequestMessage httpRequestMessage) {
		String realTargetPath = selectRealTargetPath(httpRequestMessage);

		StatusLine statusLine = generateStatusLine(httpRequestMessage);

		ResponseBody body = generateResponseBody(httpRequestMessage, realTargetPath);

		ResponseHeader header = generateResponseHeader(httpRequestMessage, statusLine, body, realTargetPath);

		return new HttpResponseMessage(statusLine, header, body);
	}

	private static String selectRealTargetPath(HttpRequestMessage httpRequestMessage) {
		String realTargetPath = ControllerHandler.runRequestMappingMethod(httpRequestMessage);

		if(realTargetPath.isEmpty()) {
			return httpRequestMessage.getRequestTarget();
		}
		return realTargetPath;
	}

	private static StatusLine generateStatusLine(HttpRequestMessage httpRequestMessage) {
		HttpMethod httpMethod = httpRequestMessage.getMethod();
		StatusCodeType statusCodeType;

		if(httpMethod == HttpMethod.POST) {
			statusCodeType = StatusCodeType.FOUND_302;
		} else {
			statusCodeType = StatusCodeType.OK_200;
		}

		return new StatusLine(httpRequestMessage.getHttpVersion(), statusCodeType);
	}

	private static ResponseBody generateResponseBody(HttpRequestMessage httpRequestMessage, String realRequestTarget) {
		String resourcePath;
		if (MimeType.TEXT_HTML.getTypePhrase().equals(httpRequestMessage.getMimeType())) {
			resourcePath = WebConfig.getTemplatesResourcePath();
		} else {
			resourcePath = WebConfig.getStaticResourcePath();
		}

		try {
			return new ResponseBody(Files.readAllBytes(new File(resourcePath + realRequestTarget).toPath()));
		} catch (IOException e) {
			return new ResponseBody(new byte[]{});
		}
	}


	private static ResponseHeader generateResponseHeader(HttpRequestMessage httpRequestMessage, StatusLine statusLine, ResponseBody body, String realTargetPath) {
		String mimeType = httpRequestMessage.getMimeType();

		ResponseHeader header = new ResponseHeader();

		if(statusLine.isStatusCodeTypeMatch(StatusCodeType.OK_200)) {
			header.put(EntityHeaderType.CONTENT_TYPE.getFieldName(), mimeType);
			header.put(EntityHeaderType.CONTENT_LENGTH.getFieldName(), String.valueOf(body.getContentLength()));
		} else if(statusLine.isStatusCodeTypeMatch(StatusCodeType.FOUND_302)) {
			header.put(ResponseHeaderType.LOCATION.getFieldName(), realTargetPath);
			if(httpRequestMessage.getCookie() != null) {
				header.put(ResponseHeaderType.SET_COOKIE.getFieldName(), httpRequestMessage.getCookie().getSetCookieResponseHeaderValue());
			}
		}

		return header;
	}
}
