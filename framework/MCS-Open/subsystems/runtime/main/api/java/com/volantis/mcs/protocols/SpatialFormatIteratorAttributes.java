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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Nov-02    Sumit           VBM:2002111105 - Created
 * 05-Mar-03    Sumit           VBM:2003022605 - Added spatial properties
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * This class holds the protocol attributes of SpatialFormatIterators
 *
 * @mock.generate base="FormatAttributes"
 */
public class SpatialFormatIteratorAttributes extends FormatAttributes {

    /**
     * The number of columns that this spatial format iterator will have.
     */
    private int columns;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public SpatialFormatIteratorAttributes() {
        initialise();
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
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

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 15-Jun-05	8784/1	philws	VBM:2005061402 Port SE P800/P900 width attribute from 3.3.1

 14-Jun-05	8779/1	philws	VBM:2005061402 Port SE P800/P900 width rendering from 3.2.3

 14-Jun-05	8756/1	philws	VBM:2005061402 Provide markup layout width rendering for SE P800/P900 phones to resolve device CSS deficiency

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 14-Oct-04	5808/1	byron	VBM:2004101317 Support style classes: Runtime DOMProtocol/DeviceLayoutRenderer

 ===========================================================================
*/
