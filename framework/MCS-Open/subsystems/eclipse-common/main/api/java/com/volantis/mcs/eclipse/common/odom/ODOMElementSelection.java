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
package com.volantis.mcs.eclipse.common.odom;

import org.eclipse.jface.viewers.IStructuredSelection;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * An IStructuredSelection for selected ODOM Elements. Instances of
 * ODOMElementSelection are immutable.
 */
public class ODOMElementSelection implements IStructuredSelection {

    /**
     * The List of elements that make up this ODOMElementSelection.
     */
    private final List selection;

    /**
     * Construct a new ODOMElementSelection based on a given selection of
     * ODOMElements.
     * @param selection The List of ODOMElements that make up this
     * ODOMElementSelection. Can be null.
     * @throws IllegalArgumentException If selection contains any objects that
     * are not ODOMElements.
     */
    public ODOMElementSelection(List selection) {
        // Check the selection.
        if (selection != null) {
            boolean invalidSelection = false;
            int i;
            for (i = 0; i < selection.size() && !invalidSelection; i++) {
                invalidSelection = !(selection.get(i) instanceof ODOMElement);
            }

            if (invalidSelection) {
                throw new IllegalArgumentException("Only ODOMElements are " + //$NON-NLS-1$
                        "permitted in the selection. Item " + (i-1) + //$NON-NLS-1$
                        " is a  " + selection.get(i-1).getClass().getName()); //$NON-NLS-1$
            }
        }

        if (selection == null) {
            selection = Collections.EMPTY_LIST;
        }

        this.selection = new ArrayList(selection);
    }

    //javadoc inherited
    public Object getFirstElement() {
        return selection.get(0);
    }

    /**
     * Provides an immutable Iterator over the ODOMElementSelection.
     */
    // rest of javadoc inherited
    public Iterator iterator() {
        return new Iterator() {
            Iterator iterator = selection.iterator();
            // javadoc inherited
            public boolean hasNext() {
                return iterator.hasNext();
            }

            // javadoc inherited
            public Object next() {
                return iterator.next();
            }

            /**
             * Does not support remove because the selection is immutable.
             */
            // rest of javadoc inherited
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // javadoc inherited
    public int size() {
        return selection.size();
    }

    // javadoc inherited
    public Object[] toArray() {
        return selection.toArray();
    }

    // javadoc inherited
    public List toList() {
        return new ArrayList(selection);
    }

    // javadoc inherited
    public boolean isEmpty() {
        return selection.isEmpty();
    }

    /**
     * Provide a type safe version of toArray() for the ODOMElements in this
     * ODOMElementSelection.
     * @return An ODOMElement array of the selected ODOMElements.
     */
    public ODOMElement [] toODOMElementArray() {
        ODOMElement array [] = new ODOMElement[selection.size()];
        selection.toArray(array);

        return array;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Apr-04	4072/4	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 29-Apr-04	4072/2	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 22-Jan-04	2659/1	allan	VBM:2003112801 Very basic rule section and theme design editor.

 15-Jan-04	2618/1	allan	VBM:2004011510 Provide an IStructuredSelection for selected ODOMElements.

 ===========================================================================
*/
