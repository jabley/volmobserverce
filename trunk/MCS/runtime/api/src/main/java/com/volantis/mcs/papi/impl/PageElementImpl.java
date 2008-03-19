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
 * $Header: /src/voyager/com/volantis/mcs/papi/PageElement.java,v 1.17 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 21-Dec-01    Paul            VBM:2001121702 - Moved call to initialiseCanvas
 *                              into its own method to allow dependent classes
 *                              to call it when they want to.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 31-Jan-02    Paul            VBM:2001122105 - Moved writeInitialHeader to
 *                              Volantis/StringProtocol.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed page head as it is
 *                              no longer used.
 * 19-Feb-02    Paul            VBM:2001100102 - Added flag to initialiseCanvas
 *                              method which indicates whether the page is an
 *                              error page or not.
 * 20-Feb-02    Allan           VBM:2002022007 - Changed logger.info() calls
 *                              into logger.debug() calls in elementStart().
 *                              elementEnd(). Modified elementStart() and
 *                              elementEnd() to only output the time when
 *                              debug is enabled.
 * 28-Feb-02    Paul            VBM:2002022804 - Removed unused import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 03-Apr-02    Adrian          VBM:2001102414 - pass brand attribute to
 *                              initialiseCanvas in MarinerPageContext.
 * 28-Apr-02    Adrian          VBM:2002040808 - Removed catch for theme
 *                              exception.
 * 22-Jul-02    Ian             VBM:2002052804 - Removed all references to old
 *                              style classes.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for
 *                              ProtocolException that throws PAPIException.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.debug.DebugTimer;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.management.tracking.CanvasDetails;
import com.volantis.mcs.management.tracking.CanvasType;
import com.volantis.mcs.management.tracking.PageDetails;
import com.volantis.mcs.management.tracking.PageTrackerException;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PageAttributes;
import com.volantis.mcs.protocols.DeviceLayoutRegionContent;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.mcs.utilities.PolicyException;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This abstract class implements functionality common across all page
 * elements.
 */
