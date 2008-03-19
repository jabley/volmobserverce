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
 * Simple event that informs of a change in the canUndo/canRedo capability of a
 * <code>UndoRedoManager</code>.
 * <p>
 * To avoid implementing <code>java.io.Serializable</code> this class does not
 * extend {@link java.util.EventObject}.
 * </p>
 */
public class UndoRedoStatusEvent {

    /**
     * final state, 'snapshot' of the UndoRedoManager cannUndo state at
     * the time of event creation
     */
    private final boolean canUndo;

    /**
     * final state, 'snapshot' of the UndoRedoManager cannUndo state at
     * the time of event creation
     */
    private final boolean canRedo;

    /**
     * the source of this event
     */
    private UndoRedoManager undoRedoManager;


    /**
     * @param undoRedoManager the source of this event
     */
    public UndoRedoStatusEvent (UndoRedoManager undoRedoManager) {
        canUndo = undoRedoManager.canUndo();
        canRedo = undoRedoManager.canRedo();
        this.undoRedoManager = undoRedoManager;
    }

    /**
     * @return true if the <code>UndoRedoManager</code> source of this event
     * could undo at the time the event was created, false otherwise
     */
    public boolean canUndo() {
        return canUndo;
    }


    /**
     * @return true if the <code>UndoRedoManager</code> source of this event
     * could redo at the time the event was created, false otherwise
     */
    public boolean canRedo() {
        return canRedo;
    }


    /**
     * @return the <code>UndoRedoManager</code> source of this event
     */
    public UndoRedoManager getUndoRedoManagerSource() {
        return undoRedoManager;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2800/7	eduardo	VBM:2004012802 codestyle fixes

 09-Feb-04	2800/5	eduardo	VBM:2004012802 undo redo works from outline view

 05-Feb-04	2800/2	eduardo	VBM:2004012802 undo redo hooked in eclipse with demarcation. Designed just for single page editors

 ===========================================================================
*/
