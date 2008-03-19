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
 * $Header: /src/voyager/com/volantis/mcs/layouts/DeviceLayoutReplicator.java,v 1.7 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jan-02    Steve            VBM:2002011412 - Created
 * This class traverses the layout tree for a fragment containing a replica format.
 * The enclosing fragment is scanned for all items that can be replicated and the
 * items are written into the namespaces for the enclosing fragment.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 10-Dec-02    Allan           VBM:2002110102 - Modified the add... methods
 *                              to use addFormat() and retrieveFormat().
 *                              Modified vist(Pane, Object) to use addFormat()
 *                              instead of addPane().
 * 05-Mar-03    Chris W         VBM:2003022706 - In FragmentVisitor.fixChildren
 *                              remove the child from the device layout before
 *                              adding it to fragment. (The fragment's format
 *                              scope is created by copying the fragments from
 *                              the device layout format scope.)
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() methods, and rethrow
 *                              FormatVisitorException as
 *                              RuntimeWrappingException in replicate().
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.shared.throwable.ExtendedRuntimeException;

/**
 * This is a class that fixes up a device layout for replica formats.
 */
public class DeviceLayoutReplicator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(DeviceLayoutReplicator.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(DeviceLayoutReplicator.class);

    private Fragment enclosingFragment;

    public DeviceLayoutReplicator() {
        // nothing to do
    }

    /**
     * Replicate replica formats in a Layout
     * @param layout The Layout to replicate.
     * @todo - when replication is fixed/removed either make this
     * more efficient or replace it with something better (or remove it
     * altogether).
     **/
    public void replicate(Layout layout) {
        Format root = layout.getRootFormat();
        if (root == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Empty Layout");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Replicating layout " + layout);
            }
            try {
                root.visit(new ReplicaVisitor(), layout);

                // Now fix the layout so that the formats that are in
                // Fragments are in the Fragments FormatScope.
                // todo - it's this and the previous visit that are in-efficient.
                root.visit(new FragmentVisitor(), layout);
            } catch (FormatVisitorException e) {
                // We don't expect any exceptions, but just in case...
                // MCSUT0004X="Unexpected exception"
                throw new ExtendedRuntimeException(
                            exceptionLocalizer.format("unexpected-exception"),
                            e);
            }
        }
    }

    protected void visitReplica(Replica replica, Layout parentLayout)
                throws FormatVisitorException {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting replica " + replica);
        }

        enclosingFragment = replica.getEnclosingFragment();
        if (enclosingFragment == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Replica is not enclosed by a fragment.");
            }
            return;
        }

        // Find the Format that this is a replica of.
        replica.findReplicant(parentLayout);
        Format format = replica.getFormat();
        if (format == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to find replicant in layout " + parentLayout);
            }
            return;
        }

        // Now add anything of interest in the replicants namespace to
        // the namespace of the enclosing fragment.
        format.visit(new NamespaceVisitor(), parentLayout);

    }

    protected void addColumnIteratorPane(ColumnIteratorPane pane, Layout layout) {
        String name = pane.getName();
        enclosingFragment.addFormat(
                    layout.retrieveFormat(name, FormatType.PANE));
    }

    protected void addRowIteratorPane(RowIteratorPane pane, Layout layout) {
        String name = pane.getName();
        enclosingFragment.addFormat(
                    layout.retrieveFormat(name, FormatType.PANE));
    }

    protected void addFormat(Pane pane, Layout layout) {
        String name = pane.getName();
        enclosingFragment.addFormat(
                    layout.retrieveFormat(name, FormatType.PANE));
    }

    protected void addForm(Form form, Layout layout) {
        String name = form.getName();
        enclosingFragment.addFormat(
                    layout.retrieveFormat(name, FormatType.FORM));
    }

    //////////////////////////////////////////////////////////////
    // This class visits all replica Formats in a layout.
    //////////////////////////////////////////////////////////////
    protected class ReplicaVisitor extends FormatVisitorAdapter {

        public boolean visit(Replica replica, Object object)
                    throws FormatVisitorException {
            Layout layout = (Layout) object;
            visitReplica(replica, layout);
            return false;
        }
    }

    /**
     * When a Fragment is encountered we need to put all of its children
     * into this Fragments FormatScope unless one of its children is a
     * Fragment in which case its children need to go in its FormatScope...
     */
    protected class FragmentVisitor extends FormatVisitorAdapter {

        /**
         * Recursively put children of a format into the FormatScope
         * of a fragment and remove it from the device layout.
         * @param format The format whose children to fix.
         * @param fragment The fragment that the children belong to.
         * @param layout The layout that the children will be removed from.
         */
        private void fixChildren(Format format, Fragment fragment,
                                 Layout layout) {
            int childCount = format.getNumChildren();
            for (int i = 0; i < childCount; i++) {
                Format child = format.getChildAt(i);
                if (child != null) {
                    if (child.getName() != null) {
                        boolean exists =
                                    fragment.retrieveFormat(child.getName(),
                                                            child.getFormatType()) != null;
                        if (!exists) {
                            layout.removeFormat(child);
                            fragment.addFormat(child);
                        }
                    }

                    if (child instanceof Fragment) {
                        fixChildren(child, (Fragment) child, layout);
                    } else {
                        fixChildren(child, fragment, layout);
                    }
                }
            }
        }

        /**
         * Fix the children of a Fragment
         * @param fragment
         * @param object
         * @return boolean flag
         */
        public boolean visit(Fragment fragment, Object object) {
            Layout layout = (Layout) object;
            fixChildren(fragment, fragment, layout);

            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // This class visits all Formats that should be replicated to a fragment.
    ///////////////////////////////////////////////////////////////////////////
    protected class NamespaceVisitor extends FormatVisitorAdapter {

        public boolean visit(Form form, Object object)
                    throws FormatVisitorException {
            Layout layout = (Layout) object;
            addForm(form, layout);
            return form.visitChildren(this, object);
        }

        public boolean visit(Pane pane, Object object) {
            Layout layout = (Layout) object;
            addFormat(pane, layout);
            return false;
        }

        public boolean visit(RowIteratorPane pane, Object object) {
            Layout layout = (Layout) object;
            addRowIteratorPane(pane, layout);
            return false;
        }

        public boolean visit(ColumnIteratorPane pane, Object object) {
            Layout layout = (Layout) object;
            addColumnIteratorPane(pane, layout);
            return false;
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 26-Mar-04	3637/1	steve	VBM:2003100901 Patched from Proteus2

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
