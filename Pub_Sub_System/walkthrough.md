# Pub-Sub System LLD Java Implementation Walkthrough

We have successfully implemented the Low-Level Design (LLD) for the Pub-Sub System in Java under `/Users/rishi/Projects/LLD/Pub_Sub_System/src/`. The design strictly follows the tiered enterprise architecture (Client -> Controller -> Service -> Repository -> Domain) and the Observer pattern specifications from `Pub_Sub_System_Design.pdf`.

---

## 1. Package Structure Created
All source files are modularized under `/Users/rishi/Projects/LLD/Pub_Sub_System/src/`:

```
src/
├── Main.java                 (System bootstrap & end-to-end simulation runner)
├── model/
│   ├── DeliveryChannel.java  (EMAIL, REALTIME)
│   ├── DeliveryStatus.java   (PENDING, DELIVERED, ACKNOWLEDGED)
│   ├── Topic.java            (Domain model)
│   ├── Subscriber.java       (Domain model)
│   ├── Subscription.java     (Domain model)
│   ├── Message.java          (Domain model)
│   ├── MessageDelivery.java  (Domain delivery model)
│   ├── SubscriberObserver.java (Observer interface)
│   ├── EmailSubscriber.java  (Concrete observer class)
│   ├── RealtimeSubscriber.java (Concrete observer class)
│   └── MessageSubject.java   (Observer pattern subject coordinator)
├── repository/
│   ├── TopicRepository.java
│   ├── SubscriberRepository.java
│   ├── SubscriptionRepository.java
│   ├── MessageRepository.java
│   └── MessageDeliveryRepository.java
└── service/
    ├── TopicService.java      (Manages channel listings)
    ├── PublisherService.java  (Handles broadcasts & offline queue routing)
    ├── SubscriberService.java (Manages status updates & online queue flush)
    ├── SubscriptionService.java (Links users to channels)
    └── MessageService.java    (Processes client confirmations)
```

---

## 2. Key Accomplishments & Design Patterns Used

1. **Observer Design Pattern**:
   - `SubscriberObserver` is the observer interface.
   - `EmailSubscriber` and `RealtimeSubscriber` are concrete observers.
   - `MessageSubject` acts as the publisher/subject, maintaining independent lists of observers and notifying them.
2. **Dual-Channel Delivery & Deferred Queue Routing**:
   - `EmailSubscriber` receives messages immediately and unconditionally.
   - `RealtimeSubscriber` receives live socket messages **only if** the subscriber's state is online (`isOnline = true`).
   - If a subscriber is offline, live message deliveries are stored with a `PENDING` status. When they go online, all pending deliveries are pushed and settled automatically.
3. **Explicit Message Acknowledgment**:
   - Real-time messages require explicit acknowledgment, updating delivery status from `DELIVERED` to `ACKNOWLEDGED`.

---

## 3. Verification & Execution Output

The implementation has been successfully compiled and verified:

### Compilation Command
```bash
javac -d out src/model/*.java src/repository/*.java src/service/*.java src/controller/*.java src/Main.java
```

### Run Command
```bash
java -cp out Main
```

### Execution Log
```
==============================================================
         PUB SUB SYSTEM LOW LEVEL DESIGN BOOT                 
==============================================================

--- Setup: Creating Channels ---
[TopicService] Created topic: 'Sports News Feed' (id=TOPIC-68E49B5D)
[TopicService] Created topic: 'Tech Tech Feed' (id=TOPIC-A10A0E6D)

--- Setup: Registering Subscribers ---
[SubscriberService] Registered subscriber: alice@example.com (id=SUB-B31694E0)
[SubscriberService] Registered subscriber: bob@example.com (id=SUB-360E9A67)

>>> [SubscriberService] Subscriber SUB-B31694E0 is going ONLINE over connection: CONN-ALICE-101

>>> [SubscriberService] Subscriber SUB-360E9A67 is going ONLINE over connection: CONN-BOB-202

--- Setup: Managing Subscriptions ---
[SubscriptionService] Subscriber SUB-B31694E0 subscribed to Topic TOPIC-68E49B5D
[SubscriptionService] Subscriber SUB-360E9A67 subscribed to Topic TOPIC-68E49B5D
[SubscriptionService] Subscriber SUB-360E9A67 subscribed to Topic TOPIC-A10A0E6D

--- Simulation 1: Live Broadcast Sports Update (Everyone Online) ---

>>> [PublisherService] Publishing message: 'Real Madrid wins Champions League!' to Topic: 'Sports News Feed'
[Email Channel] Dispatching email notification to: bob@example.com for Message ID: MSG-ADC9E33F containing: 'Real Madrid wins Champions League!'
[Realtime Channel] Pushing live socket message over connection: CONN-BOB-202 for Subscriber ID: SUB-360E9A67 containing: 'Real Madrid wins Champions League!'
[Email Channel] Dispatching email notification to: alice@example.com for Message ID: MSG-ADC9E33F containing: 'Real Madrid wins Champions League!'
[Realtime Channel] Pushing live socket message over connection: CONN-ALICE-101 for Subscriber ID: SUB-B31694E0 containing: 'Real Madrid wins Champions League!'

--- Simulation 2: Bob goes Offline, Broadcast Tech Update ---

>>> [SubscriberService] Subscriber SUB-360E9A67 went OFFLINE.

>>> [PublisherService] Publishing message: 'Apple launches Vision Pro 2!' to Topic: 'Tech Tech Feed'
[Email Channel] Dispatching email notification to: bob@example.com for Message ID: MSG-4D99B7A9 containing: 'Apple launches Vision Pro 2!'
[Deferred Queue] Subscriber bob@example.com is currently offline. Deferred Realtime Message ID MSG-4D99B7A9 to Pending Queue.

>>> [PublisherService] Publishing message: 'Djokovic wins French Open!' to Topic: 'Sports News Feed'
[Email Channel] Dispatching email notification to: bob@example.com for Message ID: MSG-249CBBEC containing: 'Djokovic wins French Open!'
[Deferred Queue] Subscriber bob@example.com is currently offline. Deferred Realtime Message ID MSG-249CBBEC to Pending Queue.
[Email Channel] Dispatching email notification to: alice@example.com for Message ID: MSG-249CBBEC containing: 'Djokovic wins French Open!'
[Realtime Channel] Pushing live socket message over connection: CONN-ALICE-101 for Subscriber ID: SUB-B31694E0 containing: 'Djokovic wins French Open!'

--- Simulation 3: Bob comes Back Online (Triggering Recovery Queue) ---

>>> [SubscriberService] Subscriber SUB-360E9A67 is going ONLINE over connection: CONN-BOB-RECONNECTED-909
[Reconnection Recovery] Pushing 2 deferred realtime messages from pending queue to: bob@example.com
[Reconnection Push] Delivering message: 'Djokovic wins French Open!' over socket: CONN-BOB-RECONNECTED-909
[Reconnection Push] Delivering message: 'Apple launches Vision Pro 2!' over socket: CONN-BOB-RECONNECTED-909
[Reconnection Recovery] Completed delivery of all queued deferred messages.

--- Simulation 4: Bob acknowledges reconnected message ---
>>> [MessageService] Subscriber SUB-360E9A67 acknowledged Message ID: MSG-249CBBEC
[MessageService] Message settled and acknowledged successfully.
<<< [MessageService] Message ID: MSG-249CBBEC marked as ACKNOWLEDGED.

==============================================================
       PUB SUB SYSTEM LOW LEVEL DESIGN SIMULATION COMPLETE    
==============================================================
```
