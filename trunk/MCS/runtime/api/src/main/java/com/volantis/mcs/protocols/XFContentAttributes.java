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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XFContentAttributes.java,v 1.3 2002/09/09 10:23:41 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Jul-02    Steve           VBM:2002062401 - Created for implementation
 *                              of the xfcontent element.
 * 09-Jul-02    Mat             VBM:2002090502 - Changed to extend 
 *                              XFFormFieldAttributes as we now treat the 
 *                              content as a form field internally.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.Pane;

public class XFContentAttributes
        extends XFFormFieldAttributes {

    private OutputBuffer buffer;
    private Pane pane;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFContentAttributes() {
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
        setTagName("xfcontent");
        buffer = null;
        pane = null;
    }

    /**
     * Set the buffer holding the pane contents
     *
     * @param buffer The pane contents
     */
    public void setOutputBuffer(OutputBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Get the buffer holding the tag contents
     *
     * @return The value of the falseValues property.
     */
    public OutputBuffer getOutputBuffer() {
        return buffer;
    }

    /**
     * Set the pane to write the output to
     */
    public void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * Get the pane to write the output to
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
