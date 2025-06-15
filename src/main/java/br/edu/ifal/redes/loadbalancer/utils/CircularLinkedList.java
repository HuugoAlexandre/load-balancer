package br.edu.ifal.redes.loadbalancer.utils;

import java.util.LinkedList;

public class CircularLinkedList<T> {

    private final LinkedList<T> list = new LinkedList<>();

    public void add(T item) {
        list.add(item);
    }

    public boolean contains(T item) {
        return list.contains(item);
    }

    public void remove(T item) {
        list.remove(item);
    }

    public int size() {
        return list.size();
    }

    public T next() {
        if (list.isEmpty()) {
            return null;
        }

        final T item = list.removeFirst();
        list.addLast(item);

        return item;
    }

}
