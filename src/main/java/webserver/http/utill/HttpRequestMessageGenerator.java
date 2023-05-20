package webserver.http.utill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.http.factor.HttpMethod;
import webserver.http.factor.QueryString;
import webserver.http.factor.body.RequestBody;
import webserver.http.factor.header.RequestHeader;
import webserver.http.factor.header.type.EntityHeaderType;
import webserver.http.factor.start_line.RequestLine;
import webserver.http.request.HttpRequestMessage;

public class HttpRequestMessageGenerator {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestMessageGenerator.class);
	private static final int START_LINE_INDEX = 0;
	private static final int HEADER_INDEX = 1;
	private static final int START_LINE_ELEMENT_NUMBER = 3;

	private HttpRequestMessageGenerator(){}

	public static HttpRequestMessage generateHttpRequestMessage(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		List<String> requestHeaderLineByLine = getRequestHeaderLineByLine(br);

		RequestLine requestLine = generateStartLine(requestHeaderLineByLine);

		RequestHeader header = generateRequestHeader(requestHeaderLineByLine);

		RequestBody requestBody = generateRequestBody(br, header);

		QueryString queryString = generateQueryString(requestLine, requestBody);

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

	private static RequestLine generateStartLine(List<String> requestHeaderLineByLine) {
		String[] splitStartLine;
		try {
			splitStartLine = requestHeaderLineByLine.get(START_LINE_INDEX).split(" ");
			if (splitStartLine.length != START_LINE_ELEMENT_NUMBER) {
				throw new IllegalArgumentException();
			}
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			logger.error("잘못된 요청 메시지가 입력되었습니다. {}", e.getMessage());
			splitStartLine = new String[]{"", "", ""};
		}

		return new RequestLine(HttpMethod.from(splitStartLine[0]), splitStartLine[1], splitStartLine[2]);
	}

	private static RequestHeader generateRequestHeader(List<String> requestHeaderLineByLine) {
		RequestHeader header = new RequestHeader();

		for(int i = HEADER_INDEX; i < requestHeaderLineByLine.size(); i++) {
			if(requestHeaderLineByLine.get(i) == null || requestHeaderLineByLine.get(i).isEmpty()) {
				break;
			}
			String[] splitHeader = requestHeaderLineByLine.get(i).split(":\\s*");
			header.put(splitHeader[0], splitHeader[1]);
		}

		return header;
	}

	private static RequestBody generateRequestBody(BufferedReader br, RequestHeader header) throws
		IOException {
		String contentLength = header.getFieldValue(EntityHeaderType.CONTENT_LENGTH);
		if(contentLength == null || contentLength.isEmpty()) {
			return new RequestBody("");
		}

		char[] body = new char[Integer.parseInt(contentLength)];
		br.read(body);

		return new RequestBody(new String(body));
	}

	private static QueryString generateQueryString(RequestLine requestLine, RequestBody body) {
		if(body.getBody().isEmpty()) {
			return new QueryString(requestLine.getQueryStringFromRequestTarget());
		}
		return new QueryString(body.getBody());
	}
}
