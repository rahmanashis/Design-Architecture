package design_patterns.structural.cache;

import java.util.HashMap;
import java.util.Map;

class Node<K, V> {
    K key;
    V value;
    Node<K, V> prev;
    Node<K, V> next;

    Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}


class DoublyLinkedList<K,V> {
    private final Node<K,V> head;
    private final Node<K,V> tail;

    public DoublyLinkedList(){
        head = new Node<>(null, null);
        tail = new Node<>(null, null);

        //Link Them Together
        head.next = tail;
        tail.prev = head;
    }

    public void addFirst(Node<K,V> node){
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }


    public void remove(Node<K,V> node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public void moveToFront(Node<K,V> node){
        remove(node);
        addFirst(node);
    }



    public Node<K,V> removeLast() {
        // check if list is empty (only dummies present)
        if(tail.prev == head) {
            return null;
        }

        Node<K,V> last = tail.prev;
        remove(last);
        return last;
    }


}


class LRUCache<K,V>{
    private final int capacity;
    private final Map<K,Node<K,V>> map;
    private final DoublyLinkedList<K,V> list; // to maintain the order MRU ---- LRU

    public LRUCache(int capacity){
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.list = new DoublyLinkedList<>();
    }

    public synchronized V get(K key){
        if(!map.containsKey(key)){
            return null;
        }
        Node<K,V> node = map.get(key);
        list.moveToFront(node);
        return node.value;
    }

    public synchronized void put(K key, V value){
        if(map.containsKey(key)){
            Node<K,V> node = map.get(key);
            node.value = value;
            list.moveToFront(node);
        }else{

            if(map.size() == capacity){
                Node<K,V> lru = list.removeLast();
                if(lru != null){
                    map.remove(lru.key);
                }
            }

            Node<K,V> newNode = new Node<>(key, value);
            list.addFirst(newNode);
            map.put(key, newNode);
        }
    }

}


public class LRUCacheDemo{
    public static void main(String[] args) {
            LRUCache<String,Integer> cache = new LRUCache<>(3);
            cache.get();
            cache.put();
    }
}