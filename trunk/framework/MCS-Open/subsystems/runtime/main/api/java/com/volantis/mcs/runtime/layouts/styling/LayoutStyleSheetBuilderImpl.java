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

import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.synergetics.UndeclaredThrowableException;

/**
 * Builds a device theme for a device layout.
 */
public class LayoutStyleSheetBuilderImpl
        implements LayoutStyleSheetBuilder {

    /**
     * The object to use to create the style sheet.
     */
    private final StyleSheetFactory styleSheetFactory;

    private final FormatRuleBuilderSelector builderSelector;

    /**
     * Initialise.
     */
    public LayoutStyleSheetBuilderImpl() {
        this(StyleSheetFactory.getDefaultInstance(),
             new FormatRuleBuilderSelector());
    }

    /**
     * Initialise.
     *
     * @param styleSheetFactory The object to use to create the style sheet.
     * @param builderSelector The object to use to select the appropriate
     * object to build style properties for each format.
     */
    public LayoutStyleSheetBuilderImpl(
            StyleSheetFactory styleSheetFactory,
            FormatRuleBuilderSelector builderSelector) {

        this.styleSheetFactory = styleSheetFactory;
        this.builderSelector = builderSelector;
    }

    public StyleSheet build(Layout layout) {
        final StyleSheet styleSheet = styleSheetFactory.createStyleSheet();

        FormatVisitorAdapter visitor = new FormatVisitorAdapter() {

            public boolean visitFormat(Format format, Object object) {

                FormatRuleBuilder builder =
                        builderSelector.selectBuilder(format);
                builder.addRules(styleSheet,  format);

                return false;
            }
        };

        Format root = layout.getRootFormat();
        if (root != null) {
            try {
                root.visit(visitor, null);
            } catch (FormatVisitorException e) {
                throw new UndeclaredThrowableException(e);
            }
        }

        return styleSheet;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
