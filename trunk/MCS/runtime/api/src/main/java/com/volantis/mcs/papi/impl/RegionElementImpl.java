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
 * $Header: /src/voyager/com/volantis/mcs/papi/RegionElement.java,v 1.8 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Nov-01    Paul            VBM:2001102403 - Created.
 * 19-Nov-01    Paul            VBM:2001110202 - Skip the region if the page
 *                              is fragmented and it is not part of the
 *                              current fragment.
 * 19-Dec-01    Paul            VBM:2001120506 - Renamed from
 *                              tags.RegionElement and made compatible with
 *                              PAPI elements.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.RegionAttributes;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * An initial implementation of a PAPI style element class.
 * It has no state, so only one instance needs to be created. It may be
 * advisable to add some state in which case it needs to be managed.
 */
public class RegionElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(RegionElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    RegionElementImpl.class);

    /**
     * The RegionInstance which this element uses.
     */
    private RegionInstance regionInstance;

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        RegionAttributes attributes = (RegionAttributes) papiAttributes;

        if (logger.isDebugEnabled()) {
            logger.debug("RegionElement start " + attributes.getName());
        }

        String regionName = attributes.getName();

        if (regionName == null) {
            logger.error("region-name-missing");
            throw new PAPIException(
                    exceptionLocalizer.format("region-name-missing"));
        }

        FormatReference formatRef =
                FormatReferenceParser.parse(regionName, pageContext);
        Region region = pageContext.getRegion(formatRef.getStem());
        NDimensionalIndex regionIndex = formatRef.getIndex();

        if (region == null) {
            logger.info("region-missing", new Object[]{attributes.getName()});
            skipped = true;
            return SKIP_ELEMENT_BODY;
        }

        regionInstance = (RegionInstance) pageContext.getFormatInstance(
                region, regionIndex);
        if (regionInstance.ignore()) {
            skipped = true;
            return SKIP_ELEMENT_BODY;
        }

        pageContext.pushContainerInstance(regionInstance);

        // Fake an open element so that whitespace is handled normally since we
        // have no element to represent pane at the moment.
        pageContext.getCurrentOutputBuffer()
                .handleOpenElementWhitespace();

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        // Fake a close element so that whitespace is handled normally since we
        // have no element to represent pane at the moment.
        pageContext.getCurrentOutputBuffer()
                .handleCloseElementWhitespace();

        RegionAttributes attributes = (RegionAttributes) papiAttributes;

        if (logger.isDebugEnabled()) {
            logger.debug("RegionElement end " + attributes.getName());
        }

        if (skipped) {
            return CONTINUE_PROCESSING;
        }

        pageContext.popContainerInstance(regionInstance);

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        regionInstance = null;
        skipped = false;

        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 24-May-05	8123/7	ianw	VBM:2005050906 Fix accurev merge issues

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 05-Apr-05	7459/3	tom	VBM:2005032101 Added SmartClientSkin protocol

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/7	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
