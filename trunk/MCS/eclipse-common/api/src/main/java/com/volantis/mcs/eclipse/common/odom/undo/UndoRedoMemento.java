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
 * A marker interface for opaque information about the state of
 * {@link UndoRedoMementoOriginator} which represents the contextual
 * environment in which the UndoRedomanger operates.
 * <p>
 * See the Memento pattern in the GOF book.
 * </p>  
 */
public interface UndoRedoMemento {

    /**
     * A nullobject implementation,
     * provided avoid using null references.
     */
    public static final UndoRedoMemento NULLOBJ = new NullUndoRedoMemento();

}

class NullUndoRedoMemento implements UndoRedoMemento {
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 17-Feb-04	2988/2	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 ===========================================================================
*/
