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
 * $Header: /src/voyager/com/volantis/mcs/papi/MontageElement.java,v 1.23 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 21-Dec-01    Paul            VBM:2001121702 - Modified initialisation to
 *                              match changes made to super class.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Ask protocol to generate
 *                              style sheets instead of the theme.
 * 31-Jan-02    Paul            VBM:2001122105 - Moved writeInitialHeader to
 *                              Volantis/StringProtocol.
 * 31-Jan-02    Paul            VBM:2001122105 - Updated to reflect changes
 *                              to protocols.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed page head as it is
 *                              no longer used.
 * 19-Feb-02    Paul            VBM:2001100102 - Update call to
 *                              initialiseCanvas.
 * 28-Feb-02    Paul            VBM:2002022804 - Moved the code which writes
 *                              the page into the protocols.
 * 04-Mar-02    Ian             VBM:2002022204 - Added code to call
 *       			pageContext.initialiseResponse ();
 * 05-Mar-02    Ian             VBM:2002022204 - Reworked to log errors in
 *                              catch.
 * 08-Mar-02    Paul            VBM:2002030607 - Removed some commented code
 *                              and changed names of protocol methods to make
 *                              them match their function better.
 * 11-Mar-02    Doug            VBM:2002011003 - Moved the redirect code into
 *                              the elementEndImpl method as segements must
 *                              be processed so that we can get hold of the
 *                              default segment.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 03-Apr-02    Adrian          VBM:2001102414 - pass null for brandName param
 *                              to initialiseCanvas in MarinerPageContext.
 * 25-Apr-02    Paul            VBM:2002042202 - Fixed montages.
 * 22-Jul-02    Ian             VBM:2002052804 - REmoved all references to old
 *                              style classes.
 * 28-Nov-02    Allan           VBM:2002110102 - Modified getDefaultSegmentSrc
 *                              to use retrieveFormat instead of getSegment.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Remove invocation of
 *                              initialiseResponse from elementStartImpl.
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
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.management.tracking.CanvasType;
import com.volantis.mcs.papi.MontageAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.layouts.SegmentInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * The montage element.
 */
