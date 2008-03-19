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

package com.volantis.mcs.eclipse.ab.editors.dom;

import org.jdom.Namespace;

/**
 * This interface enables the client of the enclosing delegates instance
 * (typically a ProxyElement) to listen for changes to the (aggregated)
 * target attributes which are managed by the delegates class, and
 * to name changes of the target elements that contain them
 */
interface AggregationListener {

    /**
     * Used to notify the listener of a new "aggregated" element name,
     * potentially derived from the name(s) of the proxied element(s).
     *
     * @param elementName the new element name
     */
    void updatedElementName(String elementName);

    /**
     * Used to notify the listener of a new element namespace,
     * potentially derived from the name(s) of the proxied element(s).
     *
     * @param elementNamespace the new element namespace
     */
    void updatedElementNamespace(Namespace elementNamespace);

    /**
     * Used to notify the listener that a new attribute is now being
     * supported by the delegates class (and this is its initial value).
     * This may be called after a selection change event has been passed
     * to the delegates class. (Note also that this is the only possible
     * occasion on which this method is called.)
     *
     * @param attribName The name of the new attribute
     * @param attribValue The value of the new attribute
     * @throws IllegalStateException The listener should
     * throw this if it already has had a newAttribute notification
     * with this attribute name that has not been succeeded by a
     * deletedAttribute notification with this attribute name
     */
    void newAttribute(String attribName, String attribValue)
            throws IllegalStateException;

    /**
     * Used to notify the listener that the value of an
     * attribute (whose name and initial value it has previously supplied
     * via newAttribute) has changed. The only
     * occasions that this method is called are when one of the proxied
     * elements changes value such that a supported attribute (previously
     * notified by newAttribute) changes its value: in
     * this case, the aggregated value is re-calculated by the delegates
     * class and "passed up" to the listener through this method. This
     * aggregated value may be the empty string, but in this case the
     * listener should NOT consider this a true deletion, but instead
     * as a resetting to an empty value. (Deletion is done through
     * deletedAttribute in this interface.)
     *
     * @param attribName The name of the attribute with a new value
     * @param attribValue The new value
     * @throws IllegalStateException The listener should
     * throw this if it has not previously received a newAttribute
     * notification with this attribute name (that has not been
     * followed by a deletedAttribute notification)
     */
    void updatedAttributeValue(String attribName, String attribValue)
            throws IllegalStateException;

    /**
     * Used to notify the listener that the specified attribute
     * is no longer being supported by the delegates class. This may be
     * called after a selection change event has been passed to the
     * delegates class. (Note also that this is the only possible occasion
     * on which this method is called.)
     *
     * @param attribName The name of the attribute to remove
     * @throws IllegalStateException TThe listener should
     * throw this if it has not previously received a newAttribute
     * notification with this attribute name (that has not been
     * followed by a deletedAttribute notification)
     */
    void deletedAttribute(String attribName)
            throws IllegalStateException;

    /**
     * Used to notify the listener that the text value of an element has been
     * modified.
     *
     * @param textValue The new text value
     */
    void updatedTextValue(String textValue);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Oct-05	9734/2	adrianj	VBM:2005100510 Allow text in ProxyElements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Mar-04	3256/1	byron	VBM:2004021106 The theme StyleList property types do not work in the theme editor

 ===========================================================================
*/
