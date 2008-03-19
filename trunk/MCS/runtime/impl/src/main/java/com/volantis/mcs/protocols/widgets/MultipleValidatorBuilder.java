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

package com.volantis.mcs.protocols.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.MultipleValidatorAttributes;
import com.volantis.mcs.protocols.widgets.renderers.MultipleValidatorDefaultRenderer;

/**
 * Multiple validator builder.
 */
public class MultipleValidatorBuilder {
    /**
     * The origin widget module.
     */
    private WidgetDefaultModule widgetModule;

    /**
     * List of added multiple validators.
     */
    private ArrayList multipleValidators;

    /**
     * Creates new instance of multiple validator builder, with specified origin
     * widget module.
     * 
     * @param widgetModule the origin widget module.
     */
    public MultipleValidatorBuilder(WidgetDefaultModule widgetModule) {
        this.widgetModule = widgetModule;
    }

    /**
     * Creates and returns new instance of MultipleValidator with specified
     * attributes, making it the current one.
     * 
     * @param attributes The multiple validator attributes
     * @return Returns created multiple validator.
     */
    public MultipleValidator addMultipleValidator(MultipleValidatorAttributes attributes) {
        MultipleValidator multipleValidator = new MultipleValidator(attributes);

        if (multipleValidators == null) {
            multipleValidators = new ArrayList(1);
        }

        multipleValidators.add(multipleValidator);

        return multipleValidator;
    }

    /**
     * Returns current multiple validator, or null if there's no current one.
     * 
     * @return Returns current multiple validator
     */
    public MultipleValidator getCurrentMultipleValidator() {
        MultipleValidator multipleValidator = null;

        if (multipleValidators != null) {
            if (!multipleValidators.isEmpty()) {
                multipleValidator = (MultipleValidator) multipleValidators.get(multipleValidators
                        .size() - 1);
            }
        }

        return multipleValidator;
    }

    /**
     * Renders all added multiple validators, using specified protocol.
     * 
     * @param protocol The protocol used for rendering
     * @throws ProtocolException
     */
    public void render(VolantisProtocol protocol) throws ProtocolException {
        // Delegate rendering to multiple validator renderer instance
        MultipleValidatorDefaultRenderer renderer = (MultipleValidatorDefaultRenderer) widgetModule
                .getMultipleValidatorRenderer();

        if (renderer != null) {
            renderer.render(protocol);
        }
    }

    /**
     * Returns iterator over added multiple validators.
     * 
     * @return Returns the iterator over added multiple validator.
     */
    public Iterator getMultipleValidatorsIterator() {
        if (multipleValidators == null) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            return multipleValidators.iterator();
        }
    }
}
