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
/*
 * $Header: /src/voyager/com/volantis/mcs/papi/Attic/NativeMarkupElement.java,v 1.1.2.3 2003/04/16 15:41:28 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - Ported from metis.
 * 14-Mar-2003  Chris W         VBM:2003030403 - Created
 * 18-Mar-2003  Chris W         VBM:2003030403 - Remove try, catch around calls
 *                              to writeOpenNativeMarkup & writeCloseNativeMarkup
 *                              in elementStart & elementEnd.
 * 15-Apr-03    Steve           VBM:2003041501 - Output to native writer.
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent(), isPreFormatted()
 *                              made package protected. getNativeWriter() pushed
 *                              down from AbstractElement.
 * 23-May-03    Phil W-S        VBM:2003052301 - Wrap logger.debug() statements
 *                              in condition.
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.ExprAttributes;
import com.volantis.mcs.papi.NativeMarkupAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.util.EmptyStackException;

/**
 * This is the PAPI class for nativemarkup elements. It selects the correct
 * output buffer in which to write the native markup.
 */
public class NativeMarkupElementImpl
        extends AbstractExprElementImpl {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(NativeMarkupElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(NativeMarkupElementImpl.class);

    /**
     * The pane instance which this element pushed onto the stack, or null if
     * no pane name was specified for this element.
     */
    private AbstractPaneInstance paneInstance;

    /**
     * The attributes to pass to the protocol methods.
     */
    private com.volantis.mcs.protocols.NativeMarkupAttributes pattributes;

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    public NativeMarkupElementImpl() {
        pattributes = new com.volantis.mcs.protocols.NativeMarkupAttributes();
    }

    /**
     * The default behaviour of the <nativemarkup>  element, when not within
     * the scope of some content selection, is to process as if the native
     * markup is intended for a WML device. The phrase 'within the scope of
     * some content selection' means either
     *
     * 1) there is an expr attribute on the <nativemarkup> element
     * or
     * 2) the <nativemarkup> element is within the scope of a <select> element
     *
     * If the markup is not intended for a WML device it means that one of
     * the above rules most be met.
     *
     * This behaviour allows backwards compatibility with the usage of the
     * <nativemarkup> element in previous releases of MCS.
     *
     * This method enforces the above rules.
     *
     * @param context    The MarinerRequestContext
     * @param attributes The attributes.
     * @return true if the element satisfies the rules.
     */
    public static boolean continueProcessing(
            MarinerRequestContext context,
            PAPIAttributes attributes) {
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);
        VolantisProtocol protocol = pageContext.getProtocol();

        //test to see if the protocol can process the native markup element
        //without having tested for meeting the above rules -
        //true indicates a WML Device.
        boolean continueProcessing =
                protocol.doProcessNativeMarkupWithoutExpression();

        //test rules.
        if (!continueProcessing) {
            // Check to see whether an expression was set.
            if (((ExprAttributes) attributes).getExpr() != null) {
                // If an expression was set, then OK to continue
                continueProcessing = true;
            } else {
                // No expression set, see if element is in a select element
                try {
                    SelectState s = pageContext.peekSelectState();
                    // Embedded in a select - ok to continue
                    continueProcessing = true;
                } catch (EmptyStackException ese) {
                    // Not embedded in a select element
                    continueProcessing = false;
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Continue processing = " + continueProcessing);
        }
        return continueProcessing;
    }

    // Javadoc inherited.
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);
        VolantisProtocol protocol = pageContext.getProtocol();

        // Check to see if the protocol supports the markup and
        // the conditions for processing the element have been
        // satisfied
        if (protocol.supportsNativeMarkup() &&
                continueProcessing(context, papiAttributes)) {
            NativeMarkupAttributes attributes =
                    (NativeMarkupAttributes) papiAttributes;
            String paneName = attributes.getPane();

            // Get the value of the targetLocation attribute, which could be
            // a text component.
            PolicyReferenceResolver resolver =
                    pageContext.getPolicyReferenceResolver();
            TextAssetReference reference = resolver.resolveQuotedTextExpression(
                    attributes.getTargetLocation());
            String target;
            if (reference != null) {
                target = reference.getText(TextEncoding.PLAIN);
            } else {
                target = null;
            }

            pattributes.setTargetLocation(target);

            if (com.volantis.mcs.protocols.NativeMarkupAttributes.PANE.
                    equals(target)) {
                if (paneName != null && !paneName.equals("")) {
                    // Deal with possibility that the native markup element is
                    // in a format iterator
                    FormatReference formatRef =
                            FormatReferenceParser
                                    .parsePane(paneName, pageContext);
                    Pane pane = pageContext.getPane(formatRef.getStem());
                    NDimensionalIndex paneIndex = formatRef.getIndex();
                    pattributes.setPane(formatRef.getStem());

                    if (pane == null) {
                        skipped = true;
                        return SKIP_ELEMENT_BODY;
                    }

                    paneInstance =
                            (AbstractPaneInstance) pageContext
                                    .getFormatInstance(
                                            pane, paneIndex);
                    if (paneInstance.ignore()) {
                        skipped = true;
                        return SKIP_ELEMENT_BODY;
                    }

                    pageContext.pushContainerInstance(paneInstance);
                } else {
                    // The target attribute is "pane" and the pane attibribute
                    // invalid. This is not allowed.
                    throw new PAPIException(
                            exceptionLocalizer.format("pane-name-required"));
                }
            }

            // Push this element onto the stack of elements.
            pageContext.pushElement(this);
            skipped = false;

            try {
                protocol.writeOpenNativeMarkup(pattributes);
            } catch (ProtocolException e) {
                // If the targetLocation was invalid, abort processing of the
                // element and issue a warning.
                logger.warn("unexpected-exception", e);
                skipped = true;
                return SKIP_ELEMENT_BODY;
            }
            return PROCESS_ELEMENT_BODY;
        } else {
            // Either the protocol doesn't support it, or
            // continueProcessing refused us.
            if (logger.isDebugEnabled()) {
                if (protocol.supportsNativeMarkup()) {
                    logger.debug("nativemarkup tag has no expression or is" +
                            " not embedded in a select statement");
                } else {
                    logger.debug(
                            "nativemarkup tag not supported in this protocol "
                                    + protocol);
                }
            }
            skipped = true;
            return SKIP_ELEMENT_BODY;
        }
    }

    // Javadoc inherited.
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {
        if (!skipped) {
            // Pop this element off the stack of elements.
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);
            pageContext.popElement(this);

            VolantisProtocol protocol = pageContext.getProtocol();

            try {
                protocol.writeCloseNativeMarkup(pattributes);
            } catch (ProtocolException e) {

                logger.error("rendering-error",
                        new Object[]{pattributes.getTagName()}, e);
                // MCSPA0071X="Error during {0} rendering"
                new PAPIException(
                        exceptionLocalizer.format("rendering-error",
                                pattributes.getTagName()),
                        e);
            }

            if (paneInstance != null) {
                pageContext.popContainerInstance(paneInstance);
            }
        }
        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        pattributes.resetAttributes();
        paneInstance = null;
        skipped = false;
        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    public void elementContent(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes,
            String content)
            throws PAPIException {

        if (logger.isDebugEnabled()) {
            logger.debug("elementContent - " + content);
        }

        Writer writer = getNativeWriter(context);
        try {
            writer.write(content);
        } catch (IOException e) {
            throw new PAPIException(e);
        }
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }

    // Javadoc inherited from super class.
    boolean isPreFormatted() {
        return true;
    }

    /**
     * Get a Writer that can be used to write native markup to the current
     * output location.
     *
     * @param context the current request context
     * @return the Writer derived object to use
     * @throws PAPIException
     */
    private Writer getNativeWriter(MarinerRequestContext context)
            throws PAPIException {
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        VolantisProtocol protocol = pageContext.getProtocol();
        return protocol.getNativeWriter();
    }

    // Javadoc inherited from super class.
    public Writer getContentWriter(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return getNativeWriter(context);
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.papi.PAPIElement#elementDirect(
     *      com.volantis.mcs.context.MarinerRequestContext,
     *      com.volantis.mcs.papi.PAPIAttributes, java.lang.String)
     */
    public void elementDirect(
            MarinerRequestContext context,
            String content) throws PAPIException {
        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 19-Feb-04	2789/7	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/5	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 23-Jun-03	459/1	mat	VBM:2003061910 Change getContentWriter() to return correct nativeWriter for Native markup elements

 ===========================================================================
*/
