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

package com.volantis.mcs.layouts.common;

import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.properties.StyleProperty;

public class Pane
        extends Format {

    private static final StyleProperty[] SUPPORTED_PROPERTIES = new StyleProperty[]{
        StylePropertyDetails.BACKGROUND_COLOR,
        StylePropertyDetails.BACKGROUND_IMAGE,
        StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,
        StylePropertyDetails.BORDER_BOTTOM_WIDTH,
        StylePropertyDetails.BORDER_LEFT_WIDTH,
        StylePropertyDetails.BORDER_RIGHT_WIDTH,
        StylePropertyDetails.BORDER_TOP_WIDTH,
        StylePropertyDetails.HEIGHT,
        StylePropertyDetails.WIDTH,
        StylePropertyDetails.PADDING_BOTTOM,
        StylePropertyDetails.PADDING_LEFT,
        StylePropertyDetails.PADDING_RIGHT,
        StylePropertyDetails.PADDING_TOP,
        StylePropertyDetails.TEXT_ALIGN,
        StylePropertyDetails.VERTICAL_ALIGN,
        //        StylePropertyDetails.MCS_OPTIMIZATION_MODE
    };

    private String styleClass;

    private StyleProperties properties;

    public Pane() {
        super(true);
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public StyleProperties getProperties() {
        return properties;
    }

    public void setProperties(StyleProperties properties) {
        this.properties = properties;
    }
}
