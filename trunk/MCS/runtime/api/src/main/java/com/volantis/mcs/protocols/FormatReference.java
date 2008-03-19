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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatReference.java,v 1.2 2002/11/25 15:23:48 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Oct-02    Chris W         VBM:2002111102 -  Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.NDimensionalIndex;

/**
 * When spatial and temporal format iterators are used, a reference to a pane
 * cannot simply consist of the pane's name as defined in the policy manager.
 * Instead the name of the pane in the policy manager is deemed to be the
 * pane's stem and the indices following this name are the tail. For instance
 * the pane reference "title.0" refers to the first instance of the title pane.
 * (Note that the form "stem{.x}" is deprecated.)
 *
 * <p>As spatial and temporal format iterators contain any single format we may
 * need to deal with multiple instances of grids, regions or forms as well as
 * panes (the contained format may itself have children). This class is
 * therefore called FormatReference and encapsulates the stem and tail of a
 * reference to a format.</p>
 *
 * @mock.generate
 */
public class FormatReference
        implements Cloneable {

    /**
     * The stem of the format reference
     */
    private String stem;

    /**
     * A NDimensionalIndex containing the tail of the format reference
     * in an easy to process format.
     */
    private NDimensionalIndex index;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param stem the stem name for the referenced instance. May not be null
     * @param tail the arbitrary dimensioned index for the instance. May not be
     *             null
     */
    public FormatReference(String stem, NDimensionalIndex tail) {
        if (stem == null) {
            throw new IllegalArgumentException("stem may not be null");
        } else if (tail == null) {
            throw new IllegalArgumentException("tail may not be null");
        }

        this.stem = stem;
        index = tail;
    }

    /**
     * Initializes the new instance.
     */
    public FormatReference() {
        super();
    }

    /**
     * Returns the index part of the reference (which describes the indices
     * associated with the reference). The value returned is the reference's
     * internal data. Any changes made to this will cause the reference to be
     * updated.
     *
     * @return the index for this reference
     */
    public NDimensionalIndex getIndex() {
        return index;
    }

    /**
     * Returns the stem for the reference.
     *
     * @return the stem for this reference
     */
    public String getStem() {
        return stem;
    }

    /**
     * Sets the stem.
     *
     * @param stem The stem to set
     */
    public void setStem(String stem) {
        this.stem = stem;
    }

    // javadoc inherited
    public Object clone() throws CloneNotSupportedException {
        FormatReference clone = (FormatReference) super.clone();

        clone.index = (NDimensionalIndex) index.clone();

        return clone;
    }

    // JavaDoc inherited
    public boolean equals(Object o) {
        if (o == null || !(o.getClass().equals(this.getClass()))) {
            return false;
        }

        if (this == o) {
            return true;
        }

        final FormatReference formatReference = (FormatReference) o;

        if ((stem != null && !stem.equals(formatReference.stem)) ||
                (stem == null && formatReference.stem != null)) {
            return false;
        }

        if ((index != null && !index.equals(formatReference.index)) ||
                (index == null && formatReference.index != null)) {
            return false;
        }

        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = (stem != null ? stem.hashCode() : 0);
        result = 29 * result + (index != null ? index.hashCode() : 0);

        return result;
    }

    // javadoc inherited
    public String toString() {
        return stem + ", " + index;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 06-May-04	3999/2	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 ===========================================================================
*/
