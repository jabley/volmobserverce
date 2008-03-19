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

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.Rule;

public abstract class AbstractSimpleFormatRuleBuilder
    extends AbstractFormatRuleBuilder {

    protected AbstractSimpleFormatRuleBuilder(
            FormatAttributeToStyleValueConverter converter,
            StyleSheetFactory factory) {
        super(converter, factory);
    }

    // Javadoc inherited.
    public void addRules(StyleSheet styleSheet, Format format) {
        Rule rule = createRuleWithoutProperties(format);

        StyleProperties styleProperties = createStyleProperties(format);
        rule.setProperties(styleProperties);

        styleSheet.addRule(rule);
    }

    public StyleProperties createStyleProperties(Format format) {

        MutableStyleProperties styleProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        initialiseStyleProperties(styleProperties, format);

        return styleProperties;
    }

    protected abstract void initialiseStyleProperties(
            MutableStyleProperties styleProperties, Format format);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
