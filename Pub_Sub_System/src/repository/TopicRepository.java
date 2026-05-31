package repository;

import model.Topic;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TopicRepository {
    private final Map<String, Topic> topicMap = new ConcurrentHashMap<>();

    public Topic save(Topic topic) {
        topicMap.put(topic.getId(), topic);
        return topic;
    }

    public List<Topic> findAll() {
        return new ArrayList<>(topicMap.values());
    }

    public Optional<Topic> findById(String topicId) {
        return Optional.ofNullable(topicMap.get(topicId));
    }

    public void deleteById(String topicId) {
        topicMap.remove(topicId);
    }
}
