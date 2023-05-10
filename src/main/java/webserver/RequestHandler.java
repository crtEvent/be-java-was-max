package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.WebConfig;
import model.MyModel;
import model.User;
import webserver.request.MyHttpRequest;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            MyHttpRequest myHttpRequest = new MyHttpRequest(in);
            logger.debug("<< HTTP Request Message >>\n{}", myHttpRequest);

            if(myHttpRequest.getQueryParams().size() > 0) {
                MyModel myModel = new MyModel(myHttpRequest.getQueryParams());
                User user = new User(myModel.get("userId"), myModel.get("password"), myModel.get("name"), myModel.get("email"));
                logger.debug("User : {}", user);
            }

            if(myHttpRequest.getMimeType().equals("text/html")) {
                String requestTarget = myHttpRequest.getRequestTarget();
                byte[] body = Files.readAllBytes(new File(WebConfig.DEFAULT_TEMPLATES_PATH + requestTarget).toPath());

                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            logger.error("읽을 수 없음: {}", e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
