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

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * This is the base class for the classes which are used to annotate a document
 * tree for the purposes of dissection.
 * <p>
 * As far as this and related classes are concerned each node is made up of a
 * number of parts where a part is something that can be added to a shard. i.e.
 * element nodes have children and text nodes have segments.
 */
public abstract class DissectableNodeAnnotation
    implements NodeAnnotation, NodeDissector {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DissectableNodeAnnotation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            DissectableNodeAnnotation.class);

    // =========================================================================
    //   Shard Constants
    //     The following constants are used to indicate a node's relationship
    //     to a shard, i.e. is it in it, before it, after it, etc.
    // todo: revisit these as nodes are always added to the correct shard even
    // todo: if an ancestor fits in a single shard. This greatly simplifies the
    // todo: task of navigating the tree and eliminates the need for some of
    // todo: these.
    // =========================================================================

    /**
     * This means that this node and all its parts are completely inside the
     * shard.
     */
    public static final int INSIDE_SHARD = 0;

    /**
     * This means that this node is the beginning of the current shard.
     */
    public static final int BEGINNING_OF_SHARD = 1;

    /**
     * This means that this node is the end of the current shard.
     */
    public static final int END_OF_SHARD = 2;

    /**
     * This means that this node is before the current shard.
     */
    public static final int BEFORE_SHARD = 3;

    /**
     * This means that this node is after the current shard.
     */
    public static final int AFTER_SHARD = 4;

    /**
     * This means that this node is the contents of the whole shard excluding
     * overheads for ancestor nodes.
     */
    public static final int WHOLE_SHARD = 5;

    // =========================================================================
    //   Constant Fields
    //     The fields in this section are all set during the annotation process
    //     and once set are not changed. This means that they can be accessed
    //     without worrying about synchronization.
    // =========================================================================

    /**
     * The cost associated with this node and all its parts.
     * <p>
     * If this is set to {@link Cost#VARIABLE} then it means that the cost is
     * variable, otherwise the cost must be greater than or equal to 0.
     */
    private int aggregateCost;

    /**
     * This flag is set to true if the selection process must check the cost
     * before any parts can be added to a shard.
     * <p>
     * The following comments explain why this is necessary.
     * <p>
     * If the cost of a node is variable then it must be calculated each time
     * the node is visited. Calculating the size involves recursing through all
     * the descendants of the node until either a leaf or node with a fixed size
     * is reached. While this is being done it is necessary to keep track of
     * any changes to the shard's SharedContentUsagesImpl in case it needs to be
     * reset back to the state it was before. All this could take a long time
     * especially as all ancestors of a variable node are marked as variable
     * so it goes all the way back up the tree.
     * <p>
     * An alternative is to not calculate the cost at all but simply to just
     * apply the normal selection rules. The problem with this is that it means
     * that if a node must (in the case of atomic elements) or should (in the
     * case of keep together) add all of its parts to the same shard then it
     * would have to remove nodes from shards if it found that it could not add
     * one to the shard. Supporting back tracking in this way would greatly
     * complicate the dissection process.
     * <p>
     * Therefore this flag allows a node to request an accurate cost to be
     * calculated before any parts are added to the shard.
     */
    private boolean mustCheckCost;

    /**
     * The document that is being dissected.
     */
    protected DissectableDocument document;

    // =========================================================================
    //   Selection Specific Fields
    //     The fields in this section are used solely by the selection process
    //     and must not be accessed during generation.
    // =========================================================================

    /**
     * This field is used for tracking changes to a shard's SharedContentUsagesImpl.
     * It is created by the first shard that needs it and used for all
     * subsequent shards. It is valid to use it on the SharedContentUsagesImpl of
     * a different shard than it was created for as all shards in the same
     * DissectedDocument have the same size SharedContentUsagesImpl.
     */
    private SharedContentUsages.ChangeSet changeSet;

    // =========================================================================
    //   Selection and Output Specific Fields
    //     The fields in this section are used by both the selection process
    //     and the output process and so could be accessed concurrently by
    //     selection and generation threads.
    // =========================================================================

    /**
     * The initial value of the {@link #firstShard} and {@link #lastShard}
     * fields that is used to determine whether a node has been added to a
     * shard yet.
     */
    private static final int NOT_IN_SHARD = -99;

    /**
     * The indexes of the first and last shard that this node has been added
     * to.
     * <p>
     * They are either both set to their initial values, or they are set to
     * valid shard numbers. It is not possible for one of them to be set to its
     * initial value and the other to be a valid shard number.
     * <p>
     * If they are set to their initial values then this node has never
     * been visited by the selection process. This can mean one of two things.
     * Either the selection process has already added an ancestor element and
     * all its descendants to a shard and therefore did not need to visit this
     * node, or it is just that the shard that this node belongs to has not been
     * populated yet.
     * <p>
     * The two situations can only be distinguished by checking an ancestor to
     * see whether it has been completely added to a single shard.
     * <p>
     * If these are both the same then it means one of two things. Either this
     * node and all its parts have been completely added to a single shard, or
     * this node and some of its parts have been added to a shard but the
     * rest have not yet been added. They can be distinguished by checking the
     * {@link #completed} flag, if it is true then this node has been completely
     * added to a shard, otherwise it has only partially been added.
     * <p>
     * If none of the above hold then firstShard must always be less than the
     * lastShard and they define the range of shards (inclusive) to which the
     * node belongs.
     */
    private int firstShard = NOT_IN_SHARD;

    /**
     * @see #firstShard
     */
    private int lastShard;

    /**
     * If true this flag means that this node and all its parts have been added
     * into a shard or shards, otherwise there are still parts that need
     * processing.
     * <p>
     * Once this has been set to true the state of this object and all its parts
     * must not be modified.
     * <p>
     * If this flag is false then it does not mean that this node is incomplete
     * as it may be that an ancestor node was marked as complete without having
     * to visitElement this node.
     * @see #firstShard
     */
    private boolean completed;

    // =========================================================================
    //   End Of Fields
    // =========================================================================

    public void setDocument(DissectableDocument document) {
        this.document = document;
    }

    protected DissectableNodeAnnotation() {
    }

    public void setMustCheckCost(boolean mustCheckCost) {
        this.mustCheckCost = mustCheckCost;
    }

    public NodeDissector getNodeDissector () {
        return this;
    }

    public int selectShardContents(Shard shard)
        throws DissectionException {

        // Completed nodes should never be visited again.
        if (completed) {
            throw new DissectionException(
                        exceptionLocalizer.format("completed-node-visited"));
        }

        // Get the available space. Check it to see whether there is any space
        // available at all for this node.
        int availableSpace = shard.getAvailableSpace();
        if (availableSpace < 0) {
            return NODE_CANNOT_FIT;
        }

        // Check to see if the whole node fits if we have not already added this
        // node to a shard. If this has been added to a shard then the
        // firstShard will have been modified otherwise it will still be set to
        // its initial value.
        if (!inAnyShard()) {
            // If the cost of this node and its parts is variable then
            // we must visitElement all the parts.

            // If this node must check the cost, or the cost is not variable so
            // we can check it easily then do so.
            boolean variable = costIsVariable();
            if (mustCheckCost || !variable) {

                // If the size is variable then we need to track changes to
                // the shards SharedContentUsagesImpl in case we need to undo them.
                SharedContentUsages table = null;
                SharedContentUsages.ChangeSet oldChangeSet = null;
                if (variable) {
                    table = shard.getSharedContentUsages();

                    // If the ChangeSet has not been created then create it
                    // now.
                    if (changeSet == null) {
                        changeSet = table.createChangeSet();
                    }

                    // Push this node's ChangeSet and remember the current
                    // one if any to restore later.
                    oldChangeSet = table.pushChangeSet(changeSet);
                }

                // Calculate the cost, this will update the shard's
                // SharedContentUsagesImpl.
                int cost = calculateCost(shard);

                if (logger.isDebugEnabled()) {
                    logger.debug("Node " + this + " would cost " + cost
                                 + " to add to shard " + shard
                                 + " which has " + availableSpace
                                 + " units of space available");
                }

                // Check to see whether this node and all its parts will fit
                // in the current shard. If the cost is infinite then it will
                // not fit in no matter what, even if the available space is
                // also infinite.
                if (cost != Cost.INFINITE && cost < availableSpace) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("Node " + this
                                     + " completely fits in shard " + shard);
                    }

                    // The cost of this node is less than the space available
                    // so add this node and all its parts to the shard and
                    // mark this node as complete.
                    addToShard(shard, true);

                    // Reduce the available space in the shard by the cost of
                    // this node.
                    shard.decrementAvailableSpace(cost);

                    // If the cost is variable then we need to remove the
                    // ChangeSet we added to the SharedContentUsagesImpl without
                    // undoing the changes.
                    if (variable) {
                        table.popChangeSet(oldChangeSet);
                    }

                    return ADDED_NODE;
                }

                // If the cost is variable then we need to remove the ChangeSet
                // that was added from the SharedContentUsagesImpl, undoing the
                // changes in the process.
                if (variable) {
                    table.popChangeSet(oldChangeSet, true);
                }

                // This node is too big to add into the shard so we will need
                // to split it.
                if (logger.isDebugEnabled()) {
                    logger.debug("Cost " + cost
                                 + " is greater than the available space "
                                 + availableSpace + " left in shard " + shard);
                }

                // Drop through to the dissection.
            } else {
                // Either this node or one of its parts has a variable cost and
                // we do not have to calculate the cost. Instead we will
                // visitElement the parts of this node directly.

                return selectVariableShardContents(shard);
            }
        }

        // If the shard is empty then this node must be dissected otherwise
        // we will end up with empty shards and possibly never finish generating
        // the page.
        boolean mustDissect = shard.isEmpty();

        // Attempt to dissect the node.
        int result = dissectNode(shard, mustDissect);
        switch (result) {
            case NODE_CANNOT_FIT:
                if (mustDissect) {
                    throw new DissectionException(
                                exceptionLocalizer.format(
                                            "disect-empty-shard-error"));
                }
                // This node cannot fit.
                if (logger.isDebugEnabled()) {
                    logger.debug("Neither " + this + " nor any of its"
                                 + " parts can fit into shard "
                                 + shard);
                }

                // Next time the shard will be empty so it will be forced to
                // dissect.
                break;

            case SHARD_COMPLETE:
                if (logger.isDebugEnabled()) {
                    logger.debug("Part of " + this + " can fit into shard "
                                 + shard);
                }

                addToShard(shard, false);
                break;

            case ADDED_NODE:
                if (logger.isDebugEnabled()) {
                    logger.debug("The remaining parts of " + this
                                 + " can fit into shard " + shard);
                }

                // This node and all its children now belong to either this
                // shard or a preceding one so add this node to the shard and
                // mark it as completed.
                addToShard(shard, true);
                break;

            default:
                throw new IllegalStateException("Unknown value " + result);
        }

        return result;
    }

    /**
     * Implementations of this method should attempt to dissect the node if
     * possible.
     * @param shard The current shard being selected.
     * @param mustDissect If true then this method must dissect at all costs,
     * otherwise it can refuse to dissect.
     * @return See {@link #selectShardContents} for details.
     * @throws com.volantis.mcs.dissection.DissectionException If there was a problem dissecting the node.
     */
    protected abstract int dissectNode(Shard shard, boolean mustDissect)
        throws DissectionException;

    protected int selectVariableShardContents(Shard shard)
        throws DissectionException {

        SharedContentUsages.ChangeSet oldChangeSet = null;
        SharedContentUsages table = shard.getSharedContentUsages();

        // If the table exists then do something with it.
        // If the ChangeSet has not been created then create it
        // now.
        if (changeSet == null) {
            changeSet = table.createChangeSet();
        }

        // Push this node's ChangeSet and remember the current
        // one if any to restore later.
        oldChangeSet = table.pushChangeSet(changeSet);

        // Flag to determine whether we should undo the changes to the
        // shared content usages.
        boolean undo = false;
        int result = selectVariableShardContentsImpl(shard);
        switch (result) {
            case NODE_CANNOT_FIT:
                // The node could not fit at all so we need to undo any changes
                // to shared content usages.
                undo = true;
                break;
            case SHARD_COMPLETE:
                addToShard(shard, false);
                break;

            case ADDED_NODE:
                addToShard(shard, true);
                break;

            default:
                throw new IllegalStateException("Unknown value " + result);
        }

        table.popChangeSet(oldChangeSet, undo);

        // Return the result that we were given back to the caller.
        return result;
    }

    protected abstract int selectVariableShardContentsImpl(Shard shard)
        throws DissectionException;

    /**
     * Calculate the cost of this node and all its children.
     * @param shard
     * @return the cost as an integer
     */
    protected int calculateCost(Shard shard)
        throws DissectionException {

        if (aggregateCost != Cost.VARIABLE) {
            return aggregateCost;
        }

        // Get an object to use to calculate the cost.
        Cost cost = shard.getTemporaryShardCost();

        addCost(cost);

        return cost.getTotal();
    }

    /**
     * Add the cost of the node to the accumulator.
     * <p>
     * The accumulator will never contain a variable total.
     * @param cost
     */
    protected abstract void addCost(Cost cost)
        throws DissectionException;

    /**
     * Add this node to the specified shard and mark it as complete if
     * necessary.
     * <p>
     * If this node only belongs to the current shard and it is marked as
     * complete then this method also adds all the parts of this node to the
     * current shard.
     * <p>
     * @param shard The shard to which this node should be added.
     * @param completed True if this node is now complete.
     */
    protected final void addToShard(Shard shard, boolean completed) {
        shard.addNode(this);

        int shardIndex = shard.getIndex();

        if (!inAnyShard()) {
            firstShard = shardIndex;
        }
        lastShard = shardIndex;

        this.completed = completed;

        if (completed && firstShard == lastShard) {
            addPartsToShard(shard);
        }
    }

    /**
     * Add the parts of this node to the specified shard.
     * <p>
     * After invoking this all parts must belong to the specified shard and
     * must be marked as complete.
     * @param shard The shard to which the parts should be added.
     */
    protected abstract void addPartsToShard(Shard shard);

    /**
     * Check to see whether this node is in any shard at the moment.
     * @return True if it is in a shard and false if it is not.
     */
    public final boolean inAnyShard() {
        return firstShard != NOT_IN_SHARD;

    }

    public final int getFirstShard() {
        return firstShard;
    }

    public final int getLastShard() {
        return lastShard;
    }

    /**
     * Checks to see whether this node is complete.
     * @return True if this node is complete and false otherwise.
     */
    protected final boolean isComplete() {
        return completed;
    }

    /**
     * Check to see whether the cost of this node is variable.
     * @return True if it is variable and false otherwise.
     */
    protected final boolean costIsVariable() {
        // The cost is variable if it is set to VARIABLE_COST.
        return (aggregateCost == Cost.VARIABLE);
    }

    /**
     * Set the cost associated with this node.
     * @param aggregateCost The cost associated with this node. This must be
     * set to {@link Cost#VARIABLE} if the cost of the node is variable.
     */
    public void setAggregateCost(int aggregateCost) {
        this.aggregateCost = aggregateCost;
    }

    /**
     * Get the cost associated with this node.
     * @return A non negative integer that is the cost of this node.
     * @throws DissectionException If the cost is variable.
     */
    protected final int getAggregateCost()
        throws DissectionException {

        // If the cost is variable then throw an exception, this is to protect
        // against accidentally doing arithmetic with negative costs which
        // could cause all sorts of problems.
        if (costIsVariable()) {            
            throw new DissectionException(
                        exceptionLocalizer.format("cost-variable-missing "));
        }

        return aggregateCost;
    }

    /**
     * Get the relationship between this node and the specified shard.
     * <p>
     * It can be one of the following values.
     * <dl>
     *   <dt>{@link #INSIDE_SHARD}
     *   <dd>
     *   </dd>
     *   <dt>{@link #BEGINNING_OF_SHARD}
     *   <dd>
     *   </dd>
     *   <dt>{@link #END_OF_SHARD}
     *   <dd>
     *   </dd>
     *   <dt>{@link #BEFORE_SHARD}
     *   <dd>
     *   </dd>
     *   <dt>{@link #AFTER_SHARD}
     *   <dd>
     *   </dd>
     * </dl>
     * @param shard The shard to check this node against.
     * @return One of the above values that indicate the relationship between
     * this node and the shard.
     */
    public int getRelationshipToShard(Shard shard) {
        int shardNumber = shard.getIndex();

        if (logger.isDebugEnabled()) {
            logger.debug("Should " + this + " which belongs to shards "
                         + firstShard + " to " + lastShard
                         + " generate output for shard " + shardNumber);
        }

        // If this node has not been added to a shard at all then it must come
        // after the current one.
        if (!inAnyShard()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Node " + this + " has not yet been added"
                             + " to any shard so it must be after shard "
                             + shardNumber);
            }

            return AFTER_SHARD;
        }

        // If the shard being generated precedes the first shard that this
        // node belongs to then we have finished.
        if (shardNumber < firstShard) {
            if (logger.isDebugEnabled()) {
                logger.debug("Node " + this + " belongs to shards "
                             + firstShard + " and above which come after "
                             + " shard " + shardNumber);
            }

            return AFTER_SHARD;
        }

        // If the shard being generated is after the last shard that this
        // nodes belongs to then ignore the sub tree rooted at this node but
        // keep searching for the first node which is part of this shard.
        if (shardNumber > lastShard) {
            if (logger.isDebugEnabled()) {
                logger.debug("Node " + this + " belongs to shards "
                             + lastShard + " and below which come before "
                             + " shard " + shardNumber);
            }

            return BEFORE_SHARD;
        }

        // If this node is not complete then this node must be the end of the
        // current shard.
        if (!completed) {
            return END_OF_SHARD;
        }

        // If this node belongs to only one shard then all its parts must also
        // belong to it.
        if (firstShard == lastShard) {
            if (logger.isDebugEnabled()) {
                logger.debug("Node " + this + " is the root of a whole"
                             + " sub tree which was added to shard "
                             + shardNumber);
            }
            return INSIDE_SHARD;
        }

        // If the last part of this node is in the shard then this is the
        // beginning of the shard.
        if (shardNumber == lastShard) {
            return BEGINNING_OF_SHARD;
        }

        // If the first part of this node is in the shard then this is the
        // end of the shard.
        if (shardNumber == firstShard) {
            return END_OF_SHARD;
        }

        // This node must be the whole of the shard contents.
        return WHOLE_SHARD;
    }

    /**
     * Get a description of the shards that contain this node.
     * @return the shard description
     */
    public String getShardDescription() {
        if (!inAnyShard()) {
            return "not in shard";
        } else if (firstShard == lastShard) {
            if (completed) {
                return Integer.toString(firstShard);
            } else {
                return firstShard + "+";
            }
        } else {
            return firstShard + "-" + lastShard + (completed ? "" : "+");
        }
    }

    public String getCostDescription() {
        if (aggregateCost == Cost.VARIABLE) {
            return "VARIABLE";
        } else if (aggregateCost == Cost.INFINITE) {
            return "INFINITE";
        } else {
            return "@" + aggregateCost;
        }
    }

    public void addState(StringBuffer buffer) {
        buffer.append("[");
        if (!inAnyShard()) {
            buffer.append("not in shard");
        } else if (firstShard == lastShard) {
            if (completed) {
                buffer.append(firstShard);
            } else {
                buffer.append(firstShard).append("+");
            }
        } else {
            buffer.append(firstShard)
                .append("-")
                .append(lastShard)
                .append(completed ? "" : "+");
        }
        buffer.append("] @");

        if (aggregateCost == Cost.VARIABLE) {
            buffer.append("VARIABLE");
        } else if (aggregateCost == Cost.INFINITE) {
            buffer.append("INFINITE");
        } else {
            buffer.append(aggregateCost);
        }
        buffer.append(" ");
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

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/3	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 12-Jun-03	309/8	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 12-Jun-03	363/3	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/3	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
