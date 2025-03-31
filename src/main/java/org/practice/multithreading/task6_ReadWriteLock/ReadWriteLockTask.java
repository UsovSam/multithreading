package org.practice.multithreading.task6_ReadWriteLock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTask {

    public static class Cache {

        ReadWriteLock lock = new ReentrantReadWriteLock();
        ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

        public String get(String key) {
            lock.readLock().lock();
            try {
                return cache.get(key);
            } finally {
                lock.readLock().unlock();
            }
        }

        public void put(String key, String value) {
            Lock writeLock = lock.writeLock();
            writeLock.lock();
            try {
                this.cache.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public void remove(String key, String value) {
            Lock writeLock = lock.writeLock();
            writeLock.lock();
            try {
                this.cache.remove(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public void reset() {
            Lock writeLock = lock.writeLock();
            writeLock.lock();
            try {
                this.cache.clear();
            } finally {
                writeLock.unlock();
            }
        }


    }


    public static void main(String[] args) {
        Cache cache = new Cache();

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
