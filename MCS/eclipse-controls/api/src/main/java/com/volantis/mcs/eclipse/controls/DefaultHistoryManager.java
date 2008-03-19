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

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import java.util.ArrayList;

/**
 * This is a generic history manager
 * It stores history items as Strings in array. The most recently added item
 * is stored at index [0].
 * The array can be stored into an Eclipse session.
 * It is up to the owner of this class to ensure that The QualifiedName used for the
 * uniqueID is unique.
 */
public class DefaultHistoryManager implements HistoryManager {

    /**
     * The key for this history manager, built up by the constructor.
     */
    private final QualifiedName uniqueId;


    /**
     * A list of history items.
     */
    private ArrayList history;

    /**
     * Constructor which takes a unique QuallifiedName which this manager will use as a key
     * to store and retrieve its history in an Eclipse session. It is up to the owner of
     * this class to ensure that the key is indeed unique.
     * @param uniqueId - A unique ID used by this class as a key for session storage
     * and retrieval
     */
    public DefaultHistoryManager(QualifiedName uniqueId) {
        this.uniqueId = uniqueId;
        populateList();
    }

    // javadoc inherited
    public void updateHistory(String value) {
        // determine this values position in the history
        int indexOfValue = history.indexOf(value);
        if (indexOfValue >= 0) {
            history.remove(indexOfValue);
            history.add(0, value);
        } else {
            history.add(0, value);
        }
    }

    // javadoc inherited
    public String[] getHistory() {
        String[] stringArray = new String[history.size()];
        for (int i = 0; i < history.size(); i++) {
            stringArray[i] = (String) history.get(i);
        }
        return stringArray;
    }

    /**
     * Populates the list from an Eclipse session if there is a valid list stored in the
     * session.
     */
    private void populateList() {
        try {
            Object o = getResource().getSessionProperty(this.uniqueId);
            if (o != null) {
                history = (ArrayList) o;
            } else {
                history = new ArrayList();
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }

    // javadoc inherited
    public void saveHistory() {
        try {
            getResource().setSessionProperty(
                    this.uniqueId,
                    history);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * Factory method which gets the root workspace in Eclipse.
     * @return the root IResource from the Eclipse workspace.
     */
    IResource getResource() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }

    /**
     * Gets a String representation of the history stored in this manager.
     * @return a String representation of the history.
     */
    public String toString() {
        String s = "DefaultHistoryManager";
        s += "\n uniqueID = " + uniqueId;
        for (int i = 0; i < history.size(); i++) {
            s += "\n" + history.get(i);
        }
        return s;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-04	4888/10	tom	VBM:2004070605 Created DefaultHistoryManager

 ===========================================================================
*/
