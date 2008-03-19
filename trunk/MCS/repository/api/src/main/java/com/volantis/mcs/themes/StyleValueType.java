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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes;

import java.util.HashMap;
import java.util.Map;

/**
 * Type safe enumeration used to provide the types of
 * {@link StyleValue} that a style property can
 * have
 */
public abstract class StyleValueType {

    /**
     * Map that stores the allowed instances of this class keyed on the
     * type property. This declaration and initialisation must come before the
     * static StyleType instances.
     */
    private static Map types = new HashMap();

    /**
     * Angle style value type
     */
    public static final StyleValueType ANGLE =
        new StyleValueType("angle") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitAngle();
            }
        };

    /**
     * Color style value type
     */
    public static final StyleValueType COLOR =
        new StyleValueType("color") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitColor();
            }
        };

    /**
     * Function call style value type
     */
    public static final StyleValueType FUNCTION_CALL =
        new StyleValueType("function-call") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitFunction();
            }
        };

    /**
     * Color style value type
     */
    public static final StyleValueType IDENTIFIER =
        new StyleValueType("identifier") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitIdentifier();
            }
        };

    /**
     * Inherit style value type
     */
    public static final StyleValueType INHERIT =
        new StyleValueType("inherit") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitInherit();
            }
        };

    /**
     * Inherit style value type
     */
    public static final StyleValueType INITIAL =
        new StyleValueType("initial") {
            public void accept(StyleValueTypeVisitor visitor) {
                throw new UnsupportedOperationException();
            }
        };

    /**
     * Integer style value type
     */
    public static final StyleValueType INTEGER =
        new StyleValueType("integer") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitInteger();
            }
        };

    /**
     * Keyword style value type
     */
    public static final StyleValueType KEYWORD =
        new StyleValueType("keyword") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitKeyword();
            }
        };

    /**
     * Length style value type
     */
    public static final StyleValueType LENGTH =
        new StyleValueType("length") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitLength();
            }
        };

    /**
     * List style value type
     */
    public static final StyleValueType LIST =
        new StyleValueType("list") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitList();
            }
        };

    /**
     * MCS component URI style value type
     */
    public static final StyleValueType COMPONENT_URI =
        new StyleValueType("componentURI") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitComponentURI();
            }
        };

    /**
     * MCS transcodable URI style value type
     */
    public static final StyleValueType TRANSCODABLE_URI =
        new StyleValueType("transcodableURI") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitTranscodableURI();
            }
        };

    /**
     * Number style value type
     */
    public static final StyleValueType NUMBER =
        new StyleValueType("number") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitNumber();
            }
        };

    /**
     * Pair style value type
     */
    public static final StyleValueType PAIR =
        new StyleValueType("pair") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitPair();
            }
        };

    /**
     * Percentage style value type
     */
    public static final StyleValueType PERCENTAGE =
        new StyleValueType("percentage") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitPercentage();
            }
        };

    /**
     * String style value type
     */
    public static final StyleValueType STRING =
        new StyleValueType("string") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitString();
            }
        };

    /**
     * Time style value type
     */
    public static final StyleValueType TIME =
        new StyleValueType("time") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitTime();
            }
        };

    /**
     * URI style value type
     */
    public static final StyleValueType URI =
        new StyleValueType("URI") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitURI();
            }
        };

    /**
     * Fraction style value type
     */
    public static final StyleValueType FRACTION =
        new StyleValueType("fraction") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitFraction();
            }
        };

    /**
     * Frequency style value type
     */
    public static final StyleValueType FREQUENCY =
        new StyleValueType("frequency") {
            public void accept(StyleValueTypeVisitor visitor) {
                visitor.visitFrequency();
            }
        };

    /**
     * Name of the style value type
     */
    private final String type;

    /**
     * The index of the object within the enumeration.
     */
    private final int index;

    /**
     * Initializes a <code>StyleType</code> class with the given
     * paramaters
     * @param type the type to the type
     */
    private StyleValueType(String type) {
        this.type = type.toLowerCase();
        index = types.size();
        if (types.put(this.type, this) != null) {
            throw new IllegalStateException("Multiple types with name " + type);
        }
    }

    /**
     * Returns the type of the type
     */
    public String getType() {
        return type;
    }

    /**
     * Get the index of the type with the enumeration.
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the named StyleType
     */
    public static StyleValueType get(String name) {
        return (StyleValueType) types.get(name.toLowerCase());
    }
    
    /**
     * Accept the visitor for this style value type.
     * 
     * @param visitor the visitor to accept.
     */ 
    public abstract void accept(StyleValueTypeVisitor visitor);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 18-Jan-04	2581/2	geoff	VBM:2003112704 Create the ThemeProxyElementDetailsFactory interface

 09-Jan-04	2467/3	doug	VBM:2003112408 Added the StyleType & StylePropertyDetails type-safe enums

 09-Jan-04	2467/1	doug	VBM:2003112408 Added the StyleType & StylePropertyDetails type-safe enums

 ===========================================================================
*/
