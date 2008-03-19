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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.PropertyValueArray;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.styling.properties.StyleProperty;

/**
 */
public class DefaultThemeFactory extends ThemeFactory {

    public Rule createRule() {
        return new RuleImpl();
    }

    public Class getRuleClass() {
        return RuleImpl.class;
    }

    public MutableStyleProperties createMutableStyleProperties() {
        return new MutableStylePropertiesImpl();
    }

    public MutableStyleProperties createMutableStyleProperties(
            final StyleProperties properties) {
        return new MutableStylePropertiesImpl(properties);
    }

    public MutableStyleProperties createMutableStyleProperties(
            final PropertyValueArray array) {
        return new MutableStylePropertiesImpl(array);
    }

    public PropertyValue createPropertyValue(final StyleProperty property,
                                             final StyleValue value) {
        return new PropertyValueImpl(property, value);
    }

    public PropertyValue createPropertyValue(final StyleProperty property,
                                             final StyleValue value,
                                             final Priority priority) {
        return new PropertyValueImpl(property, value, priority);
    }
}
