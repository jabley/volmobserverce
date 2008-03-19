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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.version;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.AbstractSelectorVisitor;
import com.volantis.mcs.themes.NthChildSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * A default implementation of CSSSelectorFilter.
 */
public class DefaultCSSSelectorFilter
        extends AbstractSelectorVisitor
        implements CSSSelectorFilter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DefaultCSSSelectorFilter.class);

    /**
     * The CSS version this filter is targetting.
     */
    private CSSVersion cssVersion;

    /**
     * Flag indicating whether the selector should be filtered out.
     */
    private boolean filtered;

    /**
     * Initialise.
     *
     * @param cssVersion the CSS version this filter is targetting.
     */
    public DefaultCSSSelectorFilter(CSSVersion cssVersion) {
        this.cssVersion = cssVersion;
    }

    // Javadoc inherited.
    public Selector filter(Selector input) {
        filtered = false;
        input.accept(this);
        if (filtered) {
            return null;
        } else {
            return input;
        }
    }

    // Javadoc inherited
    public void visit(PseudoElementSelector selector) {
        String selectorType = selector.getPseudoElementType().getType();
        filtered = filtered | filterPseudoSelector(selectorType);
    }

    // Javadoc inherited
    public void visit(NthChildSelector selector) {
        visit((PseudoClassSelector) selector);
    }

    // Javadoc inherited
    public void visit(PseudoClassSelector selector) {
        String selectorType = selector.getPseudoClassType().getType();
        filtered = filtered | filterPseudoSelector(selectorType);
    }

    boolean filterPseudoSelector(String selectorType) {
        final boolean supported =
                cssVersion.supportsPseudoSelectorId(selectorType);
        if (!supported && logger.isDebugEnabled()) {
            logger.debug("Filtering pseudo selector id " +
                         selectorType + " using " + cssVersion);
        }
        return !supported;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/1	pduffin	VBM:2005083007 Committing fixes to default CSS selector

 01-Sep-05	9412/2	adrianj	VBM:2005083007 CSS renderer using new model

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 19-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
