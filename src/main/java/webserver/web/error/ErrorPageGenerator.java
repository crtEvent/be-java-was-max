package webserver.web.error;

import java.nio.charset.StandardCharsets;

import webserver.http.component.StatusCodeType;

public class ErrorPageGenerator {

	public byte[] generate(StatusCodeType type, Exception e) {
		String html = "<html><head></head><body><h1>화이트 라벨: " + type.getFullPhrase() + "</h1>" + generateErrorStackTrace(e) + "</body></html>";

		return html.getBytes(StandardCharsets.UTF_8);
	}

	private String generateErrorStackTrace(Exception e) {
		StringBuilder sb = new StringBuilder();

		sb.append("<h2>").append(e.toString()).append("</h2>");

		for(StackTraceElement element : e.getStackTrace()) {
			sb.append("<p>").append(element.toString()).append("</p>");
		}

		return sb.toString();
	}
}
