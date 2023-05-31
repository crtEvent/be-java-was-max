package webserver.http.request.generator;

import java.io.BufferedReader;
import java.io.IOException;

import webserver.http.component.body.RequestBody;
import webserver.http.component.header.RequestHeader;
import webserver.http.component.header.type.EntityHeaderType;

public class RequestBodyGenerator {

	private RequestBodyGenerator() {}

	protected static RequestBody generateBy(BufferedReader br, RequestHeader header) throws IOException {
		String contentLength = header.getFieldValue(EntityHeaderType.CONTENT_LENGTH);
		if(contentLength == null || contentLength.isEmpty()) {
			return new RequestBody("");
		}

		char[] body = new char[Integer.parseInt(contentLength)];
		br.read(body);

		return new RequestBody(new String(body));
	}
}
