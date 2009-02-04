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

package com.volantis.styling.debug;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.Styles;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertySet;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

import java.util.Iterator;

/**
 * Write debug information for {@link Styles}.
 */
public class DebugStyles {

    /**
     * The buffer into which the debug information is added.
     */
    protected final StringBuffer debug;

    /**
     * The properties that should be output.
     */
    protected final StylePropertySet interestingProperties;

    protected final boolean onlyExplicitlySpecified;

    /**
     * Mark those properties which were explicitly specified with a '*'.
     */
    protected final boolean explicitlySpecifiedMarked;

    public DebugStyles(StylePropertySet interestingProperties,
                       boolean onlyExplicitlySpecified) {
        this(interestingProperties, onlyExplicitlySpecified, false);
    }

    /**
     * Initialise.
     *
     * @param interestingProperties The properties that should be output.
     */
    public DebugStyles(StylePropertySet interestingProperties,
            boolean onlyExplicitlySpecified,
            boolean explicitlySpecifiedMarked) {

        if (interestingProperties == null) {
            MutableStylePropertySet mutable = new MutableStylePropertySetImpl();
            mutable.addAll();
            interestingProperties = mutable;
        }

        this.interestingProperties = interestingProperties;
        this.onlyExplicitlySpecified = onlyExplicitlySpecified;
        this.explicitlySpecifiedMarked = explicitlySpecifiedMarked;
        debug = new StringBuffer(1024);
    }

    public DebugStyles() {
        this(null, false, false);
    }

    public String output(Styles styles, String indent) {
        return output(styles, null, indent);
    }

    public String output(Styles styles, PropertyValues parent, String indent) {
        debug.setLength(0);
        if (styles != null) {
            output(indent, "", styles, parent);
        }
        return debug.toString();
    }

    protected void output(
            String indent, String entityRepresentation, Styles styles,
            PropertyValues parent) {
        if (entityRepresentation != null && entityRepresentation.length() != 0) {
            debug.append(entityRepresentation).append(" ");
        }
        output(styles.getPropertyValues());
        Iterator iterator = styles.iterator();
        while (iterator.hasNext()) {
            debug.append("\n");
            NestedStyles nestedStyles = (NestedStyles) iterator.next();
            PseudoStyleEntity entity = nestedStyles.getPseudoStyleEntity();
            debug.append(indent);
            output(indent + DebugHelper.getIndent(entityRepresentation.length() + 1),
                   entityRepresentation + entity.getCSSRepresentation(),
                   nestedStyles, parent);
        }
    }

    protected void output(MutablePropertyValues values) {
        debug.append("{");
        outputDeclarationBody(values);
        debug.append("}");
    }

    protected void outputDeclarationBody(MutablePropertyValues values) {
        Iterator iterator = values.stylePropertyIterator();
        boolean appended = false;
        while (iterator.hasNext()) {
            StyleProperty property = (StyleProperty) iterator.next();
            if (isOutputtable(values, property)) {
                StyleValue value = values.getComputedValue(property);
                if (value != null) {
                    if (appended) {
                        debug.append("; ");
                    } else {
                        appended = true;
                    }
                    debug.append(property.getName()).append(": ")
                            .append(value.getStandardCSS());
                    if (explicitlySpecifiedMarked &&
                            values.wasExplicitlySpecified(property)) {
                        debug.append("*");
                    }
                }
            }
        }
    }

    protected boolean isOutputtable(MutablePropertyValues values,
                                    StyleProperty property) {

        if (!interestingProperties.contains(property)) {
            return false;
        }
        if (onlyExplicitlySpecified) {
            MutablePropertyValues extended =
                    values;
            if (!extended.wasExplicitlySpecified(property)) {
                return false;
            }
        }

        return true;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9286/1	geoff	VBM:2005072208 Normalizing of inferrable properties does not work properly.

 09-Aug-05	9195/1	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
