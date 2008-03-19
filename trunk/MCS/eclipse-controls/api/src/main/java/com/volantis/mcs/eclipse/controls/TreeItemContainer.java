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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;

/**
 * An ItemContainer for a Tree.
 */
public class TreeItemContainer implements ItemContainer {
    /**
     * The Table that is the container.
     */
    private final Tree tree;

    /**
     * Construct a new TreeItemContainer.
     * @param tree The tree that is the container.
     */
    public TreeItemContainer(Tree tree) {
        this.tree = tree;
    }

    // javadoc inherited
    public Item getItem(Point point) {
        return tree.getItem(point);
    }

    /**
     * Get the items that are the children of the top item in the tree.
     * These will be the items that have most recently been updated.
     */

    // javadoc inherited
    public Display getDisplay() {
        return tree.getDisplay();
    }

    // javadoc inherited
    public void addListener(int type, Listener listener) {
        tree.addListener(type, listener);
    }

    // javadoc inherited
    public void removeListener(int type, Listener listener) {
        tree.removeListener(type, listener);
    }

    // javadoc inherited
    public Object getData(String key) {
        return tree.getData(key);
    }

    // javadoc inherited
    public void setData(String key, Object object) {
        tree.setData(key, object);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Jan-04	2562/3	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 ===========================================================================
*/
