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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.CaptionAwareXFFormAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.xdime.LabelStrategy;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.model.XFormModel;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Describes how an XForms Label element should be processed.
 */
public class XFLabelElementImpl extends StyledXFormsElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(XFLabelElementImpl.class);

    /**
     * Initialize a new instance.
     * @param context
     */
    public XFLabelElementImpl(XDIMEContextInternal context) {
        super(XFormElements.LABEL, new LabelStrategy(), context);
    }

    // Javadoc inherited.
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {
        // The parent should write out any label content unless the model is
        // inactive or null, or the label applies to a group element.
        final XFormModel currentModel =
                context.getXFormBuilder().getCurrentModel();
        if (!(parent instanceof XFGroupElementImpl ||
                currentModel == null || !currentModel.isActive())) {

            if (parent instanceof XFormsControlElement ||
                    parent instanceof XFItemElementImpl) {
                // retrieve the parent's attributes... because this is the
                // label for a control or item.
                CaptionAwareXFFormAttributes attributes =
                        (CaptionAwareXFFormAttributes)
                        ((StyledXFormsElement)parent).getProtocolAttributes();
                final MarinerPageContext pageContext = getPageContext(context);
                final Styles labelStyles =
                        pageContext.getStylingEngine().getStyles();
                attributes.setCaptionStyles(labelStyles);

                // Process the caption as a mariner expression.
                final String labelText = getCharData();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("XForms control|item label: " + labelText);
                }

                PolicyReferenceResolver resolver =
                        pageContext.getPolicyReferenceResolver();

                TextAssetReference caption =
                        resolver.resolveQuotedTextExpression(labelText);
                attributes.setCaption(caption);

                // The caption container instance will have been set to the
                // current container as part of initialising the attributes.
                final ContainerInstance captionContainer =
                        pageContext.getCurrentContainerInstance();
                attributes.setCaptionContainerInstance(captionContainer);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 12-Oct-05	9673/11	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/9	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/5	emma	VBM:2005092807 Adding tests for XForms emulation

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
