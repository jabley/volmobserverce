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

import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.xml.xpath.XPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * An ordered container of change events that can be undone/redone.
 * <p>
 * A UnitOfWork (UOW) It is essentially a composite ODOMChangeEvent that
 * also stores an instance of UndoRedoMemento captured at creation time
 * (e.g., it plays the careteker role in the GOF memento pattern).
 * </p>
 * <p>
 * This class should not be public because it is only used by the {@link
 * UndoRedoManager}.
 * </p>
 */
class UndoRedoUnitOfWork {

    /**
     * contains the ODOMChangeEvent instances that make this UOW
     */
    private final LinkedList changeEvents;

    /**
     * The memento obtained at this UOW construction time from the originator.
     * The UOW is just a caretaker, can't do anything with this object except
     * keeping it and handing it back when requested (at the end of undo/redo)
     */
    private final UndoRedoMemento undoRedoMemento;


    /**
     * Single constructor that takes the memento into care
     * 
     * @param undoRedoMemento just stored for caretaking
     */
    public UndoRedoUnitOfWork(final UndoRedoMemento undoRedoMemento) {
        this.undoRedoMemento = undoRedoMemento;
        changeEvents = new LinkedList();
    }


    /**
     * Adds a changeEvent to this UOW
     *
     * @param changeEvent must not be null
     */
    public void addChangeEvent(ODOMChangeEvent changeEvent) {
        if (changeEvent == null) {
            throw new NullPointerException(
                    "changeEvent argument must not be null"); //$NON-NLS-1$
        }
        changeEvents.add(changeEvent);
    }


    /**
     * Undoes all the events that make this UOW
     * <p>
     * They are undone in an order that is the reverse of the one with which
     * they happened.
     * </p>
     *
     * @return An info object containing the memento associated with this UOW
     * and a List (possibly empty) of distinct XPath returned by
     * each {@link ODOMChangeEvent#undo()} when not null.
     */
    public UndoRedoInfo undo() {
        // iterate starting at the end and going backwards
        ListIterator li = changeEvents.listIterator(changeEvents.size());
        List xpaths = Collections.EMPTY_LIST;

        while (li.hasPrevious()) {
            ODOMChangeEvent changeEvent = (ODOMChangeEvent) li.previous();
            XPath xpath = changeEvent.undo();
            xpaths = addIfNotNullAndNotAlreadyContained(xpath, xpaths);
        }

        return new UndoRedoInfo(xpaths, undoRedoMemento);
    }


    /**
     * "Replays" all the events of this UOW, in chronological order.
     *
     * @return An info object containing the memento associated with this UOW
     * and a List (possibly empty) of distinct XPath returned by
     * each {@link ODOMChangeEvent#redo()} when not null.
     */
    public UndoRedoInfo redo() {
        /*
        * Implementation note: a <code>java.util.List</code> has been chosen as
        * return type, rather than a more intent-telling <code>XPath[]</code>
        * because to avoid resizing the array or checking for nulls.
        *
        * A set was avoided too because the sort order we want is the insertion
        * one: a LinkedHashSet was overkill, on this sizes checking at
        * insertion if an element is already contained is simple and good
        * enough.
        */
        ListIterator li = changeEvents.listIterator();
        List xpaths = Collections.EMPTY_LIST;

        while (li.hasNext()) {
            ODOMChangeEvent changeEvent = (ODOMChangeEvent) li.next();
            XPath xpath = changeEvent.redo();
            xpaths = addIfNotNullAndNotAlreadyContained(xpath, xpaths);
        }
        return new UndoRedoInfo(xpaths, undoRedoMemento);
    }


    /**
     * Extracted logic common to undo and redo
     *
     * @param object if not null, it is added to the returned list
     * @param list   the list to object to;
     *               if it's Collections.EMPTY_LIST a new list is created and returned
     * @return a list containing object if it is not null
     */
    private List addIfNotNullAndNotAlreadyContained(Object object, List list) {
        if (object != null) {
            if (list == Collections.EMPTY_LIST) {
                list = new ArrayList(changeEvents.size());
            }

            // we rely on the object implementing equals correctly
            // oh, well we know it's an XPath
            if (!list.contains(object)) {
                list.add(object);
            }
        }
        return list;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Feb-04	2988/4	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 09-Feb-04	2800/6	eduardo	VBM:2004012802 undo redo works from outline view

 05-Feb-04	2800/4	eduardo	VBM:2004012802 undo redo hooked in eclipse with demarcation. Designed just for single page editors

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2689/4	eduardo	VBM:2003112407 formatting changes

 29-Jan-04	2689/2	eduardo	VBM:2003112407 undo/redo manager for ODOM

 ===========================================================================
*/
