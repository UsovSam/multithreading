package org.practice.multithreading.task6_ReadWriteLock;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMapTask {

    public static class ConcurrentCache {

        ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();


        public String get(String key) {
            return cache.get(key);
        }

        public void put(String key, String value) {
            cache.putIfAbsent(key, value);
        }

        public void remove(String key) {
            cache.remove(key);
        }

        public void reset() {
            this.cache.clear();
        }

        public boolean containsKey(String key) {
            return cache.containsKey(key);
        }

    }


    public static void main(String[] args) {
        ConcurrentCache cache = new ConcurrentCache();

        // Параллельное чтение и запись в кэш
        new Thread(() -> {
            cache.put("key1", "value1");
            System.out.println("Поток 1 записал key1");
        }).start();

        new Thread(() -> {
            System.out.println("Поток 2 прочитал: " + cache.get("key1"));
        }).start();

        new Thread(() -> {
            cache.put("key2", "value2");
            System.out.println("Поток 3 записал key2");
        }).start();

        new Thread(() -> {
            System.out.println("Поток 4 прочитал: " + cache.get("key2"));
        }).start();

    }

}
