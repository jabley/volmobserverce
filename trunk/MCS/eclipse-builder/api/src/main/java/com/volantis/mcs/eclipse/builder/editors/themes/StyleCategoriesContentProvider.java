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
package com.volantis.mcs.eclipse.builder.editors.themes;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.List;

/**
 * This class is the {@link org.eclipse.jface.viewers.ITreeContentProvider} for the style category tree.
 * The tree only contains StyleCategory objects, therefore, this content
 * providers {@link #getChildren} and {@link #getElements} methods will
 * return arrays of StyleCategory objects. StyleCategory objects contain
 * two different types of child element - child StyleCategory and child style
 * properties. This content provider should ignore the syle property elements
 * as only the tree will only be displaying StyleCategory nodes.
 */

public class StyleCategoriesContentProvider implements ITreeContentProvider {

    private final static StyleCategory[] EMPTY_STYLE_CATEGORY_ARRAY =
        new StyleCategory[0];

    // javadoc inherited
    public Object[] getChildren(Object element) {

        // Get the subcategories, which are returned as a List
        final List childList = ((StyleCategory) element).getSubCategories();

        // The list of subcats may be null, in which case return an
        // empty array; otherwise, convert the list to an independent array
        // (unless it is empty in which case return the universal empty one)
        return (
            childList == null
                ? EMPTY_STYLE_CATEGORY_ARRAY
                : childList.toArray());
    }

    // javadoc inherited
    public Object getParent(Object element) {
        // Delegate the request to the given StyleCategory element
        return ((StyleCategory) element).getParent();
    }

    // javadoc inherited
    public boolean hasChildren(Object element) {
        // Delegate the request to the given StyleCategory element
        return ((StyleCategory) element).hasSubCategories();
    }

    // javadoc inherited
    public Object[] getElements(Object element) {
        // The object passed in will be an array of StyleCategory
        // objects, so just return that
        return (Object[]) element;
    }

    // javadoc inherited
    public void dispose() {
        // No action
    }

    // javadoc inherited
    public void inputChanged(Viewer viewer, Object oldInpt, Object newInput) {
        // No action
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
