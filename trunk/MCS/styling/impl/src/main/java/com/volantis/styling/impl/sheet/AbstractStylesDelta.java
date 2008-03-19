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

package com.volantis.styling.impl.sheet;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.debug.DebugStylingWriter;

/**
 * Base class for all classes that apply a change as a result of a matching
 * selector.
 */
public abstract class AbstractStylesDelta
        implements StylesDelta {

    /**
     * The path to the nested styles, will be null if the styles are for an
     * element.
     */
    protected final PseudoStyleEntity[] entities;

    /**
     * The array of values to apply.
     */
    protected final PropertyValue[] propertyValues;

    /**
     * Initialise.
     *
     * @param entities The path, may be null.
     * @param values   The values to apply.
     */ 
    public AbstractStylesDelta(
            PseudoStyleEntity[] entities, PropertyValue[] values) {

        this.entities = entities;
        this.propertyValues = values;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        if (entities != null) {
            for (int i = 0; i < entities.length; i++) {
                PseudoStyleEntity entity = entities[i];
                writer.print(entity.getCSSRepresentation());
            }
            writer.print(" ");
        }

        writer.print("{");
        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            writer.print(propertyValue.getProperty(),
                    propertyValue.getValue(), ";");
        }
        writer.print("}");
    }
}
