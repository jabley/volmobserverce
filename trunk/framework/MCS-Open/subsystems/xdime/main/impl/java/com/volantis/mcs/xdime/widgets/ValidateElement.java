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
 * (c) Volantis Systems Ltd 2006.
 * ----------------------------------------------------------------------------
*/
   
package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.ValidateAttributes;
import com.volantis.mcs.protocols.widgets.renderers.StylesExtractor;
import com.volantis.mcs.protocols.widgets.renderers.WidgetHelper;
import com.volantis.mcs.protocols.widgets.renderers.WidgetRenderer;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.XFInputElementImpl;
import com.volantis.styling.StatefulPseudoClasses;

/**
 * Validate element
 */
public class ValidateElement extends WidgetElement {

    /**
     * Default constructor.
     * @param context
     */
    public ValidateElement(XDIMEContextInternal context) {
        // Validate element is not stylable, so use UnstyledStrategy there.
        // Validate element lies inside not-validable xf:input element,
        // so its validation strategy must be AnywhereStrategy.
        super(WidgetElements.VALIDATE, UnstyledStrategy.STRATEGY,
                context);

        protocolAttributes = new ValidateAttributes();
    }
    
    /**
     * Returns validate attributes.
     * 
     * @return The validate attributes.
     */
    public ValidateAttributes getValidateAttributes() {
        return ((ValidateAttributes) protocolAttributes);
    }

    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        WidgetModule widgetModule =  getWidgetModule(context);
        
        // Skip initialisation if vakidator widget is not supported
        if (null == widgetModule){
            return;
        }
        try {
            WidgetRenderer renderer = widgetModule.getValidateRenderer();
            if (null == renderer || !renderer.isWidgetSupported(getProtocol(context))) {
                return;
            }               
        } catch (ProtocolException e) {
            throw new XDIMEException(e);
        }        
        // At this point we are sure that the widget is supported, 
        // so it makes sense to do all the initialization
        
        ValidateAttributes validateAttributes = (ValidateAttributes) protocolAttributes;

        // Generate ID for the Validator widget.
        if (validateAttributes.getId() == null) {
            validateAttributes.setId(getProtocol(context)
                    .getMarinerPageContext().generateUniqueFCID());
        }

        validateAttributes.setMessageArea(attributes.getValue("", "message-area"));
        validateAttributes.setSrc(getSrcAttributeValue(context, attributes));
        validateAttributes.setMessagePopup(attributes.getValue("", "message-popup"));

        String auto = attributes.getValue("", "auto");
        validateAttributes.setAuto((auto == null) || (!auto.equals("no")));

        // Set flag, indicating whether the validate element
        // is used as a part of simple or multiple validator.
        validateAttributes
                .setMultiple(parent instanceof MultipleValidatorElement);

        if (parent instanceof XFInputElementImpl) {
            MCSAttributes inputAttributes = ((XFInputElementImpl) parent)
                    .getProtocolAttributes();

            // If input element does not have an ID, generate it now,
            // because JavaScript Widget.SimpleValidator class needs
            // to use it as a reference
            if (inputAttributes.getId() == null) {
                inputAttributes.setId(getProtocol(context)
                        .getMarinerPageContext().generateUniqueFCID());
            }

            // Store the ID in validate attributes, for easy access.
            validateAttributes.setInputElementId(inputAttributes.getId());

//            temporarily changed (VMS-321) and implemented in javascript as event type should depends on device policy
//            which will say if browser supports mouse cursor or not.
//            For exmple for NetFront3.4 in SE K660i onblur will be replaced with onmouseout
//            Empty events attribute will be rendered and will be filled in javascript
            if (validateAttributes.isAuto()) {
//               This needs to be done using timers, because without timers the autoFocus feature does not work.
//               String validateString = "setTimeout(function(){Widget.getInstance('"
//                        + validateAttributes.getId() + "').validate()}, 0)";

                String validateString = "";
                inputAttributes.getEventAttributes(true).setEvent(
                        EventConstants.ON_BLUR, validateString);
                inputAttributes.getEventAttributes(true).setEvent(
                        EventConstants.ON_MOUSE_OUT, validateString);
            }

            // Set up the styles extractor to extract the MCS_INVALID value. Set
            // that value in the
            // ValidateAttributes object so that the extractor can use the value
            // when it is run. The
            // value used here is from a pseduoclass.

            // Extract styles from 'mcs-invalid' pseudo-class,
            // to be used to emphasise validation errors.
            StylesExtractor stylesExtractor = 
                WidgetHelper.createStylesExtractor(null, inputAttributes.getStyles());
            stylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_INVALID);
            String invalidStyles = stylesExtractor.getJavaScriptStyles();

            validateAttributes.setInvalidStyle(invalidStyles);

            stylesExtractor.setPseudoClass(null);

            boolean containsMessageAction = stylesExtractor
                    .containsMessageValidationErrorAction();
            validateAttributes
                    .setContainsMessageValidationErrorAction(containsMessageAction);

            boolean containsFocusAction = stylesExtractor
                    .containsFocusValidationErrorAction();
            validateAttributes
                    .setContainsFocusValidationErrorAction(containsFocusAction);

            String inputFormat = stylesExtractor.getInputFormat();
            validateAttributes.setInputFormat(inputFormat);
        }
    }
    
    /**
     * Retrieves the value of the 'src' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'src' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getSrcAttributeValue(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        String src = attributes.getValue("","src");
        
        if (src != null) {
            src = rewriteURLWithPageURLRewriter(context, src, PageURLType.WIDGET);
        }
        
        return src;
    }
}
