package org.practice.multithreading.task6_ReadWriteLock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTask {

    public static class Cache {

        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        Map<String, String> cache = new HashMap<>();


        public String get(String key) throws InterruptedException {
            lock.readLock().lockInterruptibly();
            try {
                return cache.get(key);
            } finally {
                lock.readLock().unlock();
            }
        }

        public void put(String key, String value) throws InterruptedException {
            Lock writeLock = lock.writeLock();
            writeLock.lockInterruptibly();
            try {
                this.cache.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public void remove(String key, String value) throws InterruptedException {
            Lock writeLock = lock.writeLock();
            writeLock.lockInterruptibly();
            try {
                this.cache.remove(key);
            } finally {
                writeLock.unlock();
            }
        }

        public void reset() throws InterruptedException {
            Lock writeLock = lock.writeLock();
            writeLock.lockInterruptibly();
            try {
                this.cache.clear();
            } finally {
                writeLock.unlock();
            }
        }

        public boolean containsKey(String key) throws InterruptedException {
            lock.readLock().lockInterruptibly();
            try {
                return cache.containsKey(key);
            } finally {
                lock.readLock().unlock();
            }
        }

    }


    public static void main(String[] args) {
        Cache cache = new Cache();

        // Параллельное чтение и запись в кэш
        new Thread(() -> {
            try {
                cache.put("key1", "value1");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Поток 1 записал key1");
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Поток 2 прочитал: " + cache.get("key1"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        new Thread(() -> {
            try {
                cache.put("key2", "value2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Поток 3 записал key2");
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Поток 4 прочитал: " + cache.get("key2"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

    }

}
