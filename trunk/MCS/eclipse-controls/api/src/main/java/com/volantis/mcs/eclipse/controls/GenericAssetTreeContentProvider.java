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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.io.File;

/**
 * Provides the content for a filesystem tree.
 */
public class GenericAssetTreeContentProvider implements ITreeContentProvider {

    private static final Object[] EMPTY_LIST = new Object[0];

    /**
     * A super root. This is required because viewers do not display
     * the root node of a content provider.
     */
    private static final File SUPER_ROOT = new File("SUPER_ROOT"); //$NON-NLS-1$

    /**
     * The real root of the content provider.
     */
    private File actualRoot;

    /**
     * Cache the actual root's children in order to reduce garbage creation.
     */
    private final Object[] actualChildren;

    /**
     * Constructs a content provider that provides File abstractions
     * of the files in a filesystem. The root directory of the
     * filesystem is required.
     * @param root The file root of the content provider
     * @throws IllegalArgumentException if the root is null,
     * does not exist, cannot be read or is not a directory.
     */
    public GenericAssetTreeContentProvider(File root) {
        if (root == null || !root.exists() || !root.canRead()) {
            String name = ""; //$NON-NLS-1$
            String msg = null;
            if (root != null) {
                name = root.getAbsolutePath();
            }
            if (!root.canRead()) {
                msg = "Cannot read root: " + name; //$NON-NLS-1$
            }
            if (!root.exists()) {
                msg = "The root does not exist: " + name; //$NON-NLS-1$
            }
            if (!root.isDirectory()) {
                msg = "The root must be a directory: " + name; //$NON-NLS-1$
            }
            if (msg != null) {
                throw new IllegalArgumentException(msg);
            }
        }
        this.actualRoot = root;
        actualChildren = new Object[] { actualRoot };
    }


    // javadoc inherited
    public Object[] getChildren(Object parent) {
        Object[] children = EMPTY_LIST;
        File file = (File) parent;
        if (SUPER_ROOT.equals(parent)) {
            children = actualChildren;
        } else {
            if (file.isDirectory() && file.canRead()) {
                File[] files = file.listFiles();
                if (files != null) {
                    children = files;
                }
            }
        }
        return children;
    }


    // javadoc inherited
    public Object getParent(Object node) {
        if (node instanceof File) {
            File file = (File) node;
            return file.getParentFile();
        }
        return EMPTY_LIST;
    }


    // javadoc inherited
    public boolean hasChildren(Object node) {
        // getChildren never returns null.
        return getChildren(node).length > 0;
    }


    // javadoc inherited
    public Object[] getElements(Object node) {
        return getChildren(node);
    }

    /**
     * Get the root item for this hierarchy. For this implementation
     * a super root is inserted because the content viewer does not
     * display the root item. When the children are processed in
     * getChildren, the real root is inserted if the super root is
     * being processed.
     * @return the root item for this hierarchy.
     */
    public Object getRoot() {
        return SUPER_ROOT;
    }

    /**
     * Gets the real root of the content provider. See
     * #getRoot. Access to the real root is required
     * because the viewer implementation can change the
     * real root of the content provider.
     * @return The real root of the content provider
     */
    public Object getActualRoot() {
        return actualRoot;
    }


    // javadoc inherited
    public void dispose() {
    }


    // javadoc inherited
    public void inputChanged(Viewer viewer, Object o, Object o1) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Feb-04	3192/1	byron	VBM:2004021303 Image asset wizard - windows - cannot handle typing in C:\ or choosing C:

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 14-Nov-03	1835/19	pcameron	VBM:2003102801 Added GenericAssetCreation wizard page and supporting resources

 ===========================================================================
*/
