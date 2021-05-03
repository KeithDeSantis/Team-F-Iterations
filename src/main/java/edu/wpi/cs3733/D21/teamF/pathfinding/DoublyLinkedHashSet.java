package edu.wpi.cs3733.D21.teamF.pathfinding;
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
     * Removes an element from this DoublyLinkedHashSet if the index is in the required bounds
     * @param index the index of the target element
     * @return the element at the specified index
     */
    public Payload removeIndex(int index) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } else {
            HashNode<Payload> head = this.head;
            int cursorIndex = 0;
            while(cursorIndex < index) {
                head = head.next;
                cursorIndex++;
            }
            Payload removedPayload = head.payload;
            remove(head.payload);
            return removedPayload;
        }
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
     * Links this DoublyLinkedHashSets with another one
     * @param set the augend set
     * @author Tony Vuolo (bdane)
     */
    public void concatenate(DoublyLinkedHashSet<Payload> set) {
        if(set != null) {
            if(this.tail != null) {
                set.head.prev = this.tail;
                this.tail.next = set.head;
            } else {
                this.head = set.head;
            }
            this.tail = set.tail;
            this.map.putAll(set.map);
        }
    }

    /**
     * Gets the head of this DoublyLinkedHashSet
     * @return this.head
     * @author Tony Vuolo (bdane)
     */
    public Payload getHead() {
        return this.head == null ? null : this.head.payload;
    }

    /**
     * Gets the tail of this DoublyLinkedHashSet
     * @return this.tail
     * @author Tony Vuolo (bdane)
     */
    public Payload getTail() {
        return this.tail == null ? null : this.tail.payload;
    }

    /**
     * Switches a Payload with the value after it
     * @param payload the target Payload
     * @author Tony Vuolo (bdane)
     */
    public void switchAfter(Payload payload) {
        HashNode<Payload> node = this.map.get(payload);
        if(node.next != null) {
            Payload nextPayload = node.next.payload;
            remove(payload);
            insertAfter(nextPayload, payload);
        }
    }

    /**
     * Inserts a Payload before another target Value in this HashList
     * @param target the target Value
     * @param value the new Value to be added to this HashList
     * @return true if the target value exists in this HashList, else false
     * @author Tony Vuolo (bdane)
     */
    public boolean insertBefore(Payload target, Payload value) {
        HashNode<Payload> hashNode = this.map.get(target);
        if(hashNode == null) {
            return false;
        }
        HashNode<Payload> newNode = new HashNode<>(value);
        newNode.prev = hashNode.prev;
        newNode.next = hashNode;
        if(hashNode.prev != null) {
            hashNode.prev.next = newNode;
        }
        hashNode.prev = newNode;
        this.map.put(value, newNode);
        return true;
    }

    /**
     * Inserts a Value after another target Value in this HashList
     * @param target the target Value
     * @param payload the new Value to be added to this HashList
     * @return true if the target payload exists in this HashList, else false
     * @author Tony Vuolo (bdane)
     */
    public boolean insertAfter(Payload target, Payload payload) {
        HashNode<Payload> hashNode = this.map.get(target);
        if(hashNode == null) {
            return false;
        } else if(hashNode.next == null) {
            add(payload);
            return true;
        } else {
            return insertBefore(hashNode.next.payload, payload);
        }
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