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
package com.volantis.mcs.eclipse.common.odom;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.input.DefaultJDOMFactory;

/**
 * The JDOMFactory implementation should be used with the DOM builder to
 * generate an Observable DOM.
 */
public class ODOMFactory extends DefaultJDOMFactory {
    // javadoc inherited
    public Attribute attribute(String name,
                               String value,
                               Namespace namespace) {
        return new ODOMAttribute(name, value, namespace);
    }

    // javadoc inherited
    public Attribute attribute(String name,
                               String value,
                               int type,
                               Namespace namespace) {
        return new ODOMAttribute(name, value, type, namespace);
    }

    // javadoc inherited
    public Attribute attribute(String name, String value) {
        return new ODOMAttribute(name, value);
    }

    // javadoc inherited
    public Attribute attribute(String name, String value, int type) {
        return new ODOMAttribute(name, value, type);
    }

    // javadoc inherited
    public Text text(String text) {
        return new ODOMText(text);
    }

    // javadoc inherited
    public Element element(String name, Namespace namespace) {
        return new ODOMElement(name, namespace);
    }

    // javadoc inherited
    public Element element(String name) {
        return new ODOMElement(name);
    }

    // javadoc inherited
    public Element element(String name, String uri) {
        return new ODOMElement(name, uri);
    }

    // javadoc inherited
    public Element element(String name, String prefix, String uri) {
        return new ODOMElement(name, prefix, uri);
    }

    // javadoc inherited
    public CDATA cdata(String str) {
        return new ODOMCDATA(str);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 15-Dec-03	2160/2	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 07-Nov-03	1813/1	philws	VBM:2003110520 Add ODOMCDATA and provide correct clone feature

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
