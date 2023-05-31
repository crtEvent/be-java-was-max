package webserver.http.component;

// RFC-2616 section 10: https://datatracker.ietf.org/doc/html/rfc2616#section-10
// Status Code: https://developer.mozilla.org/ko/docs/Web/HTTP/Status
public enum StatusCodeType {
	OK_200("200", "OK"),
	FOUND_302("302", "Found"),
	BAD_REQUEST_400("400", "Bad Request"),
	NOT_FOUND_404("404", "Not Found");

	private final String codePhrase;
	private final String reasonPhrase;

	StatusCodeType(String codePhrase, String reasonPhrase) {
		this.codePhrase = codePhrase;
		this.reasonPhrase = reasonPhrase;
	}

	public static StatusCodeType from(String codePhrase) {
		for(StatusCodeType statusCode : StatusCodeType.values()) {
			if(statusCode.getCodePhrase().equals(codePhrase)) {
				return statusCode;
			}
		}
		return BAD_REQUEST_400;
	}

	public String getCodePhrase() {
		return codePhrase;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public String getFullPhrase() {
		return codePhrase + " " + reasonPhrase;
	}
}
