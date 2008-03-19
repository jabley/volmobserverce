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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.Locator;

/**
 * This class encapsulates the fields used by all MarlinContentHandlers. This
 * class should be distinguished from the PAPIContentHandlerContext that is used
 * to share extra information between concrete subclasses of
 * AbstractPAPIContentHandler.
 */
public class MarlinContentHandlerContext {

    /**
     * This controls whether SAX2 events must be ignored or not, it should
     * never be negative.
     * <p>
     * If this has a value of 0 then SAX2 events must not be ignored.
     * <p>
     * If this has any other value then SAX2 events must be ignored.
     * <p>
     * The startElement modifies this value as follows.
     * <p>
     * If it is greater than 0 then startElement increments it and returns
     * immediately.
     * <p>
     * If a PAPIElement asks to skip its element body then this is set to 1 and
     * startElement returns immediately.
     * <p>
     * The endElement modifies this value as follows.
     * <p>
     * If it is greater than 0 then endElement decrements it. If it is still
     * greater than 0 then endElement returns immediately.
     */
    private int ignoreDepth;

    /**
     * If set to true this flag causes all processing to stop.
     */
    private boolean abort;

    /**
     * The locator to use when reporting errors.
     */
    private Locator locator;

    /**
     * Reference to a FlowControlManager that can be used to suppress 
     * SAX events at the root of a pipeline. This will be null if not running
     * in a pipeline  
     */ 
    private FlowControlManager flowControlManager;

    /**
     * The initial request context.
     */
    private MarinerRequestContext initialRequestContext;

    /**
     * Set the value of the ignoreDepth.
     * @param ignoreDepth
     */
    public void setIgnoreDepth(int ignoreDepth) {
        this.ignoreDepth = ignoreDepth;
    }

    /**
     * Get the current value of the ignoreDepth 
     * @return int
     */
    public int getIgnoreDepth() {
        return ignoreDepth;
    }
    
    /**
     * Increase the ignoreDepth value by one.
     */
    public void increaseIgnoreDepth() {
        ignoreDepth++;
    }

    /**
     * Decrease the ignoreDepth value by one.
     */
    public void decreaseIgnoreDepth() {
        ignoreDepth--;
    }
    
    /**
     * Set the abort property
     * @param abort a boolean
     */
    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    /**
     * Returns the abort property 
     * @return boolean
     */
    public boolean isAbort() {
        return abort;
    }

    /**
     * Sets the document locator
     * @param locator
     */
    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * Gets the document locator
     * @return Locator
     */
    public Locator getLocator() {
        return locator;
    }

    /**
     * Sets the FlowControlManager
     * @param flowControlManager
     */
    public void setFlowControlManager(FlowControlManager flowControlManager) {
        this.flowControlManager = flowControlManager;
    }

    /**
     * Gets the FlowControlManager
     * @return FlowControlManger
     */
    public FlowControlManager getFlowControlManager() {
        return flowControlManager;
    }

    /**
     * Sets the initial MarinerRequestContext
     * @param context MarinerRequestContext
     */
    public void setInitialRequestContext(MarinerRequestContext context) {
        initialRequestContext = context;
    }

    /**
     * Gets the initial MarinerRequestContext
     * @return MarinerRequestContext
     */
    public MarinerRequestContext getInitialRequestContext() {
        return initialRequestContext;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1253/1	doug	VBM:2003082202 Restructured MarlinContentHandler class hierarchy

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 ===========================================================================
*/
