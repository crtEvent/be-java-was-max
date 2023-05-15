package webserver;

import java.util.Objects;

import webserver.http.MyRequestMethod;

public class ControllerMapperKey {
	private final String url;
	private final MyRequestMethod requestMethod;
	private final Object instance;

	public ControllerMapperKey(String url, MyRequestMethod requestMethod, Object instance) {
		this.url = url;
		this.requestMethod = requestMethod;
		this.instance = instance;
	}

	public String getUrl() {
		return url;
	}

	public MyRequestMethod getRequestMethod() {
		return requestMethod;
	}

	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ControllerMapperKey that = (ControllerMapperKey) o;
		return Objects.equals(url, that.url) && requestMethod == that.requestMethod && Objects.equals(instance, that.instance) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, requestMethod, instance);
	}

	@Override
	public String toString() {
		return "ControllerMapperKey{" +
			"url='" + url + '\'' +
			", requestMethod=" + requestMethod +
			", instance=" + instance +
			'}';
	}
}
