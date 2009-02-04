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

import com.volantis.mcs.eclipse.common.odom.ODOMElement;

/**
 * This is the definition for classes that with to provide specific label
 * formatting for ODOMLabelProviders.
 */
public interface ODOMLabelFormatProvider {
    /**
     * Provide the label format for a given ODOMElement. A label format
     * is typically a String that uses the 'language' of
     * AttributesMessageFormatter that describes how the element and/or
     * attributes of an element should be displayed by an ODOMLabelProvider.
     * For example a message format of "{element}: {name}" when used on
     * a Pane element named "paneName" would be displayed as "Pane: paneName".
     * A message format of "{element}, {value}, {device}" on a device image
     * asset would be displayed as "Device Specific Image, animage.gif, PC"
     * where the asset value is "animage.gif" and the device is PC - note
     * that this example is in English, different locales may differ.
     *
     * (The 'language' of AttributesMessageFormatter is the same as
     * the java MessageFormat class except rather than using integers for
     * substition it uses attribute names or the word "element" for the
     * element.)
     *
     * If an attribute or the element is not in the label format then it will
     * not be displayed.
     */
    String provideLabelFormat(ODOMElement element);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Jan-04	2562/1	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 ===========================================================================
*/
