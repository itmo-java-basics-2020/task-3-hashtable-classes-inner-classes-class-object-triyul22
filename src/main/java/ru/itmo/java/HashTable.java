package ru.itmo.java;

public class HashTable {
    private Entry[] element;
    private int size;
    private int realSize;
    private int capacity;
    private int threshold;
    private double loadFactor;

    HashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.loadFactor = 0.5;
        this.size = 0;
        this.realSize = 0;
        element = new Entry[capacity];
    }

    HashTable(int initialCapacity, double loadFactor) {
        this(initialCapacity);
        this.loadFactor = loadFactor;
        this.threshold = (int) (loadFactor * capacity);
    }

    int HashFunc(Object key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        capacity *= 2;
        this.threshold = (int) (loadFactor * capacity);
        size = realSize = 0;
        Entry[] old = element;
        element = new Entry[capacity];
        for (Entry e : old) {
            if (e != null && !e.deleted) {
                put(e.key, e.value);
            }
        }
    }

    Object put(Object key, Object value) {
        var hashCode = HashFunc(key);
        var ent = new Entry(key, value);
        while (true) {
            if (element[hashCode] == null || element[hashCode].key.equals(key) && element[hashCode].deleted) {
                if (element[hashCode] != null) {
                    element[hashCode].deleted = false;
                }
                element[hashCode] = ent;
                this.size++;
                this.realSize++;
                if (realSize > this.threshold) {
                    resize();
                }
                return null;
            }
            if (element[hashCode].key.equals(key)) {
                Object old = element[hashCode].value;
                element[hashCode].value = value;
                return old;
            }
            hashCode = (hashCode + 1) % capacity;
        }
    }

    Object get(Object key) {
        int hash = HashFunc(key);
        while (true) {
            if (element[hash] == null)
                return null;
            if (element[hash].key.equals(key) && !element[hash].deleted)
                return element[hash].value;
            hash = (hash + 1) % capacity;
        }
    }

    Object remove(Object key) {
        int hash = HashFunc(key);
        while (true) {
            if (element[hash] == null) {
                return null;
            }

            if (element[hash].key.equals(key) && !element[hash].deleted) {
                element[hash].deleted = true;
                this.size--;
                return element[hash].value;
            }
            hash = (hash + 1) % capacity;
        }
    }

    int size() {
        return size;
    }

    private static class Entry {
        private Object key;
        private Object value;
        private boolean deleted = false;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
