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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.MultipleValidator;
import com.volantis.mcs.protocols.widgets.MultipleValidatorBuilder;
import com.volantis.mcs.protocols.widgets.WidgetDefaultModule;
import com.volantis.mcs.protocols.widgets.attributes.ValidateAttributes;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer for Validate widget suitable for HTML protocols.
 */
public class ValidateDefaultRenderer extends BaseValidatorDefaultRenderer {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(ValidateDefaultRenderer.class);

    /**
     * Used for message localization.
     */
    private static final MessageLocalizer messageLocalizer = LocalizationFactory
            .createMessageLocalizer(ValidateDefaultRenderer.class);

    // Javadoc inherited.
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        // If protocol does not support Framework Client, do not render
        // anything.
        if(!isWidgetSupported(protocol)) {
            return;
        }

        ValidateAttributes validateAttributes = (ValidateAttributes) attributes;

        if (!validateAttributes.isMultiple()) {
            // This is rendering code for simple validator.

            // Create invisible styles for <span> element, which would
            // contain rendered content of the validation messages.
            Styles styles = StylingFactory.getDefaultInstance().createInheritedStyles(
                    protocol.getMarinerPageContext().getStylingEngine().getStyles(),
                    DisplayKeywords.NONE);

            // Open invisible <span> element
            DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

            currentBuffer.openStyledElement("span", styles);

        } else {
            // This is rendering code multiple validator.

            WidgetDefaultModule widgetModule = (WidgetDefaultModule) protocol.getWidgetModule();

            MultipleValidatorBuilder builder = widgetModule.getMultipleValidatorBuilder();

            MultipleValidator validator = builder.getCurrentMultipleValidator();

            validator.setSourceURL(validateAttributes.getSrc());

            if (validateAttributes.getMessageArea() != null) {
                validator.setMessageArea(validateAttributes.getMessageArea());
            }

            if (validateAttributes.getMessagePopup() != null) {
                validator.setMessagePopup(validateAttributes.getMessagePopup());
            }
        }
    }

    // Javadoc inherited.
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        // Handle protocol parametrisation.
        if(!isWidgetSupported(protocol)) {
            return;
        }

        ValidateAttributes validateAttributes = (ValidateAttributes) attributes;

        // Rendering is different, if validate element is used in simple
        // validator or in multiple validator.
        if (!validateAttributes.isMultiple()) {
            // Close invisible <span> element containg content of
            // the validation messages
            DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);

            currentBuffer.closeElement("span");

            require(WIDGET_VALIDATOR, protocol, attributes);
            
            // Gather all required validate attributes.
            String messageArea = validateAttributes.getMessageArea();

            String messagePopup = validateAttributes.getMessagePopup();

            // Render JavaScript
            ScriptAttributes scriptAttributes = new ScriptAttributes();

            scriptAttributes.setLanguage("JavaScript");

            scriptAttributes.setType("text/javascript");

            openScriptElement(scriptAttributes, currentBuffer);

            StringWriter scriptWriter = new StringWriter();

            try {
                scriptWriter.write("Widget.register(" + createJavaScriptString(validateAttributes.getId()) + ",");
                scriptWriter.write("new Widget.SimpleValidator({");
                scriptWriter.write("inputId:" + createJavaScriptString(validateAttributes.getInputElementId()));

                // Render input format
                String inputFormat = validateAttributes.getInputFormat();
                if (inputFormat != null) {
                    scriptWriter.write(", inputFormat:" + createJavaScriptString(inputFormat));
                }

                // Render URL of the validator
                String src = validateAttributes.getSrc();
                if (src != null) {
                    scriptWriter.write(", url:" + createJavaScriptString(src));
                }

                // Render styles for input element
                scriptWriter.write(", invalidInputStyle: " + validateAttributes.getInvalidStyle());

                // Render message-area stuff.
                if (validateAttributes.containsMessageValidationErrorAction()) {
                    // Render message for 'empty-input' validation error.
                    String emptyMessageElementId = validateAttributes.getEmptyMessageElementId();

                    if (emptyMessageElementId != null) {
                        scriptWriter.write(", emptyMessageId:" + createJavaScriptString(emptyMessageElementId));

                    } else {
                        String emptyMessage = messageLocalizer.format("input-required");

                        scriptWriter.write(", emptyMessage:" + createJavaScriptString(emptyMessage + "."));
                    }

                    // Render message for 'invalid-input' validation error.
                    String invalidMessageElementId = validateAttributes
                            .getInvalidMessageElementId();

                    if (invalidMessageElementId != null) {
                        scriptWriter.write(", invalidMessageId:" + createJavaScriptString(invalidMessageElementId));

                    } else {
                        String invalidMessage = messageLocalizer.format("input-invalid");

                        scriptWriter.write(", invalidMessage:" + createJavaScriptString(invalidMessage + "."));
                    }

                    // If there's no messageArea and no messagePopup are
                    // specified, display validation failures in default
                    // JavaScript alert.
                    // Otherwise, display message in message-area (if
                    // specified), and display popup instance (if specified).
                    // Note, that message-area can refer to content inside
                    // the popup instance. The effect would be that before
                    // popup is shows, its content is filled with specified
                    // message.
                    if ((messageArea == null) && (messagePopup == null)) {
                        scriptWriter.write(", displayAlerts: true");

                    } else {
                        if (messageArea != null) {
                            scriptWriter.write(", messageAreaId: " + createJavaScriptString(messageArea));
                        }

                        if (messagePopup != null) {
                            scriptWriter.write(", popupId: " + createJavaScriptString(messagePopup));
                        }
                    }
                }

                // Render autoFocus attribute
                if (validateAttributes.containsFocusValidationErrorAction()) {
                    scriptWriter.write(", autoFocus: true");
                }

                // Close all parentheses.
                scriptWriter.write("}));");

                // Register the same instance of widget under the ID of the
                // input field, so that it can be accessed from multiple validator
                // on client-side.
                // TODO: This is only temporary solution. Ideal solution would be 
                // to pass list of simple validators instances directly in the
                // constructor of multiple validator.
                scriptWriter.write("Widget.register(" 
                        + createJavaScriptString(validateAttributes.getInputElementId()) 
                        + ", Widget.getInstance(" + createJavaScriptString(validateAttributes.getId()) + "));");

                // temporary workaround until new device policy is introduced (content for onblur or onmouse event will be added by javascript method)
                scriptWriter.write("Widget.addStartupItem(function(){Widget.getInstance(" + createJavaScriptString(validateAttributes.getId()) + ").setInputEvents()})");

                // Flush JavaScript to the page.
                writeJavaScript(currentBuffer, scriptWriter.toString());
            } catch (IOException e) {
                throw new ProtocolException(e);
            }

            // Close script element.
            closeScriptElement(currentBuffer);            
            
            // Register this validator widget in the possible enclosing wizard
            // widget.
            WidgetDefaultModule widgetModule = 
                (WidgetDefaultModule) protocol.getWidgetModule();

            WizardDefaultRenderer wizardRenderer = 
                (WizardDefaultRenderer) widgetModule.getWizardRenderer();

            if (wizardRenderer != null) {
                wizardRenderer.renderRegisterSimpleValidator(protocol, validateAttributes.getId());
            }
        }
    }
    
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        return isWidgetSupported(protocol);
    }    
}
