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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.annotation;

import com.volantis.mcs.dissection.*;
import com.volantis.mcs.dissection.impl.*;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class contains all the information relating to an area of dissectable
 * content within a DissectedDocument.
 * <h3>Shards</h3>
 * The main function of this class is to maintain and manage access to the list
 * of shards that have currently been populated or are in the process of being
 * populated.
 * <p>
 * The shards have to be populated in order and they are also generated lazily
 * to save time as not all shards may be needed. This class also supports
 * concurrent access by multiple threads as follows.
 * <p>
 * At most only one shard can be being populated at once, this is a direct
 * consequence of the fact that shards must be populated in order.
 * <p>
 * Once a shard has been populated it can be output concurrently by multiple
 * threads.
 * <h3>Multiple Dissectable Areas</h3>
 * <p>
 * If there is only a single dissectable area within a page then by definition
 * a dissectable area's content must exceed its available space otherwise the
 * page is in error as the fixed size exceeds the device limit.
 * <p>
 * If however there are multiple dissectable areas within the page then it is
 * possible that even though the total cost of the page exceeds the device
 * limit the cost of the contents of a dissectable area do not exceed its
 * available space.
 * <p>
 * In this case we need to explicily calculate the cost of the dissectable areas
 * contents (without taking into account the dissection overhead) so that we can
 * check whether we need to dissect or not. If we do not do this then it is
 * possible that reducing the available space by the dissection overhead would
 * be enough to cause dissection to occur.
 * <p>
 * At the moment this will not be implemented as it is an extreme case but it
 * will almost certainly need addressing in future.
 * todo: Address the above issue.
 */
public class DissectableArea
    extends ElementAnnotation {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DissectableArea.class);

