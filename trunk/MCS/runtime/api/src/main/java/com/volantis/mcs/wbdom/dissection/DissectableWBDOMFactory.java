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
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.wbdom.CodedNameElement;
import com.volantis.mcs.wbdom.DefaultWBDOMFactory;
import com.volantis.mcs.wbdom.LiteralNameElement;
import com.volantis.mcs.wbdom.WBDOMText;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * An implementation of {@link com.volantis.mcs.wbdom.WBDOMFactory} 
 * which creates a "dissectable" WBDOM.
 * <p>
 * This also adds a method to create the special dissection elements.
 */ 
public class DissectableWBDOMFactory extends DefaultWBDOMFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DissectableWBDOMFactory.class);

    private AtomicElementConfiguration atomicConf;

    public DissectableWBDOMFactory(AtomicElementConfiguration atomicConf) {
        this.atomicConf = atomicConf;
    }

    // Inherit Javadoc.
    public CodedNameElement createCodeElement(ElementNameCode code) {
        // Calculate atomicness of element up front, since it is static.
        boolean atomic = atomicConf.isElementAtomic(code.getInteger());
        if (logger.isDebugEnabled()) {
            if (atomic) {
                logger.debug("Marking element '" + code + "' as atomic");
            }
        }
        return new DissectableCodeElement(code, atomic);
    }

    // Inherit Javadoc.
    public LiteralNameElement createLiteralElement(StringReference reference) {
        // Literal elements are never atomic (well, for now at least).
        return new DissectableLiteralElement(reference);
    }
    
    /**
     * Create a special dissection element, with the element type and 
     * annotation provided.
     * 
     * @param type the dissector's element type for this element; this 
     *      indicates what type the dissector considers this element to be.
     * @param annotation the dissector's annotation for this element; this 
     *      contains all the state the dissector needs for this element.
     * @return the created special dissection element.
     */ 
    public DissectableSpecialElement createDissectionElement(ElementType type, 
            NodeAnnotation annotation) {
        return new DissectableSpecialElement(type, annotation);
    }

    // Inherit Javadoc.
    public WBDOMText createText() {
        return new DissectableWBDOMText();
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

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/3	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
