package edu.wpi.fuchsiafalcons.pathfinding;
import java.util.*;

/**
 * Maintains a doubly-linked List- and HashMap-organized data structure
 * @param <Payload> the target object
 * @author Tony Vuolo
 */
public class DoublyLinkedHashSet<Payload> implements Iterable<Payload> {
    private final HashMap<Payload, HashNode<Payload>> map;
    private HashNode<Payload> head, tail;

    /**
     * Creates a DoublyLinkedHashSet
     */
    public DoublyLinkedHashSet() {
        this.map = new HashMap<>();
    }

    /**
     * Adds a HashNode into this DoublyLinkedHashSet
     * @param payload the target Payload
     */
    public boolean add(Payload payload) {
        if(this.map.containsKey(payload)) {
            return false;
        }
        HashNode<Payload> hashNode = new HashNode<>(payload);
        if(this.head == null) {
            this.head = hashNode;
        } else {
            this.tail.next = hashNode;
            hashNode.prev = this.tail;
        }
        this.tail = hashNode;
        this.map.put(payload, hashNode);
        return true;
    }

    /**
     * Inserts a HashNode into this DoublyLinkedHashSet at the specified index
     * @param index the index of this DoublyLinkedHashSet
     * @param payload the target Payload
     */
    public boolean insert(int index, Payload payload) {
        if(this.map.containsKey(payload)) {
            return false;
        }
        int size = size();
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        } else if(index == size) {
            add(payload);
        } else {
            final int iterations = Math.min(index, size - index + 1);
            HashNode<Payload> cursor = (iterations == index) ? this.head : this.tail;
            for(int i = 0; i < iterations; i++) {
                cursor = (iterations == index) ? cursor.next : cursor.prev;
            }
            HashNode<Payload> hashNode = new HashNode<>(payload);
            hashNode.prev = cursor.prev;
            if(cursor.prev == null) {
                this.head = hashNode;
            } else {
                hashNode.prev.next = hashNode;
            }
            cursor.prev = hashNode;
            hashNode.next = cursor;
            this.map.put(payload, hashNode);
        }
        return true;
    }

    /**
     * Checks if this DoublyLinkedHashSet contains a Payload
     * @param payload the target payload
     * @return true if this DoublyLinkedHashSet contains the target Payload, else false
     */
    public boolean containsKey(Payload payload) {
        return this.map.containsKey(payload);
    }

    /**
     * Gets the Payload located in the HashNode of a specific Payload
     * @param payload the target Payload
     * @return the desired Payload
     */
    public Payload get(Payload payload) {
        return this.map.get(payload).payload;
    }

    /**
     * Gets the Payload located at a specific index in this DoublyLinkedHashSet
     * @param index the target index
     * @return the target Payload
     */
    public Payload getIndex(int index) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } else {
            HashNode<Payload> cursor = this.head;
            int currentIndex = 0;
            while(currentIndex < index) {
                cursor = cursor.next;
                currentIndex++;
            }
            return cursor.payload;
        }
    }

    /**
     * Removes an element from this DoublyLinkedHashSet
     * @param hashCode the hashCode of the target element
     * @return true if an element was successfully removed, else false
     */
    public boolean remove(Payload hashCode) {
        HashNode<Payload> removedItem = this.map.get(hashCode);
        if(removedItem == null) {
            return false;
        }
        if(removedItem.prev == null) {
            this.head = removedItem.next;
        } else {
            removedItem.prev.next = removedItem.next;
        }
        if(removedItem.next == null) {
            this.tail = removedItem.prev;
        } else {
            removedItem.next.prev = removedItem.prev;
        }
        this.map.remove(hashCode);
        return true;
    }

    /**
     * Converts this DoublyLinkedHashSet to a List
     * @return the Payload contents of this DoublyLinkedHashSet in a List
     */
    public List<Payload> asList() {
        List<Payload> list = new LinkedList<>();
        HashNode<Payload> cursor = this.head;
        while(cursor != null) {
            list.add(cursor.payload);
            cursor = cursor.next;
        }
        return list;
    }

    /**
     * Finds the size of this DoublyLinkedHashSet
     * @return this.map.size
     */
    public int size() {
        return this.map.size();
    }

    /**
     * Converts this DoublyLinkedHashSet to a printable format
     * @return this DoublyLinkedHashSet as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        HashNode<Payload> cursor = this.head;
        while(cursor != null) {
            builder.append(", ").append(cursor.payload.toString());
            cursor = cursor.next;
        }
        return "[" + (this.head == null ? "" : builder.substring(2)) + "]";
    }

    /**
     * Returns an Iterator over elements
     * @return an Iterator
     */
    @Override
    public Iterator<Payload> iterator() {
        return new Iterator<Payload>() {
            private HashNode<Payload> cursor = DoublyLinkedHashSet.this.head;
            private final int size = DoublyLinkedHashSet.this.size();
            private int index = 0;

            /**
             * Checks if there is another unchecked element in this Iterator
             * @return true if the Iterator has not reached all HashNodes, else false
             */
            @Override
            public boolean hasNext() {
                return this.index < size;
            }

            /**
             * Gets the Payload at the target cursor HashNode
             * @return the target Payload if it exists, else null
             */
            @Override
            public Payload next() {
                if(this.index == this.size) {
                    throw new NoSuchElementException();
                } else {
                    Payload payload = this.cursor.payload;
                    this.cursor = this.cursor.next;
                    this.index++;
                    return payload;
                }
            }
        };
    }

    /**
     * The node component of a DoublyLinkedHashSet
     * @param <Payload> the item held in the HashNode
     * @author Tony Vuolo
     */
    private static class HashNode<Payload> {
        Payload payload;
        HashNode<Payload> next, prev;

        /**
         * Creates a new HashNode
         * @param payload the item held in the HashNode
         */
        private HashNode(Payload payload) {
            this.payload = payload;
        }
    }
}