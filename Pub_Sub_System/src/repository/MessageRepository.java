package repository;

import model.Message;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MessageRepository {
    private final Map<String, Message> messageMap = new ConcurrentHashMap<>();

    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        return message;
    }

    public Optional<Message> findById(String messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    public void deleteById(String messageId) {
        messageMap.remove(messageId);
    }
}
