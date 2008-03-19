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
package com.volantis.mcs.eclipse.common.odom.undo;

/**
 * Implementations of this interface can be registered with an
 * {@link UndoRedoManager} and will be notified of its
 * <code>UndoRedoStatusEvent</code>s.
 */
public interface UndoRedoStatusListener {

    /**
     * Invoked whenever a canUndo/canRedo status change happens within the
     * {@link UndoRedoManager} that this listener has been registered with.
     * @param event signals a change in canUndo/canRedo capability.
     */
    public void undoStatusChanged (UndoRedoStatusEvent event);

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Feb-04	2800/2	eduardo	VBM:2004012802 undo redo hooked in eclipse with demarcation. Designed just for single page editors

 ===========================================================================
*/
