package webserver.http.request.generator;

import java.util.List;

import webserver.http.component.header.RequestHeader;

public class RequestHeaderGenerator {

	private static final int HEADER_INDEX = 1;

	private RequestHeaderGenerator() {}

	protected static RequestHeader generateBy(List<String> requestHeaderLineByLine) {
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
}
