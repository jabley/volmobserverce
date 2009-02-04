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
 * $Header: /src/voyager/com/volantis/mcs/papi/CanvasElement.java,v 1.44 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 19-Nov-01    Paul            VBM:2001110202 - Call initialiseCanvas instead
 *                              of initialise in MarinerPageContext.
 * 21-Nov-01    Payal           VBM:2001111902 - Modified method
 *                              writeInitialHeader to add flag to doComment(),
 *                              set to false to always write the comments
 *                              to the page.
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 29-Nov-01    Paul            VBM:2001112805 - Added support for
 *                              fragmentation and dissection.
 * 03-Dec-01    Paul            VBM:2001120301 - Removed comment about having
 *                              to support dissection as it is done now.
 * 06-Dec-01    Paul            VBM:2001102402 - Allowed inclusion pages
 *                              to be included in portal pages.
 * 19-Dec-01    Paul            VBM:2001120506 - Moved code common to canvas
 *                              and montage up into PageElement and extended
 *                              that.
 * 21-Dec-01    Paul            VBM:2001121702 - Modified initialisation to
 *                              distinguish between an including and an
 *                              enclosing page.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Ask protocol to generate
 *                              style sheets instead of the theme.
 * 30-Jan-02    Mat             VBM:2002011410 - Changed endElementImpl() to
 *                              create an inclusion tag if the page is an
 *                              included page.
 * 31-Jan-02    Paul            VBM:2001122105 - Updated to reflect changes
 *                              to protocols.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed page head as it is
 *                              no longer used.
 * 15-Feb-02    Paul            VBM:2002021203 - Call inclusion if we are
 *                              being generated out of the portal context and
 *                              replaced literal url parameter names with
 *                              constants from URLConstants.
 * 18-Feb-02    Allan           VBM:2002021801 - Modified elementEndImpl() to
 *                              only set dissecting if the protocol is WML.
 * 19-Feb-02    Paul            VBM:2001100102 - Update call to
 *                              initialiseCanvas and removed code which cleans
 *                              up the request URL.
 * 25-Feb-02    Paul            VBM:2002022204 - Added try...catch block
 *                              around the initialiseResponse method as it
 *                              can throw a MarinerContextException.
 * 28-Feb-02    Paul            VBM:2002022804 - Moved the code which writes
 *                              the page into the protocols.
 * 05-Mar-02    Ian             VBM:2002022204 - Reworked added log output to
 *                              catch, also set initialiseResponse true for top
 *                              level portlets.
 * 08-Mar-02    Paul            VBM:2002030607 - Made sure that the theme name,
 *                              writeHead and inclusion flags are passed to the
 *                              protocol. Also moved the code to do with
 *                              inclusions into the protocols.
 * 11-Mar-02    Paul            VBM:2001122105 - Initialise any general event
 *                              attributes.
 * 12-Mar-02    Paul            VBM:2001122703 - Add the code which checks
 *                              whether the current page is an error page or
 *                              not.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 22-Mar-02    Steve           VBM:2002031801  Marks the fact that no tags
 *                              have been seen within the canvas yet so that
 *                              the layout element can check if it is the
 *                              first element.
 * 03-Apr-02    Adrian          VBM:2001102414 - pass brand attribute to
 *                              initialiseCanvas in MarinerPageContext.
 * 22-Jul-02    Ian             VBM:2002052804 - Remove all references to old
 *                              style classes.
 * 31-Jul-02    Paul            VBM:2002073008 - Detect when canvas is running
 *                              inside a package and modify the behaviour
 *                              accordingly.
 * 06-Aug-02    Paul            VBM:2002073008 - Removed support for package
 *                              element, added support for overlay attribute.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added support for WML onevent
 *                              handlers
 * 06-Aug-02    Paul            VBM:2002080509 - Call PAPIInternals to
 *                              initialise the canvas specific events.
 * 21-Aug-02    Mat             VBM:2002081508 - Changed elementStartImpl()
 *                              to set the uaContext in pattributes.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203.
 * 12-Feb-03    Ian             VBM:2003012908 - Added processing to check if
 *                              canvas tag is supported here, this may not be
 *                              the case if we are running under MPS and a
 *                              message tag has not been processed.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Remove invocation of
 *                              initialiseResponse from processCanvasType. Also
 *                              remove the initialiseResponse variable from
 *                              that method.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for
 *                              ProtocolException that throws PAPIException.
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false as canvases can only have element
 *                              content.
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * 21-May-03    Byron           VBM:2003042208 - Modified elementStartImpl to
 *                              propogate the initial focus value from the PAPI
 *                              to the protocol's attributes.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import java.io.IOException;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.MediaAgentException;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.management.tracking.CanvasType;
import com.volantis.mcs.papi.CanvasAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.styling.Styles;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The canvas element.
 */
