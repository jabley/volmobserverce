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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.domvisitor;

import org.jdom.Element;
import org.jdom.Namespace;

public class NamespaceFixingJDOMVisitor extends SimpleVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    private Namespace namespace;

    public NamespaceFixingJDOMVisitor(Namespace namespace) {
        this.namespace = namespace;
    }

    // javadoc inherited
    public Visitor.Action visit(Element element) {
        element.setNamespace(namespace);
        return Visitor.Action.CONTINUE;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/1	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 ===========================================================================
*/
