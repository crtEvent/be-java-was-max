package webserver.http.factor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webserver.http.factor.header.Header;

class HeaderTest {

	@DisplayName("존재하지 않는 헤더를 불러오면 빈 문자열을 반환한다.")
	@Test
	void testGetHeader() {
		Header header = new Header();

		Assertions.assertThat(header.getFieldValue("Unknown Header")).isEmpty();
	}

}
