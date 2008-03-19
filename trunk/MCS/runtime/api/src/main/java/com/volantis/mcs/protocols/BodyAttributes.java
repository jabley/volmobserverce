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
 * $Header: /src/voyager/com/volantis/mcs/protocols/BodyAttributes.java,v 1.12 2002/06/06 10:29:17 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 11-Mar-02    Paul            VBM:2001122105 - Added a link to the
 *                              CanvasAttributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 06-Jun-02    Adrian          VBM:2002040808 - changed tag name to canvas.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * This class should be removed and CanvasAttributes should be used instead.
 * Currently we make the canvas attributes available from this class.
 */
public class BodyAttributes
        extends MCSAttributes {


    /**
     * The canvas attributes.
     */
    private CanvasAttributes canvasAttributes;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public BodyAttributes() {
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

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("canvas");

        canvasAttributes = null;
    }

    /**
     * Set the value of the canvasAttributes property.
     *
     * @param canvasAttributes The new value of the canvasAttributes property.
     */
    public void setCanvasAttributes(CanvasAttributes canvasAttributes) {
        this.canvasAttributes = canvasAttributes;
    }

    /**
     * Get the value of the canvasAttributes property.
     *
     * @return The value of the canvasAttributes property.
     */
    public CanvasAttributes getCanvasAttributes() {
        return canvasAttributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
