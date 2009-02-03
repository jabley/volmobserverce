/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.cache;

import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupBuilder;
import com.volantis.cache.impl.InternalCache;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.cache.notification.RemovalListener;
import com.volantis.cache.stats.StatisticsSnapshot;
import com.volantis.cache.stats.StatisticsDelta;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.DefaultConfigurator;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.shared.io.IndentingWriter;
import com.volantis.shared.system.SystemClock;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.StringWriter;
import java.util.Random;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

public class CacheStressTestCase
        extends CacheTestAbstract {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(CacheStressTestCase.class);

    private CacheFactory factory;
    private static final int GROUP_SIZE_MULTIPLE = 4;

    protected void setUp() throws Exception {
        super.setUp();

        LogManager.getLoggerRepository().resetConfiguration();
        BasicConfigurator.configure();
        Logger.getRoot().setLevel(Level.DEBUG);
        LogManager.getLoggerRepository().setThreshold(Level.OFF);
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        DefaultConfigurator.shutdown();
    }

    public void testStress() throws Exception {

        final Group[] groups = new Group[21];

        factory = CacheFactory.getDefaultInstance();
        CacheBuilder builder = factory.createCacheBuilder();
        builder.setMaxCount(40);
        builder.setObjectProvider(new CacheableObjectProvider() {
            public ProviderResult retrieve(
                    SystemClock clock, Object key, CacheEntry entry) {
                Key k = (Key) key;

                // The first key is always uncacheable.
                boolean cacheable = k.getIndex() != 0;

                String value = "value for (" + k + ")";
                return new ProviderResult(value, k.getGroup(), cacheable, "extensions");
            }
        });
        final InternalCache cache = (InternalCache) builder.buildCache();

        // Create 7 groups, of size 4,8,12,16,20,24,28 with two subgroups
        // one of which is 50% and one is 75% of the size of the parent group.
        GroupBuilder groupBuilder = factory.createGroupBuilder();
        Group rootGroup = cache.getRootGroup();
        for (int i = 0, g = 0; i < 7; i += 1) {
            int maxCount = i * GROUP_SIZE_MULTIPLE + 4;
            groupBuilder.setMaxCount(maxCount);
            Group group = rootGroup.addGroup("group" + i, groupBuilder);
            groups[g++] = group;

            for (int s = 0; s < 2; s += 1) {
                groupBuilder.setMaxCount((int) ((0.5 + s / 4.0) * maxCount));
                groups[g++] = group.addGroup("group" + i + "/" + s,
                        groupBuilder);
            }
        }

        TestRemovalListener listener = new TestRemovalListener();
        rootGroup.addRemovalListener(listener);

        // Create 10 threads, each of which will perform one of the following
        // actions at random.
        // 1) Retrieve an item from the store
        Thread[] threads = new Thread[20];
        MyRunnable[] runnables = new MyRunnable[threads.length];
        for (int i = 0; i < threads.length; i++) {
            MyRunnable runnable = new MyRunnable(i, groups, cache);
            Thread thread = new Thread(runnable);
            threads[i] = thread;
            runnables[i] = runnable;
        }

        StatisticsSnapshot snapshotBefore = rootGroup.getStatisticsSnapshot();

        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            thread.start();
        }

        boolean failed = false;
        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            MyRunnable runnable = runnables[i];
            thread.join();

            if (runnable.failed()) {
                String errors = runnable.getErrors();
                if (errors != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(errors);
                    }
                }
                failed = true;
            }
        }
        if (failed) {
            fail("Stress test failed due to exceptions thrown by threads");
        }

        // Validate the internal structure of the cache.
        ensureCacheIntegrity(cache);

        StatisticsSnapshot snapshotAfter = rootGroup.getStatisticsSnapshot();
        StatisticsDelta delta = snapshotAfter.difference(snapshotBefore);
        System.out.println("Period:                 " + delta.getPeriod());
        System.out.println("Hit count:              " + delta.getHitCount());
        System.out.println("Missed / Added count:   " + delta.getMissedAddedCount());
        System.out.println("Removed count:          " + delta.getRemovedCount());
        System.out.println("Change count:           " +
                (delta.getMissedAddedCount() - delta.getRemovedCount()));
        System.out.println("Hit rate:               " + delta.getHitRate() + "%");

        System.out.println("Key count:              " + keys.size());

        cache.debugStructure(new IndentingWriter(System.out));

        // Make sure that the number of entries removed according to the
        // snapshot is equal to the number removed according to the listener.
        int notifiedRemovedCount = listener.getRemovedCount();
        System.out.println("Notified removed count: " + notifiedRemovedCount);
        assertEquals(delta.getRemovedCount(), notifiedRemovedCount);

        if (logger.isDebugEnabled()) {
            StringWriter writer = new StringWriter();
            IndentingWriter printer = new IndentingWriter(writer);
            cache.debugStructure(printer);
            logger.debug(writer.toString());
        }
    }

    private static class Key {

        private final String text;

        private final int index;

        private final Group group;

        public Key(String text, int key, Group group) {
            this.text = text;
            this.index = key;
            this.group = group;
        }

        public int hashCode() {
            return text.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }

            Key other = (Key) obj;

            return text.equals(other.text);
        }

        public int getIndex() {
            return index;
        }

        public Group getGroup() {
            return group;
        }

        public String toString() {
            return text;
        }
    }

    private static Set keys = Collections.synchronizedSet(new HashSet());

    private static class MyRunnable
            implements Runnable {

        private final int offset;
        private final Group[] groups;
        private final Cache cache;
        private final StringWriter errors;
        private boolean failed;

        public MyRunnable(int index, Group[] groups, Cache cache) {
            this.offset = index * 10;
            this.groups = groups;
            this.cache = cache;
            errors = new StringWriter();
        }

        public void run() {
            Random random = new Random();
            for (int i = 0; i < 5000; i += 1) {

                try {
                    int v = offset + i;
                    int g = v % groups.length;
                    Group group = groups[g];
                    int k = (v / groups.length) % 13;
                    String keyText = "group" + g + ".key" + k;
                    keys.add(keyText);
                    Key key = new Key(keyText, k, group);

                    int choice = random.nextInt(100);
                    if (choice < 90) {
                        // 90% chance that it will retrieve an item from the
                        // cache.

                        if (logger.isDebugEnabled()) {
                            logger.debug("About to read entry with key '" + key + "'");
                        }

                        Object value = cache.retrieve(key);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Read '" + value + "' with key '" +
                                                key + "'");
                        }

                    } else if (choice < 98) {
                        // 8% chance that it will remove an item.

                        if (logger.isDebugEnabled()) {
                            logger.debug("About to remove entry with key '" +
                                                key + "'");
                        }

                        cache.removeEntry(key);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Removed entry with key '" + key + "'");
                        }
                    } else {
                        // 2% chance that it will flush a group.

                        if (logger.isDebugEnabled()) {
                            logger.debug("About to flush group " + g);
                        }

                        // Determine whether to flush even or odd keys.
                        final int evenOrOdd = random.nextInt(2);

                        // Select those keys with even indeces.
                        group.flush(new CacheEntryFilter() {
                            public boolean select(CacheEntry entry) {
                                Key key = (Key) entry.getKey();
                                return (key.getIndex() % 2 == evenOrOdd);
                            }
                        });

                        if (logger.isDebugEnabled()) {
                            logger.debug("Flushed group " + g);
                        }
                    }

                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Caused exception");
                    }
                    e.printStackTrace(System.err);
                    failed = true;
                }

                if (random.nextDouble() > 0.5) {
                    try {
                        synchronized(this) {
                            wait(10);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                        failed = true;
                    }
                }
            }
        }

        public boolean failed() {
            return failed;
        }

        public String getErrors() {
            StringBuffer buffer = errors.getBuffer();
            String errors = buffer.toString();
            return errors.length() == 0 ? null : errors;
        }
    }

    private static class TestRemovalListener implements RemovalListener {

        private int removedCount;

        public void entryRemoved(CacheEntry entry) {
            synchronized (this) {
                removedCount += 1;
            }
        }

        public int getRemovedCount() {
            return removedCount;
        }
    }
}
