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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.StyleableFormat;
import com.volantis.styling.Styles;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.sheet.StyleSheetContainer;
import com.volantis.styling.values.ImmutablePropertyValues;

/**
 * Applies styles from a style sheet to formats.
 *
 * <p>This is similar in purpose to a {@link StylingEngine} but applies to a
 * hierarchy of formats, rather than a hierarchy of elements.</p>
 *
 * <p>One major difference is that the formats have default styles associated
 * with them that should override initial values in.</p>
 *
 * @mock.generate 
 */
public interface FormatStylingEngine
        extends StyleSheetContainer {

    /**
     * A new format has been encountered that needs styling.
     *
     * @param styleable The object that is being styled.
     * @param styleClass The style class of the format.
     *
     * @return The styles associated with the object that has just been
     * styled.
     */
    Styles startStyleable(StyleableFormat styleable, String styleClass);

    /**
     * A object has been finished with.
     *
     * @param styleable The object that was being styled.
     */
    void endStyleable(StyleableFormat styleable);

    /**
     * Push the property values into the engine so that they will be inherited
     * from.
     *
     * <p>This does not affect the matching of contextual selectors.</p>
     *
     * @param propertyValues The property values.
     */
    void pushPropertyValues(ImmutablePropertyValues propertyValues);

    /**
     * Pop the property values from the engine.
     *
     * <p>The property values must be the same as the ones passed into a
     * matching call to {@link #pushPropertyValues}.</p>
     *
     * @param propertyValues The property values.
     */
    void popPropertyValues(ImmutablePropertyValues propertyValues);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
