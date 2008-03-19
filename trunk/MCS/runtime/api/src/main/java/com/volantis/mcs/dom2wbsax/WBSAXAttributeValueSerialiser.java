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
 * 30-May-03    Mat             VBM:2003042911 - Created to add attribute
 *                              values to the content handler.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2wbsax;

import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * A value serialiser for attribute content.
 */
public class WBSAXAttributeValueSerialiser extends WBSAXValueSerialiser {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Construct an instance of this class using the shared context provided.
     * 
     * @param context the shared processor context to use.
     */ 
    public WBSAXAttributeValueSerialiser(WBSAXProcessorContext context) {
        super(context);
    }

    // Inherit Javadoc.
    public void addString(char[] chars, int offset, int length)
            throws WBSAXException {

        context.getContentHandler().addAttributeValue(
                context.getStrings().create(chars, offset, length));

    }

    // Inherit Javadoc.
    public void addEntity(char c)
            throws WBSAXException {

        context.getContentHandler().addAttributeValueEntity(new EntityCode(c));
        
    }

    // Inherit Javadoc.
    public void addExtensionString(Extension code, char[] chars, int offset,
            int length) throws WBSAXException {

        context.getContentHandler().addAttributeValueExtension(code,
                context.getStrings().create(chars, offset, length));

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Oct-03	1469/5	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 ===========================================================================
*/
