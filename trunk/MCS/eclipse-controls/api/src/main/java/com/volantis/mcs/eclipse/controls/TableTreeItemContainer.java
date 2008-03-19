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

import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.custom.TableTree;

/**
 */
public class TableTreeItemContainer implements ItemContainer {
    private TableTree tableTree;

    public TableTreeItemContainer(TableTree tableTree) {
        this.tableTree = tableTree;
    }

    public Item getItem(Point point) {
        return tableTree.getItem(point);
    }

    public Display getDisplay() {
        return tableTree.getDisplay();
    }

    public void addListener(int type, Listener listener) {
        tableTree.addListener(type, listener);
    }

    public void removeListener(int type, Listener listener) {
        tableTree.removeListener(type, listener);
    }

    public Object getData(String key) {
        return tableTree.getData(key);
    }

    public void setData(String key, Object object) {
        tableTree.setData(key, object);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 ===========================================================================
*/
