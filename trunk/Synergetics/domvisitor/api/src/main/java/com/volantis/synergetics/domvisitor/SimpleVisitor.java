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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.domvisitor;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Text;

/**
 * This simple {@link Visitor} implementation performs a continuous nextHeader of
 * a complete DOM (sub-)tree.
 */
public class SimpleVisitor implements Visitor {
    // javadoc inherited
    public Visitor.Action visit(Element element) {
        return Visitor.Action.CONTINUE;
    }

    // javadoc inherited
    public Visitor.Action visit(Attribute attribute) {
        return Visitor.Action.CONTINUE;
    }

    // javadoc inherited
    public Visitor.Action visit(Text text) {
        return Visitor.Action.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/2	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Dec-03	2226/1	philws	VBM:2003121115 Provide JDOM traversal walker/visitor

 ===========================================================================
*/
