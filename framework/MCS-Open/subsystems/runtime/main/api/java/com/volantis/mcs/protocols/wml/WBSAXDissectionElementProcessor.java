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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.mcs.dom2wbsax.WBSAXElementProcessor;
import com.volantis.mcs.dom2wbsax.WBSAXProcessorContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.wbdom.dissection.SpecialOpaqueElementStart;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Element Processor which handles the special Dissection elements which
 * are present in the MCSDOM.
 * <p/>
 * It translates these from normal MCSDOM elements with strange names and a
 * dissection annotation in their <code>object</code> property, into WBDOM
 * elements with <code>OpaqueElementStarts</code> which contain the
 * dissection annotation.
 */
public class WBSAXDissectionElementProcessor extends WBSAXElementProcessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    WBSAXDissectionElementProcessor.class);

    private final ElementType type;

    public WBSAXDissectionElementProcessor(
            WBSAXProcessorContext context, ElementType type) {
        super(context);
        this.type = type;
    }

    public void elementStart(Element element, boolean content)
            throws WBSAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding dissection element " + element.getName());
        }
        NodeAnnotation attrs = (NodeAnnotation) element.getAnnotation();
        SpecialOpaqueElementStart opaqueElementStart =
                new SpecialOpaqueElementStart(type, attrs);
        context.getContentHandler().startElement(opaqueElementStart, content);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-Mar-05	7357/2	pcameron	VBM:2005030906 Fixed node annotation for dissection

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
