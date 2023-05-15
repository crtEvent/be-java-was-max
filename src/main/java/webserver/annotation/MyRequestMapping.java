package webserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import webserver.http.MyRequestMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRequestMapping {
	String value() default "";
	MyRequestMethod method() default MyRequestMethod.GET;
}
