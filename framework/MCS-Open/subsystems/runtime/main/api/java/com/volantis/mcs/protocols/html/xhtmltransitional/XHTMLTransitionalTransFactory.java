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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/xhtmltransitional/XHTMLTransitionalTransFactory.java,v 1.3 2003/01/17 12:03:40 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides XHTML
 *                              Transitional protocol-specific DOM table
 *                              optimization support.
 * 17-Jan-03    Phil W-S        VBM:2003010606 - Rework: Remove redundant
 *                              imports of container validator classes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmltransitional;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.html.xhtmlfull.XHTMLFullTransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * Provides XHTML Transitional protocol-specific DOM table optimization
 * support.
 */
public class XHTMLTransitionalTransFactory extends XHTMLFullTransFactory {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration protocol specific configuration to use when
     *                      transforming
     */
    public XHTMLTransitionalTransFactory(TransformationConfiguration configuration) {
        super(configuration);
    }

    // Javadoc inherited.
    public TransTable getTable(Element table, DOMProtocol protocol) {

        TransTable trans = new XHTMLTransitionalTransTable(table, protocol );
        trans.setFactory(this);
        return trans;
    }

    // Javadoc inherited.
    public TransVisitor getVisitor(DOMProtocol protocol) {

        return new XHTMLTransitionalTransVisitor(protocol, this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
