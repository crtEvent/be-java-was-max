package application.model;

import java.time.LocalDateTime;

public class Post {
	private int postNumber;
	private String title;
	private String content;
	private String userId;
	private String dateTime;

	public Post(int postNumber, String title, String content, String userId, LocalDateTime dateTime) {
		this.postNumber = postNumber;
		this.title = title;
		this.content = content;
		this.userId = userId;
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

	public String getUserId() {
		return userId;
	}

	public String getDateTime() {
		return dateTime;
	}
}
