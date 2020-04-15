package com.yuan.cn.blog.commons;

import java.util.ArrayList;
import java.util.List;

public class CachingMap<K extends Comparable<K>, V> {
    private Node dummyHead = new Node();
    private int size = 0;

    CachingMap(){}
    private static long timeout = 0L;

    public void insert(K key, V value, long timeout){
        if (!contains(key))
            insert(size, key, value, timeout);
    }

    private boolean contains(K key){
        Node cur = dummyHead;
        while (cur.next != null){
            cur = cur.next;
            if (cur.key.compareTo(key) == 0)
            {
                return true;
            }
        }
        return false;
    }
    private void insert(int i, K key, V value, long timeout){

        Node cur = dummyHead;
        for (int c = 0; c < i; c++)
        {
            cur = cur.next;
        }

        cur.next = new Node(System.currentTimeMillis(), timeout, null, cur, key, value);
        size++;
    }

    public void resetTimeout(K key){
        Node cur = dummyHead;
        while (cur.next != null) {
            cur = cur.next;
            if (cur.key.compareTo(key) == 0) {
                cur.lasting = System.currentTimeMillis();
                return;
            }
        }
    }

    public V getFirst(){
        return get(0);
    }

    private void remove(Node node){
        size--;
        if (node.next != null){
            node.next.pre = node.pre;
            node.pre.next = node.next;
            return;
        }
        node.pre.next = null;
    }
    public void remove(int i){
        Node cur = dummyHead;
        for (int i1 = 0; i1 < i; i1++)
            cur = cur.next;
        remove(cur);
    }

    public V remove(K key){
        Node cur = dummyHead;
        while (cur.next != null){
            cur = cur.next;
            if (cur.key.compareTo(key) == 0)
            {
                remove(cur);
                return cur.data;
            }
        }
        return null;
    }

    public V get(K key){
        Node cur = dummyHead;
        while (cur.next != null){
            cur = cur.next;
            if (cur.key.compareTo(key) == 0)
            {
                if ((System.currentTimeMillis() - cur.lasting) > cur.timeout){
                    remove(cur);
                    return null;
                }
                return cur.data;
            }
        }
        return null;
    }
    private V get(int i){
        Node cur = dummyHead;
        for (int c = 0; c < i; c++)
            cur = cur.next;
        Node node = cur.next;

        if ((System.currentTimeMillis() - node.lasting) > node.timeout){
            remove(cur);
            return null;
        }
        return cur.next.data;
    }

    public List<V> values(){
        List<V> list = new ArrayList<>();
        Node cur = dummyHead;
        while (cur.next != null){
            cur = cur.next;
            list.add(cur.data);
        }
        return list;
    }

    public void printf(){
        Node cur = dummyHead;
        while (cur.next != null){
            cur = cur.next;
            System.out.println(cur);
        }
    }
    public int getSize(){
        return size;
    }

    private class Node{
        long lasting;
        long timeout;
        boolean isTimeout = false;
        Node next;
        Node pre;
        V data;
        K key;

        Node(long lasting, long timeout, Node next, Node pre,K key, V data){
            this.lasting = lasting;
            this.timeout = timeout;
            this.next = next;
            this.pre = pre;
            this.data = data;
            this.key = key;
        }

        Node(Node next, Node pre, long timeout,K key, V data){
            this.next = next;
            this.pre = pre;
            this.lasting = System.currentTimeMillis();
            this.timeout = timeout;
            this.data = data;
            this.key = key;
        }
        Node(){}

        @Override
        public String toString() {
            return "Node{" +
                    "lasting=" + lasting +
                    ", timeout=" + timeout +
                    ", isTimeout=" + isTimeout +
                    ", data=" + data +
                    ", key=" + key +
                    '}';
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CachingMap<String, Boolean> map = new CachingMap<>();
        map.insert("hello", true, 1000);
        map.insert("hello", true, 1000);
        map.printf();
        System.out.println(map.get("hello"));
        Thread.sleep(1000);
        System.out.println(map.get("hello"));
    }

    private static int sum(int n){
        // 1 1 2 3 5 8 13
        return n > 2 ? sum(n - 1) + sum(n - 2) : n <= 0 ? 0 : 1;
    }
}
