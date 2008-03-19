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
package com.volantis.mcs.dom;

import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.Styles;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.debug.DebugHelper;
import com.volantis.styling.debug.DebugStyles;
import com.volantis.styling.properties.StylePropertySet;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

import java.util.Iterator;

public class XMLDebugStyles extends DebugStyles {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param interestingProperties the bit set of properties which should not
     * @param onlyExplicitlySpecified
     */
    public XMLDebugStyles(StylePropertySet interestingProperties, 
                          boolean onlyExplicitlySpecified) {
        super(interestingProperties, onlyExplicitlySpecified);
    }

    // Javadoc inherited.
    protected void output(
            String indent, String entityRepresentation,
            Styles styles, PropertyValues parent) {
        if (entityRepresentation != null && !entityRepresentation.equals("")) {
            debug.append(entityRepresentation).append(" ");
        }
        // copy the currently compiled StringBuffer out of the way
        StringBuffer current = emptyDebugBuffer();
        output(styles.getPropertyValues());
        StringBuffer propertyValues = emptyDebugBuffer();
        Iterator iterator = styles.iterator();
        while (iterator.hasNext()) {
            NestedStyles nestedStyles = (NestedStyles) iterator.next();
            PseudoStyleEntity entity = nestedStyles.getPseudoStyleEntity();
            debug.append(" ");
            output(indent +
                    DebugHelper.getIndent(entityRepresentation.length() + 1),
                    entityRepresentation + entity.getCSSRepresentation(),
                    nestedStyles, parent);
        }
        if (debug.length() > 0 || current.length() > 0) {
            propertyValues.insert(0, '{');
            propertyValues.append('}');
        }
        debug.insert(0, propertyValues);
        debug.insert(0, current);
    }

    /**
     * Convenience method to empty the debug buffer into a newly created
     * StringBuffer.
     *
     * @return a newly created StringBuffer which has been populated with the 
     * value of the debug buffer at the point when the method was called.
     */
    private StringBuffer emptyDebugBuffer() {
        StringBuffer temp = new StringBuffer();
        temp.append(debug);
        debug.delete(0, debug.length());
        return temp;
    }

    // Javadoc inherited.
    protected void output(MutablePropertyValues values) {
        outputDeclarationBody(values);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9286/1	geoff	VBM:2005072208 Normalizing of inferrable properties does not work properly.

 09-Aug-05	9195/1	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 ===========================================================================
*/
