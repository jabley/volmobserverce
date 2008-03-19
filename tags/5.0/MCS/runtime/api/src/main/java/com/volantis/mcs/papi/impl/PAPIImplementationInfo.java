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


package com.volantis.mcs.papi.impl;

public class PAPIImplementationInfo {


    /**
     * The name of this element
     */
    private String element;

    /**
     * The class that implements this PAPI element
     */
    private Class elementImplClass;

    /**
     * The class that implements the attributes associated with this PAPI element
     */
    private Class attributesImplClass;

    /**
     * Construct an empty PAPIImplementationInfo object.
     */
    public PAPIImplementationInfo() {

    }

    /**
     * Construct a new PAPIImplementationInfo object with the supplied parameters
     *
     * @param element             The name of the element.
     * @param elementImplClass    The Class that implements this Element.
     * @param attributesImplClass The class that implements the attributes for this Element.
     */
    public PAPIImplementationInfo(
            String element, Class elementImplClass, Class attributesImplClass) {

        this.setElement(element);
        this.setElementImplClass(elementImplClass);
        this.setAttributesImplClass(attributesImplClass);

    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Class getElementImplClass() {
        return elementImplClass;
    }

    public void setElementImplClass(Class elementImplClass) {
        this.elementImplClass = elementImplClass;
    }

    public Class getAttributesImplClass() {
        return attributesImplClass;
    }

    public void setAttributesImplClass(Class attributeImplClass) {
        this.attributesImplClass = attributeImplClass;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
