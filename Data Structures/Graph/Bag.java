/**
 * Created by LIWENXIN on 4/9/16.
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/** http://algs4.cs.princeton.edu/41graph/Bag.java.html
 *
 *  The Bag class represents a bag of generic items.
 *  It supports insertion and iterating over the items in an arbitrary order. (对于图的adj来说 顺序不重要)
 *
 *  This implementation uses a single-linked list with a static nested clase Node.
 *
 *  Add, size, and isEmpty take constant time while iteration takes linear time.
 */

public class Bag<Item> implements Iterable<Item> {
    private Node<Item> first;
    private int N;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

//    Non-static 的做法应该也可以
//
//    private class Node<Item> {
//        private Item item;
//        private Node<item> next;
//
//        Node(Item item, Node next) {
//            this.item = item;
//            this.next = next;
//        }
//    }

    public Bag() {
        first = null;
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void add(Item item) {
        Node<Item> oldNode = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldNode;
        N++;
    }

    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
