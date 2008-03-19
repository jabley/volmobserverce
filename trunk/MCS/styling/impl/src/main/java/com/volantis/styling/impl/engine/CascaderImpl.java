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

package com.volantis.styling.impl.engine;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerIteratee;
import com.volantis.styling.impl.sheet.StylerIterator;
import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.engine.Attributes;

/**
 * Implementation of {@link Cascader}.
 */
public class CascaderImpl
        implements StylerIteratee, Cascader {

    /**
     * The iterator over {@link Styler}s.
     */
    private final StylerIterator iterator;

    /**
     * The context used by the {@link Styler}.
     */
    private final StylerContext stylerContext;

    /**
     * Flag to keep track of whether styling was deferred by one of the
     * {@link Styler}s.
     */
    private boolean stylingDeferred;

    /**
     * Initialise.
     *
     * @param iterator The iterator.
     * @param stylerContext The context.
     */
    public CascaderImpl(StylerIterator iterator, StylerContext stylerContext) {
        this.iterator = iterator;
        this.stylerContext = stylerContext;
    }

    // Javadoc inherited.
    public void cascade(Attributes attributes) {

        // Clear the flag that indicates whether the styling has to be
        // deferred.
        stylingDeferred = false;

        // Make sure that the matcher is prepared for the cascade.
        MatcherContext matcherContext = stylerContext.getMatcherContext();
        matcherContext.prepareForCascade(attributes);

        // Iterate over the stylers asking them to match.
        iterator.iterate(this);

        if (stylingDeferred) {
            throw new UnsupportedOperationException(
                    "Deferred styling not supported");
        }
    }

    public IterationAction next(Styler styler) {
        Styler engineStyler = styler;

        StylerResult result = engineStyler.style(stylerContext);
        if (result == StylerResult.DEFERRED) {
            stylingDeferred = true;
            return IterationAction.BREAK;
        }

        return IterationAction.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Corrected issue with styling

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
