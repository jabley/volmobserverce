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
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValueFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Selects the appropriate builder to use for the specified format.
 */
public class FormatRuleBuilderSelector {

    private final Map typeToBuilder;

    public FormatRuleBuilderSelector() {
        this(StyleValueFactory.getDefaultInstance());
    }

    public FormatRuleBuilderSelector(StyleValueFactory styleValueFactory) {
        this(new FormatAttributeToStyleValueConverterImpl(styleValueFactory),
             StyleSheetFactory.getDefaultInstance());
    }
    
    public FormatRuleBuilderSelector(
            FormatAttributeToStyleValueConverter converter,
            StyleSheetFactory factory) {

        FormatRuleBuilder dissectingPane =
                new DissectingPaneRuleBuilder(converter, factory);
        FormatRuleBuilder paneBuilder = new PaneRuleBuilder(converter, factory);
        FormatRuleBuilder noRuleBuilder = new NoRuleBuilder();
        FormatRuleBuilder gridBuilder = new GridRuleBuilder(converter, factory);
        FormatRuleBuilder spatialBuilder = new SpatialRuleBuilder(converter, factory);

        typeToBuilder = new HashMap();
        typeToBuilder.put(FormatType.COLUMN_ITERATOR_PANE, paneBuilder);
        typeToBuilder.put(FormatType.ROW_ITERATOR_PANE, paneBuilder);
        typeToBuilder.put(FormatType.PANE, paneBuilder);
        typeToBuilder.put(FormatType.DISSECTING_PANE, dissectingPane);
        typeToBuilder.put(FormatType.SPATIAL_FORMAT_ITERATOR, spatialBuilder);
        typeToBuilder.put(FormatType.TEMPORAL_FORMAT_ITERATOR, noRuleBuilder);
        typeToBuilder.put(FormatType.GRID, gridBuilder);
        typeToBuilder.put(FormatType.FORM, noRuleBuilder);
        typeToBuilder.put(FormatType.FORM_FRAGMENT, noRuleBuilder);
        typeToBuilder.put(FormatType.FRAGMENT, noRuleBuilder);
        typeToBuilder.put(FormatType.REGION, noRuleBuilder);
        typeToBuilder.put(FormatType.REPLICA, noRuleBuilder);
        typeToBuilder.put(FormatType.SEGMENT_GRID, noRuleBuilder);
        typeToBuilder.put(FormatType.SEGMENT, noRuleBuilder);
    }

    public FormatRuleBuilder selectBuilder(Format format) {
        FormatType type = format.getFormatType();
        FormatRuleBuilder builder = (FormatRuleBuilder) typeToBuilder.get(type);
        if (builder == null) {
            throw new IllegalStateException(
                    "Unknown format type " + type.getTypeName());
        }

        return builder;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/3	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
