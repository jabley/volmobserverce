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
package com.volantis.mcs.eclipse.common;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * An {@link org.eclipse.jface.viewers.ITreeContentProvider} that decorates another
 * <code>ITreeContentProvider</code> with the intention of filtering the
 * nodes that are displayed in the associated tree.
 */
public class FilteringTreeContentProvider
            implements ITreeContentProvider {
    /**
     * The {@link org.eclipse.jface.viewers.ITreeContentProvider} that this decorator delegates to.
     */
    private ITreeContentProvider delegate;

    /**
     * A NodeFilter that can be used to filter out selected nodes.
     */
    private NodeFilter nodeFilter;

    /**
     * Creates a <code>FilteringTreeContentProvider</code> instance with the
     * given delegate
     * @param delegate the {@link ITreeContentProvider} whose nodes are to
     * be filtered
     * @param nodeFilter the {@link NodeFilter} that will be used to filter the
     * nodes the the the delegate provides via it's
     * {@link ITreeContentProvider#getElements} and
     * {@link ITreeContentProvider#getChildren} methods
     */
    public FilteringTreeContentProvider(ITreeContentProvider delegate,
                                        NodeFilter nodeFilter) {
        if (delegate == null) {
            throw new IllegalStateException("delegate cannot be null");
        }
        if (nodeFilter == null) {
            throw new IllegalArgumentException("nodeFilter cannot be null");
        }
        this.delegate = delegate;
        this.nodeFilter = nodeFilter;
    }

    // javadoc inherited
    public Object[] getChildren(Object parent) {
        Object[] children = delegate.getChildren(parent);
        return nodeFilter.filter(children);
    }

    // javadoc inherited
    public Object getParent(Object child) {
        return delegate.getParent(child);
    }

    // javadoc inherited
    public boolean hasChildren(Object parent) {
        return getChildren(parent).length != 0;
    }

    // javadoc inherited
    public Object[] getElements(Object root) {
        Object[] elements = delegate.getElements(root);
        return nodeFilter.filter(elements);
    }

    // javadoc inherited
    public void dispose() {
        delegate.dispose();
    }

    // javadoc inherited
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        delegate.inputChanged(viewer, oldInput, newInput);
    }

    /**
     * Interface that allows an array of nodes to be filterd
     */
    public interface NodeFilter {
        /**
         * Filter the nodes array
         * @param nodes the nodes to be filtered
         * @return the list of filtered nodes.
         */
        Object[] filter(Object[] nodes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
