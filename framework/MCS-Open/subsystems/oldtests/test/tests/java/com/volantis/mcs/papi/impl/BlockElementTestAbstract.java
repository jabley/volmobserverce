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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/BlockElementTestAbstract.java,v 1.2 2003/04/24 16:42:23 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Adrian          VBM:2003040903 - Added this class as part of 
 *                              the PAPIElement testing heirarchy. Did not 
 *                              implement any of the methods yet due to time 
 *                              constraints. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;

/**
 * This class tests the internal methods of BlockElement.
 * <p>
 * Note that no implementation has yet been included due to time constraints.
 */
public abstract class BlockElementTestAbstract extends 
        AbstractElementImplTestAbstract {

    /* (non-Javadoc)
     * @see com.volantis.mcs.papi.PAPIElementTestAbstract#createTestablePAPIElement()
     */
    protected PAPIElement createTestablePAPIElement() {
        return new MyBlockElement();
    }


    /**
     * Returns a test implementation of the BlockAttributes
     * @return com.volantis.mcs.papi.BlockAttributes
     */
    protected abstract com.volantis.mcs.papi.BlockAttributes createTestableBlockAttributes();        

    /**
     * Test implementation of abstract class BlockElement 
     */
    public class MyBlockElement extends BlockElementImpl implements PAPIElement {

        /* (non-Javadoc)
         * @see com.volantis.mcs.papi.BlockElement#elementStartImpl(com.volantis.mcs.context.MarinerRequestContext, com.volantis.mcs.papi.BlockAttributes)
         */
        protected int elementStartImpl(MarinerRequestContext context,
                                       BlockAttributes blockAttributes)
                                       throws PAPIException {
            return PROCESS_ELEMENT_BODY;
        }

        /* (non-Javadoc)
         * @see com.volantis.mcs.papi.BlockElement#elementEndImpl(com.volantis.mcs.context.MarinerRequestContext, com.volantis.mcs.papi.BlockAttributes)
         */
        protected int elementEndImpl(MarinerRequestContext context,
                                     BlockAttributes blockAttributes)
                                     throws PAPIException {
            return CONTINUE_PROCESSING;
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
