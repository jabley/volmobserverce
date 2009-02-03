/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Â  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.Â  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache.body;

import com.volantis.cache.AsyncResult;
import com.volantis.cache.Cache;
import com.volantis.cache.CacheEntry;
import com.volantis.cache.group.Group;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.DependencyContextMock;
import com.volantis.shared.dependency.FixedTTLDependency;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContextMock;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.cache.CacheKey;
import com.volantis.xml.pipeline.sax.cache.CacheProcessConfiguration;
import com.volantis.xml.pipeline.sax.cache.PipelineCacheState;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;

/**
 * Testcase for CacheBodyOperationProcess.
 *
 * NOTE: this does not extend AbstractOperationProcessTestAbstract or
 * implement XMLProcessTestCase because that test hierarchy does not work
 * properly and even a default implementation that implements
 * createTestableProcess() will break.
 */
public class CacheBodyOperationProcessTestCase extends TestCaseAbstract {
    private Dependency[] dependencies;
    private Cache cache;
    private CacheKey cacheKey;
    private CacheBodyOperationProcess process;
    private XMLPipelineContextMock pipelineContextMock;

    public void setUp() throws Exception {
        super.setUp();

        final DependencyContextMock dependencyContextMock =
            new DependencyContextMock("dependencyContextMock", expectations);
        pipelineContextMock =
            new XMLPipelineContextMock("pipelineContextMock", expectations);
        final XMLPipeline pipeline =
            new XMLPipelineProcessImpl(pipelineContextMock);

        final CacheProcessConfiguration cacheProcessConfig =
            new CacheProcessConfiguration();
        cacheProcessConfig.createCache("cacheName", "100", "50");

        // initialize the process
        process = new CacheBodyOperationProcess();
        cache = cacheProcessConfig.getCache("cacheName");
        process.setCache(cache);
        process.setCacheName("cacheName");
        cacheKey = new CacheKey();
        cacheKey.addKey("cacheKey");
        process.setCacheKey(cacheKey);
        process.setPipeline(pipeline);

        dependencies = new Dependency[1];
        pipelineContextMock.expects.getDependencyContext().returns(
            dependencyContextMock).any();
        final MockFactory mockFactory = MockFactory.getDefaultInstance();
        dependencyContextMock.fuzzy.addDependency(
            mockFactory.expectsAny()).does(new MethodAction() {
                public Object perform(MethodActionEvent event) throws Throwable {
                    assertNull(dependencies[0]);
                    dependencies[0] = (Dependency) event.getArguments()[0];
                    return null;
                }
            }).optional();
    }

    public void testCacheEntriesTimeOutCorrectly() throws Exception {
        final String key = "key";

        CacheKey cacheKey = new CacheKey();
        cacheKey.addKey(key);

        final CacheProcessConfiguration cpc = new CacheProcessConfiguration();
        final String cacheName = "cache";
        cpc.createCache(cacheName, "10", "0");

        final Cache cache = cpc.getCache("cache");

        AsyncResult async = cache.asyncQuery(cacheKey, Period.inSeconds(30));

        assertFalse(async.isReady());

        CacheEntry entry = async.getEntry();
        assertNotNull(entry);

        final SystemClock clock = SystemClock.getDefaultInstance();
        ProviderResult result = new ProviderResult(new Integer(1),
            cache.getRootGroup(), true,
            new PipelineCacheState(
                clock.getCurrentTime().addPeriod(Period.inSeconds(2))));
        async.update(result);

        CacheableObjectProvider cacheableObjectProvider =
            new ExpiredObjectsRemainExpiredCacheableObjectProvider(cache.getRootGroup());
        assertNotNull("Integer object is retrievable via the cache",
                cache.retrieve(cacheKey, cacheableObjectProvider));

        Thread.sleep(3000);

        // Slightly messy.
        // Done this way so that we don't get a NullPointerException, and have
        // to do the code below. Both ways are a little messy in that they know
        // a bit too much about the internals.
        //
        // try {
        //      cache.retrieve(cacheKey);
        //      fail("Should throw a NullPointerException when the cache "
        //          + "doesn't contain the object and falls back to the null "
        //          + "provider");
        // } catch (NullPointerException expected) {
        //         // expected
        // }
        assertNull("object has expired from cache and the provider doesn't "
                + "put it back in",
                cache.retrieve(cacheKey, cacheableObjectProvider));

    }

