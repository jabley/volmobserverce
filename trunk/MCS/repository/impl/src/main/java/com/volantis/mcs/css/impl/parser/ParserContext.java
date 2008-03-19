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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.css.impl.parser.properties.StyleValueIterator;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.styling.properties.StyleProperty;

import java.util.Set;

/**
 * <p/>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate 
 */
public interface ParserContext {

    /**
     * Get the style properties that are being constructed.
     * <p/>
     * <p>This is only valid when called while processing declarations within
     * a CSS file. Attempting to call it at any other time will result in an
     * {@link IllegalStateException}.</p>
     *
     * @return The style properties that are being built up.
     */
    public MutableStyleProperties getStyleProperties();

    /**
     * Get the current property name.
     * <p/>
     * <p>This is only valid when called while process a declaration within a
     * CSS file. Attempting to call it at any other time will result in an
     * {@link IllegalStateException}.</p>
     *
     * @return The current property name.
     */
    public String getCurrentPropertyName();

    /**
     * Get the current priority.
     * <p/>
     * <p>This is only valid when called while process a declaration within a
     * CSS file. Attempting to call it at any other time will result in an
     * {@link IllegalStateException}.</p>
     *
     * @return The current priority.
     */
    public Priority getCurrentPriority();

    /**
     * Add a diagnostic message.
     *
     * @param key  The message key.
     * @param args The arguments.
     * @deprecated Use {@link #addDiagnostic(SourceLocation, String, Object[])}. 
     */
    void addDiagnostic(String key, Object[] args);

    /**
     * Add a diagnostic message.
     *
     * @param location The source location of the diagnostic.
     * @param key      The message key.
     * @param args     The arguments.
     */
    void addDiagnostic(SourceLocation location, String key, Object[] args);

    /**
     * Check that the value is valid for the property and if necessary
     * convert the value into an appropriate format for the property.
     * <p/>
     *
     * @param supportedTypes The set of supported types.
     * @param allowableKeywords         The optional mapper.
     * @param value          The value to convert.
     * @return The converted value, or null.
     */
    StyleValue convert(
            Set supportedTypes, AllowableKeywords allowableKeywords, StyleValue value);

    /**
     * Check that the value is valid for the property and if necessary
     * convert the value into an appropriate format for the property.
     * <p/>
     * <p>If the value is valid then it is consumed, i.e. the iterator is
     * advanced to the next value.</p>
     *
     * @param supportedTypes The set of supported types.
     * @param allowableKeywords         The optional mapper.
     * @param iterator       The iterator over the values.
     * @return The converted value, or null.
     */
    StyleValue convertAndConsume(
            Set supportedTypes, AllowableKeywords allowableKeywords,
            StyleValueIterator iterator);

    /**
     * Set the property to the specified value.
     *
     * <p>This also sets the priority.</p>
     *
     * @param property The property to set.
     * @param value The value to set.
     */
    void setPropertyValue(StyleProperty property, StyleValue value);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
