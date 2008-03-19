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
 * $Header: /src/voyager/com/volantis/mcs/papi/BlockElement.java,v 1.15 2003/03/12 16:10:43 sfound Exp $
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
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement and
 *                              pushed and popped the element.
 * 28-Feb-02    Paul            VBM:2002022804 - Made the elementEnd pop the
 *                              element rather than push it again.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 22-Jul-02    Phil W-S        VBM:2002071911 - There must be a containing
 *                              pane or a pane name must be explicitly
 *                              identified.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203.
 * 29-Oct-02    Chris W         VBM:2002111101 - Gets pane name from
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
 * 23-Apr-03    Steve           VBM:2003041606 - Override isBlock() from
 *                              AbstractElement as all elements that derive
 *                              from here are block elements.
 * 19-May-03    Chris W         VBM:2003051902 - isBlock() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This abstract class implements functionality which is common across all
 * PAPI block elements.
 */
public abstract class BlockElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(BlockElementImpl.class);

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
     * Create a new <code>BlockElement</code>.
     */
    public BlockElementImpl() {
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
     * ignored then this method calls elementStartImpl to do the element specific
     * work.
     * </p>
     *
     * Javadoc inherited from super class.
     */
    protected final int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        BlockAttributes attributes = (BlockAttributes) papiAttributes;

        // Try and find the pane with the specified name, if it could not be
        // found then return and skip the element body.
        String paneName = attributes.getPane();
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

            pageContext.pushContainerInstance(paneInstance);
        } else if (pageContext.getCurrentPane() == null) {
            if (pageContext.getEnclosingRegionInstance() == null) {
                // this element doesn't have a pane specified and the pane and
                // region stacks are empty. This is not allowed.
                throw new PAPIException(
                        exceptionLocalizer.format("pane-name-not-found"));
            }
        }

        // Push this element.
        pageContext.pushElement(this);

        return elementStartImpl(context, attributes);
    }

    /**
     * This method is only called if output for the pane is required.
     *
     * @see #elementStartImpl
     */
    protected abstract int elementStartImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException;

    /**
     * Javadoc inherited from super class.
     */
    protected final int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        if (skipped) {
            return CONTINUE_PROCESSING;
        }

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Pop this element.
        pageContext.popElement(this);

        int result = elementEndImpl(context,
                (BlockAttributes) papiAttributes);

        if (paneInstance != null) {
            pageContext.popContainerInstance(paneInstance);
        }

        return result;
    }

    /**
     * This method is only called if output for the pane is required.
     *
     * @see #elementStartImpl
     */
    protected abstract int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException;

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        paneInstance = null;
        skipped = false;

        super.elementReset(context);
    }


    // Javadoc inherited from super class.
    boolean isBlock() {
        // All children are block elements
        return true;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6076/4	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/4	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 19-Jun-03	407/1	steve	VBM:2002121215 Flow elements and PCData in regions

 ===========================================================================
*/
