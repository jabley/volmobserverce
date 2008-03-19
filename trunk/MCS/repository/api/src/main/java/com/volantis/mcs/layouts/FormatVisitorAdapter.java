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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatVisitorAdapter.java,v 1.10 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Created
 * 29-Oct-01    Paul            VBM:2001102901 - Added an extra parameter to
 *                              all the visitor methods to make it compatible
 *                              with the interface.
 * 02-Nov-01    Paul            VBM:2001102403 - Added support for regions.
 * 24-Jan-02    Steve			VBM:2002011412 - Default implementation of replica
 *                              format visit method. Returns false.
 * 13-Feb-02    Steve			VBM:2001101803 - Default implementation of form
 *                              fragment format visit method. Visits any child
 *                              nodes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 28-Oct-02    Chris W         VBM:2002110511 - Default implementation of
 *                              spatial and temporal format iterators visit
 *                              method. Visits any child nodes.
 * 03-Jan-03    Allan           VBM:2003010303 - Added visitFormat() and
 *                              visitFormatChildren(). Updated all the visit()
 *                              methods to return either visitFormat() or
 *                              visitFormatChildren().
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

/**
 * An implementation of FormatVisitor which visits every format but does not
 * do anything. This is useful when you need to create a simple visitor which
 * does not implement every method.
 */
public abstract class FormatVisitorAdapter
        implements FormatVisitor {

    /**
     * Generic method called by all the visit methods implemented in this
     * adapter.
     * @param format The Format being visited.
     * @param object A general use object.
     * @return true if visitor traversal should terminate and false otherwise.
     */
    public boolean visitFormat(Format format, Object object) {
        return false;
    }

    /**
     * Generic method called by all the visit methods implemented in this
     * adapter whose associated Format can have children. Note that is
     * implementation visits children in the order the exist in the hierarchy
     * as opposed to visiting the deepest child first and then working back
     * up the hierarchy.
     * @param format The Format being visited.
     * @param object A general use object.
     * @return true if visitor traversal should terminate and false otherwise.
     */
    public boolean visitFormatChildren(Format format, Object object)
            throws FormatVisitorException {
        boolean finished = visitFormat(format, object);

        if(finished) {
            return true;
        } else {
            return format.visitChildren(this, object);
        }
    }

    // Javadoc inherited.
    public boolean visit(ColumnIteratorPane pane, Object object) {
        return visitFormat(pane, object);
    }

    // Javadoc inherited.
    public boolean visit(DissectingPane pane, Object object) {
        return visitFormat(pane, object);
    }

    // Javadoc inherited.
    public boolean visit(Form form, Object object)
            throws FormatVisitorException {
        return visitFormatChildren(form, object);
    }

    // Javadoc inherited.
    public boolean visit(Fragment fragment, Object object)
            throws FormatVisitorException {
        return visitFormatChildren(fragment, object);
    }

    // Javadoc inherited.
    public boolean visit(FormFragment formFragment, Object object)
            throws FormatVisitorException {
        return visitFormatChildren(formFragment, object);
    }

    // Javadoc inherited.
    public boolean visit(Grid grid, Object object)
            throws FormatVisitorException {
        return visitFormatChildren(grid, object);
    }

    // Javadoc inherited.
    public boolean visit(Pane pane, Object object) {
        return visitFormat(pane, object);
    }

    // Javadoc inherited.
    public boolean visit(RowIteratorPane pane, Object object) {
        return visitFormat(pane, object);
    }

    // Javadoc inherited.
    public boolean visit(Region region, Object object) {
        return visitFormat(region, object);
    }

    // Javadoc inherited.
    public boolean visit(Segment segment, Object object) {
        return visitFormat(segment, object);
    }

    // Javadoc inherited.
    public boolean visit(SegmentGrid segmentGrid, Object object)
            throws FormatVisitorException {
        return visitFormatChildren(segmentGrid, object);
    }

    // Javadoc inherited.
    public boolean visit(SpatialFormatIterator spatial, Object object)
            throws FormatVisitorException {
        return visitFormatChildren(spatial, object);
    }

    // Javadoc inherited.
    public boolean visit(TemporalFormatIterator temporal, Object object) 
            throws FormatVisitorException {
        return visitFormatChildren(temporal, object);
    }

    // Javadoc inherited.
    public boolean visit(Replica replica, Object object)
            throws FormatVisitorException {
        return visitFormat(replica, object);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
