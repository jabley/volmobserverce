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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/htmlversion4_0/HTML4_0TransVisitor.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides HTML 4.0
 *                              protocol-specific DOM table optimization
 *                              support.
 * 04-Jun-03    Byron           VBM:2003042204 - Now subclasses
 *                              XHTMLTransitionalTransVisitor.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.htmlversion4_0;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.html.xhtmltransitional.XHTMLTransitionalTransVisitor;
import com.volantis.mcs.protocols.trans.TransFactory;

/**
 * Provides HTML 4.0 protocol-specific DOM table optimization support.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class HTML4_0TransVisitor extends XHTMLTransitionalTransVisitor {
    
    protected HTML4_0TransVisitor(DOMProtocol protocol, TransFactory factory) {
        super(protocol, factory);
    }

    protected String[] getPromotePreserveStyleAttributes() {
        // The class attribute is handled by the superclass so is not
        // included here.
        final String[] attributes =
            {"align", "bgcolor", "border", "cellpadding", "cellspacing",
             "datapagesize", "dir", "frame", "id", "lang",
             "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup",
             "onmousedown", "onmousemove", "onmouseout", "onmouseover",
             "onmouseup",
             "rules", "style", "summary", "title", "width"};

        return attributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
