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

import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Interface for XDIME Attributes.
 * 
 * @mock.generate
 */
public interface XDIMEAttributes {

    /**
     * Get the element type to which these attributes apply.
     *
     * <p>If this interface is made public then this method must not be exposed
     * on it as the {@link ElementType} class is purely internal.</p>
     *
     * @return The element type to which these attributes apply.
     */
    ElementType getElementType();

    /**
     * Gets an attribute value from these attributes, given the attribute name
     * and namespace.
     *
     * <p>The behaviour is undefined in the attribute is not supported on the
     * element. Currently the attribute is ignored but in future it may throw
     * an {@link IllegalArgumentException}.</p>
     *
     * @param nameSpace the namespace of the attribute
     * @param localName the name of the attribute
     * @return the value of the attribute. May be null.
     */
    String getValue(String nameSpace, String localName);

    /**
     * Sets the attribute value for the specified attribute and namespace.
     *
     * <p>The behaviour is undefined in the attribute is not supported on the
     * element. Currently the attribute is ignored but in future it may throw
     * an {@link IllegalArgumentException}.</p>
     *
     * @param nameSpace the namespace
     * @param localName the attribute name
     * @param value the attribute value
     */
    void setValue(String nameSpace, String localName, String value);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 18-Jul-05	9021/1	ianw	VBM:2005071114 interim commit of XDIME API for DISelect integration

 ===========================================================================
*/
