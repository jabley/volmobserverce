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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jdom.Element;

/**
 * An IStructuredContentProvider for JDOM Elements.
 */
public class ElementChildrenContentProvider
        implements IStructuredContentProvider {
    /**
     * Get the child elements of the given object which must be a JDOM Element.
     * @param parentElement The JDOM Element whose children to get.
     * @throws IllegalArgumentException If parentElement is not a JDOM Element
     * or is null.
     */
    // rest of javadoc inherited
    public Object[] getElements(Object parentElement) {
        if(parentElement==null) {
            throw new IllegalArgumentException(
                        "Cannot be null: parentElement"); //$NON-NLS-1$
        }
        if(!(parentElement instanceof Element)) {
            throw new IllegalArgumentException(
                        "Expected a JDOM Element " + //$NON-NLS-1$
                        "for parentElement but was: " + //$NON-NLS-1$
                        parentElement.getClass().getName());
        }

        Element children [] = new Element[0];
        return ((Element)parentElement).getChildren().toArray(children);
    }

    // javadoc inherited
    public void dispose() {
    }

    // javadoc inherited
    public void inputChanged(Viewer viewer, Object o, Object o1) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
