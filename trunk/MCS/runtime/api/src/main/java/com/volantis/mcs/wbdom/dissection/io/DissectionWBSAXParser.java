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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-May-03    steve      		VBM:2003042917   Created.
 * 30-May-03    Mat             VBM:2003042906 - Call pushElement from 
 *                              Element()
 * 02-Jun-03    Mat             VBM:2003042906 - Call end element from 
 *                              startElement() if there are no contents.
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.WBDOMFactory;
import com.volantis.mcs.wbdom.dissection.DissectableWBDOMFactory;
import com.volantis.mcs.wbdom.dissection.SpecialOpaqueElementStart;
import com.volantis.mcs.wbdom.io.WBSAXParser;
import com.volantis.mcs.wbdom.io.SerialisationConfiguration;
import com.volantis.mcs.wbsax.OpaqueElementStart;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * A {@link WBSAXParser} which adds the ability to create WBDOM special
 * dissection elements from WBSAX opaque element starts.  
 */
public class DissectionWBSAXParser extends WBSAXParser {

    /**
     * Create a WBSAXDissectionParser
     * @param factory for creation of dissection elements, etc 
     */
    public DissectionWBSAXParser(DissectableWBDOMFactory factory, 
            SerialisationConfiguration configuration) {
        super(factory, configuration);
    }
    
    // Inherit Javadoc.
    public void startElement(OpaqueElementStart element, boolean content)
        throws WBSAXException {
        SpecialOpaqueElementStart specialStart = 
                (SpecialOpaqueElementStart) element;
        // Create a new element
        WBDOMElement specialElement = ((DissectableWBDOMFactory) factory).
                createDissectionElement(specialStart.getType(), 
                        specialStart.getAnnotation());
        pushElement(specialElement);
        // If there is no content - fake an end element.
        if (!content) {
            endElement();
        }
    }
    
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 12-Jun-03	388/1	mat	VBM:2003061101 Improve WMLC debugging and tidy up WMLRoot

 ===========================================================================
*/