public class MontageElementImpl
        extends PageElementImpl {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MontageElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(MontageElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.MontageAttributes pattributes;

    /**
     * The attributes passed to this element.
     */
    private MontageAttributes attributes;

    /**
     * Flag which if set indicates that the request has been redirected.
     */
    private boolean redirected;

    /**
     * Create a new <code>MontageElement</code>.
     */
    public MontageElementImpl() {
        pattributes = new com.volantis.mcs.protocols.MontageAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // javadoc inherited
    public int elementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        // @todo overrides the attributes member of this tag
        MontageAttributes attributes = (MontageAttributes) papiAttributes;

        // Get the internal context from the request context.
        pageContext = ContextInternals.getMarinerPageContext(context);

        // The MarinerPageContext has been initialised for the page but not for
        // the canvas so we need to initialise it here before we do anything
        // else.
        initialiseCanvas(pageContext, false,
                false,
                attributes.getId(),
                null,
                attributes.getTheme(),
                attributes.getLayoutName());

        // Push the style sheet associated with the montage onto the styling
        // engine before we try and process the element. This is required since
        // montage can itself have styles.
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        getThemeStyleSheets().pushAll(pageContext.getStylingEngine());

        return super.elementStart(context, papiAttributes);
    }

    // javadoc inherited
    public int elementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        int result = super.elementEnd(context, papiAttributes);

        // Now that montage has finished processing, remove the style sheet
        // associated with the montage element.
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        getThemeStyleSheets().popAll(pageContext.getStylingEngine());

        return result;
    }


    // Javadoc inherited from super class.
    protected int elementStartImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        try {
            // Get the protocol.
            VolantisProtocol protocol = pageContext.getProtocol();

            // Open the montage page.
            // @todo pattributes are not set here - does something else do it?
            protocol.openMontagePage(pattributes);

            return PROCESS_ELEMENT_BODY;
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            throw new PAPIException(e);
        }
    }

    /**
     * Return Montage type.
     */
    CanvasType getCanvasType() {
        return CanvasType.MONTAGE;
    }

    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {
        // If we have no page context, then we can't process anything
        if (pageContext == null) {
            return CONTINUE_PROCESSING;
        }

        try {
            // Get the protocol.
            VolantisProtocol protocol = pageContext.getProtocol();

            // If this device does not support aggregation then we
            // need to send a redirect to the default segment specified
            // in the layout.
            boolean supportsAggregation
                    = pageContext.getBooleanDevicePolicyValue("aggregation");

            if (!supportsAggregation) {

                MarinerURL requestURL = pageContext.getRequestURL(true);
                RuntimeDeviceLayout deviceLayout =
                        pageContext.getDeviceLayout();
                String defaultSegmentSrc = getDefaultSegmentSrc(deviceLayout,
                        pageContext);

                if (defaultSegmentSrc != null) {
                    // Resolve the defaultSegmentSrc relative to the current request URL
                    MarinerURL url =
                            new MarinerURL(requestURL, defaultSegmentSrc);

                    String destination = url.getExternalForm();

                    if (logger.isDebugEnabled()) {
                        logger.debug("MontageTag: device "
                                + pageContext.getDeviceName()
                                + "does not support aggregation."
                                + " Sending redirect to "
                                + destination);
                    }

                    pageContext.sendRedirect(url);

                    //	redirected = true;
                    return ABORT_PROCESSING;
                } else {
                    throw new PAPIException(exceptionLocalizer.format(
                            "default-segment-missing-no-aggregation",
                            new Object[]{pageContext.getDeviceName(),
                                    deviceLayout.getName()}));
                }
            }

            pageContext.endPhase1BeginPhase2();

            protocol.closeMontagePage(pattributes);
            return CONTINUE_PROCESSING;
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            throw new PAPIException(e);
        } catch (ProtocolException e) {
            logger.error("unexpected-exception", e);
            throw new PAPIException(e);
        }
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        pattributes.resetAttributes();

        attributes = null;
        redirected = false;

        super.elementReset(context);
    }

    /**
     * Get the src attribute of the default segment. This method should only
     * be called after the <segment/> tags in the montage canvas have been
     * processed.
     * <h2>
     * You MUST NOT change the protection level on this member.
     * </h2>
     *
     * @return The src attribute of the default segment, or null if there
     *         is no default segment specified, or the segment does not exist, or
     *         it does not have a src.
     */
    private String getDefaultSegmentSrc(
            RuntimeDeviceLayout deviceLayout,
            MarinerPageContext context) {

        String name = deviceLayout.getDefaultSegmentName();
        if (name == null) {
            logger.error("default-segment-missing");
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Default segment name is " + name);
        }

        Segment segment = (Segment)
                deviceLayout.retrieveFormat(name, FormatType.SEGMENT);
        if (segment == null) {
            logger.error("segment-non-existant", new Object[]{name});
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Default segment is " + segment);
        }

        SegmentInstance segmentInstance
                = (SegmentInstance) context.getFormatInstance(segment,
                NDimensionalIndex.ZERO_DIMENSIONS);
        SegmentAttributes attributes = segmentInstance.getAttributes();

        if (attributes == null) {
            logger.error("segment-attributes-missing", new Object[]{name});
            return null;
        }

        LinkAssetReference object = attributes.getSrc();
        if (object == null) {
            logger.error("segment-src-missing", new Object[]{name});
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Src object is " + object);
        }

        String src = object.getURL();
        if (src == null) {
            logger.error("segment-no-src-object", new Object[]{name});
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Src is " + src);
        }

        return src;
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

 08-Nov-05	9990/3	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 07-Nov-05	10173/3	emma	VBM:2005103107 Supermerge required

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 30-Sep-05	9616/1	adrianj	VBM:2005092610 Fix for MCS capabilities requests in JSPs

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/3	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 05-Jul-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts again)

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4702/4	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/2	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
