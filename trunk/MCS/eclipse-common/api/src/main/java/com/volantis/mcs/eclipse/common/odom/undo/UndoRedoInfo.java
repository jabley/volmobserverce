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

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;

import org.jdom.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * A holder for a memento along with the ODOM xpaths
 * for changed elements.
 */
public class UndoRedoInfo {

    /**
     * @see #getChangedNodesXPaths()
     */
    private final List changedNodesXPaths;

    /**
     * @see #getMemento()
     */
    private final UndoRedoMemento memento;


    /**
     * Constructor friend of UndoRedoUnitOfWork
     *
     * @param xpaths  list of XPaths providec
     * @param memento held
     */
    UndoRedoInfo(List xpaths, UndoRedoMemento memento) {
        this.changedNodesXPaths = xpaths;
        this.memento = memento;
    }


    /**
     * @return a List of distinct {@link com.volantis.mcs.xml.xpath.XPath} for nodes that have been
     *         added/changed with each single change event
     *         (paths for event that removed a node are omitted)
     */
    public List getChangedNodesXPaths() {
        return changedNodesXPaths;
    }


    /**
     * @return the memento object associated with each UndoRedo UnitOfWork
     */
    public UndoRedoMemento getMemento() {
        return memento;
    }


    /**
     * Converts the changedNodeXPaths into a list of ODOM Elements
     *
     * @param document - the context for evaluation of the XPaths
     * @return A list of ODOM Elements
     * @throws com.volantis.mcs.xml.xpath.XPathException rethrown from the underlying XPath implementation
     */
    public List getChangedElements(Document document) throws XPathException {
        List result = new ArrayList(changedNodesXPaths.size());

        for (int i = 0; i < changedNodesXPaths.size(); i++) {
            XPath xPath = (XPath) changedNodesXPaths.get(i);

            while (xPath != null) {
                ODOMObservable o = (ODOMObservable) xPath.selectSingleNode(
                        document);

                if (o != null) {
                    if (o instanceof ODOMElement) {
                        result.add(o);
                    } else {
                        result.add(o.getParent());
                    }
                    break; //exit while loop
                } else {
                    xPath = xPath.getParent();
                }
            }

        }

        return result;
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

 17-Feb-04	2988/3	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 ===========================================================================
*/
