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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/htmlversion3_2/HTML3_2TransVisitor.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides HTML 3.2
 *                              protocol-specific DOM table optimization
 *                              support.
 * 03-Jun-03    Byron           VBM:2003042204 - Now subclasses
 *                              XHTMLTransitionalTransVisitor and overrides
 *                              handleStyles and removeRedundantTables.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.htmlversion3_2;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.html.xhtmltransitional.XHTMLTransitionalTransVisitor;
import com.volantis.mcs.protocols.trans.TransFactory;

/**
 * Provides HTML 3.2 protocol-specific DOM table optimization support.
 */
public class HTML3_2TransVisitor extends XHTMLTransitionalTransVisitor {
    
    protected HTML3_2TransVisitor(DOMProtocol protocol, TransFactory factory) {
        super(protocol, factory);
    }

    protected String[] getPromotePreserveStyleAttributes() {
        // bgcolor is not in the DTD but is commonly supported by browsers
        final String[] attributes =
            {"align", "bgcolor", "border", "cellpadding", "cellspacing",
             "width"};

        return attributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 12-Jul-05	8990/3	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
