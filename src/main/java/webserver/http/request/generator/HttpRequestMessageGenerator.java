package webserver.http.request.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import webserver.http.component.QueryString;
import webserver.http.component.body.RequestBody;
import webserver.http.component.header.RequestHeader;
import webserver.http.component.start_line.RequestLine;
import webserver.http.request.HttpRequestMessage;

public class HttpRequestMessageGenerator {

	private HttpRequestMessageGenerator(){}

	public static HttpRequestMessage generateHttpRequestMessage(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		List<String> requestHeaderLineByLine = getRequestHeaderLineByLine(br);

		RequestLine requestLine = RequestLineGenerator.generateBy(requestHeaderLineByLine);

		RequestHeader header = RequestHeaderGenerator.generateBy(requestHeaderLineByLine);

		RequestBody requestBody = RequestBodyGenerator.generateBy(br, header);

		QueryString queryString = QueryStringGenerator.generateBy(requestLine, requestBody);

		return new HttpRequestMessage(requestLine, header, requestBody, queryString);
	}

	private static List<String> getRequestHeaderLineByLine(BufferedReader br) throws IOException {
		List<String> requestHeaderLineByLine = new ArrayList<>();

		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			requestHeaderLineByLine.add(line);
		}
		return requestHeaderLineByLine;
	}

}
