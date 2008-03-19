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
package com.volantis.mcs.eclipse.ab.search.devices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.WorkbenchContentProvider;
import com.volantis.synergetics.ArrayUtils;
import com.volantis.synergetics.ArrayUtils;

/**
 * WorkbencContentProvider specialization that provides content from
 * resources and DeviceSearchMatch objects in a tree format.
 *
 * Much of this is based on
 * org.eclipse.search.internal.ui.text.FileTreeContentProvider
 */
public class DeviceSearchResultContentProvider
        extends WorkbenchContentProvider {

    /**
     * The DeviceSearchResult associated with this DeviceSearchContentProvider.
     */
    private DeviceSearchResult deviceSearchResult;

    /**
     * The AbstractTreeViewer that content is provided to.
     */
    private AbstractTreeViewer treeViewer;

    /**
     * A Map that maps parent resources to their children.
     */
    private Map childrenMap;


    /**
     * Construct a new DeviceSearchResultContentProvider for a given
     * AbstractTreeViewer.
     * @param viewer the AbstractTreeViewer.
     */
    DeviceSearchResultContentProvider(AbstractTreeViewer viewer) {
        treeViewer = viewer;
    }

    /**
     * Set the DeviceSearchResult for this content provider.
     * @param result the DeviceSearchResult.
     */
    public void setDeviceSearchResult(DeviceSearchResult result) {
        assert(result != null);
        this.deviceSearchResult = result;
    }


    // javadoc inherited
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }


    // javadoc inherited
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput instanceof DeviceSearchResult) {
            initialize((DeviceSearchResult) newInput);
        }
    }

    /**
     * Initialize this DeviceSearchContentProvider with a given
     * DeviceSearchResult.
     * @param result the DeviceSearchResult.
     */
    protected synchronized void initialize(DeviceSearchResult result) {
        childrenMap = new HashMap();
        if (result != null) {
            Object[] elements = result.getElements();
            for (int i = 0; i < elements.length; i++) {
                insert(elements[i], false);
            }
        }
    }

    /**
     * Insert a given child into the tree at the right place.
     * @param child the child
     * @param refreshViewer flag indicating whether or not to refresh the
     * viewer.
     */
    protected void insert(Object child, boolean refreshViewer) {
        Object parent = getParent(child);
        boolean childInserted = true;
        while (parent != null && childInserted) {
            childInserted = insertChild(parent, child);
            if (childInserted) {
                if (refreshViewer) {
                    treeViewer.add(parent, child);
                }
                child = parent;
                parent = getParent(child);
            } else {
                if (refreshViewer) {
                    treeViewer.refresh(parent);
                }
            }
        }

        if(childInserted && insertChild(deviceSearchResult, child)) {
            if (refreshViewer) {
                treeViewer.add(deviceSearchResult, child);
            }
        }
    }

    /**
     * Associate a child with a parent.
     *
     * @param parent the parent
     * @param child the child.
     * @return true if the child already was a child of parent.
     */
    private boolean insertChild(Object parent, Object child) {
        Set children = (Set) childrenMap.get(parent);
        if (children == null) {
            children = new HashSet();
            childrenMap.put(parent, children);
        }
        return children.add(child);
    }

    /**
     * Remove an element from the tree
     * @param element the element to remove
     * @param refreshViewer flag indicating whether or not to refresh the
     * viewer.
     */
    protected void remove(Object element, boolean refreshViewer) {
        if (hasChildren(element)) {
            if (refreshViewer) {
                treeViewer.refresh(element);
            }
        } else {
            if (deviceSearchResult.getMatchCount(element) == 0) {
                childrenMap.remove(element);
                Object parent = getParent(element);
                if (parent != null) {
                    removeFromSiblings(element, parent);
                    remove(parent, refreshViewer);
                } else {
                    removeFromSiblings(element, deviceSearchResult);
                    if (refreshViewer) {
                        treeViewer.refresh();
                    }
                }
            } else {
                if (refreshViewer) {
                    treeViewer.refresh(element);
                }
            }
        }
    }

    /**
     * Remove an element from a set of siblings.
     * @param element the element to remove
     * @param parent the parent of the siblings
     */
    private void removeFromSiblings(Object element, Object parent) {
        Set siblings = (Set) childrenMap.get(parent);
        if (siblings != null) {
            siblings.remove(element);
        }
    }

    // javadoc inherited
    public Object[] getChildren(Object parentElement) {
        Set children = (Set) childrenMap.get(parentElement);
        if (children == null)
            return ArrayUtils.EMPTY_ARRAY;
        return children.toArray();
    }

    // javadoc inherited
    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    // javadoc inherited
    public synchronized void elementsChanged(Object[] updatedElements) {
        for (int i = 0; i < updatedElements.length; i++) {
            if (deviceSearchResult.getMatchCount(updatedElements[i]) > 0)
                insert(updatedElements[i], true);
            else
                remove(updatedElements[i], true);
        }
    }

    // javadoc inherited
    public void clear() {
        initialize(deviceSearchResult);
        treeViewer.refresh();
    }

    // javadoc inherited
    public Object getParent(Object element) {
        Object parent = null;
        if (element instanceof DeviceSearchMatch) {
            parent = ((DeviceSearchMatch) element).getFile().getParent();
        } else if (element instanceof IContainer &&
                !(element instanceof IProject)) {
            parent = ((IContainer) element).getParent();
        }
        return parent;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	8925/1	allan	VBM:2005062308 Move ArrayUtils to Synergetics

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/5	allan	VBM:2004070608 Device search

 08-Oct-04	5557/3	allan	VBM:2004070608 Device search

 ===========================================================================
*/
