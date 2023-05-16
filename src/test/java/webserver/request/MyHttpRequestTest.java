package webserver.request;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webserver.http.request.MyHttpRequest;

class MyHttpRequestTest {

	private final String validHttpRequestMessage = "GET /user/form.html HTTP/1.1\n"
		+ "Host: localhost:8080\n"
		+ "Connection: keep-alive\n"
		+ "Cache-Control: max-age=0\n"
		+ "sec-ch-ua: \"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"\n"
		+ "sec-ch-ua-mobile: ?0\n"
		+ "sec-ch-ua-platform: \"Windows\"\n"
		+ "Upgrade-Insecure-Requests: 1\n"
		+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36\n"
		+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
		+ "Sec-Fetch-Site: none\n"
		+ "Sec-Fetch-Mode: navigate\n"
		+ "Sec-Fetch-User: ?1\n"
		+ "Sec-Fetch-Dest: document\n"
		+ "Accept-Encoding: gzip, deflate, br\n"
		+ "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n"
		+ "Cookie: rememberCheckbox=admin; JSESSIONID=F466F13D43C617D547721036DA103590";

	private final String validHttpRequestMessageWithQueryString = "GET /user/create?userId=user01&password=1234&name=ape&email=&hobby=reading&hobby=swimming&hobby= HTTP/1.1\n"
		+ "Host: localhost:8080\n"
		+ "Connection: keep-alive\n"
		+ "Cache-Control: max-age=0\n"
		+ "sec-ch-ua: \"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"\n"
		+ "sec-ch-ua-mobile: ?0\n"
		+ "sec-ch-ua-platform: \"Windows\"\n"
		+ "Upgrade-Insecure-Requests: 1\n"
		+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36\n"
		+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
		+ "Sec-Fetch-Site: none\n"
		+ "Sec-Fetch-Mode: navigate\n"
		+ "Sec-Fetch-User: ?1\n"
		+ "Sec-Fetch-Dest: document\n"
		+ "Accept-Encoding: gzip, deflate, br\n"
		+ "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n"
		+ "Cookie: rememberCheckbox=admin; JSESSIONID=F466F13D43C617D547721036DA103590";

	private final String validPostHttpRequestMessage = "POST /user/create HTTP/1.1\n"
		+ "Host: localhost:8080\n"
		+ "Connection: keep-alive\n"
		+ "Content-Length: 79\n"
		+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
		+ "\n"
		+ "userId=user01&password=1234&name=ape&email=&hobby=reading&hobby=swimming&hobby=";

	private final String InvalidHttpRequestMessage = "invalid http request message";
	private final String emptyHttpRequestMessage = "";

	private InputStream in;
	private MyHttpRequest myHttpRequest;

	@DisplayName("리퀘스트 메시지에서 request-target(URI)만 추출할 수 있다.")
	@Test
	void testGetUriPath() throws IOException {
		in = new ByteArrayInputStream(validHttpRequestMessage.getBytes(StandardCharsets.UTF_8));
		myHttpRequest = new MyHttpRequest(in);

		String path = myHttpRequest.getRequestTarget();
		assertThat(path).isEqualTo("/user/form.html");
	}

	@DisplayName("리퀘스트 메시지에서 queryString의 parameter를 각각 추출할 수 있다.")
	@Test
	void testGetQueryParams() throws IOException {
		in = new ByteArrayInputStream(validHttpRequestMessageWithQueryString.getBytes(StandardCharsets.UTF_8));
		myHttpRequest = new MyHttpRequest(in);

		SoftAssertions softAssertions = new SoftAssertions();
		softAssertions.assertThat(myHttpRequest.getQueryParam("userId")).isEqualTo("user01");
		softAssertions.assertThat(myHttpRequest.getQueryParam("password")).isEqualTo("1234");
		softAssertions.assertThat(myHttpRequest.getQueryParam("name")).isEqualTo("ape");
		softAssertions.assertThat(myHttpRequest.getQueryParam("email")).isEqualTo("");
		softAssertions.assertThat(myHttpRequest.getQueryParam("hobby")).isEqualTo("reading,swimming");

		softAssertions.assertAll();
	}

	@DisplayName("리퀘스트 메시지에서 존재하지 않는 queryString의 parameter 추출 시 빈 문자열을 반환한다.")
	@Test
	void testGetEmptyQueryParams() throws IOException {
		in = new ByteArrayInputStream(validHttpRequestMessage.getBytes(StandardCharsets.UTF_8));
		myHttpRequest = new MyHttpRequest(in);

		String paramValue = myHttpRequest.getQueryParam("not exist");
		assertThat(paramValue).isEmpty();
	}

	@DisplayName("잘못된 리퀘스트 메시지가 들어가도 에러가 나지 않는다.")
	@Test
	void testFail() throws IOException {
		in = new ByteArrayInputStream(emptyHttpRequestMessage.getBytes(StandardCharsets.UTF_8));
		myHttpRequest = new MyHttpRequest(in);

		SoftAssertions softAssertions = new SoftAssertions();

		softAssertions.assertThatCode(() -> {
			myHttpRequest = new MyHttpRequest(in);
		}).doesNotThrowAnyException();

		in = new ByteArrayInputStream(InvalidHttpRequestMessage.getBytes(StandardCharsets.UTF_8));
		myHttpRequest = new MyHttpRequest(in);

		softAssertions.assertThatCode(() -> {
			myHttpRequest = new MyHttpRequest(in);
		}).doesNotThrowAnyException();

		softAssertions.assertAll();
	}

	@DisplayName("리퀘스트 메시지에서 MIME Type만 추출할 수 있다.")
	@Test
	void testHeader() throws IOException {
		in = new ByteArrayInputStream(validHttpRequestMessage.getBytes(StandardCharsets.UTF_8));
		myHttpRequest = new MyHttpRequest(in);

		String mimdeType = myHttpRequest.getMimeType();
		assertThat(mimdeType).isEqualTo("text/html");
	}

	@DisplayName("POST 요청일 때 body의 파라미터를 추출할 수 있다.")
	@Test
	void testBody() throws IOException {
		in = new ByteArrayInputStream(validPostHttpRequestMessage.getBytes(StandardCharsets.UTF_8));
		myHttpRequest = new MyHttpRequest(in);

		SoftAssertions softAssertions = new SoftAssertions();
		softAssertions.assertThat(myHttpRequest.getQueryParam("userId")).isEqualTo("user01");
		softAssertions.assertThat(myHttpRequest.getQueryParam("password")).isEqualTo("1234");
		softAssertions.assertThat(myHttpRequest.getQueryParam("name")).isEqualTo("ape");
		softAssertions.assertThat(myHttpRequest.getQueryParam("email")).isEqualTo("");
		softAssertions.assertThat(myHttpRequest.getQueryParam("hobby")).isEqualTo("reading,swimming");

		softAssertions.assertAll();
	}

}
