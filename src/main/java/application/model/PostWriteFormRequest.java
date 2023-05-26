package application.model;

import java.time.LocalDateTime;

public class PostWriteFormRequest {

	private String title;
	private String content;
	private String userId;

	public PostWriteFormRequest(String title, String content, String userId) {
		this.title = title;
		this.content = content;
		this.userId = userId;
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

	public Post toEntity(int postNumber) {
		return new Post(postNumber, title, content, userId, LocalDateTime.now());
	}

}
