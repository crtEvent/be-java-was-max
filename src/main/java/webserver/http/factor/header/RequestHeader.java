package webserver.http.factor.header;

import webserver.http.factor.header.type.EntityHeaderType;
import webserver.http.factor.header.type.RequestHeaderType;

public class RequestHeader extends Header {

	public String getFieldValue(RequestHeaderType type){
		String value = headers.get(type.getFieldName());
		return value != null? value : "";
	}

	public String getFieldValue(EntityHeaderType type){
		String value = headers.get(type.getFieldName());
		return value != null? value : "";
	}
}
