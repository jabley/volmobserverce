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
 * $Header: /src/voyager/com/volantis/mcs/protocols/GridInstance.java,v 1.3 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Feb-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062810 - Sorted out the copyright.
 * 29-Oct-01    Paul            VBM:2001102901 - Moved from layouts package.
 * 28-Feb-02    Paul            VBM:2002022804 - Made release method
 *                              consistently call super.release after it has
 *                              released this class's resources.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;

/**
 * Contains all the state associated with a Grid in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="AbstractGridInstance"
 */
public class GridInstance
        extends AbstractGridInstance {

    /**
     * The attributes which are passed to the Grid related
     * protocol methods.
     */
    private final GridAttributes attributes;

    /**
     * The attributes which are passed to the GridRow related
     * protocol methods.
     */
    private final GridRowAttributes rowAttributes;

    /**
     * The attributes which are passed to the GridChild related
     * protocol methods.
     */
    private final GridChildAttributes childAttributes;

    /**
     * Create a new <code>GridInstance</code>.
     */
    public GridInstance(NDimensionalIndex index) {
        super(index);
        attributes = new GridAttributes();
        rowAttributes = new GridRowAttributes();
        childAttributes = new GridChildAttributes();
    }

    // Javadoc inherited from super class.
    public void initialise() {
        super.initialise();
    }

    /**
     * Get the attributes which are passed to the Grid related
     * protocol methods.
     * @return The attributes which are passed to the Grid related
     * protocol methods.
     */
    public GridAttributes getAttributes() {
        return attributes;
    }

    /**
     * Get the attributes which are passed to the GridRow related
     * protocol methods.
     * @return The attributes which are passed to the GridRow related
     * protocol methods.
     */
    public GridRowAttributes getRowAttributes() {
        return rowAttributes;
    }

    /**
     * Get the attributes which are passed to the GridChild related
     * protocol methods.
     * @return The attributes which are passed to the GridChild related
     * protocol methods.
     */
    public GridChildAttributes getChildAttributes() {
        return childAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 ===========================================================================
*/
