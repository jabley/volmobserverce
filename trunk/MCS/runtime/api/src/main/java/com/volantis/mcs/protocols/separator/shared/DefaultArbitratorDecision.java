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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.separator.shared;

import com.volantis.mcs.protocols.separator.ArbitratorDecision;
import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

public class DefaultArbitratorDecision implements ArbitratorDecision {

    private SeparatedContent previousContent;

    private SeparatedContent triggeringContent;

    private SeparatorRenderer deferredSeparator;

    private SeparatorRenderer triggeringSeparator;

    /**
     * The renderer chosen by the arbitrator, if null then the renderer was
     * ignored.
     */
    private SeparatorRenderer chosenRenderer;

    /**
     * Indicates whether the decision was deferred.
     *
     * <p>If true then it indicates that decision as to whether the chosen
     * renderer should be used has been deferred.</p>
     */
    private boolean decisionDeferred;

    /**
     * Indicates whether deferral is allowed.
     *
     * <p>If false then the arbitrator must not defer the decision.</p>
     */
    private boolean allowDeferral;

    /**
     * Initialise this instance.
     */
    public DefaultArbitratorDecision() {
        allowDeferral = true;
    }

    public SeparatedContent getPreviousContent() {
        return previousContent;
    }

    public void setPreviousContent(SeparatedContent previousContent) {
        this.previousContent = previousContent;
    }

    public SeparatedContent getTriggeringContent() {
        return triggeringContent;
    }

    public void setTriggeringContent(SeparatedContent triggeringContent) {
        this.triggeringContent = triggeringContent;
    }

    public SeparatorRenderer getDeferredSeparator() {
        return deferredSeparator;
    }

    public void setDeferredSeparator(SeparatorRenderer deferredSeparator) {
        this.deferredSeparator = deferredSeparator;
    }

    public SeparatorRenderer getTriggeringSeparator() {
        return triggeringSeparator;
    }

    public void setTriggeringSeparator(SeparatorRenderer triggeringSeparator) {
        this.triggeringSeparator = triggeringSeparator;
    }

    // Javadoc inherited.
    public void use(SeparatorRenderer separator) {
        chosenRenderer = separator;
        decisionDeferred = false;
    }

    // Javadoc inherited.
    public void defer(SeparatorRenderer separator) {
        if (!allowDeferral) {
            throw new IllegalStateException("Deferral not allowed");
        }

        chosenRenderer = separator;
        decisionDeferred = true;
    }

    // Javadoc inherited.
    public void ignore() {
        chosenRenderer = null;
        decisionDeferred = false;
    }

    /**
     * Get the separator that the arbitrator chose.
     * @return The separator that the arbitrator chose, may be
     */
    SeparatorRenderer getChosenSeparator() {
        return chosenRenderer;
    }

    /**
     * Return whether the decision was deferred.
     * @return True if the decision was deferred and false if it was not.
     */
    boolean isDecisionDeferred() {
        return decisionDeferred;
    }

    /**
     * Controls whether a deferral decision is allowed.
     * @param allowDeferral If true then deferral is allowed, otherwise it is
     * not allowed and will fail.
     */
    void setAllowDeferral(boolean allowDeferral) {
        this.allowDeferral = allowDeferral;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/4	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3610/3	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
