package lockfree;

import lockfree.PrioritySkipList.Node;

public class SkipQueue<T> {
    PrioritySkipList<T> skiplist;
    public SkipQueue() {
        skiplist = new PrioritySkipList<T>();
    }
    /**
     * Add an item to the priority queue
     * @param item add this item
     * @param priority
     * @return true iff queue was modified
     */
    public boolean add(T item, int priority) {
        Node<T> node = new Node<T>(item, priority);
        return skiplist.add(node);
    }

    public boolean remove(T item, int priority) {
        Node<T> node = new Node<T>(item, priority);
        return skiplist.remove(node);
    }

    /**
     * remove and return item with least priority
     * @return item with least priority
     */
    public T removeMin() {
        Node<T> node = skiplist.findAndMarkMin();
        if (node != null) {
            skiplist.remove(node);
            return node.item;
        } else{
            return null;
        }
    }

}
