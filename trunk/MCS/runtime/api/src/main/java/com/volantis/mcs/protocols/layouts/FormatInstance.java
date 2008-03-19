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
 * $Header: /src/voyager/com/volantis/mcs/protocols/FormatInstance.java,v 1.13 2003/03/20 15:15:31 sumit Exp $
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
 * 31-Oct-01    Paul            VBM:2001102608 - Updated javadoc.
 * 02-Nov-01    Paul            VBM:2001102403 - Changed reference to a
 *                              MarinerPageContext to a reference to a 
 *                              DeviceLayoutContext as these objects are now
 *                              owned by a DeviceLayoutContext.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Added debugging.
 * 26-Apr-02    Paul            VBM:2002042205 - Added rendering property.
 * 31-Oct-02    Sumit           VBM:2002111103 - isEmpty and isEmptyImpl now
 *                              take FormatInstanceReferences in order to 
 *                              identify the object they must clean up
 * 21-Nov-02    Chris W         VBM:2002111103 - released attribute changed to
 *                              protected from private, unnecessary imports
 *                              removed and dimenions attribute reset to 0
 *                              when setDimensions is called.
 * 29-Nov-02    Sumit           VBM:2002112806 - Moved isEmpty method up to
 *                              this class as empty calculation by FIR
 *                              must be maintianed by all contexts. Added an
 *                              emptyChecked set for FIRs that have been verfied
 *                              as being empty
 * 07-Feb-03    Chris W         VBM:2003020609 - Moved isSkippable + helpers
 *                              from FormatIteratorFormatFilter to this class.
 *                              FormatIteratorFormatFilter has been removed.
 *                              This groups all the runtime checking for format
 *                              iterators in this class. 
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.NDimensionalContainer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;

/**
 * This abstract class is the base class for all the classes which contain the
 * instance for specific Formats.
 *
 * @mock.generate 
 */
public abstract class FormatInstance {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(FormatInstance.class);

    /**
     * The index of this format instance in the containing NDimensionalContainer.
     * <p>
     * Having this here enables us to pass around a format instance and have it
     * represent the format instance that it relates to simply.
     */
    protected final NDimensionalIndex index;

    /**
     * A flag which indicates whether this instance is empty of not. It is only
     * valid when emptyChecked is true.
     */
    private boolean empty;

    /**
     * The format which this object is associated with.
     */
    protected Format format;

    /**
     * The device layout context which this object is associated with.
     */
    protected DeviceLayoutContext context;

    /**
     * A boolean which indicates whether we have already checked whether a 
     * specific output buffer stored by this instance is empty.
     */
    private boolean emptyChecked;

    /**
     * Initialise.
     * 
     * @param index the index of this context within it's containing 
     * N dimensional container.
     */
    protected FormatInstance(NDimensionalIndex index) {
        this.index = index;
    }

    /**
     * Set the format.
     * @param format The format.
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * Get the format.
     * @return The format.
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Set the device layout context.
     * @param context The device layout context.
     */
    public void setDeviceLayoutContext(DeviceLayoutContext context) {
        this.context = context;
    }

    /**
     * @see #index
     */
    public NDimensionalIndex getIndex() {
        return index;
    }

    /**
     * This is called after the format and the context have been set and allows
     * the sub class to do any initialisation which depends on those values.
     */
    public void initialise() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialising resources associated with " + this);
        }
    }

    /**
     * Check to see whether this object is empty.
     * <p>
     * The isEmptyImpl actually does the work of checking, this method just
     * manages the caching of the result to try and reduce the amount of work
     * which is done.
     * </p>
     * @return True if this object is empty and false otherwise.
     */
    public boolean isEmpty() {
        
        if (!emptyChecked) {
            empty = isEmptyImpl();
            emptyChecked = true;
        }
        return empty;
    }

    /**
     * <p>
     * Returns true if this format instance is empty (ie has no content)
     * targeted in all avaialable indexes relative to the current index
     * obtained from {@link DeviceLayoutContext#getCurrentFormatIndex}.
     * </p>
     * <p>
     * Consider the following example:
     * </p>
     * <p>
     * Suppose the current index is 3 and we have a format that has
     * content targeted at the following indexes:
     * </p>
     * 1.1, 2.1, 5.0
     * <p>
     * This method would return true as there is no content at an index
     * starting with 3, eg 3.1, 3.2, 3.3 etc...
     * </p>
     * <p>
     * However, if the current index is 2, then this method would return
     * false as we have content at 2.1.
     * </p>
     *
     * @return true if this format instance is empty at indexes starting
     * with the current index;{@link DeviceLayoutContext#getCurrentFormatIndex}
     */
    protected boolean isEmptyRelativeToCurrentIndex() {

        boolean foundContent = false;

        NDimensionalContainer container =
                context.getFormatInstancesContainer(format);

        Iterator containerIter = container.iterator();
        // Iterate over all instances of the current format and check for
        // emptyness.
        while (!foundContent && containerIter.hasNext()) {
            FormatInstance currentFormatInstance =
                    (FormatInstance)containerIter.next();

            if (!currentFormatInstance.isEmpty() &&
                    currentFormatInstance.getIndex().startsWith(
                            context.getCurrentFormatIndex())) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Found content in: " + currentFormatInstance);
                    logger.debug("Format index: " +
                                 currentFormatInstance.getIndex());
                    logger.debug("Current Index: " +
                         context.getCurrentFormatIndex() + "\n");

                }
                foundContent = true;
            }
        }
        return !foundContent;
    }

    /**
     * Implement the sub class specific tests for emptiness.
     * @return True if this object is empty and false otherwise.
     */
    protected abstract boolean isEmptyImpl();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/10	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/8	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/6	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.
 
 04-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 04-Nov-04	5871/1	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - rework issues

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
