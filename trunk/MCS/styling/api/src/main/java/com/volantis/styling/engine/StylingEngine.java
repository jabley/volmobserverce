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

package com.volantis.styling.engine;

import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.sheet.StyleSheetContainer;
import com.volantis.mcs.themes.StyleValues;

/**
 * Applies the styles from a style sheet to a document.
 *
 * <p>These are created from a {@link CompiledStyleSheet} using the
 * {@link StylingFactory#createStylingEngine} method.</p>
 *
 * <p>The document is provided as a sequence of events similar to SAX 2.</p>
 *
 * @mock.generate 
 */
public interface StylingEngine
        extends StyleSheetContainer {

    /**
     * A new element has been encountered that needs styling.
     *
     * @param namespace The namespace of the element, may not be null.
     * @param localName The local name of the element.
     * @param attributes The attributes of the element.
     */
    void startElement(String namespace, String localName,
                      Attributes attributes);

    /**
     * Get the styles associated with the element that has just been styled.
     *
     * @return The styles associated with the element that has just been
     * styled.
     */
    Styles getStyles();

    /**
     * An element has been finished with.
     *
     * @param namespace The namespace of the element, may not be null.
     * @param localName The local name of the element, may not be null.
     */
    void endElement(String namespace, String localName);

    /**
     * Push the property values into the engine so that they will be inherited
     * from.
     *
     * <p>This does not affect the matching of contextual selectors.</p>
     *
     * @param propertyValues The property values.
     */
    void pushPropertyValues(StyleValues propertyValues);

    /**
     * Pop the property values from the engine.
     *
     * <p>The property values must be the same as the ones passed into a
     * matching call to {@link #pushPropertyValues}.</p>
     *
     * @param propertyValues The property values.
     */
    void popPropertyValues(StyleValues propertyValues);

    /**
     * Retrieve the value of a named counter from the engine.
     *
     * <p>If the counter does not currently exist, then the underlying
     * {@link com.volantis.styling.impl.counter.Counters} class will cause
     * one to be created and set to zero.</p>
     *
     * @param counterName The name of the counter to be retrieved
     * @return The current value of that counter
     */
    int getCounterValue(String counterName);

    /**
     * Retrieve all in-scope values of a named counter from the engine.
     *
     * <p>If the counter does not currently exist, then the underlying
     * {@link com.volantis.styling.impl.counter.Counters} class will cause
     * one to be created and set to zero.</p>
     *
     * @param counterName The name of the counter to be retrieved
     * @return The current in-scope values of that counter
     */
    int[] getCounterValues(String counterName);

    /**
     * Get the context within which the evaluation of function occurs.
     *
     * @return The evaluation context.
     */
    EvaluationContext getEvaluationContext();
    
    /**
     * Adding the style sheet to style sheet list
     *
     * @param styleSheet The name of the style sheet to be added
     */
    void addNestedStyleSheet(CompiledStyleSheet styleSheet );
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 30-Sep-05	9635/3	adrianj	VBM:2005092817 Counter functions for CSS

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 18-Aug-05	9007/6	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/4	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