public abstract class PageElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PageElementImpl.class);

    // The types of canvas.
    public static final String CANVAS_TYPE_MAIN = "main";

    public static final String CANVAS_TYPE_INCLUSION = "inclusion";

    public static final String CANVAS_TYPE_PORTAL = "portal";

    public static final String CANVAS_TYPE_PORTLET = "portlet";

    public static final String CANVAS_TYPE_GEAR = "gear";

    public static final String CANVAS_TYPE_MONTAGE = "montage";

    /**
     * A timer object for timing the processing of this element if in debug
     * mode.
     */
    private DebugTimer debugTimer;

    /**
     * The <code>MarinerPageContext</code> for the current request.
     * <h2>
     * You MUST NOT change the protection level on this member.
     * </h2>
     */
    MarinerPageContext pageContext;

    /**
     * The collection of compiled theme style sheets for this page.
     * <p>
     * Currently XDIME 1 only supports the single theme passed in the page
     * element's theme= attribute and inline themes via the style element. The
     * link element themes and project themes are not supported. There is an
     * argument that project themes should have been supported in XDIME 1 and
     * it would not be hard to do but we didn't do it and there seems little
     * point now that all new development should be using XDIME 2.
     * <p>
     * In XDIME 1 there is no head element, so the style element must be a
     * child of the canvas. This is a bit nasty and has the following
     * consequences:
     * <ul>
     * <li>In XDIME2/CP, any inline styles apply to the body element, whereas
     * in XDIME1, any inline styles only apply the the children of the page
     * element.
     * <li>In XDIME2/CP, all theme styles are added into the collection before
     * the canvas is initialised and then the entire collection is pushed into
     * styling engine in one go. In XDIME1, only the theme= theme is available
     * at canvas initialisation time and the inline themes are found later.
     * This means that under XDIME1 we must add the inline styles into this
     * collection via a nasty "back door" as normally the collection is
     * immutable once it has been pushed onto the styling engine - see
     * {@link CompiledStyleSheetCollection#pushAdditionalStyleSheet}.
     * </ul>
     */
    protected CompiledStyleSheetCollection themeStyleSheets;

    /**
     * Create a new <code>PageElement</code>.
     */
    public PageElementImpl() {
    }

    // Javadoc inherited from super class.
    public int styleElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        // Get the internal context from the request context.
        pageContext = ContextInternals.getMarinerPageContext(context);

        if (logger.isDebugEnabled()) {
            debugTimer = new DebugTimer();
            debugTimer.beginTimer();

            // Log the URL of the requested page.
            String relativeRequestURL = pageContext.getRelativeRequestURL();
            logger.debug("Requested page is "
                    + relativeRequestURL);

            // Log the pure URL.
            logger.debug("Processed request is "
                    + pageContext.getPureRequestURL().getExternalForm());
        }

        return super.styleElementStart(context, papiAttributes);
    }

    /**
     * Initialise the MarinerPageContext object for a canvas.
     * <h2>
     * You MUST NOT change the protection level on this member.
     * </h2>
     *
     * @param pageContext
     * @param inclusion   Specifies whether this page should be treated as an
     *                    inclusion.
     * @param id          The id of the page.
     * @param themeName   The name of the theme used in this page.
     * @param layoutName  The name of the layout used in this page.
     */
    void initialiseCanvas(
            final MarinerPageContext pageContext,
            boolean inclusion,
            boolean errorPage,
            String id,
            String brandName,
            String themeName,
            String layoutName)
            throws PAPIException {
        if (layoutName == null) {
            throw new IllegalArgumentException(
                    "canvas must provide a layoutName");
        }

        try {

            //set the brand name before trying to retrieve the style sheet
            //so that the brand name is appended to the style sheet location
            pageContext.setBrandName(brandName, inclusion);

            // Create the list of theme style sheets that apply for this page
            // and add the single canvas theme into it (for now). More can
            // be added later by the <style> element.
            themeStyleSheets = new CompiledStyleSheetCollection();
            if (themeName != null) {
                CompiledStyleSheet compiledStyleSheet =
                        pageContext.retrieveThemeStyleSheet(themeName);
                if (compiledStyleSheet != null) {
                    themeStyleSheets.addStyleSheet(compiledStyleSheet);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No theme specified for page");
                }
            }

            pageContext.initialise(inclusion, errorPage, id, brandName,
                    themeStyleSheets, layoutName);

        } catch (LayoutException e) {
            logger.error("unexpected-exception", e);
            throw new PAPIException(e);
        } catch (PolicyException e) {
            logger.error("unexpected-exception", e);
            throw new PAPIException(e);
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);
            throw new PAPIException(e);
//        } catch (JiBXException e) {
//            logger.error("unexpected-exception", e);
//            throw new PAPIException(e);
//        } catch (IOException e) {
//            logger.error("unexpected-exception", e);
//            throw new PAPIException(e);
        }
    }

    /**
     * @see #themeStyleSheets
     */
    protected CompiledStyleSheetCollection getThemeStyleSheets() {
        return themeStyleSheets;
    }


    /**
     * Start the element specific processing.
     *
     * @param context        The MarinerRequestContext within which this
     *                       element is being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the
     *                       implementation of PAPIElement.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws PAPIException If there was a problem processing the element.
     */
    protected abstract int elementStartImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException;


    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return elementStartImpl(context, papiAttributes);
    }


    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        int result = elementEndImpl(context, papiAttributes);

        // add page tracking functionality
        PageTrackerFactory ptFactory = pageContext.
                getVolantisBean().getPageTrackerFactory();
        if (ptFactory != null) {
            PageAttributes canvasAttributes = (PageAttributes) papiAttributes;
            CanvasDetails canvasDetails = ptFactory.
                    createCanvasDetails(canvasAttributes.getPageTitle(),
                            getCanvasType(),
                            canvasAttributes.getTheme(),
                            canvasAttributes.getLayoutName());
            PageDetails pageDetails = ptFactory.createPageDetails(
                    canvasDetails,
                    pageContext.getDeviceName());
            try {
                ptFactory.createPageDetailsManager().addPageDetails(
                        pageDetails);
            } catch (PageTrackerException pte) {
                // ptFactory does not log errors so do it here.
                logger.error("pagedetails-creation-failure", pte);
                throw new PAPIException(
                        "Error occured when creating PageDetails", pte);
            }
        }

        // If we have been included in a region then we mustn't throw the
        // DeviceLayoutContext away, we must add it to the region context.
        if (pageContext.getDeviceLayoutContext() != null) {
            if (pageContext.getEnclosingRegionInstance() != null) {
                pageContext.getEnclosingRegionInstance().addRegionContent(
                        new DeviceLayoutRegionContent(
                                pageContext.getDeviceLayoutContext()));
            }
        }
        if (pageContext.getDeviceLayoutContext() != null) {
            pageContext.popDeviceLayoutContext();
        }

        if (logger.isDebugEnabled()) {

            String relativeRequestURL = pageContext.getRelativeRequestURL();
            debugTimer.stopTimer(relativeRequestURL + " page runtime = ");
        }

        return result;
    }

    /**
     * derived classes should implement this to return thier canvas type.
     *
     * @return The canvas type of the derived class. This is package private to
     *         maintain the public API.
     */
    abstract CanvasType getCanvasType();

    /**
     * End the element specific processing.
     *
     * @param context        The MarinerRequestContext within which this
     *                       element is being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the
     *                       implementation of PAPIElement.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws PAPIException If there was a problem processing the element.
     */
    protected abstract int elementEndImpl(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException;

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        debugTimer = null;
        themeStyleSheets = null;
        pageContext = null;

        super.elementReset(context);
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	9990/7	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 15-Dec-05	9990/5	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 08-Nov-05	9990/2	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 22-Nov-05	10394/3	ibush	VBM:2005111812 Fix Canvas Branding

 22-Nov-05	10394/1	ibush	VBM:2005111812 Fix Canvas Branding

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 22-Nov-05	10394/3	ibush	VBM:2005111812 Fix Canvas Branding

 22-Nov-05	10394/1	ibush	VBM:2005111812 Fix Canvas Branding

 15-Nov-05	10326/7	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 01-Jul-04	4702/8	matthew	VBM:2004061402 merge problems

 24-Jun-04	4702/6	matthew	VBM:2004061402 rework JMXPageTrackerFactory error handling

 21-Jun-04	4702/4	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/2	matthew	VBM:2004061402 management functionality added

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 11-Aug-03	1013/1	chrisw	VBM:2003080806 Refactored expressions to be jsp independent

 ===========================================================================
*/
