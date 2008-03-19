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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.layouts.Format;

/**
 *
 */

public class SpatialRuleBuilder extends AbstractFormatRuleBuilder {

    public SpatialRuleBuilder(FormatAttributeToStyleValueConverter converter, StyleSheetFactory factory) {
        super(converter, factory);
    }

    public void addRules(StyleSheet styleSheet, Format format) {

        Rule rule = createRuleWithoutProperties(format);

        MutableStyleProperties styleProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        // At the moment a spatial is equivalent to a CSS table but there is
        // really no reason why it could not also be a block.
        initialiseCommonProperties(styleProperties, format,
                DisplayKeywords.TABLE);

        rule.setProperties(styleProperties);
        styleSheet.addRule(rule);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10443/2	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 ===========================================================================
*/
