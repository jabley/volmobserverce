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
 * $Header: /src/voyager/com/volantis/mcs/protocols/GridAttributes.java,v 1.19 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history and
 *                              renamed GridFormat to Grid.
 * 05-Jul-01    Paul            VBM:2001070509 - Force emacs to indent 2 spaces
 *                              when editing the file.
 * 23-Jul-01    Paul            VBM:2001070507 - Renamed setBorder method to
 *                              setBorderWidth so that it more closely matches
 *                              its use.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 14-Aug-01    Payal           VBM:2001080803 - Height property added to Grid,
 *                              added height attribute, added setHeight and 
 *                              getHeight method to set and get the value of
 *                              height property.
 * 14-Sep-01    Allan           VBM:2001091103 - Added format property.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

/**
 * @mock.generate base="BaseGridAttributes"
 */
public class GridAttributes
        extends BaseGridAttributes {

    private int columns;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public GridAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        columns = 0;
    }

    /**
     * Set the columns property.
     *
     * @param columns The new value of the columns property.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Get the value of the columns property.
     *
     * @return The value of the columns property.
     */
    public int getColumns() {
        return columns;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 15-Jun-05	8784/1	philws	VBM:2005061402 Port SE P800/P900 width attribute from 3.3.1

 14-Jun-05	8779/2	philws	VBM:2005061402 Port SE P800/P900 width rendering from 3.2.3

 14-Jun-05	8756/1	philws	VBM:2005061402 Provide markup layout width rendering for SE P800/P900 phones to resolve device CSS deficiency

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
