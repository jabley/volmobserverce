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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFContentElement.java,v 1.12 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 19-Feb-02    Paul            VBM:2001100102 - Create and initialise a
 *                              FieldDescriptor object.
 * 04-Mar-02    Paul            VBM:2001101803 - Moved initialisation of the
 *                              field descriptor into a separate method.
 * 09-Sep-02    Mat             VBM:2002090502 - Changed to add the contents
 *                              of the tag to the form as a form field to
 *                              ensure the content appears in the correct place
 *                              on the form.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203, and fixed the original
 *                              bug reported there by adding skipEndTag.
 * 29-Oct-02    Chris W         VBM:2002111101 - Gets pane name from
 *                              FormatReference rather than directly from
 *                              MarinerPageContext
 * 13-Jan-02    Doug            VBM:2002111806 - Added the Pane and
 *                              FormatInstanceReference member variables.
 *                              Modified elementStart() to initialise the
 *                              new member variables and to push the current
 *                              Pane onto the MarinerPageContext stack.
 *                              Modified elementEnd() pop the pane from
 *                              the page context pane stack. Modified
 *                              elementReset() to null the new member
 *                              variables.
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
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFContentAttributes;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.forms.ContentFieldType;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The xfaction element.
 */
public class XFContentElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    XFContentElementImpl.class);

    private OutputBuffer buffer;

    /**
     * True if the end tag is to be skipped.
     */
    private boolean skipEndTag;

    /**
     * The protocol attributes
     */
    private com.volantis.mcs.protocols.XFContentAttributes pattributes;

    /**
     * The pane instance which this element targets its contented at.
     */
    private AbstractPaneInstance paneInstance;

    /**
     * Create a new <code>XFContentElement</code>.
     */
    public XFContentElementImpl() {
    }

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        XFContentAttributes attributes = (XFContentAttributes) papiAttributes;

        // Get the page context.
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // Get the enclosing form element's attributes.
        XFFormElementImpl formElement
                = (XFFormElementImpl) pageContext.getCurrentElement();
        com.volantis.mcs.protocols.XFFormAttributes formAttributes =
                formElement.getProtocolAttributes();

        String paneName = attributes.getPane();
        Pane pane = null;
        if (null == paneName) {
            FormInstance form = (FormInstance) formAttributes.getFormData();
            pane = ((Form) form.getFormat()).getDefaultPane();
            paneName = pane.getName();
        }

        FormatReference formatRef =
                FormatReferenceParser.parsePane(paneName, pageContext);
        NDimensionalIndex paneIndex = formatRef.getIndex();

        if (null == pane) {
            // get hold of the Pane via it's FormatReference
            pane = pageContext.getPane(formatRef.getStem());
            if (null == pane) {
                // nothing to do, skip both the content and the end tag
                skipEndTag = true;
                return SKIP_ELEMENT_BODY;
            }
        }

        paneInstance = (AbstractPaneInstance)
                pageContext.getFormatInstance(pane, paneIndex);
        if (paneInstance.ignore()) {
            skipEndTag = true;
            return SKIP_ELEMENT_BODY;
        }

        VolantisProtocol protocol = pageContext.getProtocol();
        buffer = protocol.getOutputBufferFactory().createOutputBuffer();
        if (buffer == null) {
            throw new PAPIException(exceptionLocalizer.format(
                    "output-buffer-allocation-failure"));
        }

        pattributes = new com.volantis.mcs.protocols.XFContentAttributes();
        pattributes.setPane(pane);
        pattributes.setOutputBuffer(buffer);
        pattributes.setStyles(pageContext.getStylingEngine().getStyles());
        // Create the field descriptor.
        FieldDescriptor fieldDescriptor = new FieldDescriptor();
        fieldDescriptor.setType(ContentFieldType.getSingleton());

        // Add a reference to the field descriptor into the attributes.
        pattributes.setFieldDescriptor(fieldDescriptor);
        // Add the attributes to the list.
        formAttributes.addField(pattributes);

        pageContext.pushContainerInstance(paneInstance);
        pageContext.pushOutputBuffer(buffer);

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * This method is only called if output for the pane is required.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        if (skipEndTag) {
            return CONTINUE_PROCESSING;
        }

        // Get the page context.
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        // pop the Pane & OutputBuffer form the pageContext stacks.
        pageContext.popOutputBuffer(pattributes.getOutputBuffer());
        pageContext.popContainerInstance(paneInstance);

        return CONTINUE_PROCESSING;
    }

    // javadoc inherited from superclass
    public void elementReset(MarinerRequestContext context) {
        super.elementReset(context);
        paneInstance = null;
        pattributes = null;
        skipEndTag = false;
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10321/1	emma	VBM:2005103109 Forward port: Styling not applied correctly to some xf selectors

 14-Nov-05	10300/1	emma	VBM:2005103109 Styling not applied correctly to some xf selectors

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 18-Nov-04	6183/2	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
