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
 * 02-Jun-03    Geoff           VBM:2003042906 - Commit some logging changes
 *                              that Paul made.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.annotation;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableIterator;
import com.volantis.mcs.dissection.dom.DissectableNode;
import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

public class ElementAnnotation
    extends DissectableNodeAnnotation {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ElementAnnotation.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(ElementAnnotation.class);

    // =========================================================================
    //   Constant Fields
    //     The fields in this section are all set during the annotation process
    //     and once set are not changed. This means that they can be accessed
    //     without worrying about synchronization.
    // =========================================================================

    /**
     * If true then this element is atomic and must not be dissected.
     */
    private boolean atomic;

    /**
     * The overhead costs associated with this element. This is basically the
     * cost of the open and close tags or their equivalent.
     * <p>
     * If this is set to {@link Cost#VARIABLE} then it means that the overhead
     * is variable, otherwise is must be greater than or equal to 0.
     */
    private int overhead;

    /**
     * The element that this object relates to.
     */
    private DissectableElement element;

    /**
     * Set to true if the element should generate any markup and false
     * otherwise.
     */
    private boolean generateMarkup;

    // =========================================================================
    //   Selection Specific Fields
    //     The fields in this section are used solely by the selection process
    //     and must not be accessed during generation.
    // =========================================================================

    /**
     * The index of the first incomplete child.
     */
    private int firstIncompleteChild;

    /**
     * This object is used to cache the iterator that was returned from the
     * {@link com.volantis.mcs.dissection.dom.DissectableDocument#childrenIterator} method.
     */
    private DissectableIterator elementIterator;

    // =========================================================================
    //   End Of Fields
    // =========================================================================

    public void setAtomic(boolean atomic) {
        this.atomic = atomic;

        // If this element is atomic then we must check the cost.
        if (atomic) {
            setMustCheckCost(true);
        }
    }

    public void setGenerateMarkup(boolean generateMarkup) {
        this.generateMarkup = generateMarkup;
    }

    public boolean getGenerateMarkup() {
        return generateMarkup;
    }

    public void setElement(DissectableElement element) {
        if (this.element != null) {
            throw new IllegalStateException("Element already set");
        }
        this.element = element;
    }

    public DissectableElement getElement() {
        return element;
    }

    protected int dissectNode(Shard shard, boolean mustDissect)
        throws DissectionException {

        // Atomic elements cannot be dissected into anything smaller.
        if (atomic) {
            if (logger.isDebugEnabled()) {
                logger.debug(document.getElementDescription(element)
                             + " cannot be broken down");
            }
            return NODE_CANNOT_FIT;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to break " + this
                             + " into smaller pieces for shard "
                             + shard);
            }
        }

        int overhead = getOverhead();

        return selectContents(shard, overhead);
    }

    protected int selectVariableShardContentsImpl(Shard shard)
        throws DissectionException {

        Cost cost = shard.getTemporaryShardCost();
        addOverhead(cost);
        int overhead = cost.getTotal();

        return selectContents(shard, overhead);
    }

    protected int selectContents(Shard shard, int overhead)
        throws DissectionException {

        int availableSpace = shard.getAvailableSpace();

        if (overhead > availableSpace) {
            if (logger.isDebugEnabled()) {
                logger.debug("Overhead for " + this + " of " + overhead
                             + " is greater than the available space "
                             + availableSpace + " left in shard " + shard);
            }

            return NODE_CANNOT_FIT;
        }

        // We should never get here with atomic element.
        if (atomic) {
            throw new IllegalStateException("Internal Error");
        }

        // Reduce the available space by the overhead of this element.
        availableSpace = shard.decrementAvailableSpace(overhead);

        if (logger.isDebugEnabled()) {
            logger.debug("Overhead for " + this + " is " + overhead
                         + " space remaining is " + availableSpace
                         + " in shard " + shard);
        }

        // Keep track of how many child nodes have been added to this shard.
        int childNodesAdded = 0;

        if (logger.isDebugEnabled()) {
            logger.debug("First incomplete child is " + firstIncompleteChild);
        }

        // Iterate over the children of the element starting with the first
        // one that has not yet been completed.
        elementIterator = document.childrenIterator(element, elementIterator,
                                                    firstIncompleteChild);

        int childIndex = firstIncompleteChild;
        for (; elementIterator.hasNext();) {
            DissectableNode child = elementIterator.next();
            DissectableNodeAnnotation annotation
                = (DissectableNodeAnnotation) document.getAnnotation(child);
            NodeDissector dissector = annotation.getNodeDissector();

            if (logger.isDebugEnabled()) {
                logger.debug("Checking child " + childIndex + " of " + this);
            }
            int result = dissector.selectShardContents(shard);

            switch (result) {
                case NODE_CANNOT_FIT:
                    // If some of the previous children fitted into the shard
                    // then return SHARD_COMPLETE, otherwise return
                    // NODE_CANNOT_FIT.
                    if (childNodesAdded == 0) {
                        // No children nodes have been added to the shard.
                        if (logger.isDebugEnabled()) {
                            logger.debug("No children of " + this
                                         + " have been added to shard "
                                         + shard);
                        }
                        return NODE_CANNOT_FIT;
                    } else {
                        // Some children nodes have been added to the shard.
                        if (logger.isDebugEnabled()) {
                            logger.debug("Some children of " + this
                                         + " have been added to shard "
                                         + shard);
                        }
                        return SHARD_COMPLETE;
                    }

                case SHARD_COMPLETE:
                    // The shard has been completed so return straight away.
                    if (logger.isDebugEnabled()) {
                        logger.debug("A descendant of this " + this
                                     + " was the last node to be"
                                     + " added to shard "
                                     + shard);
                    }
                    return SHARD_COMPLETE;

                case ADDED_NODE:
                    // The child could fit in the shard and so the available
                    // space may have been reduced.
                    availableSpace = shard.getAvailableSpace();

                    // Keep track of how many child nodes have been added.
                    childNodesAdded += 1;

                    // Remember that this child has been completed so we do not
                    // visit it again.
                    firstIncompleteChild += 1;

                    // Break out of the switch and loop back round to the
                    // next child.
                    break;

                default:
                    throw new IllegalStateException("Unknown value " + result);
            }

            childIndex += 1;
        }

        // If we get here then it means that the last child has been
        // added to this shard.
        if (logger.isDebugEnabled()) {
            logger.debug("Last child of " + this
                         + " was added to shard "
                         + shard);
        }

        return ADDED_NODE;
    }

    protected void addCost(Cost cost)
        throws DissectionException {

        // Iterate over all the children of the element.
        addOverhead(cost);
        elementIterator = document.childrenIterator(element, elementIterator);
        for (; elementIterator.hasNext();) {
            DissectableNode child = elementIterator.next();
            DissectableNodeAnnotation annotation
                = (DissectableNodeAnnotation) document.getAnnotation(child);
            annotation.addCost(cost);
        }
    }

    /**
     * Check to see whether the overhead of this node is variable.
     * @return True if it is variable and false otherwise.
     */
    protected final boolean overheadIsVariable() {
        // The overhead is variable if it is set to VARIABLE_COST.
        return (overhead == Cost.VARIABLE);
    }

    /**
     * Set the overhead associated with this node.
     * @param overhead The overhead associated with this node. This must be set to
     * {@link Cost#VARIABLE} if the overhead of the node is variable.
     */
    public final void setOverhead(int overhead) {
        this.overhead = overhead;
    }

    /**
     * Get the overhead associated with this node.
     * @return A non negative integer that is the overhead of this node.
     * @throws com.volantis.mcs.dissection.DissectionException If the overhead is variable.
     */
    public final int getOverhead()
        throws DissectionException {

        // If the overhead is variable then throw an exception, this is to protect
        // against accidentally doing arithmetic with negative overheads which could
        // cause all sorts of problems.
        if (overheadIsVariable()) {
            throw new DissectionException(
                        exceptionLocalizer.format("overhead-variable-missing"));
        }

        return overhead;
    }

    /**
     * Add the overhead of this element to the specified cost.
     * @param cost The Cost into which the overhead is added.
     */
    public final void addOverhead(Cost cost)
        throws DissectionException {

        if (!overheadIsVariable()) {
            cost.add(getOverhead());
        } else {
            cost.addElementOverhead(document, element);
        }
    }

    /**
     * Add all the children of this element to the specified shard and mark
     * them as complete.
     * @param shard The shard to which all the children should belong.
     */
    protected void addPartsToShard(Shard shard) {
        elementIterator = document.childrenIterator(element, elementIterator);
        for (; elementIterator.hasNext();) {
            DissectableNode child = elementIterator.next();
            DissectableNodeAnnotation annotation
                = (DissectableNodeAnnotation) document.getAnnotation(child);
            annotation.addToShard(shard, true);
        }
    }

    public void addState(StringBuffer buffer) {
        super.addState(buffer);

        buffer.append("^");
        if (overhead == Cost.VARIABLE) {
            buffer.append("VARIABLE");
        } else if (overhead == Cost.INFINITE) {
            buffer.append("INFINITE");
        } else {
            buffer.append(overhead);
        }
        buffer.append(" ");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 12-Jun-03	363/3	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/5	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/3	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
