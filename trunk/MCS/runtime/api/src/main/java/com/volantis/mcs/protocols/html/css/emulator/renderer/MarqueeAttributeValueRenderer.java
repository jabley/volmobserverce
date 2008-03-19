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
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationAttributeValueRenderer;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.MCSMarqueeRepetitionKeywords;

/**
 * Style emulation renderer which renders the marquee attribute values as css,
 * ensuring that they are valid, and defaulting to initial values if not.
 */
public final class MarqueeAttributeValueRenderer
        implements StyleEmulationAttributeValueRenderer {

    /**
     * The infinite keyword is not supported by all protocols that support the
     * marquee element (e.g. HTML_iMode). This flag indicates whether the
     * keyword should be rendered as is, or whether it should be mapped to the
     * initial value.
     */
    private final boolean supportsInfiniteKeyword;

    /**
     * The maximum repetition value as stated in the iMode specification
     * (values greater than this are not guaranteed to be supported). This is
     * used when the infinite keyword is used but the protocol doesn't support it.
     */
    private static final StyleInteger MAX_REPETITION =
        StyleValueFactory.getDefaultInstance().getInteger(null, 16);

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param supportsInfiniteKeyword   indicates whether the infinite keyword
     *                                  is supported for this protocol.
     */
    public MarqueeAttributeValueRenderer(
            boolean supportsInfiniteKeyword) {
        this.supportsInfiniteKeyword = supportsInfiniteKeyword;
    }

    // Javadoc inherited.
    public String render(StyleValue value) {
        String renderedValue;
        if (MCSMarqueeRepetitionKeywords.INFINITE.equals(value) &&
                !supportsInfiniteKeyword) {
            // If the infinite keyword isn't supported, then use the largest
            // allowable value.
            renderedValue = renderRepetition(MAX_REPETITION);
        } else if (value instanceof StyleInteger) {
            // The only marquee style which can be an integer is repetition.
            renderedValue = renderRepetition((StyleInteger) value);
        } else {
            renderedValue = value.getStandardCSS();
        }
        return renderedValue;
    }

    /**
     * Render the given integer as css, ensuring that is valid (i.e. not
     * negative and not more than the maximum supported value) and if not,
     * rendering the initial value.
     *
     * @param integer   to be rendered
     * @return String css representation of the supplied parameter
     */
    private String renderRepetition(StyleInteger integer) {
        final String renderedValue;

        int repetition = integer.getInteger();
        if (repetition > 0 && repetition <= MAX_REPETITION.getInteger()) {
            renderedValue = integer.getStandardCSS();
        } else {
            // If it's not a valid value, then use the default
            renderedValue = StylePropertyDetails.MCS_MARQUEE_REPETITION.
                getStandardDetails().getInitialValue().getStandardCSS();
        }

        return renderedValue;
    }
}
