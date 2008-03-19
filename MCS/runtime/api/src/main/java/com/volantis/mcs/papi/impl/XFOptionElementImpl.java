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
* $Header: $
* ----------------------------------------------------------------------------
* (c) Volantis Systems Ltd 2001. 
* ----------------------------------------------------------------------------
* Change History:
*
* Date         Who             Description
* ---------    --------------- -----------------------------------------------
* 30-Nov-01    Paul            VBM:2001112909 - Created.
* 25-Feb-02    Adrian          VBM:2002012501 - Set the selected attribute on
*                              the protocol attributes.
* 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                               to string.
* 26-Jun-02    Steve           VBM:2002062401 Added attributes entryPane,
*                              captionPane and captionClass
* 01-Aug-02    Sumit           VBM:2002073109 - optgroup support added
* 01-Oct-02    Allan           VBM:2002093002 - Modified startElement() to
*                              to process the prompt attribute.
* 22-Oct-02    Byron           VBM:2002100402 - Modified elementStart() to
*                              copy the styleclass and id.
* 29-Oct-02    Chris W         VBM:2002111101- Gets pane name from
*                              FormatReference rather than directly from
*                              MarinerPageContext
* 15-Jan-03    Adrian          VBM:2002120908 - Set the tag name on the
*                              protocol attributes to "xfoption"
* 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
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
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFOptionAttributes;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;

/**
 * The xfoption element.
 */
public class XFOptionElementImpl
        extends AbstractExprElementImpl {

    /**
     * Create a new <code>XFOptionElement</code>.
     */
    public XFOptionElementImpl() {
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        XFOptionAttributes attributes = (XFOptionAttributes) papiAttributes;

        // Create a new protocol attributes object every time, as this element
        // could be reused before the attributes have actually been finished with
        // by the protocol.
        com.volantis.mcs.protocols.SelectOption pattributes
                = new com.volantis.mcs.protocols.SelectOption();

        // Copy the base attributes.
        pattributes.setId(attributes.getId());
        pattributes.setTagName("xfoption");

        // get styles for the current element and set it on the MCSAttributes
        final StylingEngine stylingEngine = pageContext.getStylingEngine();
        pattributes.setStyles(stylingEngine.getStyles());

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        TextAssetReference object;
        // Process the caption as a mariner expression.
        object = resolver.resolveQuotedTextExpression(attributes.getCaption());
        pattributes.setCaption(object);

        // Process the prompt as a mariner expression.
        object = resolver.resolveQuotedTextExpression(attributes.getPrompt());
        pattributes.setPrompt(object);

        // Set the value attribute.
        pattributes.setValue(attributes.getValue());

        // Find the caption pane
        PaneInstance captionPaneInstance = getPaneInstance(
                pageContext, attributes.getCaptionPane());

        if ((captionPaneInstance != null) &&
                (captionPaneInstance.getFormat() != null)) {
            pattributes.setCaptionContainerInstance(captionPaneInstance);
        }

        // And the entry pane
        PaneInstance entryPaneInstance = getPaneInstance(
                pageContext, attributes.getEntryPane());

        if ((entryPaneInstance != null) &&
                (entryPaneInstance.getFormat() != null)) {
            pattributes.setEntryContainerInstance(entryPaneInstance);
        }

        // Set the styles for the caption
        final String captionClass = attributes.getCaptionClass();
        if (captionClass != null) {
            final String namespace = XDIMESchemata.CDM_NAMESPACE;
            final PAPIAttributes genericAttributes =
                    attributes.getGenericAttributes();
            genericAttributes.setAttributeValue(null, "class", captionClass);
            stylingEngine.startElement(namespace, "xfoption",
                    (Attributes) genericAttributes);
            pattributes.setCaptionStyles(stylingEngine.getStyles());
            stylingEngine.endElement(namespace, "xfoption");
        }

        // Set the selected attribute
        String selected = attributes.getSelected();
        if (selected != null) {
            boolean isSelected = new Boolean(selected).booleanValue();
            pattributes.setSelected(isSelected);
        }

        Object enclosing = pageContext.getCurrentElement();
        if (enclosing instanceof XFSelectElementImpl) {
            XFSelectAttributes selectAttributes =
                    ((XFSelectElementImpl) enclosing).getProtocolAttributes();
            selectAttributes.addOption(pattributes);
        } else {
            SelectOptionGroup sfoga =
                    ((XFOptionGroupElementImpl) enclosing).getOptionGroup();
            sfoga.addSelectOption(pattributes);
        }

        pattributes.setInsertAfterCaptionPaneNode(
                getInsertAfterNode(captionPaneInstance, pageContext));

        pattributes.setInsertAfterEntryPaneNode(
                getInsertAfterNode(entryPaneInstance, pageContext));

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Get the pane instance given the page context and paneName.
     *
     * @param pageContext the mariner page context.
     * @param paneName    the paneName name expression.
     * @return the pane instance for the paneName.
     */
    private PaneInstance getPaneInstance(
            MarinerPageContext pageContext,
            String paneName) {
        PaneInstance paneInstance = null;
        if (paneName != null) {
            FormatReference formatRef = FormatReferenceParser.parsePane(
                    paneName, pageContext);
            Pane pane = pageContext.getPane(formatRef.getStem());
            if (pane != null) {
                paneInstance = (PaneInstance) pageContext.getFormatInstance(
                        pane, formatRef.getIndex());
            }
        }
        return paneInstance;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return CONTINUE_PROCESSING;
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

 06-Dec-05	10619/1	ibush	VBM:2005113017 Fix xfoption typeSelectors

 06-Dec-05	10606/1	ibush	VBM:2005113017 Fix xfoption typeSelectors

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 25-Aug-05	9370/6	gkoch	VBM:2005070507 applied revision comments

 25-Aug-05	9370/3	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6320/1	geoff	VBM:2004112604 xfaction with captionPane= throws a NullPointerException

 22-Nov-04	6183/5	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6183/2	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6135/4	byron	VBM:2004081726 Allow spatial format iterators within forms

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 23-Jun-03	424/3	byron	VBM:2003022825 Fixed javadoc and variable naming

 20-Jun-03	424/1	byron	VBM:2003022825 Enhance behaviour of pane element within xfform

 ===========================================================================
*/
