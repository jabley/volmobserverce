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
 * $Header: /src/voyager/com/volantis/mcs/protocols/dissection/NodeAnnotation.java,v 1.10 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 03-May-02    Paul            VBM:2002042203 - Wrapped all debug calls
 *                              which create a new String with a check to make
 *                              sure that debug is enabled.
 * 23-May-02    Paul            VBM:2002042202 - Moved most of the divide hint
 *                              code into the markShardNodes method.
 * 05-Jun-02    Adrian          VBM:2002021103 - Added methods isKeeptogether..
 *                              ..Element and isChildOfKeeptogetherElement..
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 20-Jan-03    Adrian          VBM:2003011605 - undid the changes from 
 *                              2002021103. updated markShardNodes to call 
 *                              isKeepTogetherElement where the content is 
 *                              found not to fit. if it is a keeptogether then 
 *                              the content will not be split at that attempt. 
 *                              As isKeeptogetherElement is guaranteed to 
 *                              return false on a second call the content of a 
 *                              keeptogether will be split if it is still found
 *                              not to fit when it is processed for the next 
 *                              shard. 
 * 24-Feb-03    Phil W-S        VBM:2003022006 - Update javadoc.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is the base class for the classes which are used to annotate a dom
 * tree for the purposes of dissection.
 */
