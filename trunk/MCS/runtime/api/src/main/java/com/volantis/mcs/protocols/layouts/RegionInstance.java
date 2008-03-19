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
 * $Header: /src/voyager/com/volantis/mcs/protocols/RegionInstance.java,v 1.16 2003/04/29 09:46:34 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Nov-01    Paul            VBM:2001102403 - Created.
 * 19-Nov-01    Paul            VBM:2001110202 - Added method to test whether
 *                              the region should be ignored or not, similar
 *                              to that for pane.
 * 03-Dec-01    Paul            VBM:2001102403 - Made isEmptyImpl method return
 *                              true if the list of DeviceLayoutContexts is
 *                              empty.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 14-Feb-02    Paul            VBM:2002021203 - Added support for inserting
 *                              ssi include statements in the list of
 *                              inclusions.
 * 15-Feb-02    Paul            VBM:2002021203 -  Renamed server side include
 *                              methods to content component include.
 * 27-Feb-02    Mat             VBM:2002021203 - Renamed
 *                              contentComponentInclude to addWSDirective.
 * 08-Mar-02    Paul            VBM:2002030607 - Fixed NullPointerException
 *                              which can happen if a previous error has
 *                              occurred.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Changed the method
 *                              addWSDirective to addInclusionReference.
 * 15-Jul-02    Steve           VBM:2002071103 - Commented out the body of
 *                              ignoreImpl so that the method always returns
 *                              false. Regions are only rendered if they are
 *                              in the current fragment which is incorrect if
 *                              they have been replicated inside a fragment.
 * 31-Oct-02    Sumit           VBM:2002111103 - isEmpty and isEmptyImpl now
 *                              take FormatInstanceReferences in order to 
 *                              identify the object they must clean up#
 * 09-Jan-02    Sumit           VBM:2002112201 - Instead of releasing DLC back
 *                              to pool reinitialise() sets the DLC to null
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 29-Apr-03    Adrian          VBM:2003031807 - Updated reinitialise() to 
 *                              check for an instance of DeviceLayoutContext 
 *                              since the Object is then cast to a 
 *                              DeviceLayoutContext. Previously it was checked 
 *                              that the Object was not a String. Also removed 
 *                              a couple of redundant lines of code from the 
 *                              method. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.RegionContent;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Contains all the state associated with a Region in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="ContainerInstanceImpl"
 */
public class RegionInstance extends ContainerInstanceImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(RegionInstance.class);

    /**
     * The name of the element that references a {@link RegionContent} instance.
     */
    static final String REGION_CONTENT_ELEMENT = "REGION CONTENT ELEMENT";

    /**
     * The output buffer for the region
     */
    private DOMOutputBuffer outputBuffer;

    /**
     * A count of the number of region content instances added to this
     * instance.
     */
    private int regionContentCount;

    /**
     * Create a new <code>RegionInstance</code>.
     */
    public RegionInstance(NDimensionalIndex index) {
        super(index);
    }

    // Javadoc inherited.
    public OutputBuffer getCurrentBuffer() {
        return getCurrentBuffer(true);
    }

    private OutputBuffer getCurrentBuffer(boolean create) {
        if (outputBuffer == null && create) {
            outputBuffer = (DOMOutputBuffer) context.allocateOutputBuffer();
        }

        return outputBuffer;
    }

    /**
     * Add the specified content to the list.
     * 
     * @param content The RegionContent to add to the list.
     */
    public void addRegionContent(RegionContent content) {

        DOMOutputBuffer buffer = (DOMOutputBuffer) getCurrentBuffer();
        Element element = buffer.addElement(REGION_CONTENT_ELEMENT);
        element.setAnnotation(content);

        regionContentCount += 1;

        if (logger.isDebugEnabled()) {
            logger.debug("Added content " + content + " to " + this +
                         " at " + index);
        }
    }

    public RegionContent getRegionContent() {
        boolean containsNestedContent = (regionContentCount != 0);
        return new EmbeddedRegionContent(outputBuffer, containsNestedContent);
    }

    /**
     * Get the number of region content instances that have been added to the
     * region instance.
     *
     * @return The number of region content instances that have been added to
     *         the region instance.
     */
    public int getRegionContentCount() {
        return regionContentCount;
    }

    /**
     * Get the region to which this instance applies.
     * 
     * @return The region to which this instance applies.
     */
    public Region getRegion() {
        
        return (Region) format;
    }

    // Javadoc inherited from super class.
    protected boolean isEmptyImpl() {
        return outputBuffer == null || outputBuffer.isEmpty();
    }

    // Javadoc inherited.
    protected boolean ignoreImpl() {
        return format.isSkippable(index);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	10090/2	pabbott	VBM:2005103105 White space collapse problem

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/3	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/7	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-Jun-04	4698/1	geoff	VBM:2003061912 RegionContent should not store a MarinerPageContext

 19-Jun-03	407/1	steve	VBM:2002121215 Flow elements and PCData in regions

 ===========================================================================
*/
