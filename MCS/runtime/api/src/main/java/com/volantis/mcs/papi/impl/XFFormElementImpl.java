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
 * $Header: /src/voyager/com/volantis/mcs/papi/XFFormElement.java,v 1.12 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 07-Dec-01    Paul            VBM:2001120703 - Use the default method of
 *                              "get" if it has not been set.
 * 11-Feb-02    Paul            VBM:2001122105 - Added call to initialise
 *                              the form event attributes.
 * 13-Feb-02    Steve           VBM:2001101803 - Initialise the fragmentation
 *                              state for the form when the element is created.
 * 19-Feb-02    Paul            VBM:2001100102 - Create and initialise a
 *                              FormDescriptor object.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 21-Mar-02    Doug            VBM:2002011101 - Modified the elementStart()
 *                              method so that id & styleClass attributes are
 *                              set in the protocol attributes.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 22-Oct-02    Geoff           VBM:2002102102 - Backed out the last change
 *                              for VBM:2002091203, and fixed the original
 *                              bug reported there by adding skipEndTag.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for
 *                              ProtocolException that throws PAPIException.
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
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.XFFormAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The xfform element.
 *
 * @mock.generate
 */
public class XFFormElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XFFormElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(XFFormElementImpl.class);

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.XFFormAttributes pattributes;

    /**
     * The descriptor of the form.
     */
    private FormDescriptor formDescriptor;

    /**
     * Create a new <code>XFFormElement</code>.
     */
    public XFFormElementImpl() {
        pattributes = new com.volantis.mcs.protocols.XFFormAttributes();
    }

    // Javadoc inherited.
    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    /**
     * Get this element's protocol attributes.
     * <h2>
     * You MUST NOT change the protection level on this method.
     * </h2>
     */
    com.volantis.mcs.protocols.XFFormAttributes getProtocolAttributes() {
        return pattributes;
    }

    /**
     * Get this element's descriptor.
     * <h2>
     * You MUST NOT change the protection level on this method.
     * </h2>
     *
     * @return The <code>FormDescriptor</code>.
     */
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        final ResponseCachingDirectives cachingDirectives =
                pageContext.getEnvironmentContext().getCachingDirectives();
        if (cachingDirectives != null) {
            cachingDirectives.disable();
        }

        XFFormAttributes attributes = (XFFormAttributes) papiAttributes;

        // Try and find the form with the specified name, if it could not be
        // found then return and skip the element body.
        String formName = attributes.getName();
        Form form = pageContext.getForm(formName);
        if (form == null) {
            skipped = true;
            return SKIP_ELEMENT_BODY;
        }

        TextAssetReference textReference;
        LinkAssetReference linkReference;
        String value;

        // Copy the base attributes.
        pattributes.setId(attributes.getId());

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Process the action as a mariner expression.
        linkReference = resolver.resolveQuotedLinkExpression(
                attributes.getAction(), PageURLType.FORM);
        pattributes.setAction(linkReference);

        // Set the form attribute.
        FormInstance formInstance =
                (FormInstance) pageContext.getFormatInstance(
                        form, NDimensionalIndex.ZERO_DIMENSIONS);
        pattributes.setFormData(formInstance);

        // Process the help as a mariner expression.
        textReference =
                resolver.resolveQuotedTextExpression(attributes.getHelp());
        pattributes.setHelp(textReference);

        // Set the method attribute, the default is "get".
        value = attributes.getMethod();
        if (value == null) {
            value = "get";
        }
        pattributes.setMethod(value);

        // Set the name attribute.
        String name = attributes.getName();
        pattributes.setName(name);

        // Process the prompt as a mariner expression.
        textReference = resolver.resolveQuotedTextExpression(
                attributes.getPrompt());
        pattributes.setPrompt(textReference);

        // Set the segment attribute.
        pattributes.setSegment(attributes.getSegment());

        // Setup the fragmentation state for the form
        try {
            pageContext.updateFormFragmentationState(form);
        }
        catch (LayoutException le) {
            throw new PAPIException(exceptionLocalizer.format(
                    "form-fragmentation-update-failure"));
        }

        // Initialise form event attributes.
        PAPIInternals.initialiseFormEventAttributes(pageContext, attributes,
                pattributes);

        // Create the form descriptor.
        formDescriptor = new FormDescriptor();
        formDescriptor.setName(name);

        pattributes.setFormDescriptor(formDescriptor);

        // Push this element onto the stack of elements.
        pageContext.pushElement(this);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        if (skipped) {
            return CONTINUE_PROCESSING;
        }

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        VolantisProtocol protocol = pageContext.getProtocol();

        try {
            protocol.doForm(pattributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", pattributes.getTagName(), e);
            throw new PAPIException(
                    exceptionLocalizer.format("rendering-error",
                            pattributes.getTagName()),
                    e);
        }

        // Pop this element off the stack of elements.
        pageContext.popElement(this);

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        skipped = false;
        pattributes.resetAttributes();
        formDescriptor = null;

        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }
}
