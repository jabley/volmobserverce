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
 * $Header: /src/voyager/com/volantis/mcs/themes/StyleValueFactory.java,v 1.3 2002/07/30 14:38:36 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Doug            VBM:2002040803 - Created as part of
 *                              implementation of new internal themes structure
 * 02-May-02    Doug            VBM:2002040803 - Added a getBitSet method.
 * 29-Jul-2002  Sumit           VBM:2002072906 - Added StyleTime support 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.expression.impl.PolicyExpressionString;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleUserAgentDependent;
import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleNumber;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleInvalid;

import java.util.List;

/**
 */
public class DefaultStyleValueFactory extends StyleValueFactory {

    private static final StyleInherit STYLE_INHERIT = new StyleInheritImpl();
    private static final StyleUserAgentDependent STYLE_USER_AGENT_DEPENDENT =
            new StyleUserAgentDependentImpl();

    public StyleAngle getAngle(SourceLocation location,
                               double number, AngleUnit unit) {
        return new StyleAngleImpl(location, number, unit);
    }

    public StyleColor getColorByRGB(SourceLocation location, int rgb) {
        return new StyleColorRGBImpl(location, rgb);
    }

    public StyleColor getColorByPercentages(
            SourceLocation location, double red, double green, double blue) {

        return new StyleColorPercentagesImpl(location, red, green, blue);
    }


    public StyleColorName getColorByName(String name, StyleColorRGB rgb) {
        return new StyleColorNameImpl(name, rgb);
    }

    public StyleComponentURI getComponentURI(
            SourceLocation location, String uri) {

        return new StyleComponentURIImpl(
                location, new PolicyExpressionString(uri));
    }

    public StyleComponentURI getComponentURI(
            SourceLocation location, PolicyExpression expression) {
        return new StyleComponentURIImpl(location, expression);
    }

    public StyleTranscodableURI getTranscodableURI(
            final SourceLocation location, final String uri) {

        return new StyleTranscodableURIImpl(location, uri);
    }

    public StyleIdentifier getIdentifier(
            SourceLocation location, String identifier) {
        return new StyleIdentifierImpl(location, identifier);
    }

    public StyleFunctionCall getFunctionCall(SourceLocation location,
                                             String name, List arguments) {
        return new StyleFunctionCallImpl(location, name, arguments);
    }

    public StyleInherit getInherit() {
        return STYLE_INHERIT;
    }

    public StyleInteger getInteger(SourceLocation location, int integer) {
        return new StyleIntegerImpl(integer);
    }


    public StyleInvalid getInvalid(String value) {
        return new StyleInvalidImpl(value);
    }

    public StyleKeyword getKeyword(String identifier) {
        return new StyleKeywordImpl(identifier);
    }

    public StyleLength getLength(SourceLocation location,
                                 double number, LengthUnit unit) {
        return new StyleLengthImpl(location, number, unit);
    }

    public StyleTime getTime(SourceLocation location,
                             double number, TimeUnit unit) {
        return new StyleTimeImpl(location, number, unit);
    }

    public StyleList getList(List list) {
        return new StyleListImpl(list);
    }

    public StyleList getList(List list, boolean unique) {
        return new StyleListImpl(list, unique);
    }

    public StyleNumber getNumber(SourceLocation location, double number) {
        return new StyleNumberImpl(location, number);
    }

    public StylePercentage getPercentage(SourceLocation location,
                                         double percentage) {
        return new StylePercentageImpl(location, percentage);
    }

    public StylePair getPair(StyleValue first, StyleValue second) {
        return new StylePairImpl(first, first, second);
    }

    public StyleString getString(SourceLocation location, String string) {
        return new StyleStringImpl(location, string);
    }

    public StyleURI getURI(SourceLocation location, String uri) {
        return new StyleURIImpl(location, uri);
    }

    public StyleUserAgentDependent getUserAgentDependent() {
        return STYLE_USER_AGENT_DEPENDENT;
    }

    public StyleFrequency getFrequency(
            SourceLocation location, double frequency, FrequencyUnit unit) {
        return new StyleFrequencyImpl(location, frequency, unit);
    }

    public StyleFraction getFraction(
            StyleValue numerator,
            StyleValue denominator) {
        return new StyleFractionImpl(numerator, numerator, denominator);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/3	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
