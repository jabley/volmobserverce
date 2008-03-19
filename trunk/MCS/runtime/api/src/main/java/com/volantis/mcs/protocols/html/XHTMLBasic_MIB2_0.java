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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasic_MIB2_0.java,v 1.3 2003/01/16 11:29:17 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jun-02    Steve           VBM:2002061202 Mobile Internet Browser version
 *                              of XHTML.
 * 15-Jan-03    Phil W-S        VBM:2002110402 - Rework: make this protocol use
 *                              the original transformer as this protocol does
 *                              not support rowspan and colspan. Adds
 *                              getDOMTransformer.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.dom.Document;

/**
 *
 * @author  steve
 */
public class XHTMLBasic_MIB2_0 extends XHTMLBasic
{
    
    /** Creates a new instance of XHTMLBasic_MIB2_0 */
    public XHTMLBasic_MIB2_0(ProtocolSupportFactory protocolSupportFactory, 
            ProtocolConfiguration protocolConfiguration)
    {
        super(protocolSupportFactory, protocolConfiguration);
    }


    // Javadoc inherited.
    protected void doProtocolString(Document document) {
        addXHTMLBasicDocType(document);
    }

    protected DOMTransformer getDOMTransformer() {
        return new XHTMLBasicTransformer();
    }
    
    //  Javadoc inherited from super class
    public String defaultMimeType() {
        return "text/html";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 14-Nov-03	1861/1	mat	VBM:2003110602 Add correct mimetype to descendants of XHTMLBasic

 ===========================================================================
*/
