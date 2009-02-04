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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.engine.sheet;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.styling.impl.engine.selectionstates.SelectionState;
import com.volantis.styling.impl.sheet.IndexableStyler;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerIteratee;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of {@link StylerList}.
 */
abstract class StylerListImpl
        implements IndexableStylerList {
    /**
     * A map associating elements with bitsets showing which stylers can match
     * those elements.
     */
    private transient Map elementsIndex;

    /**
     * A map associating classes with bitsets showing which stylers can match
     * those classes.
     */
    private transient Map classesIndex;

    /**
     * A bitset showing which stylers can match regardless of class/element.
     */
    private transient BitSet matchAll;

    /**
     * A bitset showing which stylers have a required state before matching.
     */
    private transient BitSet stateRequired;

    /**
     * A map associating styler states with bitsets showing which stylers
     * require them.
     */
    private transient Map statesIndex;

    /**
     * Flag to show whether the index is currently up to date.
     */
    private transient boolean indexed = false;

    /**
     * The underlying list of {@link Styler}s.
     */
    List list;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StylerListImpl(StylerList value) {
        list = new ArrayList();
        Iterator iterator = value.iterator();
        while (iterator.hasNext()) {
            Styler styler = (Styler) iterator.next();
            list.add(styler);
        }
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StylerListImpl() {
        list = new ArrayList();
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableStylerList createImmutableStylerList() {
        return new ImmutableStylerListImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableStylerList createMutableStylerList() {
        return new MutableStylerListImpl(this);
    }

    /**
     * Re-index the styler
     */
    protected void index() {
        if (!indexed) {
            if (elementsIndex == null) {
                elementsIndex = new HashMap();
            } else {
                elementsIndex.clear();
            }

            if (classesIndex == null) {
                classesIndex = new HashMap();
            } else {
                classesIndex.clear();
            }

            if (statesIndex == null) {
                statesIndex = new HashMap();
            } else {
                statesIndex.clear();
            }

            matchAll = new BitSet(list.size());
            stateRequired = new BitSet(list.size());

            for (int i = 0; i < list.size(); i++) {
                Object stylerObject = list.get(i);
                if (stylerObject instanceof IndexableStyler) {
                    IndexableStyler styler = (IndexableStyler) stylerObject;
                    String element = styler.getMatchableElement();
                    if (element != null) {
                        BitSet elementMatches =
                                (BitSet) elementsIndex.get(element);
                        if (elementMatches == null) {
                            elementMatches = new BitSet(list.size());
                            elementsIndex.put(element, elementMatches);
                        }
                        elementMatches.set(i);
                    }

                    String[] classesMatch = styler.getMatchableClasses();
                    if (classesMatch != null) {
                        for (int j = 0; j < classesMatch.length; j++) {
                            BitSet classMatches =
                                    (BitSet) classesIndex.get(classesMatch[j]);
                            if (classMatches == null) {
                                classMatches = new BitSet(list.size());
                                classesIndex.put(classesMatch[j], classMatches);
                            }
                            classMatches.set(i);
                        }
                    }

                    if (styler.isMatchAny()) {
                        matchAll.set(i);
                    }

                    SelectionState state =
                            styler.getRequiredSelectionState();
                    if (state != null) {
                        BitSet stateMatches =
                                (BitSet) statesIndex.get(state);
                        if (stateMatches == null) {
                            stateMatches = new BitSet(list.size());
                            statesIndex.put(state, stateMatches);
                        }
                        stateMatches.set(i);
                        stateRequired.set(i);
                    }
                } else {
                    matchAll.set(i);
                }
            }

            indexed = true;
        }
    }

    // Javadoc inherited
    public void lookupElement(BitSet indexRef, String element) {
        index();
        BitSet elementMatches = (BitSet) elementsIndex.get(element);
        if (elementMatches != null) {
            indexRef.or(elementMatches);
        }
    }

    // Javadoc inherited
    public void lookupClass(BitSet indexRef, String className) {
        index();
        BitSet classMatches = (BitSet) classesIndex.get(className);
        if (classMatches != null) {
            indexRef.or(classMatches);
        }
    }

    // Javadoc inherited
    public void lookupState(BitSet indexRef, SelectionState state) {
        index();
        BitSet statesMatches = (BitSet) statesIndex.get(state);
        if (statesMatches != null) {
            indexRef.or(statesMatches);
        }
    }

    // Javadoc inherited
    public BitSet stateRequired() {
        index();
        return (BitSet) stateRequired.clone();
    }

    // Javadoc inherited
    public List indexedList(BitSet indexRef) {
        index();
        return new BitMaskedList(list, indexRef);
    }

    // Javadoc inherited
    public BitSet createIndexRef() {
        index();
        return (BitSet) matchAll.clone();
    }

    /**
     * Invalidate the current index. This should be called by any methods that
     * modify the content of the list, and will result in the styler list being
     * re-indexed the next time index operations are carried out on it.
     */
    protected void invalidateIndex() {
        indexed = false;
    }

    // Javadoc inherited.
    public Iterator iterator() {
        // todo make this safe, wrap it so that remove does not work.
        return list.iterator();
    }

    // Javadoc inherited.
    public IterationAction iterate(StylerIteratee iteratee) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Styler styler = (Styler) iterator.next();
            IterationAction action = iteratee.next(styler);
            if (action == IterationAction.BREAK) {
                return action;
            }
        }

        return IterationAction.CONTINUE;
    }

    // Javadoc inherited.
    public IterationAction iterateIndexed(StylerIteratee iteratee, BitSet indexRef) {
        for (Iterator iterator = indexedList(indexRef).iterator();
             iterator.hasNext();) {
            Styler styler = (Styler) iterator.next();
            IterationAction action = iteratee.next(styler);
            if (action == IterationAction.BREAK) {
                return action;
            }
        }

        return IterationAction.CONTINUE;
    }

    // Javadoc inherited
    public void debug(DebugStylingWriter writer) {
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Styler styler = (Styler) iterator.next();
            writer.print(styler).newline();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
