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
 * $Header: /src/voyager/com/volantis/mcs/protocols/AbstractPaneInstance.java,v 1.18 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Oct-01    Paul            VBM:2001102901 - Created.
 * 14-Nov-01    Paul            VBM:2001111402 - Moved the code which checks
 *                              whether a pane is going to be ignored from
 *                              VolantisTag to here.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 24-Jan-02    Steve           VBM:2002011412 - Removed check that an
 *                              enclosing fragment exists in ignorePaneImpl().
 * 28-Feb-02    Paul            VBM:2002022804 - Generalised this object to
 *                              allow it to use any type of OutputBuffer. Also
 *                              fixed problem in fragmented forms by ignoring
 *                              a pane if it is inside a form fragment which
 *                              is not the current one.
 * 04-Mar-02    Paul            VBM:2001101803 - Removed the check to see
 *                              whether the pane is inside a fragmented form as
 *                              the xf elements need to differentiate between
 *                              a pane which is not included because it is
 *                              not in the layout and a pane which is not
 *                              included because it is not in the current
 *                              form fragment.
 * 13-Mar-02    Paul            VBM:2002031301 - Added getOutputBuffer method
 *                              and renamed get/endContentBuffer to
 *                              get/endCurrentBuffer respectively.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 24-May-02    Steve           VBM:2002040809 - Added style class setting with
 *                              get and set methods.
 * 26-Jul-02    Steve           VBM:2002040809 - The reinitialise method was
 *                              not clearing the styleClass attribute. If the
 *                              pane was subsequently re-used then the old
 *                              style class setting would be re-used unless
 *                              the new pane had its own style class set.
 * 31-Oct-02    Sumit           VBM:2002111103 - Removed getOutputBuffer,
 *                              changed getCurrentBuffer to receive a
 *                              FormatInstanceReference and a create flag as
 *                              parameters. IsEmpty, isEmptyImpl and release()
 *                              now take FormatInstanceReferences as params
 * 13-Nov-02    Sumit           VBM:2002111301 - Added NDimensional.remove(fir) 
 *                              in the release(fir) method
 * 21-Nov-02    Chris W         VBM:2002110404 - Calls nextInCurrentDimension, 
 *                              a renamed method in NDimensionalContainer. Unit
 *                              tests of the NDimensionalContainer revealed
 *                              that the method was incorrectly named for the
 *                              functionality that it provided.
 * 29-Nov-02    Sumit           VBM:2002112806 - Removed isEmpty method up to
 *                              parent class as empty calculation by FIR
 *                              must be maintianed by all contexts
 * 13-Jan-03    Chris W         VBM:2003011311 - Added getNumCellsInDimension &
 *                              getContainerContents to return the number of
 *                              cells in a specified dimension. This is needed
 *                              to deal with FormatIterator that render a 
 *                              variable number of cells.
 * 07-Feb-03    Chris W         VBM:2003020609 - Added ignore(fir) to make
 *                              life easier for clients. ignore(fir) calls
 *                              ignore() and isSkippable().
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Abstract some of the common contextual information for Pane subclasses.
 *
 * @mock.generate base="ContainerInstanceImpl"
 */
public abstract class AbstractPaneInstance extends ContainerInstanceImpl {
       
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractPaneInstance.class);

    /**
     * The style class for the pane
     */
    private String styleClass;
    
    /**
     * Usually a pane only needs one output buffer. To save us from wading
     * through an NDimensionalContainer we use this shortcut.
     */
    private OutputBuffer zeroDOutputBuffer;
    
    /**
     * Create a new <code>AbstractPaneInstance</code>.
     */
    public AbstractPaneInstance(NDimensionalIndex index) {
        super(index);
    }
    
    // Javadoc inherited from super class.
    protected boolean isEmptyImpl() {
        
        OutputBuffer ob = getCurrentBuffer(false);
        if(ob!=null) {
            return ob.isEmpty();
        }
        return true;
    }
    
    // Javadoc inherited.
    public OutputBuffer getCurrentBuffer() {
        
        return getCurrentBuffer(true);
    }
    /**
     * Get the current OutputBuffer which contains the content of the Pane. 
     * Creates a buffer if required. Will always create a zero dim index buffer
     * 
     * @param create - create (or not) a buffer for the index if it does not 
     *      exist
     * @return The current OutputBuffer which contains the content of the Pane.
     */
    public OutputBuffer getCurrentBuffer(boolean create) {
        
        if(logger.isDebugEnabled()){
            logger.debug("Getting "+(create?"or creating":"")+" buffer with " +
                    "index " + index.toString());
        }

    	// Shortcut to return the output buffer if we have a regular pane
    	// i.e. one that is not enclosed by a format iterator.
        if (create && zeroDOutputBuffer == null) {
                zeroDOutputBuffer = context.allocateOutputBuffer();
                // Make sure that the content buffer trims any leading and                 
                // trailing spaces off the final content.
                zeroDOutputBuffer.setTrim(true);
        }
        return zeroDOutputBuffer;
    }

    // Javadoc inherited.
    protected boolean ignoreImpl() {
        if (format.isSkippable(index)) {
            return true;
        }

        MarinerPageContext pageContext = context.getMarinerPageContext();

        // Filter away panes if device usability is lower than filter value.
        String filterLevel =
                (String) format.getAttribute(
                        FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE);

        if (filterLevel != null) {
            try {
                int filter = Integer.parseInt(filterLevel);

                if (filter != 0) {
                    String keybUsability =
                            pageContext.getDevicePolicyValue("kbusability");
                    String handUsability =
                            pageContext.getDevicePolicyValue("hwrusability");

                    int keyb = Integer.parseInt(keybUsability);
                    int hand = Integer.parseInt(handUsability);

                    // Max of handrecognition and keyboard.
                    int level = (keyb > hand) ? keyb : hand;

                    if (logger.isDebugEnabled()) {
                        logger.debug(
                                "Pane filtering: Usability '"
                                + level
                                + "' Filter removes anything below filter '"
                                + filter
                                + "'");
                    }
                    if (filter > level) {
                        return true;
                    }
                }
            } catch (NumberFormatException e) {
                logger.error("keyboard-filter-error", new Object[]{filterLevel}, e);
            }
        }

        return false;
    }

    // TODO: later add a abstract getVolantisAttribute method and remove setStyleClass?
    // This allows us to set the style class directly on the underlying attributes.
    
    /**
     * Set the style class for the pane
     * 
     * @param style The name of the style class
     */
    public void setStyleClass(String style) {
        
        if( logger.isDebugEnabled() ){
            logger.debug( "Setting style class for reference " + index + " to " + style );
        }
        styleClass = style;
    }
    
    /**
     * Get the style class for the pane
     * 
     * @return the style class for the pane
     */
    public String getStyleClass() {
        
        return styleClass;
    }

    /**
     * Get the attributes for the pane.
     *
     * @return the attributes for the pane
     */
    public abstract PaneAttributes getAttributes();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/3	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 23-Feb-05	7114/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 23-Feb-05	7079/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/7	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 24-Jun-03	450/3	steve	VBM:2003060909 Use ZERO DIMENSION index

 24-Jun-03	450/1	steve	VBM:2003060909 Style classes on Spatial Iterator panes

 ===========================================================================
*/
