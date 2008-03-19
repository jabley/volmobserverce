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
 * $Header: /src/voyager/com/volantis/mcs/protocols/PaneAttributes.java,v 1.24 2002/07/11 16:20:09 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header.
 * 09-Jul-01    Paul            VBM:2001062810 - Added top level parameter.
 * 23-Jul-01    Paul            VBM:2001070507 - Renamed setBorder method to
 *                              setBorderWidth so that it more closely matches
 *                              its use.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 26-Jul-01    Paul            VBM:2001072301 - Add a pane attribute.
 * 03-Aug-01    Kula            VBM:2001080102 Height property added to pane
 * 14-Sep-01    Allan           VBM:2001091103 - Added format property.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 10-Jul-02    Steve           VBM:2002040807 - Call setTagName to set the
 *                              name of the tag to 'pane' so that correct CSS
 *                              tags are generated.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.Pane;

/**
 * @mock.generate base="FormatAttributes"
 */
public class PaneAttributes
        extends FormatAttributes {

    private Pane pane;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public PaneAttributes() {
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
        setTagName("pane");
        pane = null;
    }

    /**
     * Set the pane property.
     *
     * @param pane The new value of the pane property.
     */
    public void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * Get the value of the pane property.
     *
     * @return The value of the pane property.
     */
    public Pane getPane() {
        return pane;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 15-Jun-05	8784/1	philws	VBM:2005061402 Port SE P800/P900 width attribute from 3.3.1

 14-Jun-05	8779/1	philws	VBM:2005061402 Port SE P800/P900 width rendering from 3.2.3

 14-Jun-05	8756/1	philws	VBM:2005061402 Provide markup layout width rendering for SE P800/P900 phones to resolve device CSS deficiency

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
