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

package com.volantis.mcs.protocols.widgets.renderers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.html.XHTMLBasic;
import com.volantis.mcs.protocols.widgets.MultipleValidator;
import com.volantis.mcs.protocols.widgets.MultipleValidatorBuilder;
import com.volantis.mcs.protocols.widgets.WidgetDefaultModule;
import com.volantis.mcs.protocols.widgets.attributes.FieldAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MultipleValidatorAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Default implementation of MultipleValidatorRenderer.
 * 
 * Implementation note: Multiple Validators can not be rendered on-line, 
 * because it may require to access some piece of data, which are 
 * available only after all elements of the page were processed.
 */
public class MultipleValidatorDefaultRenderer extends BaseValidatorDefaultRenderer implements
        MultipleValidatorRenderer {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(MultipleValidatorDefaultRenderer.class);

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        
        // Handle protocol parametrisation
        if(!isWidgetSupported(protocol)) {
            return;
        }

        require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        require(WIDGET_VALIDATOR, protocol, attributes);

        // Add new Multiple Validator instance to the builder.
        MultipleValidatorAttributes validatorAttributes = (MultipleValidatorAttributes) attributes;

        // This should never be null and should cast properly.
        WidgetDefaultModule widgetModule = (WidgetDefaultModule) protocol.getWidgetModule();
        
        MultipleValidatorBuilder builder = widgetModule.getMultipleValidatorBuilder();

        builder.addMultipleValidator(validatorAttributes);
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        // Handle protocol parametrisation
        if(!isWidgetSupported(protocol)) {
            return;
        }

        // Register this validator widget in the possible enclosing wizard
        // widget. This should never be null and should cast properly.
        WidgetDefaultModule widgetModule = (WidgetDefaultModule) protocol.getWidgetModule();

        WizardDefaultRenderer wizardRenderer = 
            (WizardDefaultRenderer) widgetModule.getWizardRenderer();

        if (wizardRenderer != null) {
            wizardRenderer.renderRegisterMultipleValidator(protocol, attributes.getId());
        }
    }

    /**
     * Renders all built multiple validators.
     * 
     * @param protocol The protocol used for rendering
     * @throws ProtocolException
     */
    public void render(VolantisProtocol protocol) throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }
        
        // This should never be null and should cast properly.
        WidgetDefaultModule widgetModule = (WidgetDefaultModule) protocol.getWidgetModule();
        
        MultipleValidatorBuilder builder = widgetModule.getMultipleValidatorBuilder();

        // Render all multiple validators, which were added to the builder.
        Iterator iterator = builder.getMultipleValidatorsIterator();

        while (iterator.hasNext()) {
            MultipleValidator validator = (MultipleValidator) iterator.next();

            render(protocol, builder, validator);
        }
    }

    /**
     * Renders an instance multiple validator.
     * 
     * @param protocol The protocol to use for rendering
     * @param builder The validator's owning builder
     * @param validator The validator to render
     * @throws ProtocolException
     */
    private void render(VolantisProtocol protocol, MultipleValidatorBuilder builder,
            MultipleValidator validator) throws ProtocolException {

        // This should always work, since this implementation of rendered is
        // tightly coupled with XHTMLBasic protocol
        XHTMLBasic actualProtocol = (XHTMLBasic) protocol; 

        // Render script element.
        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

        openScriptElement(currentBuffer);

        StringWriter scriptWriter = new StringWriter();

        StylesExtractor stylesExtractor = new StylesExtractor();

        try {
            // Render multiple validator registration.
            String id = validator.getAttributes().getId();

            boolean renderRegistration = (id != null);

            if (renderRegistration) {
                scriptWriter.write("Widget.register(" + createJavaScriptString(id) + ",");
            }

            // Render multiple validator instantiation
            scriptWriter.write("new Widget.MultipleValidator({");

            // Render fields involved in validation.
            scriptWriter.write("fields:{");

            Iterator fieldsIterator = validator.getFieldsIterator();

            boolean firstField = true;

            while (fieldsIterator.hasNext()) {
                FieldAttributes fieldAttributes = (FieldAttributes) fieldsIterator.next();

                // Get attributes of refered input element.
                String inputId = fieldAttributes.getRef();

                XFFormFieldAttributes formFieldAttributes = actualProtocol.getFormFieldAttributes(inputId);

                // If no attributes were found, it means that the refered text
                // input field
                // does not exist. In that case, don't render it.
                if (formFieldAttributes != null) {
                    // Render comma, if this is not the first rendered field.
                    if (!firstField) {
                        scriptWriter.write(", ");
                    }

                    // Get styles of the input element.
                    stylesExtractor.setStyles(formFieldAttributes.getStyles());

                    stylesExtractor.setPseudoClass(null);

                    // Render field name, which equals to ID of the input
                    // element.
                    scriptWriter.write(createJavaScriptString(inputId) + ":{");

                    // Render ID of the input element.
                    scriptWriter.write("inputId: " + createJavaScriptString(inputId));

                    // Render message area for field, if 'message' was specified
                    // as one of the validation error actions.
                    boolean displayMessages = stylesExtractor
                            .containsMessageValidationErrorAction();

                    if (displayMessages) {
                        String messageId = fieldAttributes.getMessageArea();

                        if (messageId != null) {
                            scriptWriter.write(", messageAreaId: " + createJavaScriptString(messageId));
                        }
                    }

                    // Render invalid styles of the input field.
                    stylesExtractor.setPseudoClass(StatefulPseudoClasses.MCS_INVALID);

                    scriptWriter.write(", invalidInputStyle:"
                            + stylesExtractor.getJavaScriptStyles());

                    // Render parenthesis closure of the field attributes
                    scriptWriter.write("}");

                    // First field has just been rendered, so clear the
                    // 'firstField' flag.
                    firstField = false;
                }
            }

            // Close fields parenthesis.
            scriptWriter.write("}");

            // Render URL of the validator.
            String url = validator.getSourceURL();

            if (url != null) {
                scriptWriter.write(", url:" + createJavaScriptString(url));
            }

            stylesExtractor.setStyles(validator.getAttributes().getStyles());

            stylesExtractor.setPseudoClass(null);

            // Render stuff to enable validation messages.
            if (stylesExtractor.containsMessageValidationErrorAction()) {
                // Render message area of the validator.
                String messageArea = validator.getMessageArea();

                String messagePopup = validator.getMessagePopup();

                if ((messageArea == null) && (messagePopup == null)) {
                    // No message area and no popup specified -> use default
                    // browser's alert dialog to display validation result.
                    scriptWriter.write(", displayAlerts: true");

                } else {
                    // If message area is specified -> render it
                    if (messageArea != null) {
                        scriptWriter.write(", messageAreaId: " + createJavaScriptString(messageArea));

                    }

                    // If popup is specified -> render it
                    if (messagePopup != null) {
                        scriptWriter.write(", popupId: " + createJavaScriptString(messagePopup));
                    }
                }
            }

            // Render stuff to enable autoFocus feature.
            if (stylesExtractor.containsFocusValidationErrorAction()) {
                scriptWriter.write(", autoFocus: true");
            }

            // Render closure of all remaining parentheses.
            scriptWriter.write("})");

            // Remember about the closing parenthesis of widget registration.
            if (renderRegistration) {
                scriptWriter.write(")");
            }

            // And flush buffered Javascript string into page.
            writeJavaScript(currentBuffer, scriptWriter.toString());
        } catch (IOException e) {
            throw new ProtocolException();
        }

        // Close <script> element
        closeScriptElement(currentBuffer);
    }
}
