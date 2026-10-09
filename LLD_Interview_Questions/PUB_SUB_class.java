/*
DESIGN PUB-SUB:

functional Req:
	1  create topic
	2. subscribe to a topic
	3. unsubscribe from a topic
	4. publish message
	5. deliver message to subscribers
	multiple publishers


non-functional Requirements:
	Thread-safe
	no blocking between topics
	low-latency
	extensible
	should follow SOLID Principles
	use observer design pattern
	scalable


Entities and Relationship:
							Entities, repositories, services, concurrency

Message
	id
	payload

Publisher (interface)
	- id
	- message
	- publishMessage()

MessageBroker
	<String, Topic>

Topic
	- List<Subscriber>
	- name
	- addSubsriber
	- removeSubsriber


Subscribers
	consume()


Publisher ----< Message , Topic >------> MessageBroker ---- Subscribers
*/

public class PUB_SUB_class {

    public static void main(String[] args) {
        System.out.println("PUB_SUB_class design notes. See PubSubSystem.java for full implementation.");
    }
}