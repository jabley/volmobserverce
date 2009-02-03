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
import com.volantis.cache.provider.ProviderResult;
import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.shared.dependency.Tracking;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.dependency.FixedTTLDependency;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.cache.CacheControl;
import com.volantis.xml.pipeline.sax.cache.CacheKey;
import com.volantis.xml.pipeline.sax.cache.CacheNotFoundException;
import com.volantis.xml.pipeline.sax.cache.PipelineCacheState;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import com.volantis.xml.pipeline.sax.recorder.PipelinePlayer;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecording;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This operation process caches the sax events passed through its
 * ContentHandler methods.  The cache is then used on subsequent requests
 * for the same cache.
 */
public class CacheBodyOperationProcess extends AbstractOperationProcess {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CacheBodyOperationProcess.class);

    /**
     * Default wait time to be used when maxWaitTime not set
     */
    public static final Period DEFAULT_MAX_WAIT_TIME =
        Period.inMilliSeconds(300000);

    /**
     * The maximum time to wait before aborting a wait for a cache entry to be
     * populated.
     */
    private Period maxWaitTime = DEFAULT_MAX_WAIT_TIME;
    
    /**
     * The {@link PipelineRecorder} that we will use to store and
     * playback SAX events.
     */
    private PipelineRecorder recorder;

    /**
     * The name of the cache that we are using.
     */
    private String cacheName;

    /**
     * The key into the cache which we will use to store/retrieve a
     * {@link PipelineRecording}
     */
    private CacheKey cacheKey;

    /**
     * This {@link CacheControl} for the cache.
     */
    private CacheControl cacheControl;

    /**
     * The {@link ContentHandler} that we are currently delegating to.  If we
     * are recording this will be a handler that forwards events to both a
     * SAXEventRecorder and the next process.
     */
    private ContentHandler currentHandler;

    /**
     * This is used to determine whether this instance of
     * CacheBodyOperationProcess should operate to...
     * <ul>
     * <li> forward SAX events and record them to the cache.
     * <li> playback SAX events from the cache and suppress incoming events.
     * <li> forward all SAX events (because there isn't a cache)
     * </ul>
     */
    private CacheBodyOperationProcessState state =
            CacheBodyOperationProcessState.UNINITIALIZED;

    /**
     * The recording that has been taken.
     */
    private PipelineRecording recording;


    /**
     * The cache instance used by this process.
     */
    private Cache cache;

    /**
     * Async result of the cache query
     */
    private AsyncResult async;

    /**
     * True if the operation works in "fixed" expiry time mode.
     */
    private boolean fixedExpiryMode;

    /**
     *
     */
    public CacheBodyOperationProcess() {
    }

    /**
     * Tracks the fatal errors that may occure processing the pipeline.
     *
     * @param exception the exception that represents the error being reported
     * @throws SAXException thrown if the error being passed in cannot be
     * handled.
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        updateCache();
        super.fatalError(exception);
    }

    /**
     * Tracks the errors that may occure processing the pipeline.
     *
     * @param exception the exception that represents the error being reported
     * @throws SAXException thrown if the error being passed in cannot be
     * handled.
     */
    public void error(SAXParseException exception) throws SAXException {
        updateCache();
        super.error(exception);
    }

    /**
     * If the error occures, then we do not want to leave cache entry
     * in half life state. We simply remove the cache entry.
     */
    private void updateCache() {
        if (cacheKey != null && cache != null) {
            cache.removeEntry(cacheKey);
        }
    }

    /**
     * @param cache
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * Get the PipelineRecorder for this cache process
     *
     * @return The PipelineRecorder for this process
     */
    protected PipelineRecorder getPipelineRecorder() {
        return recorder;
    }

    /**
     * Set the name of the cache that we are using.
     *
     * @param name The name of the cache that we are using.
     */
    public void setCacheName(String name) {
        cacheName = name;
    }

    /**
     * Set the value of the {@link CacheKey} that will be used to access the
     * cache.
     *
     * @param key The {@link CacheKey} that will be used to access the cache.
     */
    public void setCacheKey(CacheKey key) {
        cacheKey = key;
    }

    /**
     * Set the {@link CacheControl}.
     *
     * @param cacheControl The {@link CacheControl}.
     */
    public void setCacheControl(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
    }

    /**
     * Get the {@link CacheControl} for this CacheBodyOperationProcess.
     *
     * @return cacheControl.
     */
    public CacheControl getCacheControl() {
        return cacheControl; 
    } 
       
    /**
     * @param maxWaitTime
     */
    public void setMaxWaitTime(Period maxWaitTime) {
        if (maxWaitTime == null) {
            this.maxWaitTime = DEFAULT_MAX_WAIT_TIME;
        } else {
            this.maxWaitTime = maxWaitTime;
        }
    }    

    /**
     * This method is used to set the state of this process.
     *
     * If there is no cache then the state will be:
     * {@link CacheBodyOperationProcessState#NO_CACHE_FORWARD_ONLY}
     *
     * If there is a cache but no entry for the given key the state will be:
     * {@link CacheBodyOperationProcessState#RECORD_AND_FORWARD}
     *
     * if there is a cache and an entry for the give key the state will be:
     * {@link CacheBodyOperationProcessState#PLAYBACK_AND_SUPPRESS}
     *
     * In the third case the current thread may be put to sleep if the entry
     * is incomplete until a notification occurs that it has been completed.
     *
     * @return One of
     *         {@link CacheBodyOperationProcessState#NO_CACHE_FORWARD_ONLY},
     *         {@link CacheBodyOperationProcessState#RECORD_AND_FORWARD},
     *         {@link CacheBodyOperationProcessState#PLAYBACK_AND_SUPPRESS}
     * @throws SAXException if a problem occured in determining the state.
     */
    public CacheBodyOperationProcessState initializeProcessState()
            throws SAXException {

        final Group group = getCacheGroup();
        if (group != null) {
            this.recording = null;

            async = cache.asyncQuery(cacheKey, maxWaitTime);

            // see the AsyncResult documentation.
            if (async.isReady()) {

                // We have some content in the cache that can be retrieved via
                // async.getValue(). We should return the cached content.

                // Get the recording from the cache.
                final RecordingWithDependency value =
                    (RecordingWithDependency) async.getValue();
                recording = value.getRecording();
                state = CacheBodyOperationProcessState.PLAYBACK_AND_SUPPRESS;

                // add the dependency to the dependency context
                final DependencyContext dependencyContext =
                    getPipelineContext().getDependencyContext();
                dependencyContext.addDependency(value.getDependency());
            } else {

                // We should check to see if this thread is responsible for
                // updating the cached result.
                final CacheEntry entry = async.getEntry();
                if (entry == null) {

                    // Previous result wasn't cached, this thread _isn't_
                    // responsible for updating the cached result, so just
                    // forward the events as is.
                    state = CacheBodyOperationProcessState.NO_CACHE_FORWARD_ONLY;
                } else {
                    final RecordingWithDependency value =
                        (RecordingWithDependency) async.getValue();
                    if (fixedExpiryMode || value == null) {
                        // fixed-age expiry mode or first retrieve for the key

                        // This thread _is_ responsible for updating the cached
                        // result, so record it along with forwarding the events
                        // to any downstream clients.
                        this.state =
                            CacheBodyOperationProcessState.RECORD_AND_FORWARD;
                    } else {
                        // auto mode
                        // check if the dependency is fresh or it can be
                        // revalidated
                        final DependencyContext dependencyContext =
                            getPipelineContext().getDependencyContext();
                        final Dependency dependency = value.getDependency();
                        Freshness freshness =
                            dependency.freshness(dependencyContext);
                        if (freshness == Freshness.REVALIDATE) {
                            freshness =
                                dependency.revalidate(dependencyContext);
                        }

                        // if the dependency is fresh, cache can play the
                        // content back
                        if (freshness == Freshness.FRESH) {
                            // Get the recording from the cache.
                            recording = value.getRecording();
                            this.state =
                                CacheBodyOperationProcessState.PLAYBACK_AND_SUPPRESS;

                            final SystemClock clock =
                                SystemClock.getDefaultInstance();
                            final Time currentTime = clock.getCurrentTime();
                            final PipelineCacheState pcs =
                                new PipelineCacheState(currentTime.addPeriod(
                                    dependency.getTimeToLive()));
                            dependencyContext.addDependency(dependency);

                            // update the cache with the old value so other
                            // threads don't need to wait the timeout period
                            ProviderResult result = new ProviderResult(
                                value, group, true, pcs);
                            async.update(result);
                        } else {
                            // This thread _is_ responsible for updating the
                            // cached result, so record it along with forwarding
                            // the events to any downstream clients.
                            this.state =
                                CacheBodyOperationProcessState.RECORD_AND_FORWARD;
                        }
                    }
                }
            }
        } else {
            // Send a warning down the pipeline that the cache could not be
            // be found.
            warning(new CacheNotFoundException("The requested cache " +
                    cacheName + " could not be found.",
                    this.getPipelineContext().getCurrentLocator(), cacheName));
            state = CacheBodyOperationProcessState.NO_CACHE_FORWARD_ONLY;
        }

        return state;
    }

    /**
     * Return the cache group with the specified name or null if no such group
     * exists in the cache.
     *
     * @return the cache group or null
     */
    private Group getCacheGroup() {
        Group group = null;
        if (cache != null) {
            group = cache.getRootGroup();
        }
        return group;
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("Cache entry in " +
                    cacheName +
                    " with key " +
                    cacheKey +
                    " processing state is " +
                    state);
        }

        if (state == CacheBodyOperationProcessState.RECORD_AND_FORWARD) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cache entry in " +
                        cacheName +
                        " with key " +
                        cacheKey +
                        " recording started");
            }

            // This thread is responsible for caching the content and updating
            // the AsyncResult.
            final XMLPipelineContext pipelineContext = getPipelineContext();
            pipelineContext.getDependencyContext().pushDependencyTracker(
                fixedExpiryMode? Tracking.DISABLED: Tracking.ENABLED);

            XMLPipelineFactory factory = pipelineContext.getPipelineFactory();
            recorder = factory.createPipelineRecorder();
            boolean startedRecorder = false;
            try {
                recorder.startRecording(getPipeline());

                // Note that we don't forward the events directly in case one
                // or more event would cause flow control to be activated (which
                // would compromise the cache storage)
                setCurrentHandler(recorder.getRecordingHandler());

                // At this point the recorder has been successfully started and
                // we will not need to report that the AsyncResult.update has
                // failed.
                startedRecorder = true;
            } finally {
                if (!startedRecorder) {
                    // Something has gone wrong while starting the event
                    // recorder - notify waiting threads and remove the cached
                    // entry.
                    async.failed(new Throwable("recorder not started"));
                }
            }
        } else if (state ==
                CacheBodyOperationProcessState.NO_CACHE_FORWARD_ONLY) {

            // This thread isn't going to update the cached result and we don't
            // have a cached result to use, so just forward the SAX events.
            setCurrentHandler(getNextProcess());
        } else if (state == CacheBodyOperationProcessState.UNINITIALIZED) {

            // This process hasn't been initialized and thus isn't in a fit
            // state for use.
            throw new IllegalStateException("This process has not been " +
                    "initialized.  initializeState() must be called before " +
                    "startProcess()");
        }
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        XMLPipelineContext context = getPipelineContext();

        boolean inErrorRecoveryMode = context.inErrorRecoveryMode();

        // If this thread is responsible for updating the cache entry, we need
        // to wake up any threads that are waiting for us to finish.
        if (state == CacheBodyOperationProcessState.RECORD_AND_FORWARD) {

            if (!inErrorRecoveryMode) {

                // Capture the recording
                recording = recorder.stopRecording();
            }

            // Update the cachable result of this operation.
            final SystemClock clock = SystemClock.getDefaultInstance();
            final DependencyContext dependencyContext =
                getPipelineContext().getDependencyContext();
            dependencyContext.popDependencyTracker();
            final Dependency dependency;
            if (fixedExpiryMode) {
                dependency = new FixedTTLDependency(
                    clock, cacheControl.getTimeToLive());
            } else {
                dependency = dependencyContext.extractDependency();
            }
            dependencyContext.addDependency(dependency);

            // Set the result to cachable based on the inErrorRecoveryMode
            // state.
            final PipelineCacheState pcs = new PipelineCacheState(
                clock.getCurrentTime().addPeriod(dependency.getTimeToLive()));
            final ProviderResult result = new ProviderResult(
                new RecordingWithDependency(recording, dependency),
                getCacheGroup(), !inErrorRecoveryMode, pcs);
            async.update(result);

            if (inErrorRecoveryMode) {

                // Remove the cache entry so that subsequent threads can try
                // to populate it.
                cache.removeEntry(cacheKey);
            } else {

                // Forward the recorded events down the pipeline, allowing the
                // events to cause flow control etc. as necessary
                playback();
            }

        } else if (state == CacheBodyOperationProcessState.
                PLAYBACK_AND_SUPPRESS && !inErrorRecoveryMode) {
            // We MUST playback the cached events in stopProcess(). If we
            // attempted to do this in startProcess() the events would
            // be suppressed by the FlowControlProcess that sits at the
            // tail of the pipeline

            // playback the cached content
            playback();
        }
    }

    /**
     * Convenience method used to playback recorded events.
     *
     * @throws SAXException if there is a problem playing back the events
     */
    private void playback() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("Cache entry in " +
                    cacheName +
                    " with key " +
                    cacheKey +
                    " playback started");
        }

        PipelinePlayer player = recording.createPlayer();
        player.play(getNextProcess());

        if (logger.isDebugEnabled()) {
            logger.debug("Cache entry in " +
                    cacheName +
                    " with key " +
                    cacheKey +
                    " playback complete");
        }
    }

    /**
     * Set the current {@link ContentHandler}
     *
     * @param handler the {@link ContentHandler} to set as current.
     */
    protected void setCurrentHandler(ContentHandler handler) {
        this.currentHandler = handler;
    }

    /**
     * Get the current {@link ContentHandler}
     *
     * @return the current {@link ContentHandler}
     */
    protected ContentHandler getCurrentHandler() {
        return currentHandler;
    }

    // ========================================================================
    // ContentHandler interface methods.
    // ========================================================================

    // Javadoc inherited from ContentHandler interface

    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        getCurrentHandler().characters(ch, start, length);
    }

    // Javadoc inherited from ContentHandler interface
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {
        getCurrentHandler().ignorableWhitespace(ch, start, length);
    }

    // Javadoc inherited from ContentHandler interface
    public void endDocument() throws SAXException {
        getCurrentHandler().endDocument();
    }

    // Javadoc inherited from ContentHandler interface
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        getCurrentHandler().endElement(namespaceURI, localName, qName);
    }

    // Javadoc inherited from ContentHandler interface
    public void endPrefixMapping(String prefix) throws SAXException {
        getCurrentHandler().endPrefixMapping(prefix);
    }

    // Javadoc inherited from ContentHandler interface
    public void processingInstruction(String target, String data)
            throws SAXException {
        getCurrentHandler().processingInstruction(target, data);
    }

    // Javadoc inherited from ContentHandler interface
    public void setDocumentLocator(Locator locator) {
        getCurrentHandler().setDocumentLocator(locator);
    }

    // Javadoc inherited from ContentHandler interface
    public void skippedEntity(String name) throws SAXException {
        getCurrentHandler().skippedEntity(name);
    }

    // Javadoc inherited from ContentHandler interface
    public void startDocument() throws SAXException {
        getCurrentHandler().startDocument();
    }

    // Javadoc inherited from ContentHandler interface
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        getCurrentHandler().startElement(namespaceURI, localName, qName, atts);
    }

    // Javadoc inherited from ContentHandler interface
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        getCurrentHandler().startPrefixMapping(prefix, uri);
    }

    /**
     * Return the CacheBodyOperationProcessState of this
     * CacheBodyOperationProcess.
     *
     * @return the state.
     */
    protected CacheBodyOperationProcessState getCacheBodyOperationProcessState() {
        return state;
    }

    /**
     * Sets the expiry mode.
     *
     * @param fixedExpiryMode true to set the mode to fixed-age, false to set
     * the mode to auto
     */
    public void setFixedExpiryMode(final boolean fixedExpiryMode) {
        this.fixedExpiryMode = fixedExpiryMode;
    }

    /**
     * Returns true iff the expiry mode if fixed-age.
     *
     * @return true iff the expiry mode if fixed-age.
     */
    public boolean isFixedExpiryMode() {
        return fixedExpiryMode;
    }

    /**
     * Returns the recorded recording.
     *
     * <p>Exposed for testing purposes only.</p>
     *
     * @return the current recording
     */
    PipelineRecording getRecording() {
        return recording;
    }

    /**
     * Class to store the values of the cache. Stores a pipeline recording with
     * the matching dependency.
     */
    static final class RecordingWithDependency {
        /**
         * The recording that has been taken.
         */
        private PipelineRecording recording;

        /**
         * The dependency for the recording object
         */
        private Dependency dependency;

        public RecordingWithDependency(final PipelineRecording recording,
                                       final Dependency dependency) {
            this.recording = recording;
            this.dependency = dependency;
        }

        public PipelineRecording getRecording() {
            return recording;
        }

        public Dependency getDependency() {
            return dependency;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Jun-05	8817/1	philws	VBM:2005060908 Port cache operation process fix from 3.3.1

 16-Jun-05	8815/1	philws	VBM:2005060908 Prevent flow control or other following pipeline process side-effects from disrupting the cache process

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 19-Dec-03	489/1	doug	VBM:2003120807 Ensured that our current xml processes are recoverable when inside a try op

 31-Oct-03	440/4	doug	VBM:2003102911 Fixed problem with non standard characters

 31-Oct-03	440/1	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 07-Aug-03	316/1	allan	VBM:2003080501 Redesigned CacheControl and added timeToLive

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 09-Jun-03	49/4	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 09-Jun-03	49/2	adrian	VBM:2003060505 Updated xml caching process to include cacheBody element

 ===========================================================================
*/
