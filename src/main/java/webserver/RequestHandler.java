package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.http.request.MyHttpRequest;
import webserver.http.response.MyHttpResponse;

public class RequestHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private static final String REQUEST_EMOJI = "\uD83D\uDCE8";
	private static final String RESPONSE_EMOJI = "\uD83D\uDCEC";

	private final Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			MyHttpRequest myHttpRequest = new MyHttpRequest(in);
			debug(myHttpRequest);

			String realTargetPath = ControllerHandler.runRequestMappingMethod(myHttpRequest);

			MyHttpResponse myHttpResponse = new MyHttpResponse(myHttpRequest, realTargetPath);
			DataOutputStream dos = new DataOutputStream(out);
			debug(myHttpResponse);

			responseHeader(dos, myHttpResponse);
			responseBody(dos, myHttpResponse);

		} catch (IOException | InvocationTargetException | IllegalAccessException e) {
			logger.error("읽을 수 없음: {}", e.getMessage());
		}
	}

	private void debug(MyHttpResponse myHttpResponse) {
		logger.debug("<< HTTP Response Message >> \n{}\n{}", RESPONSE_EMOJI, myHttpResponse.responseHeader());
	}

	private void debug(MyHttpRequest myHttpRequest) {
		if (myHttpRequest.getMimeType().equals("text/html")) {
			logger.debug("<< HTTP Request Message >> \n{}\n{}", REQUEST_EMOJI, myHttpRequest);
		} else {
			logger.debug("<< HTTP Request Message >> \n{}\n{} {}", REQUEST_EMOJI, myHttpRequest.getMethod(),
				myHttpRequest.getRequestTarget());
		}
	}

	private void responseHeader(DataOutputStream dos, MyHttpResponse myHttpResponse) {
		try {
			dos.writeBytes(myHttpResponse.responseHeader());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, MyHttpResponse myHttpResponse) {
		try {
			dos.write(myHttpResponse.getBody(), 0, myHttpResponse.getContentLength());
			dos.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