public class CanvasElementImpl
        extends PageElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CanvasElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(CanvasElementImpl.class);

    // The types of canvas.
    public static final String CANVAS_TYPE_MAIN = "main";

    public static final String CANVAS_TYPE_INCLUSION = "inclusion";

    public static final String CANVAS_TYPE_PORTAL = "portal";

    public static final String CANVAS_TYPE_PORTLET = "portlet";

    public static final String CANVAS_TYPE_GEAR = "gear";

    public static final String CANVAS_TYPE_MONTAGE = "montage";

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.CanvasAttributes pattributes;

    /**
     * The attributes passed to this element.
     */
    private CanvasAttributes attributes;

    /**
     * A flag which when true causes the header (and footer) of the page
     * to be generated.
     */
    private boolean writeHead;

    /**
     * The same as writeHead, but for the containing canvas (if it exists).
     */
    private boolean previousWriteHead;

    /**
     * A flag which when true causes the whole page to be generated.
     */
    private boolean writePage;

    /**
     * A flag which specifies whether this page should be treated as an
     * inclusion inside another page or not.
     */
    private boolean inclusion;

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    /**
     * Create a new <code>CanvasElement</code>.
     */
    public CanvasElementImpl() {
        pattributes = new com.volantis.mcs.protocols.CanvasAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    // javadoc inherited
    public int elementStart(
            MarinerRequestContext context, PAPIAttributes papiAttributes)
            throws PAPIException {

        ApplicationContext applicationContext =
                ContextInternals.getApplicationContext(context);
        if (!applicationContext.isCanvasTagSupported()) {
            // Hmmm, we could be running under another app that requires a different
            // top level tag, so lets die
            throw new IllegalStateException("canvas tag not allowed here");
        }

        attributes = (CanvasAttributes) papiAttributes;

        // Process the canvas type.
        int result = processCanvasType(context);
        if (result == SKIP_ELEMENT_BODY) {
            skipped = true;
            return result;
        }

        // Push the theme style sheets associated with the canvas onto the
        // styling engine before we try and process the element. This is
        // required since canvas can itself have styles.
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

        // Now that canvas has finished processing, remove the style sheet
        // associated with the canvas element.
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
            // Update the fragmentation state.
            pageContext.updateFragmentationState();

            // Handle caching.
            // TODO

            // Initialise the protocol canvas attributes.
            pattributes.setId(attributes.getId());
            pattributes.setTitle(attributes.getTitle());

            pattributes.setPageTitle(attributes.getPageTitle());
            pattributes.setUaContext(attributes.getUaContext());
            pattributes.setInitialFocus(attributes.getInitialFocus());

            // Initialise the general event attributes.
            PAPIInternals.initialiseGeneralEventAttributes(pageContext,
                    attributes,
                    pattributes);

            // Initialise the page event attributes.
            PAPIInternals.initialisePageEventAttributes(pageContext,
                    attributes,
                    pattributes);

            // Initialise the canvas event attributes.
            PAPIInternals.initialiseCanvasEventAttributes(pageContext,
                    attributes,
                    pattributes);

            // Get the protocol.
            VolantisProtocol protocol = pageContext.getProtocol();

            // Canvas has not seen any child tags yet
            pageContext.setCanvasHasChildren(false);

            // This canvas may be nested inside a canvas with a different value
            // for writeHead. VolantisProtocol#writeCanvasContent determines
            // whether or not to write out the head element and content and is
            // called before the final canvas' elementEndImpl. Therefore to
            // ensure that the head gets written out if the containing canvas
            // requested it, the protocol write head value must be reset by the
            // contained canvas. This is a temporary fix.
            // @todo figure out a better way to do this.
            previousWriteHead = protocol.getWriteHead();
            // Make sure that the protocol does not write the head out if it is
            // not needed.
            protocol.setWriteHead(writeHead);
            protocol.setInclusion(inclusion);

            // Make sure that the protocol has access to the attributes of the
            // whole canvas.
            protocol.setCanvasAttributes(pattributes);

            // Get the property values for the canvas and store them in the
            // device layout context so that it will inherit from them.
            Styles styles = pattributes.getStyles();
            PropertyValues propertyValues = styles.getPropertyValues();
            ImmutablePropertyValues immutablePropertyValues =
                    propertyValues.createImmutablePropertyValues();

            DeviceLayoutContext dlc = pageContext.getDeviceLayoutContext();
            dlc.setInheritableStyleValues(immutablePropertyValues);

            if (writePage) {
                // Open the canvas page.
                protocol.openCanvasPage(pattributes);
            } else if (inclusion) {
                // This is an inclusion/portlet being rendered in context so we do
                // not have to open the canvas page as that has already been done by
                // the including page but we may have to wrap the contents of the
                // inclusion in something.
                protocol.openInclusionPage(pattributes);
            }
        } catch (LayoutException e) {
            logger.error("unexpected-exception", e);
            throw new PAPIException(e);
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);
            throw new PAPIException(e);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            throw new PAPIException(e);
        } catch (ProtocolException e) {

            logger.error("rendering-error", pattributes.getTagName(), e);
            throw new PAPIException(
                    exceptionLocalizer.format("rendering-error",
                            pattributes.getTagName()),
                    e);
        }

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Return the type of this canvas.
     */
    CanvasType getCanvasType() {
        return CanvasType.literal(pageContext.peekCanvasType());
    }

    /**
     * Waits until MediaAgent processes all its requests, and invokes all
     * pending callbacks.
     *
     * @param context An XDIME context
     * @throws XDIMEException
     */
    private void waitForMediaAgentCompletion(MarinerPageContext pageContext)
            throws PAPIException {
        
        // Get an instance of MediaAgent.
        MediaAgent mediaAgent = pageContext.getMediaAgent(false);

        // Do nothing, if there's no MediaAgent instance associated with page
        // context.
        if (mediaAgent != null) {
            try {
                mediaAgent.waitForComplete();
            } catch (MediaAgentException e) {
                logger.error("rendering-error", "CanvasElement", e);

                throw new PAPIException(exceptionLocalizer.format(
                        "rendering-error", "CanvasElement"), e);
            }
        }
    }


    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {
        // If we have no page context, then we can't process anything
        if (pageContext == null) {
            return CONTINUE_PROCESSING;
        }

        // If we skipped processing the canvas then don't do anything in here.
        if (skipped) {
            return CONTINUE_PROCESSING;
        }

        try {
            //pageContext.popCanvasType();
            VolantisProtocol protocol = pageContext.getProtocol();
            protocol.setInclusion(inclusion);
            // If this page is cacheable then open a stream to write the contents
            // of the page to a file.
            // TODO

            int returnCode = CONTINUE_PROCESSING;

            if (writePage) {
                pageContext.endPhase1BeginPhase2();

                // Wait for MediaAgent to complete all its requests and invoke all
                // pending callbacks.
                waitForMediaAgentCompletion(pageContext);

                protocol.closeCanvasPage(pattributes);

            } else if (inclusion) {
                // todo not sure whether to end phase 1 in here or not. It
                // should probably be optional depending on whether the
                // inclusion is out of context or not.

                protocol.closeInclusionPage(pattributes);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("PAGE: Not writing page");
                }
            }

            // See comment in elementStartImpl when setting previousWriteHead.
            protocol.setWriteHead(previousWriteHead);

            return returnCode;
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
        pageContext = null;
        writeHead = true;
        writePage = true;
        inclusion = false;
        skipped = false;

        super.elementReset(context);
    }

    /**
     * Check the canvas type, make sure that the combination of this canvas
     * and the enclosing canvas (if any) are valid and do any initialisation
     * needed.
     */
    private int processCanvasType(MarinerRequestContext context)
            throws PAPIException {

        // Get the page context.
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        // Check to see what type of canvas we are and take any appropriate
        // actions.
        String type = attributes.getType();

        // The default type is "main".
        if (type == null) {
            type = CANVAS_TYPE_MAIN;
        } else if (CANVAS_TYPE_GEAR.equals(type)) {
            // "gear" is a synonym for "portlet".
            type = CANVAS_TYPE_PORTLET;
        }

        // If this is an error page then we have to assume that we are the top
        // level page as some applications servers call the error page before
        // cleaning up the page which had the error.
        boolean isErrorPage = "error".equals(attributes.getPageUsage());

        // Get the enclosing canvas's type, region context if any.
        String enclosingCanvasType = null;
        RegionInstance regionInstance = null;
        if (pageContext.peekCanvasType() != null) {
            enclosingCanvasType = pageContext.peekCanvasType();
            regionInstance = pageContext.getEnclosingRegionInstance();
        }

        // Check to see whether we should treat the canvas as an overlay.
        boolean overlay = "true".equalsIgnoreCase(attributes.getOverlay());
        pattributes.setOverlay(overlay);

        // By default we write out the page and the head.
        writePage = true;
        writeHead = true;

        // Validate the canvas type.
        if (CANVAS_TYPE_MAIN.equals(type)) {

            // "main" canvasses cannot be included in any mariner page.
            if (enclosingCanvasType != null) {
                throw new IllegalStateException
                        ("'" + type + "' canvas cannot be included in a '"
                                + enclosingCanvasType + "' canvas");
            }

            // This has to be the top level page so can't be an inclusion
            inclusion = false;

        } else if (CANVAS_TYPE_PORTAL.equals(type)) {

            // "portal" canvasses cannot be included in any mariner page.
            if (enclosingCanvasType != null) {
                throw new IllegalStateException
                        ("'" + type + "' canvas cannot be included in a '"
                                + enclosingCanvasType + "' canvas");
            }

            // This has to be the top level page so can't be an inclusion
            inclusion = false;

            // Do any portal specific processing.

        } else if (CANVAS_TYPE_PORTLET.equals(type)) {

            // "portlet" canvasses can be included by either a "portal"
            // or "portlet" canvas but does not have to be included in any
            // mariner page, e.g. if it is part of an application servers
            // framework.
            if (enclosingCanvasType != null
                    && !CANVAS_TYPE_PORTAL.equals(enclosingCanvasType)
                    && !CANVAS_TYPE_PORTLET.equals(enclosingCanvasType)) {

                throw new IllegalStateException
                        ("'" + type + "' canvas cannot be included in a '"
                                + enclosingCanvasType + "' canvas");
            }

            if (enclosingCanvasType == null) {
                // We are executing inside an application server framework
                // so don't write the head.
                writeHead = false;
            } else {

                // If this canvas is not an overlay then it must be inside a region.
                if (!overlay) {
                    // We must be executing within a region.
                    if (regionInstance == null) {
                        throw new IllegalStateException
                                ("Not being included inside a region");
                    }
                }

                // We are executing inside a Mariner portal or portlet so
                // don't write anything.
                writePage = false;
                writeHead = false;
            }

            // A portlet is always treated as an inclusion whether or not it
            // is running in context.
            inclusion = true;

        } else if (CANVAS_TYPE_INCLUSION.equals(type)) {

            // "inclusion" canvasses must be included by either a
            // "main", "inclusion", "portal", or "portlet" canvas.
            if (enclosingCanvasType == null) {
                throw new IllegalStateException
                        ("'" + type + "' canvas must be included in either a '"
                                + CANVAS_TYPE_MAIN + "', '"
                                + CANVAS_TYPE_INCLUSION + "', or a '"
                                + CANVAS_TYPE_PORTLET + "' canvas");
            }

            if (!CANVAS_TYPE_MAIN.equals(enclosingCanvasType)
                    && !CANVAS_TYPE_INCLUSION.equals(enclosingCanvasType)
                    && !CANVAS_TYPE_PORTAL.equals(enclosingCanvasType)
                    && !CANVAS_TYPE_PORTLET.equals(enclosingCanvasType)) {
                throw new IllegalStateException
                        ("'" + type + "' canvas cannot be included in a '"
                                + enclosingCanvasType + "' canvas");
            }

            // If this canvas is not an overlay then it must be inside a region.
            if (!overlay) {
                // We must be executing within a region.
                if (regionInstance == null) {
                    throw new IllegalStateException
                            ("Not being included inside a region");
                }
            }

            // We are executing inside a Mariner portal or portlet so
            // don't write anything.
            writePage = false;
            writeHead = false;

            // This cannot be the top level page so we must be an inclusion
            inclusion = true;

        } else {
            throw new IllegalArgumentException
                    ("Invalid canvas type '" + type + "'");
        }

        // Initialise the canvas section of the page context.

        // The MarinerPageContext has been initialised for the page but not for
        // the canvas so we need to initialise it here before we do anything
        // else.
        initialiseCanvas(pageContext, inclusion,
                isErrorPage,
                attributes.getId(),
                attributes.getBrand(),
                attributes.getTheme(),
                attributes.getLayoutName());

        // Set the canvas type.
        pageContext.pushCanvasType(type);

        if (logger.isDebugEnabled()) {
            logger.debug("Canvas type " + type
                    + " write page " + writePage
                    + " write head " + writeHead
                    + " inclusion " + inclusion);
        }

        return PROCESS_ELEMENT_BODY;
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

 15-Dec-05	9990/4	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 08-Nov-05	9990/1	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 06-Dec-05	10638/1	emma	VBM:2005120505 Forward port: Generated XHTML was invalid - had no head tag but had head content

 06-Dec-05	10623/1	emma	VBM:2005120505 Generated XHTML was invalid: missing head tag but head content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 10-Nov-05	10233/1	ianw	VBM:2005110812 Fixup memory leaks

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 10-Nov-05	10261/1	ianw	VBM:2005110812 Fixup memory leaks

 10-Nov-05	10233/1	ianw	VBM:2005110812 Fixup memory leaks

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9616/1	adrianj	VBM:2005092610 Fix for MCS capabilities requests in JSPs

 12-Sep-05	9372/2	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 24-Jun-04	4702/6	matthew	VBM:2004061402 rework JMXPageTrackerFactory error handling

 21-Jun-04	4702/4	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/2	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 25-Jun-03	544/1	geoff	VBM:2003061007 Allow JSPs to create binary output

 ===========================================================================
*/