public abstract class NodeAnnotation {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(NodeAnnotation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(NodeAnnotation.class);

    protected static final int IGNORE_NODE = -1;
    protected static final int NODE_CANNOT_FIT = -2;
    protected static final int SHARD_COMPLETE = -3;
    protected static final int REFUSED_TO_SPLIT = -4;

    /**
     * The protocol which generated the tree.
     */
    protected DOMProtocol protocol;

    /**
     * The node to which this annotation is attached.
     */
    //protected Node node;

    /**
     * The size of the contents of the node and all its children, if any.
     */
    private int contentsSize = -1;

    /**
     * The size of the contents of the node and all its children except
     * for any DissectableNodes and their children.
     */
    private int fixedContentsSize = -1;

    /**
     * The size of the overhead associated with the node.
     */
    private int overheadSize = -1;

    /**
     * The number of the first shard which contains the node.
     * Each node can be used by 1 or more shards depending on where it is
     * in the tree. The root node for instance will be used by all the shards
     * but a node further down the tree may only be used by one of two shards.
     */
    protected int firstShard = -1;

    /**
     * The number of the last shard which contains this node.
     */
    protected int lastShard = -1;

    /**
     * A flag to indicate whether this node and all its children have been
     * added to shards.
     */
    protected boolean completed = false;

    /**
     * A flag to indicate whether this node is atomic, i.e. should not be
     * broken down into smaller items.
     */
    protected boolean atomic = false;


    public NodeAnnotation() {
    }

    public void initialise()
            throws ProtocolException {
    }

    /**
     * Set the value of the protocol property.
     *
     * @param protocol The new value of the protocol property.
     */
    public void setProtocol(DOMProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Get the value of the protocol property.
     *
     * @return The value of the protocol property.
     */
    public DOMProtocol getProtocol() {
        return protocol;
    }

    /**
     * Set the value of the atomic property.
     *
     * @param atomic The new value of the atomic property.
     */
    public void setAtomic(boolean atomic) {
        this.atomic = atomic;
    }

    /**
     * Get the value of the atomic property.
     *
     * @return The value of the atomic property.
     */
    public boolean getAtomic() {
        return atomic;
    }

    /**
     * Get the node associated with this annotation.
     */
    protected abstract Node getNode();

    /**
     * Get the size of all the contents, if it has not yet been calculated
     * then calculate it first.
     *
     * @return The size of the contents.
     */
    public int getContentsSize() {
        if (contentsSize == -1) {
            contentsSize = calculateContentsSize();
        }

        return contentsSize;
    }

    /**
     * Get the size of the fixed contents, if it has not yet been calculated
     * then calculate it first.
     *
     * @return The size of the fixed contents.
     */
    public int getFixedContentsSize() {
        if (fixedContentsSize == -1) {
            fixedContentsSize = calculateFixedContentsSize();
        }

        return fixedContentsSize;
    }

    /**
     * Get the overhead of using this node in a shard, if it has not yet been
     * calculated then calculate it first.
     *
     * @return The overhead of using this node in a shard.
     */
    public int getOverheadSize() throws ProtocolException {
        if (overheadSize == -1) {
            overheadSize = calculateOverheadSize();
        }

        return overheadSize;
    }

    /**
     * This method should calculate the size of the contents of the node and
     * its children if it has any.
     *
     * @return The size of the contents.
     */
    protected abstract int calculateContentsSize();

    /**
     * This method should calculate the size of the fixed contents of the node
     * and its children if it has any.
     *
     * @return The size of the fixed contents.
     */
    protected abstract int calculateFixedContentsSize();

    /**
     * This method should calculate the overhead of using the node in a shard.
     *
     * @return The overhead of using the node in a shard.
     */
    protected abstract int calculateOverheadSize() throws ProtocolException;

    /**
     * This method marks those nodes which belong to the specified shard. A
     * node can belong to either one shard or a consecutive range of shards
     * so each node stores the range of shards that it belongs to.
     *
     * @param shardNumber The number of the shard currently being marked.
     * @param limit       The amount of available space remaining in the shard.
     * @return If the current node and all its children which are not part
     *         of a previous shard can fit into the space remaining then this method
     *         must return the space that they consume.
     *         <p>
     *         If neither the current node or any of its children which are not part
     *         of a previous shard can fit into the space remaining then this method
     *         must return NODE_CANNOT_FIT.
     *         <p>
     *         If the current node and all its children are already part of a shard
     *         then this method must return IGNORE_NODE.
     *         <p>
     *         If some of the children of the current node which are not part of a
     *         previous shard could fit in the space remaining but some of them could
     *         not then this method must return SHARD_COMPLETE.
     */
    protected final int markShardNodes(int shardNumber, int limit)
            throws ProtocolException {

        // Ignore this node if it and all its children have already been
        // added to a shard.
        if (completed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring " + this
                        + " already covered by shards "
                        + firstShard + " to " + lastShard);
            }

            return IGNORE_NODE;
        }

        // Get the ElementAnnotation of the parent if is exists.
        Node node = getNode();
        Element parent = node.getParent();
        ElementAnnotation parentAnnotation;
        boolean activateDivideHint;
        if (parent == null) {
            parentAnnotation = null;
            activateDivideHint = false;
        } else {
            parentAnnotation = (ElementAnnotation) parent.getObject();
            // If this node is a divide hint element then we need to activate the
            // divide hint on exiting this method.
            activateDivideHint = isDivideHintElement();
        }
        // Only check to see if this is small enough to fit if we have not
        // visited here before. We can tell that by looking at the firstShard
        // value. If it is -1 then we have not visited, otherwise we have visited
        // and in the process added some of its descendants to the shard.
        if (firstShard == -1) {
            int size = getContentsSize();
            if (size < limit) {

                // Update the range of shards which this node belongs to.
                firstShard = shardNumber;
                lastShard = shardNumber;

                // The whole node has been added to the shard so mark it as
                // completed.
                completed = true;

                if (logger.isDebugEnabled()) {
                    logger.debug("Adding " + this + " whose size is " + size
                            + " to shard " + shardNumber);
                }

                // Make sure that we set the divide hint active flag properly. We do
                // this on exiting as the value set when processing the previous
                // node is used in the body of this method.
                if (parentAnnotation != null) {
                    if (logger.isDebugEnabled()) {
                        if (activateDivideHint) {
                            logger.debug("Activating divide hint in " + parent);
                        } else if (parentAnnotation.isDivideHintActive()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Deactivating divide hint in " +
                                        parent);
                            }
                        }
                    }
                    parentAnnotation.setDivideHintActive(activateDivideHint);
                }

                return size;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Size of contents " + size
                        + " is greater than the available space "
                        + limit + " left in shard " + shardNumber);
            }
        }

        boolean triedToSplit;
        int result;

        // At this point we know that we cannot fit the whole of this node into
        // the shard. If the preceding node was a divide hint element then we
        // should not try and break up this node.
        if (parentAnnotation != null &&
                (parentAnnotation.isDivideHintActive() ||
                        isKeepTogetherElement())) {
            triedToSplit = false;
            result = NODE_CANNOT_FIT;
        } else {
            // See whether we can break this node up into different parts.
            triedToSplit = true;
            result = markShardNodesImpl(shardNumber, limit);
        }

        switch (result) {
            case IGNORE_NODE:
                // Ignore this node. This should never happen.
                throw new ProtocolException(
                        exceptionLocalizer.format("internal-error"));

            case NODE_CANNOT_FIT:
                // This node cannot fit.
                if (logger.isDebugEnabled()) {
                    if (triedToSplit) {
                        logger.debug("Neither " + this + " nor any of its"
                                + " children can fit into shard "
                                + shardNumber);
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug(this + " cannot fit into shard "
                                    + shardNumber +
                                    " and it could not be split as there"
                                    + " is an active divide hint");
                        }
                    }
                }
                break;

            case SHARD_COMPLETE:
                if (logger.isDebugEnabled()) {
                    logger.debug("Part of " + this
                            + " or a child can fit into shard "
                            + shardNumber);
                }

                // Part of this node, or a child of this node fits in the shard so
                // update the range of shards to which it belongs.
                if (firstShard == -1) {
                    firstShard = shardNumber;
                }
                lastShard = shardNumber;
                break;

            default:
                if (logger.isDebugEnabled()) {
                    logger.debug("The remainder of " + this
                            + " and any children can fit into shard "
                            + shardNumber);
                }

                // This node and all its children fit in the shard so mark this node
                // as completed and update the range of shards to which it belongs.
                completed = true;

                if (firstShard == -1) {
                    firstShard = shardNumber;
                }
                lastShard = shardNumber;
                break;
        }

        // Make sure that we set the divide hint active flag properly. We do
        // this on exiting as the value set when processing the previous
        // node is used in the body of this method.
        if (parentAnnotation != null) {
            if (logger.isDebugEnabled()) {
                if (activateDivideHint) {
                    logger.debug("Activating divide hint in " + parent);
                } else if (parentAnnotation.isDivideHintActive()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Deactivating divide hint in " + parent);
                    }
                }
            }
            parentAnnotation.setDivideHintActive(activateDivideHint);
        }

        return result;
    }

    /**
     * Check to see whether this node is actually a divide hint element.
     */
    protected boolean isDivideHintElement() {
        return false;
    }

    /**
     * Check to see whether this node is actually a dissection hint element.
     */
    protected boolean isKeepTogetherElement() {
        return false;
    }

    /**
     * Classes should implement this to add node specific marking of nodes
     * associated with a shard.
     *
     * @param shardNumber The number of the shard currently being marked.
     * @param limit       The amount of available space remaining in the shard.
     * @return See markShardNodes.
     */
    protected abstract int markShardNodesImpl(int shardNumber, int limit)
            throws ProtocolException;

    /**
     * Generate the contents of the whole tree. This is for debug purposes
     * only.
     *
     * @param buffer The buffer onto which the contents should be appended.
     */
    protected abstract void generateContents(ReusableStringBuffer buffer);

    /**
     * Generate the fixed contents of the whole tree. This is for debug purposes
     * only.
     *
     * @param buffer The buffer onto which the fixed contents should be
     *               appended.
     */
    public abstract void generateFixedContents(ReusableStringBuffer buffer);

    /**
     * Generate the dissected contents. This includes all the fixed contents
     * and the current shard contents for each dissectable tree. This method
     * is not called for nodes in a dissectable tree.
     *
     * @param buffer The buffer onto which the contents should be appended.
     */
    public abstract void generateDissectedContents(ReusableStringBuffer buffer)
            throws ProtocolException;

    /**
     * Generate the contents of the specified shard. This is only called for
     * nodes in a dissectable tree.
     *
     * @param buffer      The buffer onto which the contents should be appended.
     * @param shardNumber The number of the shard currently being generated.
     * @return True if the whole shard has been generated, false otherwise.
     */
    protected final boolean generateShardContents(
            ReusableStringBuffer buffer,
            int shardNumber,
            boolean all) {

        if (logger.isDebugEnabled()) {
            logger.debug("Should " + this + " which belongs to shards "
                    + firstShard + " to " + lastShard
                    + " generate output for shard " + shardNumber);
        }

        // Always include a node if it is part of a sub tree which has all been
        // added to the same shard.
        if (all) {
            if (logger.isDebugEnabled()) {
                logger.debug(this
                        + " is part of a sub tree"
                        + " which was added to shard "
                        + shardNumber + " in entirety");
            }
        } else {

            // If this node has never been visited then we have finished as it
            // is the first node of the next shard.
            if (firstShard == -1 || lastShard == -1) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Node " + this + " has not yet been added"
                            + " to any shard so it must be after shard "
                            + shardNumber);
                }

                return true;
            }

            // If the shard being generated precedes the first shard that this
            // node belongs to then we have finished.
            if (shardNumber < firstShard) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Shard " + shardNumber
                            + " is before the first shard "
                            + firstShard + " which "
                            + this + " belongs to");
                }

                return true;
            }

            // If the shard being generated is after the last shard that this
            // nodes belongs to then ignore the sub tree rooted at this node but
            // keep searching for the first node which is part of this shard.
            if (shardNumber > lastShard) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Shard " + shardNumber
                            + " is after last shard "
                            + lastShard + " which "
                            + this + " belongs to");
                }

                return false;
            }

            // If the whole sub tree rooted at this node was added to the shard
            // then the children haven't actually been visited so we need to pass
            // an extra flag to indicate that all nodes have to be added.
            if (completed && firstShard == lastShard) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Node " + this + " is the root of a whole"
                            + " sub tree which was added to shard "
                            + shardNumber);
                }
                all = true;
            }
        }

        return generateShardContentsImpl(buffer, shardNumber, all);
    }

    /**
     * Classes should implement this to add node specific generation of shard
     * contents.
     *
     * @param shardNumber The number of the shard currently being marked.
     * @return True if the whole shard has been generated, false otherwise.
     */
    protected abstract boolean generateShardContentsImpl(
            ReusableStringBuffer buffer,
            int shardNumber,
            boolean all);

    /**
     * Return a debug representation of the contents of the tree from this
     * point downwards.
     *
     * @return The debug tree.
     */
    public String getDebugOutput() {
        ReusableStringBuffer output = new ReusableStringBuffer("\n");
        generateDebugOutput(output, "");
        return output.toString();
    }

    /**
     * Append debug representation of the contents of this node and its
     * children to the buffer.
     *
     * @param buffer The buffer to append to.
     * @param indent The indent to use for all lines added by this node.
     */
    protected abstract void generateDebugOutput(
            ReusableStringBuffer buffer,
            String indent);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
