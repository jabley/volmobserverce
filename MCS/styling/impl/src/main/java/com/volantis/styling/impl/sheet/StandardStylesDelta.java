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

package com.volantis.styling.impl.sheet;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.Styles;
import com.volantis.styling.impl.engine.StylerContext;
import com.volantis.styling.impl.engine.StandardStylerContext;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Applies set of property values to a standard set of styles.
 */
public class StandardStylesDelta
        extends AbstractStylesDelta {

    /**
     * Initialise.
     *
     * @param entities The path, may be null.
     * @param values   The values to apply.
     */
    public StandardStylesDelta(
            PseudoStyleEntity[] entities,
            PropertyValue[] values) {
        super(entities, values);
    }

    // Javadoc inherited.
    public void applyTo(StylerContext context) {

        StandardStylerContext standardContext = (StandardStylerContext) context;
        Styles styles = standardContext.getStyles();

        // Iterate through the entities (if any) getting the styles associated
        // with them.
        if (entities != null) {
            for (int i = 0; i < entities.length; i++) {
                PseudoStyleEntity entity = entities[i];
                styles = styles.getNestedStyles(entity);
            }
        }

        MutablePropertyValues target = styles.getPropertyValues();

        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            target.setSpecifiedValue(propertyValue.getProperty(),
                    propertyValue.getValue());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
