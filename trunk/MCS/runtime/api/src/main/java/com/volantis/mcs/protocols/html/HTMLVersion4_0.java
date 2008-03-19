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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLVersion4_0.java,v 1.4 2003/01/15 12:42:09 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header and fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified by not creating
 *                              StringBuffers when returning a fixed string
 *                              and also by renaming all the parameters to
 *                              attributes.
 * 10-Aug-01    Allan           VBM:2001081004 - Make this protocol use
 *                              the loose instead of the strict version
 *                              of HTML version 4.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 08-Mar-02    Adrian          VBM:2002030705 - Removed openSpan and closeSpan
 *                              as these were identical the superclass methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 01-Aug-02    Sumit           VBM:2002073109 - optgroup support added. HTML40
 *                              supports an optgroup depth of 1
 * 05-Nov-02    Adrian          VBM:2002100310 - Added space char to
 *                              protocolString.
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Add in usage of the required
 *                              optimizingTransformer in the constructor.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.html.htmlversion4_0.HTML4_0UnabridgedTransformer;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is a sub-class of the HTMLRoot protocol class to provide the precise
 * definition of the HTML v4.0 protocol. Very little here is different from
 * the HTMLRoot class definition, so most things are referenced to the
 * superclass.
 */
public class HTMLVersion4_0
        extends HTMLRoot {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(HTMLVersion4_0.class);

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     *                       objects.
     * @param configuration  The protocol specific configuration.
     */
    public HTMLVersion4_0(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        // set the level of opt group support for this protocol
        setOptGroupDepth(1);

        // This protocol requires different optimization rules to be applied
        // than those used by the base class
        optimizingTransformer =
                new HTML4_0UnabridgedTransformer(protocolConfiguration);

    }

    // ==========================================================================
    //   General helper methods
    // ==========================================================================

    // ==========================================================================
    //   Page element methods
    // ==========================================================================

    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "HTML", "-//W3C//DTD HTML 4.0//EN",
                "http://www.w3.org/TR/REC-html40/loose.dtd",
                null, MarkupFamily.SGML);
        document.setDocType(docType);
    }

    // ==========================================================================
    //   Dissection methods
    // ==========================================================================

    // ==========================================================================
    //   Layout / format methods
    // ==========================================================================

    // JavaDoc inherited from superclass.

    protected void setUpCssEmulation() {
        // At the moment the only css property we need to emulate is css2's
        // border-spacing
        cssEmulation.add(BORDER_SPACING);
    }

    // ==========================================================================
    //   Navigation methods.
    // ==========================================================================

    // ==========================================================================
    //   Block element methods.
    // ==========================================================================

    // ==========================================================================
    //   List element methods.
    // ==========================================================================

    // ==========================================================================
    //   Table element methods.
    // ==========================================================================

    // Javadoc inherited from super class.

    protected void addTableAttributes(
            Element element,
            TableAttributes attributes)
            throws ProtocolException {

        super.addTableAttributes(element, attributes);

        // Add a cellpadding attribute to the table element if we need to emulate
        // the css2 property border-spacing
        if (cssEmulation.contains(BORDER_SPACING)) {
            Styles styles = attributes.getStyles();
            String cellSpacing =
                    borderSpacingHandler.getAsString(styles);
            if (cellSpacing != null) {
                element.setAttribute("cellspacing", cellSpacing);
            }

            if ((element.getAttributeValue("border") == null) &&
                    ("0".equals(cellSpacing) || cellSpacing == null)) {
                // @todo better should the above if statement include: context.getDeviceName().endsWith("Netscape4")
                // Netscape 4 fix. Whilst this code isn't device dependent,
                // it could be moved to the HTMLVersion4_0_NS4 protocol -
                // but this defeats the design of trying to keep the code
                // together and only using the HTMLVersion4_0_IE and
                // HTMLVersion4_0_NS4 to set up the list of CSS properties
                // that require emulation. Alternatively, instead of using
                // a set the cssEmulation attribute could contain flags to
                // indicate the level of emulation required.
                //
                // As this is an L3 and we're pressed for time.
                // To get rid of the gaps between table cells, we set
                // the border to zero provided:
                // - that the border hasn't already been set to something else
                // - that the user doesn't want gaps between cells i.e. the
                //   required cellspacing is zero.
                element.setAttribute("border", "0");

                if (logger.isDebugEnabled()) {
                    logger.debug("Emulating CSS2 border-spacing: " +
                            "cellspacing = " + cellSpacing);
                }
            }
        }
    }

    // ==========================================================================
    //   Inline element methods.
    // ==========================================================================

    // ==========================================================================
    //   Special element methods.
    // ==========================================================================

    // ==========================================================================
    //   Menu element methods.
    // ==========================================================================

    // ==========================================================================
    //   Script element methods.
    // ==========================================================================

    // ==========================================================================
    //   Classic form element methods.
    // ==========================================================================

    // ==========================================================================
    //   Extended function form element methods.
    // ==========================================================================
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 23-Jun-04	4739/1	steve	VBM:2004042912 Move border spacing out of IE and NS4

 23-Jun-04	4730/1	steve	VBM:2004042912 Move border spacing out of IE and NS4

 13-Oct-03	1542/1	allan	VBM:2003101101 HTML_iMode table handling patched from Proteus2

 12-Oct-03	1540/1	allan	VBM:2003101101 Add emulated and native table support on HTML_iMode

 21-Aug-03	1240/3	chrisw	VBM:2003070811 implemented rework

 21-Aug-03	1240/1	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from mimas to proteus

 21-Aug-03	1219/3	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from metis to mimas

 20-Aug-03	1152/1	chrisw	VBM:2003070811 Emulate CSS2 border-spacing using cellspacing on table element

 ===========================================================================
*/
