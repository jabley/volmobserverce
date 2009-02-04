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
 * Represents the environemnt in which the undo/redo actions take place
 * (e.g. the UI).
 * <p>
 * It is invoked during undo/redo to allow itself to return to a previous state
 * or similar one. See the <em>Memento</em> Pattern in the Design Patterns book.
 * </p>
 * <p>
 * This interface allows the undo/redo framework not to depend explicitly
 * on its usage context.<br/>
 * At run time, this interface is implemented by the UI
 * environemnt where the ODOM editors operate (Eclipse).
 * </p>
 */
public interface UndoRedoMementoOriginator {

    /**
     * @return a memento object - opaque information about itself.
     */
    public UndoRedoMemento takeSnapshot();

    /**
     * Has a chance to use the memento and the information about the ODOM
     * document changes to return to a previous state (or similar).
     * @param undoRedoInfo - the memento previously along with the ODOM xpaths 
     * for changed elements
     */
    public void restoreSnapshot(UndoRedoInfo undoRedoInfo);

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Feb-04	2988/2	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 ===========================================================================
*/
