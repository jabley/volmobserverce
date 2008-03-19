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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.MultipleValidator;
import com.volantis.mcs.protocols.widgets.MultipleValidatorBuilder;
import com.volantis.mcs.protocols.widgets.WidgetDefaultModule;
import com.volantis.mcs.protocols.widgets.attributes.FieldAttributes;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Widget renderer implementation for Field element.
 */
public class FieldDefaultRenderer extends WidgetDefaultRenderer {
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(MultipleValidatorDefaultRenderer.class);

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        
        if (!isWidgetSupported(protocol)) {
            return;
        }

        // Add this field to the current multiple validator instance.
        FieldAttributes fieldAttributes = (FieldAttributes) attributes;

        WidgetDefaultModule widgetModule = (WidgetDefaultModule) protocol.getWidgetModule();
        
        MultipleValidatorBuilder builder = widgetModule.getMultipleValidatorBuilder();

        MultipleValidator validator = builder.getCurrentMultipleValidator();

        if (validator != null) {
            validator.addField(fieldAttributes);
        }
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        // no-op
    }
}
