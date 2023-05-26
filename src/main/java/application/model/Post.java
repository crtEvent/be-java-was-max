package application.model;

import java.time.LocalDateTime;

public class Post {
	private int postNumber;
	private String title;
	private String content;
	private String writer;
	private String dateTime;

	public Post(int postNumber, String title, String content, String writer, LocalDateTime dateTime) {
		this.postNumber = postNumber;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.dateTime = dateTime.toString();
	}

	public int getPostNumber() {
		return postNumber;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getWriter() {
		return writer;
	}

	public String getDateTime() {
		return dateTime;
	}
}
