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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime;

import com.volantis.styling.engine.Attributes;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.HashMap;

/**
 * XDIME Attributes implementation.
 */
public class XDIMEAttributesImpl
        implements XDIMEAttributes, Attributes {

    /**
     * The type of element to which these attributes belong.
     */
    private final ElementType elementType;

    /**
     * Map containing the values of the attributes that have been set.
     */
    HashMap values;

    public XDIMEAttributesImpl(ElementType elementType) {
        this.elementType = elementType;
    }

    // Javadoc inherited.
    public ElementType getElementType() {
        return elementType;
    }

    // Javadoc inherited.
    public String getValue(String nameSpace, String localName) {
        String value = null;
        if (values != null) {
            String name;
            if (nameSpace != null && !nameSpace.equals("")) {
                name = "{" + nameSpace + "}" + localName;
            } else {
                name = localName;
            }
            value = (String) values.get(name);
        }
        return value;
    }

    // Javadoc inherited.
    public void setValue(String nameSpace, String localName, String value) {
        if (values == null) {
            values = new HashMap();
        }
        String name;
        if (nameSpace != null && !nameSpace.equals("")) {
            name = "{" + nameSpace + "}" + localName;
        } else {
            name = localName;
        }
        values.put(name, value);
    }

    /**
     * Returns the number of attributes with values in this XDIMEAttributesImpl.
     *
     * @return number of attributes with values set
     */
    public int getLength() {
        int length = 0;
        if (values != null) {
            length = values.size();
        }
        return length;
    }

    // Javadoc inherited.
    public String getAttributeValue(String namespace, String localName) {
        return getValue(namespace, localName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 18-Jul-05	9021/1	ianw	VBM:2005071114 interim commit of XDIME API for DISelect integration

 ===========================================================================
*/



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 27-Jul-05	9060/1	tom	VBM:2005071304 Added Sel Select

 ===========================================================================
*/
