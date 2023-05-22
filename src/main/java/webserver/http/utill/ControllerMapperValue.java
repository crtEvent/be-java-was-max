package webserver.http.utill;

import java.lang.reflect.Method;

public class ControllerMapperValue {
	private final Method method;
	private final Object instance;

	public ControllerMapperValue(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
	}

	public Method getMethod() {
		return method;
	}

	public Object getInstance() {
		return instance;
	}
}
