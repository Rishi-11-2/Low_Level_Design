package repository;

import model.Comment;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CommentRepository {
    private final Map<Integer, Comment> commentMap = new ConcurrentHashMap<>();

    public Comment save(Comment comment) {
        commentMap.put(comment.getId(), comment);
        return comment;
    }

    public List<Comment> findByTaskId(int taskId) {
        return commentMap.values().stream()
                .filter(c -> c.getTaskId() == taskId)
                .collect(Collectors.toList());
    }
}
