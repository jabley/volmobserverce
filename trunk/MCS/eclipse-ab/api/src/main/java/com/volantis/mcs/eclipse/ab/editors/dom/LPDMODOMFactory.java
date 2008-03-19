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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.odom.LPDMODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.LPDMODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * This LPDMODOMFactory specialization overrides the element and attribute
 * factory creation methods to create instances of LPDMODOMElement and
 * LPDMODOMAttribute. These nodes behave in the same way as ODOMElements and
 * ODOMAttributes except for how the empty string value is handled for
 * attributes. Setting an attribute of an LPDMODOMElement to "" automatically
 * results in the attribute detaching itself from its parent. All created
 * attributes and elements automatically belong to the default namespace
 * unless indicated otherwise.
 */
public class LPDMODOMFactory extends ODOMFactory {

    /**
     * Creates an LPDMODOMAttribute with the given name, value and namespace.
     * @param name the attribute's name
     * @param value the attribute's value
     * @param namespace the attribute's namespace
     * @return the LPDMODOMAttribute
     */
    public Attribute attribute(String name,
                               String value,
                               Namespace namespace) {
        return new LPDMODOMAttribute(name, value, namespace);
    }

    /**
     * Creates an LPDMODOMAttribute with the given name, value, type and
     * namespace.
     * @param name the attribute's name
     * @param value the attribute's value
     * @param type the attribute's type. See {@link Attribute} for the types.
     * @param namespace the attribute's namespace
     * @return the LPDMODOMAttribute
     */
    public Attribute attribute(String name,
                               String value,
                               int type,
                               Namespace namespace) {
        return new LPDMODOMAttribute(name, value, type, namespace);
    }

    /**
     * Creates an LPDMODOMAttribute with the given name and value.
     * @param name the attribute's name
     * @param value the attribute's value
     * @return the LPDMODOMAttribute
     */
    public Attribute attribute(String name, String value) {
        return new LPDMODOMAttribute(name, value);
    }

    /**
     * Creates an LPDMODOMAttribute with the given name, value and type.
     * @param name the attribute's name
     * @param value the attribute's value
     * @param type the attribute's type. See {@link Attribute} for the types.
     * @return the LPDMODOMAttribute
     */
    public Attribute attribute(String name, String value, int type) {
        return new LPDMODOMAttribute(name, value, type);
    }

    /**
     * Creates an LPDMODOMElement with the given name and namespace.
     * @param name the element's name
     * @param namespace the element's namespace
     * @return the LPDMODOMElement
     */
    public Element element(String name, Namespace namespace) {
        return new LPDMODOMElement(name, namespace);
    }

    /**
     * Creates a new LPDMODOMElement which belongs to the
     * {@link MCSNamespace#LPDM} namespace.
     * @param name the name of the element to create
     * @return the new LPDMODOMElement
     */
    public Element element(String name) {
        return new LPDMODOMElement(name, MCSNamespace.LPDM);
    }

    /**
     * Creates an LPDMODOMElement with the given name and namespace URI.
     * @param name the element's name
     * @param uri the element's namespace URI
     * @return the LPDMODOMElement
     */
    public Element element(String name, String uri) {
        return new LPDMODOMElement(name, uri);
    }

    /**
     * Creates an LPDMODOMElement with the given name, namespace prefix and
     * namespace URI.
     * @param name the element's name
     * @param prefix the element's namespace prefix
     * @param uri the element's namespace URI
     * @return the LPDMODOMElement
     */
    public Element element(String name, String prefix, String uri) {
        return new LPDMODOMElement(name, prefix, uri);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Sep-04	5451/22	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 08-Sep-04	5251/9	pcameron	VBM:2004081609 Empty attribute values are deleted by editors

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 17-Dec-03	2219/1	doug	VBM:2003121502 Added dom validation to the eclipse editors

 ===========================================================================
*/
