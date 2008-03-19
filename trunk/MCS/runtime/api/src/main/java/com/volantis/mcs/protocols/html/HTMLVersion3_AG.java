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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLVersion3_AG.java,v 1.6 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Jul-01    Paul            VBM:2001070507 - Added this change history,
 *                              and reimplemented menus to match changes in
 *                              VolantisProtocol.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Stopped calling the deprecated
 *                              doMeta method.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 22-Jul-02    Ian             VBM:2002052804 - Removed all references to old
 *                              style classes.
 * 15-Aug-02    Allan           VBM:2002081503 - Change closeFont() signature
 *                              to take a DOMOutputBuffer instead of a
 *                              StringOutputBuffer.
 * 29-Oct-02    Sumit           VBM:2002091801 - Added support for use of the
 *                              HTMLVersion3_AGOutputter
 * 29-Jan-03    Byron           VBM:2003012803 - Modified constructor to set
 *                              protocolConfiguration value and any static
 *                              variables dependent on it.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * 23-May-03    Mat             VBM:2003042907 - Renamed getXMLOutputter() to
 *                              getDocumentOutputter()
 * 28-May-03    Mat             VBM:2003042911 - Changed to use outputter instead
 *                              of outputterWriter
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;

/**
 * AvantGo protocol. The AvantGo is a largely the same as HTML version 3.2
 * with the exception that it requires additional meta tags in the header,
 * doesn't support rollover images and doesn't encode the page in quite the
 * same way.
 */
public class HTMLVersion3_AG extends HTMLVersion3_2 {

    /**
     * Initializes the new instance.
     */
    public HTMLVersion3_AG(ProtocolSupportFactory supportFactory,
                          ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    // javadoc inherited
    protected void openHead(DOMOutputBuffer dom,
                            boolean empty) {

        // Write the meta tags to the head.
        MetaAttributes ma = new MetaAttributes();
        ma.setName("HandheldFriendly");
        ma.setContent("true");
        writeMeta(ma);

        super.openHead(dom, empty);
    }

    /**
     * This method appends the output for the menu item to the outStr buffer.
     *
     * @return True if a horizontal separator is required and false otherwise.
     *         Images do not require a horizontal separator but text items do.
     * @todo later since the introduction of enhanced menus this method is not used. This protocol should be updated to ensure that it creates a modified version of menu rendering that correctly re-maps rolloverimages
     */
    protected boolean doMenuItem(DOMOutputBuffer dom,
            MenuAttributes attributes,
            MenuItem item) throws ProtocolException {
        String type = attributes.getType ();

        // Rollover image is not supported so fallback to rollover text.
        if ("rolloverimage".equals (type)) {
            attributes.setType ("rollovertext");
        }

        return super.doMenuItem (dom, attributes, item);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 25-May-05	8513/1	philws	VBM:2005052408 Port AvantGo protocol fix from 3.3

 25-May-05	8506/1	philws	VBM:2005052408 Fix HTMLVersion3_AG configuration handling

 24-May-05	8462/1	philws	VBM:2005052310 Port device access key override from 3.3

 24-May-05	8430/1	philws	VBM:2005052310 Allow the device to override a protocol's access key support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 01-Jun-04	4616/2	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 01-Jun-04	4614/1	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 12-Jun-03	381/1	mat	VBM:2003061101 Better debugging for WMLRoot

 05-Jun-03	285/6	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
