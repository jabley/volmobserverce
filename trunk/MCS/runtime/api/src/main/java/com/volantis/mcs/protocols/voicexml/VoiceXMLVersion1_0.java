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
 * $Header: /src/voyager/com/volantis/mcs/protocols/voicexml/VoiceXMLVersion1_0.java,v 1.1 2003/04/28 16:14:55 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 14-Nov-01    Pether          VBM:2001110101 - Added  method protocolString()
 *                              to return correct protocolString.
 * 15-Nov-01    Pether          VBM:2001110101 - Changed protocolString to
 *                              refer to voicexml1-0.dtd on voicexml.org.
 * 03-Dec-01    Pether          VBM:2001110101 - Added a linebreak before
 *                              the Doctype tag in protocolString()
 * 10-Dec-01    Paul            VBM:2001110101 - Changed protocol string to
 *                              specify SYSTEM instead of PUBLIC.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 17-Apr-03    Byron           VBM:2003032608 - Implemented doProtocolString
 *                              as per DOM model.
 * 28-Apr-03    Allan           VBM:2003042802 - Moved from protocols package. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.voicexml;

import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;

/**
 * This class supports version 1.0 of the VoiceXML protocol.
 */
public class VoiceXMLVersion1_0 extends VoiceXMLRoot {

    /**
   * Create a new <code>VoiceXMLVersion1_0</code>.
   */
    public VoiceXMLVersion1_0(ProtocolSupportFactory protocolSupportFactory,
          ProtocolConfiguration protocolConfiguration) {
      
      super(protocolSupportFactory, protocolConfiguration);
  }

    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "vxml", null, "http://www.voicexml.org/voicexml1-0.dtd", null,
                MarkupFamily.XML);
        document.setDocType(docType);

        addXMLDeclaration(document);
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/4	geoff	VBM:2003071405 now with fixed architecture

 23-Jul-03	807/2	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
