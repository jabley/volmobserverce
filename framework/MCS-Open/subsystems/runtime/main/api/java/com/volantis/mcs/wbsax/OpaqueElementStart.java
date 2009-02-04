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
 * 30-May-03    Mat             VBM:2003042911 - Updated Javadoc
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents the start of an opaque element in a WBSAX event stream.
 * <p>
 * WBXML does not define an opaque element start, but we thought such a 
 * construct has would be required for WBDOM so we have defined it in WBSAX.
 * Unfortunately we were wrong and it wasn't very useful! I suspect this 
 * class will disappear when we clean up the optimiser a bit more.
 * <p>
 * An opaque element start includes the name of the element, and all of it's 
 * attributes. So, a single call to 
 * {@link WBSAXContentHandler#startElement(OpaqueElementStart, boolean)} is 
 * equivalent to:
 * <ul>
 *   <li>{@link WBSAXContentHandler#startElement}
 *   <li>{@link WBSAXContentHandler#startAttributes}
 *   <li>...
 *   <li>{@link WBSAXContentHandler#endAttributes}
 * </ul>
 * 
 * @todo see if we can really get rid of this class when we fix optimiser
 */ 
public interface OpaqueElementStart {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Return the content of this opaque element start, as bytes. 
     * <p>
     * This must include the name and any attribute values required.
     * 
     * @return The bytes
     */
    byte[] getBytes();
    
    /**
     * Return the name of this opaque element start. 
     * 
     * @return The name.
     */
    String getName();
    
    // Hmm. No way to obtain the full string representation of this class.
    // Yet another reason to get rid of it :-).
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/5	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/5	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/3	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
