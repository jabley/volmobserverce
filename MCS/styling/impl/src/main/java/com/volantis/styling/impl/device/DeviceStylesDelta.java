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

package com.volantis.styling.impl.device;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.impl.engine.StylerContext;
import com.volantis.styling.impl.sheet.AbstractStylesDelta;

/**
 * The delta that is applied when a rule matches.
 */
public class DeviceStylesDelta
        extends AbstractStylesDelta {

    /**
     * Initialise.
     *
     * @param entities The array of entities that defines the path from the
     *                 root styles.
     * @param values   The array of values to set.
     */
    public DeviceStylesDelta(
            PseudoStyleEntity[] entities,
            PropertyValue[] values) {
        super(entities, values);
    }

    // Javadoc inherited.
    public void applyTo(StylerContext context) {

        DeviceStylerContext deviceContext = (DeviceStylerContext) context;
        DeviceStylesBuilder styles = deviceContext.getStylesBuilder();

        // Iterate through the entities (if any) getting the styles associated
        // with them.
        if (entities != null) {
            for (int i = 0; i < entities.length; i++) {
                PseudoStyleEntity entity = entities[i];
                styles = styles.getNestedBuilder(entity);
            }
        }

        DeviceValuesBuilder target = styles.getValuesBuilder();

        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            target.setPropertyValue(propertyValue);
        }
    }
}
