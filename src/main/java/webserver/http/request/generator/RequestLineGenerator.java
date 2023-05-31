package webserver.http.request.generator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.http.component.HttpMethod;
import webserver.http.component.start_line.RequestLine;

public class RequestLineGenerator {

	private static final Logger logger = LoggerFactory.getLogger(RequestLineGenerator.class);
	private static final int START_LINE_INDEX = 0;
	private static final int START_LINE_ELEMENT_NUMBER = 3;

	private RequestLineGenerator() {}

	protected static RequestLine generateBy(List<String> requestHeaderLineByLine) {
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
}
