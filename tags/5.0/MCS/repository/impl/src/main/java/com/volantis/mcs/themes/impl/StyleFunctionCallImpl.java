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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValue;

import java.util.List;

/**
 */
public final class StyleFunctionCallImpl
        extends StyleValueImpl implements StyleFunctionCall {

    /**
     * The name of the function being invoked.
     */
    private String name;

    /**
     * The arguments to pass to the function.
     */
    private List arguments;

    /**
     * Package private constructor for use by JiBX.
     */
    StyleFunctionCallImpl() {
    }

    /**
     * Initialise.
     *
     * @param name      The name of the function.
     * @param arguments The arguments as {@link StyleValue}s.
     */
    public StyleFunctionCallImpl(String name, List arguments) {
        this(null, name, arguments);
    }

    /**
     * Initialise.
     *
     * @param location  The source location of the object, may be null.
     * @param name      The name of the function.
     * @param arguments The arguments as {@link StyleValue}s.
     */
    public StyleFunctionCallImpl(
            SourceLocation location, String name, List arguments) {
        super(location);

        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List getArguments() {
        return arguments;
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        return StyleValueType.FUNCTION_CALL;
    }

    // Javadoc inherited.
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    protected boolean equalsImpl(Object o) {
        if (!(o instanceof StyleFunctionCallImpl)) {
            return false;
        }

        StyleFunctionCallImpl other = (StyleFunctionCallImpl) o;

        return name.equals(other.name) && arguments.equals(other.arguments);
    }

    protected int hashCodeImpl() {
        int result = 0;
        result = 37 * result + getName().hashCode();
        result = 37 * result + arguments.hashCode();
        return result;
    }

    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            StyleValue styleValue = (StyleValue) arguments.get(i);
            if (i != 0) {
                buffer.append(", ");
            }
            buffer.append(styleValue);
        }
        buffer.append(")");
        return buffer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
