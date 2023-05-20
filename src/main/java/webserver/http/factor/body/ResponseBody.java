package webserver.http.factor.body;

public class ResponseBody {

	private final byte[] body;

	public ResponseBody(byte[] body) {
		this.body = body;
	}

	public byte[] getBody() {
		return body;
	}

	public int getContentLength() {
		return body.length;
	}
}
