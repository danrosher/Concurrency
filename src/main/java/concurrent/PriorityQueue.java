package concurrent;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityQueue<T extends Comparable<T>> {

    PriorityBlockingQueue<Node<T>> pq;

    public PriorityQueue(){
        pq = new PriorityBlockingQueue<Node<T>>();
    }

    public boolean add(T item, int priority) {
        Node<T> node = new Node<T>(item, priority);
        return pq.add(node);
    }

    public boolean remove(T item, int priority) {
        Node<T> node = new Node<T>(item, priority);
        return pq.remove(node);
    }

    public T removeMin() {
        Node<T> t = pq.poll();
        return t == null ? null : t.item;
    }

    public static final class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
        final T item;
        final int priority;

        public Node(T item, int priority) {
            this.item = item;
            this.priority = priority;
        }

        public Node(int priority) {
            this.item = null;
            this.priority = priority;
        }

        public int compareTo(Node<T> o) {
            return item.compareTo(o.item);
        }
    }
}
