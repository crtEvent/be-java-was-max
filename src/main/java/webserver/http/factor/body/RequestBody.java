package webserver.http.factor.body;

public class RequestBody {
	private final String body;

	public RequestBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}
}
