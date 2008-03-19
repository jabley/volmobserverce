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
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.MCSMenuOrientationKeywords;

public class DissectingPaneRuleBuilder
        extends PaneRuleBuilder {

    private static final PropertyValue SHARD_MENU_ORIENTATION =
        ThemeFactory.getDefaultInstance().createPropertyValue(
            StylePropertyDetails.MCS_MENU_ORIENTATION,
                MCSMenuOrientationKeywords.HORIZONTAL);

    public DissectingPaneRuleBuilder(
            FormatAttributeToStyleValueConverter converter,
            StyleSheetFactory factory) {
        super(converter, factory);
    }

    protected void initialiseStyleProperties(
            MutableStyleProperties styleProperties, Format format) {
        super.initialiseStyleProperties(styleProperties, format);

        // The default menu orientation for shards is different to that for
        // normal menus so as the styles for the shard menu are obtained from
        // the styles for the dissecting pane it is set here.
        styleProperties.setPropertyValue(SHARD_MENU_ORIENTATION);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
