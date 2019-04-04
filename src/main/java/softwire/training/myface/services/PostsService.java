package softwire.training.myface.services;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;
import softwire.training.myface.models.dbmodels.Post;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService extends DatabaseService {

    public List<Post> getPostsOnWall(String recipient) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM posts WHERE recipient = :recipient")
                        .bind("recipient", recipient)
                        .mapToBean(Post.class)
                        .list()
        );
    }

    public Post getSinglePostOnWall(int id) {
        return jdbi.withHandle(handle ->
            handle.createQuery("SELECT * FROM posts WHERE id = :id")
                    .bind("id", id)
                    .mapToBean(Post.class)
                    .findOnly()
        );
    }

    public void createPost(String sender, String recipient, String content) {
        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO posts (sender, recipient, content) VALUES (:sender, :recipient, :content)")
                        .bind("sender", sender)
                        .bind("recipient", recipient)
                        .bind("content", content)
                        .execute()
        );
    }

    public void deletePost(int id) {
        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM posts WHERE id = :id")
                    .bind("id", id)
                    .execute()
        );
    }

}
