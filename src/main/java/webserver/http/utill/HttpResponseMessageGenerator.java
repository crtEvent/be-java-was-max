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
import webserver.model.ModelAndView;

public class HttpResponseMessageGenerator {

	private static final Logger logger = LoggerFactory.getLogger(HttpResponseMessageGenerator.class);

	private HttpResponseMessageGenerator() {}

	public static HttpResponseMessage generateHttpResponseMessage(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = selectRealTargetPath(httpRequestMessage);

		StatusLine statusLine = generateStatusLine(httpRequestMessage);

		ResponseBody body = generateResponseBody(httpRequestMessage, modelAndView);

		ResponseHeader header = generateResponseHeader(httpRequestMessage, statusLine, body, modelAndView);

		return new HttpResponseMessage(statusLine, header, body);
	}

	private static ModelAndView selectRealTargetPath(HttpRequestMessage httpRequestMessage) {
		ModelAndView modelAndView = ControllerMapper.runRequestMappingMethod(httpRequestMessage);

		if(modelAndView == null) {
			modelAndView = new ModelAndView(httpRequestMessage.getRequestTarget());
		}
		return modelAndView;
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

	private static ResponseBody generateResponseBody(HttpRequestMessage httpRequestMessage, ModelAndView modelAndView) {
		String resourcePath;
		if (isTextHtmlMimeType(httpRequestMessage.getMimeType())) {
			resourcePath = WebConfig.getTemplatesResourcePath();
			return new ResponseBody(TemplateEngineParser.parseHtmlDynamically(new File(resourcePath + modelAndView.getView()).toPath(), modelAndView));
		} else {
			resourcePath = WebConfig.getStaticResourcePath();
			try {
				return new ResponseBody(Files.readAllBytes(new File(resourcePath + modelAndView.getView()).toPath()));
			} catch (IOException e) {
				return new ResponseBody(new byte[]{});
			}
		}
	}

	private static boolean isTextHtmlMimeType(String mimeType) {
		return MimeType.TEXT_HTML.getTypePhrase().equals(mimeType);
	}


	private static ResponseHeader generateResponseHeader(HttpRequestMessage httpRequestMessage, StatusLine statusLine, ResponseBody body, ModelAndView modelAndView) {
		String mimeType = httpRequestMessage.getMimeType();

		ResponseHeader header = new ResponseHeader();

		if(statusLine.isStatusCodeTypeMatch(StatusCodeType.OK_200)) {
			header.put(EntityHeaderType.CONTENT_TYPE.getFieldName(), mimeType);
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
