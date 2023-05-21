package webserver.http.utill;

import java.lang.reflect.Method;

public class ControllerHandlerValue {
	private final Method method;
	private final Object instance;

	public ControllerHandlerValue(Method method, Object instance) {
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
