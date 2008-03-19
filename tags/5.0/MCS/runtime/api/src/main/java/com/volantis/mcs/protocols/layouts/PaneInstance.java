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
 * $Header: /src/voyager/com/volantis/mcs/protocols/PaneInstance.java,v 1.8 2003/03/05 10:10:28 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Feb-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 09-Jul-01    Paul            VBM:2001062810 - Cleaned up.
 * 29-Oct-01    Paul            VBM:2001102901 - Moved from layouts package.
 * 14-Nov-01    Paul            VBM:2001111402 - Removed some duplicate code.
 * 28-Feb-02    Paul            VBM:2002022804 - Generalised this object to
 *                              allow it to use any type of OutputBuffer.
 * 13-Mar-02    Paul            VBM:2002031301 - Added getOutputBuffer method
 *                              and renamed get/endContentBuffer to
 *                              get/endCurrentBuffer respectively.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 24-May-02    Steve           VBM:2002040809 - Set style class when the pane
 *                              attributes are requested.
 * 31-Oct-02    Sumit           VBM:2002111103- Removed getOutputBuffer,
 *                              changed getCurrentBuffer to receive a
 *                              FormatInstanceReference as a parameter.
 *                              endCurrentBuffer() now takes a FormatInstanceRef
 * 03-Mar-03    Sumit           VBM:2003022107 - overrode reinitialise to set
 *                              the attributes object to null;
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Contains all the state associated with a Pane in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="AbstractPaneInstance"
 */
public class PaneInstance extends AbstractPaneInstance {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(PaneInstance.class);

    /** The attributes which are passed to the Pane related protocol methods. */
    private PaneAttributes attributes;

    /**
     * Indicates how the protocol has decided to render the format to which
     * this instance is associated. The values are protocol specific. The
     * default value is to do nothing.
     */
    private PaneRendering rendering = PaneRendering.DO_NOTHING;

    /**
     * Create a new <code>PaneInstance</code>.
     */
    public PaneInstance(NDimensionalIndex index) {
        super(index);
    }

    /**
     * Get the attributes which are passed to the Pane related protocol
     * methods.
     * 
     * @return The attributes which are passed to the Pane related protocol
     *         methods.
     */
    public PaneAttributes getAttributes() {
        if (attributes == null) {
            attributes = new PaneAttributes();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Getting pane attributes for reference " + index);
//            logger.debug("Style class is " + getStyleClass());
        }

//        attributes.setStyleClass(getStyleClass());

        return attributes;
    }

    /**
     * Nothing required for normal panes.
     */
    public void endCurrentBuffer() {
    }

    /**
     * Set the value of the rendering property.
     * @param rendering The new value of the rendering property.
     */
    public void setRendering(PaneRendering rendering) {
        this.rendering = rendering;
    }

    /**
     * Get the value of the rendering property.
     * @return The value of the rendering property.
     */
    public PaneRendering getRendering() {
        return rendering;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 03-Mar-05	7277/1	philws	VBM:2005011906 Port pane styling fix from MCS 3.3

 03-Mar-05	7273/1	philws	VBM:2005011906 Ensure panes are thematically styled as per the requesting XDIME style class specifications

 06-Jan-05	6391/1	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/10	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/8	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/6	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 04-Nov-04	5871/4	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - rework issues

 03-Nov-04	5871/2	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 24-Jun-03	450/1	steve	VBM:2003060909 Style classes on Spatial Iterator panes

 ===========================================================================
*/
