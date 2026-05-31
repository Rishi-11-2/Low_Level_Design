package service;

import model.Topic;
import repository.TopicRepository;
import java.util.List;
import java.util.UUID;

public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic createTopic(String name) {
        String id = "TOPIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Topic topic = new Topic(id, name);
        topicRepository.save(topic);
        System.out.println("[TopicService] Created topic: '" + name + "' (id=" + id + ")");
        return topic;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public void deactivateTopic(String topicId) {
        topicRepository.findById(topicId).ifPresent(t -> {
            t.setActive(false);
            topicRepository.save(t);
            System.out.println("[TopicService] Deactivated topic: '" + t.getName() + "'");
        });
    }
}
