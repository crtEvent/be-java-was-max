package webserver.http.response.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import webserver.http.component.MimeType;
import webserver.http.component.body.ResponseBody;
import webserver.http.request.HttpRequestMessage;
import webserver.web.config.WebConfig;
import webserver.web.model.ModelAndView;
import webserver.web.utill.TemplateEngineParser;

public class ResponseBodyGenerator {

	private ResponseBodyGenerator() {}

	protected static ResponseBody generateBy(HttpRequestMessage httpRequestMessage, ModelAndView modelAndView) {
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
}
