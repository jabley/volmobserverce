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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.graphics.Point;

/**
 * An ItemContainer for a Table.
 */
public class TableItemContainer implements ItemContainer {

    /**
     * The Table that is the container.
     */
    private final Table table;

    /**
     * Construct a new TableItemContainer.
     * @param table The table that is the container.
     */
    public TableItemContainer(Table table) {
        this.table = table;
    }

    // javadoc inherited
    public Item getItem(Point point) {
        return table.getItem(point);
    }


    // javadoc inherited
    public Display getDisplay() {
        return table.getDisplay();
    }


    // javadoc inherited
    public void addListener(int type, Listener listener) {
        table.addListener(type, listener);
    }

    // javadoc inherited
    public void removeListener(int type, Listener listener) {
        table.removeListener(type, listener);
    }

    // javadoc inherited
    public Object getData(String key) {
        return table.getData(key);
    }

    // javadoc inherited
    public void setData(String key, Object object) {
        table.setData(key, object);
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

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
