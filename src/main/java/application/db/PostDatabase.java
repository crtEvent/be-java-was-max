package application.db;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import application.model.Post;
import application.model.PostWriteFormRequest;

public class PostDatabase {

	private static final List<Post> posts = new ArrayList<>();

	static {
		for(int i = 1; i <= 100; i++) {
			String formattedNumber = String.format("%03d", i);
			posts.add(new Post(i
					, "제목"+formattedNumber
					, "얼마나 많은 생각이 오갔는지... "+formattedNumber
					, "user"+formattedNumber
					, LocalDateTime.now()));
		}
	}

	private PostDatabase() {}

	public static void addPost(PostWriteFormRequest request) {
		int postNumber = posts.get(posts.size() - 1).getPostNumber() + 1;
		posts.add(request.toEntity(postNumber));
	}

	public static Post findPostBy(int index) {
		return posts.get(index);
	}

	public static Collection<Post> findAll() {
		List<Post> postsToReverse = new ArrayList<>(posts);
		Collections.reverse(postsToReverse);
		return Collections.unmodifiableCollection(postsToReverse);
	}
}
