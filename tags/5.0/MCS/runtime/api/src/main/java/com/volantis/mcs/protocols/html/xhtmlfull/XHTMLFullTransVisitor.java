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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/xhtmlfull/XHTMLFullTransVisitor.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides XHTMLFull
 *                              protocol-specific DOM table optimization
 *                              support.
 * 03-Jun-03    Byron           VBM:2003042204 - Added normalize method
 *                              (overrides parent implementation).
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmlfull;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.html.XHTMLBasicTransVisitor;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.VisitorStatus;

/**
 * Provides XHTMLFull protocol-specific DOM table optimization support.
 */
public class XHTMLFullTransVisitor extends XHTMLBasicTransVisitor {
    protected XHTMLFullTransVisitor(DOMProtocol protocol, TransFactory factory) {
        super(protocol, factory);
    }

    protected String[] getPromotePreserveStyleAttributes() {
        // The class attribute is handled by the superclass so is not
        // included here.
        final String[] attributes =
            {"border", "cellpadding", "cellspacing", "dir",
             "frame", "id", "lang", "onclick", "ondblclick", "onkeydown",
             "onkeypress", "onkeyup", "onmousedown", "onmousemove",
             "onmouseout", "onmouseover", "onmouseup", "rules", "style",
             "summary", "title", "width", "xml:lang"};

        return attributes;
    }

    protected void normalize(Element table, TransCell cell) {

        // If the parent is a 'div' tag and the grandparent is a 'form' tag
        // then do not do the normalization.
        Element parent = table.getParent();
        boolean ignore = false;

        if ("div".equals(parent.getName())) {
            parent = parent.getParent();

            if ("form".equals(parent.getName())) {
                ignore = true;
            }
        }

        if (!ignore) {
            super.normalize(table, cell);
        } else {
            // Simply re-visit this element, as if it were totally
            // stand-alone. Mark it as visited
            revisitElement(table);
            table.setObject(VisitorStatus.getDefaultInstance());
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8990/2	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 11-Jul-05	8988/1	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
