import java.util.concurrent.ConcurrentHashMap;

/**
 * A simplified, thread-safe LRU Cache implementation in a single file.
 * Thread safety is achieved using simple synchronized methods.
 */
public class LRUCache<K, V> {

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + key + ":" + value + ")";
        }
    }

    private final int capacity;
    private final ConcurrentHashMap<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new ConcurrentHashMap<>();
        
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public synchronized V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return node.value;
    }

    public synchronized void put(K key, V value) {
        Node<K, V> node = map.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            if (map.size() >= capacity) {
                Node<K, V> lru = tail.prev;
                if (lru != head) {
                    map.remove(lru.key);
                    removeNode(lru);
                }
            }
            Node<K, V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            addToHead(newNode);
        }
    }

    public synchronized int size() {
        return map.size();
    }

    public synchronized void clear() {
        map.clear();
        head.next = tail;
        tail.prev = head;
    }

    // =========================================================================
    // Doubly Linked List Helpers (Always called inside synchronized methods)
    // =========================================================================

    private void addToHead(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    public synchronized void printCacheState() {
        StringBuilder sb = new StringBuilder("[Head] -> ");
        Node<K, V> current = head.next;
        while (current != tail) {
            sb.append(current).append(" -> ");
            current = current.next;
        }
        sb.append("[Tail]");
        System.out.println(sb.toString());
    }

    // =========================================================================
    // Demo Driver Main Method
    // =========================================================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Running Simple LRU Cache Demo ===");
        
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        
        System.out.println("Putting (1, One), (2, Two), (3, Three)...");
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        cache.printCacheState(); // Expected: 3 -> 2 -> 1
        
        System.out.println("Getting key 2: " + cache.get(2)); // Expected: "Two"
        cache.printCacheState(); // Expected: 2 -> 3 -> 1
        
        System.out.println("Putting (4, Four) (should evict key 1)...");
        cache.put(4, "Four");
        cache.printCacheState(); // Expected: 4 -> 2 -> 3
        
        System.out.println("Getting key 1 (should be null): " + cache.get(1)); // Expected: null
        
        System.out.println("\n=== Running Concurrent Thread-Safety Demo ===");
        LRUCache<Integer, Integer> concurrentCache = new LRUCache<>(10);
        
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                concurrentCache.put(i % 15, i);
                try { Thread.sleep(2); } catch (InterruptedException ignored) {}
            }
        });

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                concurrentCache.get(i % 15);
                try { Thread.sleep(2); } catch (InterruptedException ignored) {}
            }
        });

        writer.start();
        reader.start();
        
        writer.join();
        reader.join();
        
        System.out.print("Concurrent cache final state: ");
        concurrentCache.printCacheState();
        System.out.println("Cache size: " + concurrentCache.size());
        System.out.println("Demo completed successfully!");
    }
}
