package concurrent;

import lockfree.SkipQueue;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.fail;

class PriorityQueueTest {

    static final int LOG_RANGE = 5;
    static final int TEST_SIZE = 128;
    static final int RANGE = 32;
    private final static int THREADS = 8;
    private final static int PER_THREAD = TEST_SIZE / THREADS;
    static Random random = new Random();
    PriorityQueue<Integer> instance;
    Thread[] thread = new Thread[THREADS];
    Map<Integer,Integer> log = new ConcurrentHashMap<Integer,Integer>();


    @Test
    public void testAdd() {
        Map<Integer,Integer> log = new HashMap<Integer,Integer>();
        int key, value;

        PriorityQueue<Integer> instance = new PriorityQueue<Integer>();
        int range = (1 << LOG_RANGE);
        for (int i = 0; i < TEST_SIZE; i++) {
            key = random.nextInt(range);
            value = random.nextInt();
            log.put(value, key);
            instance.add(value, key);
        }

        // validate
        int oldKey = -1;
        for (int i = 0; i < TEST_SIZE; i++) {
            Integer v = instance.removeMin();
            if (v != null) {
                value = v;
                key = log.get(value);
                if (oldKey > key) {
                    fail("non-ascending keys!");
                }
            }
        }
    }

    @Test
    public void testParallelAdd()  throws Exception {
        int key, value;
        instance = new PriorityQueue<Integer>();

        for (int i = 0; i < THREADS; i++) {
            thread[i] = new AddThread(i * PER_THREAD);
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].join();
        }
        int oldKey = -1;
        for (int i = 0; i < TEST_SIZE; i++) {
            value = instance.removeMin();
            key = log.get(value);
            if (oldKey > key) {
                fail("non-ascending keys!");
            }
            oldKey = key;
        }
    }

    @Test
    public void testParallelBoth()  throws Exception {
        instance = new PriorityQueue<Integer>();

        for (int i = 0; i < THREADS; i++) {
            thread[i] = new AddThread(i * PER_THREAD);
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].join();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i] = new RemoveMinThread();
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].join();
        }
    }

    class AddThread extends Thread {
        int base;
        AddThread(int i) {
            base = i;
        }
        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                int x = base + i;
                log.put(x, x);
                instance.add(x, x);
            }
        }
    }
    class RemoveMinThread extends Thread {
        int last;
        RemoveMinThread() {
            last = Integer.MIN_VALUE;
        }
        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                int x = instance.removeMin();
                if (x < last) {
                    fail("non-ascending priorities: " + last + ", " + x);
                }
                last = x;
            }
        }
    }

}