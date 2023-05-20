package webserver.http.factor;

// RFC-2045 - MIME Part One: Format of Internet Message Bodies: https://datatracker.ietf.org/doc/html/rfc2045
// Mime Wiki: https://ko.wikipedia.org/wiki/MIME
// MimeType 설명: https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types
// MimeType 전체 종류: https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types

public enum MimeType {
	ALL_TYPES("*/*"),
	TEXT_PLAIN("text/plain"),
	TEXT_HTML("text/html"),
	TEXT_CSS("text/css"),
	TEXT_JAVASCRIPT("text/javascript"),
	IMAGE_AVIF("image/avif"),
	IMAGE_GIF("image/gif"),
	IMAGE_PNG("image/png"),
	IMAGE_JPEG("image/jpeg"),
	IMAGE_BMP("image/bmp"),
	IMAGE_WEBP("image/webp");


	final String typePhrase;

	MimeType(String typePhrase) {
		this.typePhrase = typePhrase;
	}

	public String getTypePhrase() {
		return typePhrase;
	}
}
