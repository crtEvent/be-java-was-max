package webserver.http.factor.header;

import webserver.http.factor.header.type.EntityHeaderType;
import webserver.http.factor.header.type.ResponseHeaderType;

public class ResponseHeader extends Header {

	public String getFieldValue(ResponseHeaderType type){
		String value = headers.get(type.getFieldName());
		return value != null? value : "";
	}

	public String getFieldValue(EntityHeaderType type){
		String value = headers.get(type.getFieldName());
		return value != null? value : "";
	}

}
