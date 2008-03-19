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
import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This class represents the cost of a part of a document.
 * <p>
 * It contains both the total cost and also the context
 * ({@link com.volantis.mcs.dissection.SharedContentUsages}) within which any calculations must be
 * performed.
 * <p>
 * It has two modes of operation depending on the value of the update flag
 * that is specified when creating it.
 * <p>
 * If update is true then the {@link com.volantis.mcs.dissection.SharedContentUsages} will be updated
 * with any shared references that are used while calculating the cost.
 * <p>
 * It it is false then the {@link com.volantis.mcs.dissection.SharedContentUsages} will not be updated.
 * Instead if the calculations find any references to shared content that has
 * not yet been used within the context of the {@link com.volantis.mcs.dissection.SharedContentUsages} then
 * the cost will be {@link #VARIABLE} and use the arithmetic rules defined in
 * {@link #add}.
 *
 * @see #add
 * @see DissectableDocument#addElementOverhead
 * @see DissectableDocument#addTextCost
 * @see com.volantis.mcs.dissection.SharedContentUsages
 */
public final class Cost
    implements Accumulator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(Cost.class);

    /**
     * This is used to indicate that the cost is dependent on the context.
     */
    //public static final int VARIABLE = DissectableDocument.VARIABLE_COST;

    /**
     * This reoresents an infinite cost that will not fit on any device.
     */
    public static final int INFINITE = Integer.MAX_VALUE;

    /**
     * The context within which the calculations are performed.
     */
    private final SharedContentUsages sharedContentUsages;

    /**
     * Controls whether the sharedContentUsages is updated while calculating.
     */
    private final boolean update;

    /**
     * A description of what this cost is for.
     */
    private String description;

    /**
     * The total cost.
     */
    private int total;

    public Cost(String description,
                SharedContentUsages usages,
                boolean update) {
        this.description = description;
        this.sharedContentUsages = usages;
        this.update = update;
    }

    /**
     * Initialise this object.
     * <p>
     * This resets the total back to 0.
     */
    public void resetTotal() {
        total = 0;
    }

    public int saveTotal(int newTotal) {
        return swapTotal(newTotal);
    }

    public int restoreTotal(int oldTotal) {
        return swapTotal(oldTotal);
    }

    /**
     * Replaces the current total with the specified total.
     * @param total The new total.
     * @return The current total before swapping.
     */
    private int swapTotal(int total) {
        int swapTotal = this.total;
        this.total = total;
        return swapTotal;
    }

    /**
     * Add two costs together.
     * <p>
     * Costs associated with elements during the annotation phase are more 
     * than just simple numbers.
     * <p>
     * The cost of an object can be infinite in order to force dissection to 
     * occur.
     * <p>
     * The cost of an object can be variable if it contains references to as 
     * yet unused shared content.
     * <p>
     * The cost of an object can also just be a simple number.
     * <p>
     * The rules for adding costs together are as follows and are commutative,
     * i.e. a + b = b + a:
     * <ol>
     *   <li>{@link Cost#INFINITE} plus anything is {@link Cost#INFINITE}.
     *   <li>{@link Cost#VARIABLE} plus a number is {@link Cost#VARIABLE}.
     *   <li>Number plus number is a number.
     * </ol>
     * As can be seen there is a precedence order between the values:
     * <pre>
     *     {@link Cost#INFINITE} > {@link Cost#VARIABLE} > number
     * </pre>
     * <p>
     * 
     * @param c1 the first cost
     * @param c2 the second cost
     * @return the costs added together, as described above.
     */ 
    public static int add(int c1, int c2) {
        if (c1 == INFINITE || c2 == INFINITE) {
            return INFINITE;
        }

        if (c1 == VARIABLE || c2 == VARIABLE) {
            return VARIABLE;
        }

        return c1 + c2;
    }
    
    public String saveDescription(String newDescription) {
        return swapDescription(newDescription);
    }

    public String restoreDescription(String oldDescription) {
        return swapDescription(oldDescription);
    }

    private String swapDescription(String description) {
        String swapDescription = this.description;
        this.description = description;
        return swapDescription;
    }

    public int getTotal() {
        return total;
    }

    public SharedContentUsages getSharedContentUsages() {
        return sharedContentUsages;
    }

    public void addDocumentOverhead(DissectableDocument document)
        throws DissectionException {

        // Calculate the individual cost of the document overhead (so we may
        // log it), and then add it to the running total. 
        int oldTotal = saveTotal(0);
        document.addDocumentOverhead(this);
        int newTotal = add(oldTotal, total);
        int overhead = restoreTotal(newTotal);
        
        if (logger.isDebugEnabled()) {
            logger.debug("(" + description + ")"
                         + " Document Overhead: " + overhead
                         + " Total: " + getTotalDescription());
        }
    }

    public void addElementOverhead(DissectableDocument document,
                                   DissectableElement element)
        throws DissectionException {

        // Calculate the individual cost of the element overhead (so we may
        // log it), and then add it to the running total. 
        int oldTotal = saveTotal(0);
        document.addElementOverhead(element, this);
        int newTotal = add(oldTotal, total);
        int overhead = restoreTotal(newTotal);

        if (logger.isDebugEnabled()) {
            logElementOverhead(document, element, overhead);
        }
    }

    public void addElementOverhead(DissectableDocument document,
                                   DissectableElement element,
                                   int overhead)
        throws DissectionException {

        add(overhead);

        if (logger.isDebugEnabled()) {
            logElementOverhead(document, element, overhead);
        }
    }

    public void addElementCost(DissectableDocument document,
                               DissectableElement element,
                               int cost)
        throws DissectionException {

        add(cost);
    }

    private void logElementOverhead(DissectableDocument document,
                                    DissectableElement element,
                                    int overhead)
        throws DissectionException {

        // todo: This check is only here to get past the commit check that all
        // todo: logger.debug calls are wrapped in an
        // todo:     if (logger.isDebugEnabled()) ...
        // todo: check. Really the parser should be made a little cleverer so
        // todo: that we do not have to do this.
        if (logger.isDebugEnabled()) {
            logger.debug("(" + description + ")"
                         + " Element: "
                         + crop(document.getOpenElementDescription(element), 20)
                         + " Overhead: " + overhead
                         + " Total: " + getTotalDescription());
        }
    }

    public void addShardLinkCost(DissectableDocument document,
                                 DissectableElement element,
                                 ShardLinkDetails shardLinkDetails)
        throws DissectionException {

        // Calculate the individual cost of the shard link (so we may log it), 
        // and then add it to the running total. 
        int oldTotal = saveTotal(0);
        document.addShardLinkCost(element, this, shardLinkDetails);
        int newTotal = add(oldTotal, total);
        int overhead = restoreTotal(newTotal);

        if (logger.isDebugEnabled()) {
            logger.debug("(" + description + ")"
                         + " Element: "
                         + crop(document.getOpenElementDescription(element),
                                20)
                         + " Overhead: " + overhead
                         + " Total: " + getTotalDescription());
        }
    }

    public void addTextCost(DissectableDocument document,
                            DissectableText text)
        throws DissectionException {

        document.addTextCost(text, this);
        /*
                if (logger.isDebugEnabled()) {
                    logTextCost(document, text);
                }
        */
    }

    public void addTextCost(DissectableDocument document,
                            DissectableText text,
                            int cost)
        throws DissectionException {

        add(cost);

        /*
                if (logger.isDebugEnabled()) {
                    logTextCost(document, text, cost);
                }
        */
    }

    private void logTextCost(DissectableDocument document,
                             DissectableText text,
                             int cost)
        throws DissectionException {

        // todo: This check is only here to get past the commit check that all
        // todo: logger.debug calls are wrapped in an
        // todo:     if (logger.isDebugEnabled()) ...
        // todo: check. Really the parser should be made a little cleverer so
        // todo: that we do not have to do this.
        if (logger.isDebugEnabled()) {
            logger.debug("(" + description + ")"
                         + " Text: "
                         + crop(document.getTextDescription(text), 20)
                         + " Cost: " + cost
                         + " Total: " + getTotalDescription());
        }
    }

    public static String crop(String string, int length) {
        if (string.length() > length) {
            string = string.substring(0, length - 3) + "...";
        }
        return string;
    }

    private String getTotalDescription() {
        if (total == VARIABLE) {
            return "VARIABLE";
        } else if (total == INFINITE) {
            return "INFINITE";
        } else {
            return Integer.toString(total);
        }
    }

    // =========================================================================
    //   Accumulator Methods
    // =========================================================================

    public int add(int cost) {
        total = add(total, cost);
        return total;
    }

    public int addShared(int sharedContentIndex,
                         int referenceCost, int contentCost) {

        // If the total is currently variable or infinite then return
        // immediately as that is not going to change.
        if (total == VARIABLE || total == INFINITE) {
            return total;
        }

        // Shared content cost always includes the reference cost.
        total = add(total, referenceCost);

        // If the shared content has not been accounted for then account for it
        // now.
        if (!sharedContentUsages.isSharedContentUsed(sharedContentIndex)) {
            // If the usages can be updated then do so, otherwise the cost is
            // variable.
            if (update) {
                sharedContentUsages.addSharedContentUsage(sharedContentIndex);
                total = add(total, contentCost);
            } else {
                total = VARIABLE;
            }
        }

        return total;
    }

    public boolean isCalculationFinished() {
        return total == INFINITE || total == VARIABLE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 09-Sep-03	1336/3	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC (more documentation)

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 10-Jun-03	363/3	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