    /**
     * If the cached recording is up to date then initializeProcess should
     * return PLAYBACK_AND_SUPPRESS, update the recording field and store the
     * dependency in the dependency context.
     */
    public void testInitializeProcessStateCacheReady() throws Exception {
        // initialize the cache
        final PipelineRecording recording = new PipelineRecording() {
            public PipelinePlayer createPlayer() {
                return null;
            }

            public boolean isComplex() {
                return false;
            }
        };
        final Dependency dependency = new FixedTTLDependency(
            SystemClock.getDefaultInstance(), Period.inSeconds(100));
        cache.retrieve(cacheKey, new TestCacheableObjectProvider(
            cache.getRootGroup(), true,
            new CacheBodyOperationProcess.RecordingWithDependency(
                recording, dependency)));

        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.PLAYBACK_AND_SUPPRESS, state);
        assertSame(dependencies[0], dependency);
        assertSame(recording, process.getRecording());
    }

    /**
     * If the cache group cannot be found, NO_CACHE_FORWARD_ONLY is returned,
     * no dependency  is added to the dependency context and the recording
     * remains null.
     */
    public void testInitializeProcessStateGroupNotFound() throws Exception {
        pipelineContextMock.expects.getCurrentLocator().returns(null);
        // we don't have a cache for this name
        process.setCacheName("invalid name");
        process.setCache(null);
        // initialize the cache
        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.NO_CACHE_FORWARD_ONLY, state);
        assertNull(dependencies[0]);
        assertNull(process.getRecording());
    }

    /**
     * If the recording was uncacheable, NO_CACHE_FORWARD_ONLY is returned,
     * no dependency  is added to the dependency context and the recording
     * remains null.
     */
    public void testInitializeProcessStateUncacheable() throws Exception {
        // initialize the cache
        final PipelineRecording recording = new PipelineRecording() {
            public PipelinePlayer createPlayer() {
                return null;
            }

            public boolean isComplex() {
                return false;
            }
        };
        final Dependency dependency = new FixedTTLDependency(
            SystemClock.getDefaultInstance(), Period.inSeconds(0));
        cache.retrieve(cacheKey, new TestCacheableObjectProvider(
            cache.getRootGroup(), false,
            new CacheBodyOperationProcess.RecordingWithDependency(
                recording, dependency)));

        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.NO_CACHE_FORWARD_ONLY, state);
        assertNull(dependencies[0]);
        assertNull(process.getRecording());
    }

    /**
     * If there is no entry in the cache for this key then RECORD_AND_FORWARD is
     * returned, no dependency is saved in the dependency context and no
     * recording is created (yet).
     */
    public void testInitializeProcessStateEmptyCache() throws Exception {
        // initialize the cache
        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.RECORD_AND_FORWARD, state);
        assertNull(dependencies[0]);
        assertNull(process.getRecording());
    }

    /**
     * If the entry in the cache for this key is expired and the expiry mode is
     * fixed-age then RECORD_AND_FORWARD is returned, no dependency is saved in
     * the dependency context and no recording is created (yet).
     */
    public void testInitializeProcessStateExpiredFixedAge() throws Exception {
        // initialize the cache
        final PipelineRecording recording = new PipelineRecording() {
            public PipelinePlayer createPlayer() {
                return null;
            }

            public boolean isComplex() {
                return false;
            }
        };
        final Dependency dependency = new FixedTTLDependency(
            SystemClock.getDefaultInstance(), Period.inSeconds(0));
        cache.retrieve(cacheKey, new TestCacheableObjectProvider(
            cache.getRootGroup(), true,
            new CacheBodyOperationProcess.RecordingWithDependency(
                recording, dependency)));

        Thread.sleep(20);

        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.RECORD_AND_FORWARD, state);
        assertNull(dependencies[0]);
        assertNull(process.getRecording());
    }

    /**
     * If the entry in the cache for this key is expired and the expiry mode is
     * auto, but the dependency is not fresh and cannot be revalidated then
     * RECORD_AND_FORWARD is returned, no dependency is saved in
     * the dependency context and no recording is created (yet).
     */
    public void testInitializeProcessStateExpiredAuto() throws Exception {
        process.setFixedExpiryMode(false);
        // initialize the cache
        final PipelineRecording recording = new PipelineRecording() {
            public PipelinePlayer createPlayer() {
                return null;
            }

            public boolean isComplex() {
                return false;
            }
        };
        final Dependency dependency = new FixedTTLDependency(
            SystemClock.getDefaultInstance(), Period.inSeconds(0));
        cache.retrieve(cacheKey, new TestCacheableObjectProvider(
            cache.getRootGroup(), true,
            new CacheBodyOperationProcess.RecordingWithDependency(
                recording, dependency)));

        Thread.sleep(20);

        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.RECORD_AND_FORWARD, state);
        assertNull(dependencies[0]);
        assertNull(process.getRecording());
    }

    /**
     * If the cached recording is expired, but the dependency is fresh then
     * initializeProcess should return PLAYBACK_AND_SUPPRESS, update the
     * recording field and store the dependency in the dependency context.
     */
    public void testInitializeProcessStateExpiredButFresh() throws Exception {
        process.setFixedExpiryMode(false);
        // initialize the cache
        final PipelineRecording recording = new PipelineRecording() {
            public PipelinePlayer createPlayer() {
                return null;
            }

            public boolean isComplex() {
                return false;
            }
        };
        final Dependency dependency = new FixedTTLDependency(
            SystemClock.getDefaultInstance(), Period.inSeconds(0)) {
            public Freshness freshness(final DependencyContext context) {
                return Freshness.FRESH;
            }
        };
        cache.retrieve(cacheKey, new TestCacheableObjectProvider(
            cache.getRootGroup(), true,
            new CacheBodyOperationProcess.RecordingWithDependency(
                recording, dependency)));

        Thread.sleep(20);

        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.PLAYBACK_AND_SUPPRESS, state);
        assertSame(dependencies[0], dependency);
        assertSame(recording, process.getRecording());
    }

    /**
     * If the cached recording is expired, but the dependency can be revalidated
     * to fresh state then initializeProcess should return PLAYBACK_AND_SUPPRESS,
     * update the recording field and store the dependency in the dependency
     * context.
     */
    public void testInitializeProcessStateExpiredButRevalidateable()
            throws Exception {
        process.setFixedExpiryMode(false);
        // initialize the cache
        final PipelineRecording recording = new PipelineRecording() {
            public PipelinePlayer createPlayer() {
                return null;
            }

            public boolean isComplex() {
                return false;
            }
        };
        final Dependency dependency = new FixedTTLDependency(
            SystemClock.getDefaultInstance(), Period.inSeconds(0)) {
            public Freshness freshness(final DependencyContext context) {
                return Freshness.REVALIDATE;
            }
            public Freshness revalidate(final DependencyContext context) {
                return Freshness.FRESH;
            }
        };
        cache.retrieve(cacheKey, new TestCacheableObjectProvider(
            cache.getRootGroup(), true,
            new CacheBodyOperationProcess.RecordingWithDependency(
                recording, dependency)));

        Thread.sleep(20);

        // test
        final CacheBodyOperationProcessState state =
            process.initializeProcessState();
        assertSame(CacheBodyOperationProcessState.PLAYBACK_AND_SUPPRESS, state);
        assertSame(dependencies[0], dependency);
        assertSame(recording, process.getRecording());
    }

    /**
     * Factory method for creating an XMLPipelineConfiguration. This default
     * impelementation simple returns the configuration that
     * {@link IntegrationTestHelper#getPipelineConfiguration} returns
     *
     * @return An XMLPipelineConfiguration instance
     */
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        XMLPipelineConfiguration configuration = null;
        try {
            configuration =
                    new IntegrationTestHelper().getPipelineConfiguration();
        } catch (Exception e) {
            fail("Unable to create XMLPipelineConfiguration for test");
        }
        return configuration;
    }

    class ExpiredObjectsRemainExpiredCacheableObjectProvider implements CacheableObjectProvider {

        private Group group;

        public ExpiredObjectsRemainExpiredCacheableObjectProvider(Group group) {
            this.group = group;
        }

        public ProviderResult retrieve(SystemClock clock, Object key, CacheEntry entry) {
            return new ProviderResult(null, group, false, null);
        }
    }

    /**
     * CacheableObjectProvider implementation to add test elements into the
     * cache.
     */
    private static class TestCacheableObjectProvider
            implements CacheableObjectProvider {
        private final Group group;
        private final boolean cacheable;
        private final CacheBodyOperationProcess.RecordingWithDependency value;

        public TestCacheableObjectProvider(
                final Group group, final boolean cacheable,
                final CacheBodyOperationProcess.RecordingWithDependency value) {
            this.group = group;
            this.cacheable = cacheable;
            this.value = value;
        }

        public ProviderResult retrieve(final SystemClock clock,
                                       final Object key,
                                       final CacheEntry entry) {
            final PipelineCacheState state = new PipelineCacheState(
                SystemClock.getDefaultInstance().getCurrentTime().addPeriod(
                    value.getDependency().getTimeToLive()));
            return new ProviderResult(value, group, cacheable, state);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6828/1	matthew	VBM:2005012601 Allow new Cache mechanism to work with MCS (not optimally though)

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 07-Aug-03	316/1	allan	VBM:2003080501 Redesigned CacheControl and added timeToLive

 ===========================================================================
*/
