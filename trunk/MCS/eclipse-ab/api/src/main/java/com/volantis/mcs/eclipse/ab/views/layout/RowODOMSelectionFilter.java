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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.layouts.LayoutSchemaType;

import java.util.List;
import java.util.ArrayList;

import org.jdom.Element;

/**
 * The Row ODOM selection filter that will resolve elements that are grid rows.
 */
public class RowODOMSelectionFilter extends ODOMSelectionFilter {

    /**
     * Create an ODOMElement that is a placeholder for no matches for this
     * filter. This is necessary because the obvious choice (null value) is
     * ignored in {@link com.volantis.mcs.eclipse.common.odom.DefaultODOMElementSelectionProvider#update}.
     */
    private static final ODOMElement NON_ROW = new ODOMElement(
            ODOMElement.NULL_ELEMENT_NAME);

    /**
     * Store the list of possible parent elements.
     */
    private static List parentMatchList = new ArrayList();
    static {
        // Initialize the values with row and segment row grid element names.
        parentMatchList.add(LayoutSchemaType.GRID_FORMAT_ROWS_ELEMENT.getName());
        parentMatchList.add(LayoutSchemaType.SEGMENT_GRID_FORMAT_ROW_ELEMENT.getName());
    };

    /**
     * Default constructor that initializes the selection filter with no
     * resolver or array filter names.
     */
    public RowODOMSelectionFilter()  {
        super(null, null);
    }

    // javadoc inherited
    public ODOMElement resolve(ODOMElement element) throws XPathException {
        // If we don't find a match then the default return is set to a NON-ROW
        ODOMElement resolved = NON_ROW;
        final Element parent = element.getParent();
        if (parent != null &&
                parentMatchList.contains(parent.getName())) {
            resolved = (ODOMElement) parent;
        }
        return resolved;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: Format Attributes View: Row Page

 ===========================================================================
*/
