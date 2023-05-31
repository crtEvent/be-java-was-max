package webserver.http.component.header;

import webserver.http.component.header.type.EntityHeaderType;
import webserver.http.component.header.type.ResponseHeaderType;

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
