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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.graphics.Point;

/**
 * An common interface for SWT widgets that contain items such as Tables
 * and Trees.
 */
public interface ItemContainer {
    /**
     * Get the Item at a given Point.
     */
    public Item getItem(Point point);

    /**
     * Get the Display associated with this container.
     */
    public Display getDisplay();

    /**
     * Add a listener to this container.
     */
    public void addListener(int type, Listener listener);

    /**
     * Remove a listener from this container.
     */
    public void removeListener(int type, Listener listener);

    /**
     * Get the data item associated this ItemContainer.
     */
    public Object getData(String key);

    /**
     * Set a data item on this ItemContainer.
     */
    public void setData(String key, Object object);
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
