package com.scarlatti.weakhashmapdemo;

import org.junit.Test;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Alessandro Scarlatti
 * @since Tuesday, 1/15/2019
 */
public class WeakHashMapTest {

    String key;

    @Test
    public void weakHashMapWorks() {
        Map<String, String> map = new WeakHashMap<>();
        map.put(new String("Maine"), "Augusta");

        Future future = Executors.newFixedThreadPool(1).submit(() -> {
            while (map.containsKey("Maine")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                System.out.println("Thread waiting");
//                System.gc();
            }
        });

        System.out.println("Main waiting");
        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void weakHashMapDoesNotRemoveEntry() {
        Map<String, String> map = new WeakHashMap<>();
        key = new String("Maine");
        map.put(key, "Augusta");

        Future future = Executors.newFixedThreadPool(1).submit(() -> {

            int i = 0;
            while (map.containsKey("Maine")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                System.out.println("Thread waiting");
                if (i == 4) {
                    key = null;
                }
                System.gc();
                i++;
            }
        });

        System.out.println("Main waiting");
        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
