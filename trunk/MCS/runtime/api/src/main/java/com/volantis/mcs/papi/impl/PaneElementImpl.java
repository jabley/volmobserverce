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
 * $Header: /src/voyager/com/volantis/mcs/papi/PaneElement.java,v 1.20 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement.
 * 26-Feb-01    Adrian          VBM:2002022006 - Override method elementDirect
 *                              to check if the pane only contains whitespace
 *                              content.  If not then super.elementDirect.
 *                              This prevents tables being generated for only
 *                              whitespace.
 * 28-Feb-02    Paul            VBM:2002022804 - Removed the above fix as it
 *                              was flawed. This is now fixed in the protocol
 *                              StringOutputBuffer class.
 * 04-Mar-02    Paul            VBM:2001101803 - Removed some commented code.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 23-May-02    Steve           VBM:2002040809 - If the pane element has a 
 *                              styleClass attribute, set it in the Pane format
 *                              so that it can be retrieved by the protocol.
 * 24-May-02    Steve           VBM:2002040809 - The style class has moved from
 *                              the pane format to the pane context.
 * 22-Jul-02    Phil W-S        VBM:2002071911 - The pane must be named
 *                              explicitly or an alternative pane identified
 *                              by an ancestor of this element.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203.
 * 29-Oct-02    Chris W         VBM:2002111101- Gets pane name from 
 *                              FormatReference rather than directly from 
 *                              MarinerPageContext. Store the 
 *                              FormatInstanceReference as a property
 *                              of this object. 
 * 29-Jan-03    Chris W         VBM:2003012203 - elementStart calls
 *                              FormatIteratorFormatFilter.isSkippable to see
 *                              if tag refers to a particular instance of a
 *                              pane that is outside the max. permitted by a
 *                              spatial or temporal format iterator.
 * 07-Feb-03    Chris W         VBM:2003020609 - Code in FormatIteratorFormatFilter
 *                              moved to FormatInstance, so elementStart calls
 *                              ignore(fir) on a FormatInstance instead.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PaneAttributes;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The pane element.
 */
public class PaneElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(PaneElementImpl.class);

    /**
     * The pane instance which this element pushed onto the stack, or null if no
     * pane name was specified for this element.
     */
    private AbstractPaneInstance paneInstance;

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    /**
     * Create a new <code>PaneElement</code>.
     */
    public PaneElementImpl() {
    }

    /**
     * This methods handles resolving the pane name to a specific pane and
     * checking to see whether the pane's output will be ignored and therefore
     * does not need generating.
     * <p>
     * If the pane name is specified and either the pane does not exist, or its
     * output will be ignored for some reason then this method immediately
     * returns SKIP_ELEMENT_BODY and causes elementEnd to return immediately
     * without doing anything.
     * </p><p>
     * If the pane name is not specified, or the pane exists and will not be
     * ignored then this method returns PROCESS_ELEMENT_BODY.
     * </p>
     * Some Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        PaneAttributes attributes = (PaneAttributes) papiAttributes;

        // Try and find the pane with the specified name, if it could not be
        // found then return and skip the element body.
        String paneName = attributes.getName();
        if (paneName != null) {

            FormatReference formatRef =
                    FormatReferenceParser.parsePane(paneName, pageContext);
            Pane pane = pageContext.getPane(formatRef.getStem());
            NDimensionalIndex paneIndex = formatRef.getIndex();

            if (pane == null) {
                skipped = true;
                return SKIP_ELEMENT_BODY;
            }

            paneInstance = (AbstractPaneInstance)
                    pageContext.getFormatInstance(pane, paneIndex);
            if (paneInstance.ignore()) {
                skipped = true;
                return SKIP_ELEMENT_BODY;
            }

            paneInstance.setStyleClass(attributes.getStyleClass());
            MCSAttributes pAttributes = paneInstance.getAttributes();
            // get the styles for the current element and set it on the MCSAttributes
            pAttributes.setStyles(pageContext.getStylingEngine().getStyles());

            pageContext.pushContainerInstance(paneInstance);
        } else if (pageContext.getCurrentPane() == null) {
            // this element doesn't have a pane name specified and the pane stack is
            // empty. This is not allowed.
            throw new PAPIException(
                    exceptionLocalizer.format("pane-name-not-found"));
        }

        // Fake an open element so that whitespace is handled normally since we
        // have no element to represent pane at the moment.
        pageContext.getCurrentOutputBuffer()
                .handleOpenElementWhitespace();

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        if (skipped) {
            return CONTINUE_PROCESSING;
        }

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Fake a close element so that whitespace is handled normally since we
        // have no element to represent pane at the moment.
        pageContext.getCurrentOutputBuffer()
                .handleCloseElementWhitespace();

        if (paneInstance != null) {
            pageContext.popContainerInstance(paneInstance);
        }
        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        paneInstance = null;
        skipped = false;
        super.elementReset(context);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 08-Dec-05	10621/2	geoff	VBM:2005113024 Pagination page rendering issues

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 23-Feb-05	7114/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 23-Feb-05	7079/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 08-Jan-04	2461/1	steve	VBM:2003121701 Patch pane name changes from Proteus2

 07-Jan-04	2389/1	steve	VBM:2003121701 Enhanced pane referencing

 14-Aug-03	1083/4	geoff	VBM:2003081305 fix supermerge problems

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 24-Jun-03	450/1	steve	VBM:2003060909 Style classes on Spatial Iterator panes

 ===========================================================================
*/
