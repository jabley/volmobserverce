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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes;

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.synergetics.factory.MetaDefaultFactory;

import java.util.List;

/**
 * This interface contains methods which create instances of StyleValue.
 *
 * @mock.generate
 */
public abstract class StyleValueFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
            new MetaDefaultFactory(
                "com.volantis.mcs.themes.impl.DefaultStyleValueFactory",
                StyleValueFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static StyleValueFactory getDefaultInstance() {
        return (StyleValueFactory)
            metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Get a StyleAngle object which encapsulates the specified information.
     *
     * @param location The source location of the object, may be null.
     * @param number   The number.
     * @param unit     The unit.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleAngle getAngle(SourceLocation location,
                               double number, AngleUnit unit);

    /**
     * Get a StyleColor object which encapsulates the specified information.
     *
     * @param location The source location of the object, may be null.
     * @param rgb      The RGB value of the color.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleColor getColorByRGB(SourceLocation location, int rgb);

    /**
     * Get a StyleColor object which encapsulates the specified information.
     *
     * @param location The source location of the object, may be null.
     * @param red      The percentage red value.
     * @param green    The percentage green value.
     * @param blue     The percentage blue value.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleColor getColorByPercentages(
            SourceLocation location, double red, double green, double blue);

    /**
     * Get a StyleColor object which encapsulates the specified information.
     *
     * @param name     The the name of the color.
     * @param rgb      The RGB value of the color.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     * @deprecated Use enumeration.
     */
    public abstract StyleColorName getColorByName(
        String name, StyleColorRGB rgb);

    /**
     * Get a StyleComponentURI object which encapsulates the specified
     * information.
     *
     * @param location The source location of the object, may be null.
     * @param uri      The URI.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleComponentURI getComponentURI(
            SourceLocation location, String uri);

    /**
     * Get a StyleComponentURI object which encapsulates the specified
     * information.
     *
     * @param location   The source location of the object, may be null.
     * @param expression The policy expression.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleComponentURI getComponentURI(
            SourceLocation location, PolicyExpression expression);

    /**
     * Get a StyleTranscodableURI object which encapsulates the specified
     * information.
     *
     * @param location The source location of the object, may be null.
     * @param uri      The URI.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleTranscodableURI getTranscodableURI(
            SourceLocation location, String uri);

    /**
     * Get a StyleIdentifier object which encapsulates the specified
     * information.
     *
     * @param location   The source location of the object, may be null.
     * @param identifier The identifier.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleIdentifier getIdentifier(
            SourceLocation location, String identifier);

    /**
     * Get a StyleFunctionCall object which encapsulates the specified
     * information.
     *
     * @param location   The source location of the object, may be null.
     * @param name      The function name.
     * @param arguments The function arguments.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleFunctionCall getFunctionCall(SourceLocation location,
                                             String name, List arguments);

    /**
     * Get the StyleInherit object.
     */
    public abstract StyleInherit getInherit();

    /**
     * Get a StyleInteger object which encapsulates the specified information.
     *
     * @param location The source location of the object, may be null.
     * @param integer  The integer value.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleInteger getInteger(SourceLocation location, int integer);

    /**
     * Get a StyleKeyword object which encapsulates the specified
     * information.
     *
     * @param identifier The identifier.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     * @deprecated Use generated keyword.
     */
    public abstract StyleKeyword getKeyword(String identifier);

    /**
     * Get a StyleInvalid object which encapsulates the specified information.
     *
     * @param value The invalid value.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleInvalid getInvalid(String value);

    /**
     * Get a StyleLength object which encapsulates the specified information.
     *
     * @param location The source location of the object, may be null.
     * @param number   The number.
     * @param unit     The unit.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleLength getLength(SourceLocation location,
                                 double number, LengthUnit unit);

    /**
     * Get a StyleTime object which encapsulates the specified information.
     *
     * @param location The source location of the object, may be null.
     * @param number   The number.
     * @param unit     The unit.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleTime getTime(SourceLocation location,
                             double number, TimeUnit unit);

    /**
     * Get a StyleList object which encapsulates the specified information.
     *
     * @param list The underlying list.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleList getList(List list);

    /**
     * Get a StyleList object which encapsulates the specified information.
     *
     * @param list The underlying list.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleList getList(List list, boolean unique);

    /**
     * Get a StyleNumber object which encapsulates the specified information.
     *
     * @param location The source location of the object, may be null.
     * @param number   The double value.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleNumber getNumber(SourceLocation location, double number);

    /**
     * Get a StylePercentage object which encapsulates the specified
     * information.
     *
     * @param location   The source location of the object, may be null.
     * @param percentage The percentage value.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StylePercentage getPercentage(SourceLocation location,
                                         double percentage);

    /**
     * Get a StylePair object which encapsulates the specified
     * information.
     *
     * @param first  The first StyleValue.
     * @param second The second StyleValue.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StylePair getPair(StyleValue first, StyleValue second);

    /**
     * Get a StyleString object which encapsulates the specified
     * information.
     *
     * @param location The source location of the object, may be null.
     * @param string   The string value.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleString getString(SourceLocation location, String string);

    /**
     * Get a StyleURI object which encapsulates the specified
     * information.
     *
     * @param location The source location of the object, may be null.
     * @param uri The URI.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleURI getURI(SourceLocation location, String uri);

    /**
     * Get a user agent dependent value.
     *
     * @return The new value.
     */
    public abstract StyleUserAgentDependent getUserAgentDependent();

    /**
     * Get a StyleFrequency object which encapsulates the specified
     * information.
     *
     * @param location  The source location of the object, may be null.
     * @param frequency The frequency value
     * @param unit
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleFrequency getFrequency(
            SourceLocation location, double frequency, FrequencyUnit unit);

    /**
     * Get a StyleFraction object which encapsulates the specified
     * information.
     *
     * @param numerator   The numerator component of the fraction value.
     * @param denominator The denominator component of the fraction value.
     * @throws IllegalArgumentException If any of the arguments are invalid.
     */
    public abstract StyleFraction getFraction(
            StyleValue numerator,
            StyleValue denominator);
}
