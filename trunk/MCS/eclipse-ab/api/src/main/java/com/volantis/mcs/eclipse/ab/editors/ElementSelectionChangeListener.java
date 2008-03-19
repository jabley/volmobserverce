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

package com.volantis.mcs.eclipse.ab.editors;

/**
 * The element selection listener that may be used to listen to element
 * selection changed events.
 */
public interface ElementSelectionChangeListener {

    /**
     * Interested parties will have to implement this method in order to listen
     * to selection changed events.
     *
     * @param event the <code>ElementSelectionChangeEvent</code> itself.
     */
    public void elementSelectionChanged(ElementSelectionChangeEvent event);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Nov-03	1808/3	byron	VBM:2003110406 ElementSelectionChange event handling - fixed javadoc

 06-Nov-03	1808/1	byron	VBM:2003110406 ElementSelectionChange event handling

 ===========================================================================
*/