/**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(DissectableArea.class);

    public static final int UNINITIALISED_INDEX = -1;

    // =========================================================================
    //   Constant Fields
    //     The fields in this section are all set during the annotation process
    //     and once set are not changed. This means that they can be accessed
    //     without worrying about synchronization.
    // =========================================================================

    private DissectedDocumentImpl dissectedDocument;

    private DissectableAreaIdentity identity;

    /**
     * The index of this area within the list of dissectable areas maintained
     * by the document.
     */
    private int index = UNINITIALISED_INDEX;

    /**
     * The number of next links.
     */
    private int nextLinkCount;

    /**
     * The number of previous links.
     */
    private int previousLinkCount;

    /**
     * The index to use to generate the next link URL.
     */
    private int nextChangeIndex;

    /**
     * The index to use to generate the previous link URL.
     */
    private int previousChangeIndex;

    /**
     * This object records the shared content that is used in all of the shard
     * links.
     */
    private SharedContentUsages overheadUsages;

    /**
     * The available space that this dissectable area has.
     */
    private int availableSpace;

    // =========================================================================
    //   Selection Specific Fields
    //     The fields in this section are used solely by the selection process
    //     and must not be accessed during generation.
    // =========================================================================

    // =========================================================================
    //   Begin Shard Monitor Scope
    //     Access to all the fields within this section are protected by the
    //     shardsMonitor.
    //
    //     In addition access to the state of the ShardWrapper objects that are
    //     added to the list of shards are also protected by the shardsMonitor.
    // =========================================================================

    private Object shardsMonitor = new Object();

    private ArrayList shards;

    private Shard pendingShard;

    // =========================================================================
    //   End Shard Monitor Scope
    // =========================================================================

    public DissectableArea() {
        shards = new ArrayList();
    }

    /**
     * Set the index of this area within the list of areas maintained by the
     * DissectedDocument.
     * @param index The index to set which must not be negative
     * @throws java.lang.IllegalStateException If this method is called more than once on
     * an area.
     */
    public void setIndex(int index) {
        if (this.index != UNINITIALISED_INDEX) {
            throw new IllegalStateException("Index already set");
        }
        this.index = index;
    }

    /**
     * Get the index of this area within the list of areas maintained by the
     * DissectedDocument.
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    public void setAvailableSpace(int availableSpace) {
        this.availableSpace = availableSpace;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }

    /**
     * Set the identity of this area.
     * @param identity The identity of this area.
     * @throws java.lang.IllegalStateException If this method is called more than once on
     * an area.
     */
    public void setIdentity(DissectableAreaIdentity identity) {
        if (this.identity != null) {
            throw new IllegalStateException("Attributes already set");
        }
        this.identity = identity;
    }

    /**
     * Get the identity associated with this area.
     * @return the dissectable area identity
     */
    public DissectableAreaIdentity getIdentity() {
        return identity;
    }

    public void setOverheadUsages(SharedContentUsages overheadUsages) {
        if (this.overheadUsages != null) {
            throw new IllegalStateException("Method already called");
        }
        this.overheadUsages = overheadUsages;
    }

    public SharedContentUsages getOverheadUsages() {
        return overheadUsages;
    }

    /**
     * This method initialises the state of this object to make sure that it
     * is ready for use.
     * <p>
     * Among other things it checks to make sure that this DissectableArea is
     * valid, if it is not then it throws a DissectionException.
     * <p>
     * The validity criteria are that it has at least one next and one previous
     * link and that it has been initialised.
     */
    public void initialise(DissectedDocumentImpl dissectedDocument)
        throws DissectionException {

        if (index == UNINITIALISED_INDEX) {
            throw new DissectionException(
                        exceptionLocalizer.format(
                                    "dissectable-area-not-initialised"));
        }
        if (nextLinkCount == 0) {
            throw new DissectionException(
                        exceptionLocalizer.format(
                                    "dissectable-area-no-next-link"));
        }
        if (previousLinkCount == 0) {
            // MCSDI0009X="DissectableArea has no previous link"
            throw new DissectionException(
                        exceptionLocalizer.format(
                                    "dissectable-area-missing-previous-link"));

        }

        DissectionURLManager urlManager = dissectedDocument.getUrlManager();

        this.dissectedDocument = dissectedDocument;

        DissectableAreaIdentity identity = getIdentity();
        nextChangeIndex = urlManager.getNextChangeIndex(identity);

        previousChangeIndex = urlManager.getPreviousChangeIndex(identity);
    }

    /**
     * Keep track of the links that have been registered with this area.
     * <p>
     * At the moment this area is only interested in whether there is at
     * least one next and one previous link associated with this area so it just
     * keeps count.
     * @param link The ShardLink to add.
     */
    public void addShardLink(ShardLink link, ShardLinkActionImpl action) {
        action.incrementLinkCount(this);
    }

    /**
     * Increment the count of the number of next links that there are for this
     * area.
     */
    public void incrementNextLinkCount() {
        nextLinkCount += 1;
    }

    /**
     * Increment the count of the number of previous links that there are for
     * this area.
     */
    public void incrementPreviousLinkCount() {
        previousLinkCount += 1;
    }

    /**
     * This method retrieves the requested shard or the most appropriate one
     * if the requested shard index is invalid.
     * <p>
     * If the requested shard index is negative then this is an internal error
     * and so an exception is thrown.
     * <p>
     * If the requested shard index is less than or equal to the last shard
     * generated then the appropriate shard is returned.
     * <p>
     * If the requested shard index is greater than the last shard generated
     * then the behaviour depends on whether the dissectable area is complete
     * and whether a shard is currently being generated.
     * <ul>
     *   <li>If the dissectable area is complete, i.e. all the shards have been
     *       generated then the last shard is returned.
     *   <li>If it is not complete but a shard is currently being selected
     *       then this method waits for the next shard to complete and returns
     *       that.
     *   <li>If it is not complete and no shard is currently being selected
     *       then the next shard is selected.
     * </ul>
     * @param requestedShard
     * @return the shard
     */
    public Shard retrieveShard(DissectionContext dissectionContext,
                               int requestedShard,
                               AvailableShardsImpl availableShards)
        throws DissectionException {

        // Make sure that the shard is not negative. Do this outside the
        // synchronize block as it is not dependent on the size of the list.

        if (requestedShard < 0) {
            throw new DissectionException(
                        exceptionLocalizer.format("shard-index-invalid"));
        }

        boolean wait = false;
        Shard shard;

        // Synchronize on the shards monitor.
        synchronized (shardsMonitor) {
            int count = shards.size();

            // If requested update the information about the available shards.
            if (availableShards != null) {
                availableShards.setShardCount(index, count, isComplete());
            }

            // If this is complete then we can simply make sure that it is in
            // the correct range.
            if (isComplete()) {
                // A complete area must have at least one shard.
                if (count == 0) {
                    throw new DissectionException(
                                exceptionLocalizer.format("area-missing-shards",
                                                          this));
                }

                // todo: If it is outside the range throw an exception, the
                // todo: user must ensure that it is valid. If this is done then
                // todo: the protocol will need to do the range checking itself.
                if (requestedShard >= count) {
                    requestedShard = count - 1;
                }

                // The requested shard is valid and references a shard that is
                // ready.
                return (Shard) shards.get(requestedShard);
            } else if (requestedShard < count) {
                // The requested shard is valid and references a shard that is
                // ready.
                return (Shard) shards.get(requestedShard);
            } else if (pendingShard != null) {
                // A shard is pending so we need to wait for it.
                wait = true;
                shard = pendingShard;
            } else {
                pendingShard = new Shard();
                shard = pendingShard;
            }
        }


        // At this point we have a Shard that is in pending state (i.e. is not
        // ready) and the wait flag indicates whether it is this thread or
        // another thread that is responsible for populating it.

        if (wait) {
            try {
                // Another thread is currently populating the shard so wait for
                // it.
                synchronized (shard) {
                    // It is possible that since the state of the shard was last
                    // checked the other thread has completed and updated it so
                    // check it again before we go to sleep.
                    synchronized (shardsMonitor) {
                        if (pendingShard == null) {
                            return shard;
                        }
                    }

                    shard.wait();
                }
            } catch (InterruptedException e) {
                throw new DissectionException(e);
            }

            // At this point the wrapper must be ready.
            synchronized (shardsMonitor) {
                if (pendingShard == null) {
                    return shard;
                }
            }

            // If we get here then something has gone wrong.
            throw new DissectionException();
        }

        try {
            // It is this thread's responsibility to populate the shard.
            if (logger.isDebugEnabled()) {
                logger.debug("Start populating shard " + shard);
            }

            // First thing to do is to set the shard index, this must be done
            // before anything else, however we cannot add the shard into the
            // list yet as the list only contains shards that are ready.
            int shardIndex = shards.size();
            shard.setIndex(shardIndex);

            // Create a SharedContentUsages to track usages.
            SharedContentUsages usages
                = DissectionHelper.createSharedContentUsages(document);
            shard.setSharedContentUsages(usages);

            // A previous link is needed if the shard is not the first one.
            boolean previousLinkNeeded = (shardIndex != 0);

            // Whether a next link is needed or not depends on whether this is
            // the last shard.
            boolean nextLinkNeeded;

            // Set the available space in the shard.
            shard.setAvailableSpace(availableSpace);

            int result = selectShardContents(shard);
            switch (result) {
                case NODE_CANNOT_FIT:
                    // Debug output.
                    if (logger.isDebugEnabled()) {
                        DebugOutputVisitor visitor = new DebugOutputVisitor();
                        DissectedDocumentImpl docImpl
                            = (DissectedDocumentImpl) document.getAnnotation();
                        visitor.debug(docImpl, new PrintWriter(System.out));
                    }
                    throw new DissectionException(
                                exceptionLocalizer.format("node-cannot-fit"));

                case SHARD_COMPLETE:
                    nextLinkNeeded = true;
                    break;

                case ADDED_NODE:
                    nextLinkNeeded = false;
                    break;

                default:
                    throw new IllegalStateException("Unknown value " + result);
            }

            DissectionURLManager urlManager = dissectedDocument.getUrlManager();
            MarinerURL documentURL = dissectedDocument.getDocumentURL();
            ShardLinkDetailsImpl linkDetails;
            MarinerURL url;
            if (nextLinkNeeded) {
                url = urlManager.makeURL(dissectionContext,
                                         documentURL,
                                         nextChangeIndex);
                linkDetails = new ShardLinkDetailsImpl();
                linkDetails.setURL(url);
                linkDetails.setDestinationShardIndex(shardIndex + 1);
                shard.setNextLink(linkDetails);
            }
            if (previousLinkNeeded) {
                url = urlManager.makeURL(dissectionContext,
                                         documentURL,
                                         previousChangeIndex);
                linkDetails = new ShardLinkDetailsImpl();
                linkDetails.setURL(url);
                linkDetails.setDestinationShardIndex(shardIndex - 1);
                shard.setPreviousLink(linkDetails);
            }

            // Add the pending shard to the list.
            synchronized (shardsMonitor) {
                pendingShard = null;
                shards.add(shard);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("End populating shard " + shard);
            }

            return shard;
        } finally {
            // Whatever happens we must wake up any threads waiting for the
            // shard.
            synchronized (shard) {
                shard.notifyAll();
            }
        }
    }

    public ShardIterator getShardIterator(DissectionContext dissectionContext) {
        return new ShardIteratorImpl(dissectionContext);
    }

    private class ShardIteratorImpl implements ShardIterator {

        private DissectionContext dissectionContext;

        private int shardIndex;

        public ShardIteratorImpl(DissectionContext dissectionContext) {
            this.dissectionContext = dissectionContext;
            this.shardIndex = 0;
        }

        public boolean hasMoreShards()
            throws DissectionException {

            synchronized (shardsMonitor) {
                if (shardIndex < shards.size()) {
                    return true;
                }

                return !isComplete();
            }
        }

        public void populateNextShard()
            throws DissectionException {

            retrieveShard(dissectionContext, shardIndex, null);
            shardIndex += 1;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
