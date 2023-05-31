package webserver.web.utill;

import java.util.Objects;

import webserver.http.component.HttpMethod;

public class ControllerMapperKey {
	private final String url;
	private final HttpMethod requestMethod;

	public ControllerMapperKey(String url, HttpMethod requestMethod) {
		this.url = url;
		this.requestMethod = requestMethod;
	}

	public String getUrl() {
		return url;
	}

	public HttpMethod getRequestMethod() {
		return requestMethod;
	}

	public boolean isMatch(String url, HttpMethod requestMethod) {
		return Objects.equals(this.url, url) && this.requestMethod == requestMethod;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ControllerMapperKey that = (ControllerMapperKey) o;
		return Objects.equals(url, that.url) && requestMethod == that.requestMethod;
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, requestMethod);
	}

	@Override
	public String toString() {
		return "ControllerMapperKey{" +
			"url='" + url + '\'' +
			", requestMethod=" + requestMethod +
			'}';
	}
}
