package io.github.schance;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> {

    List<Node<K, V>> table = new ArrayList<>();

    public V put(K key, V value) {
        for (Node<K, V> kvNode : table) {
            if (kvNode.key.equals(key)) {
                V oldValue = kvNode.value;
                kvNode.value = value;
                return oldValue;
            }
        }
        Node<K, V> newNode = new Node<>(key, value);
        table.add(newNode);
        return null;
    }

    public V get(K key) {
        for (Node<K, V> kvNode : table) {
            if (kvNode.key.equals(key)) {
                return kvNode.value;
            }
        }
        return null;
    }

    public V remove(K key) {
        for (int i = 0; i < table.size(); i++) {
            Node<K, V> kvNode = table.get(i);
            if (kvNode.key.equals(key)) {
                Node<K, V> removedNode = this.table.remove(i);
                return removedNode.value;
            }
        }
        return null;
    }

    public int size() {
        return this.table.size();
    }
    class Node<K, V> {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
