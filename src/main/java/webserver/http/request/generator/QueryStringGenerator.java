package webserver.http.request.generator;

import webserver.http.component.QueryString;
import webserver.http.component.body.RequestBody;
import webserver.http.component.start_line.RequestLine;

public class QueryStringGenerator {

	private QueryStringGenerator() {}

	protected static QueryString generateBy(RequestLine requestLine, RequestBody body) {
		if(body.getBody().isEmpty()) {
			return new QueryString(requestLine.getQueryStringFromRequestTarget());
		}
		return new QueryString(body.getBody());
	}
}
