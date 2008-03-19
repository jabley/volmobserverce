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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.integration.optimizer;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

public class OptimizerTestHelper {

    private static final StylingFactory STYLING_FACTORY =
            StylingFactory.getDefaultInstance();

    private final CSSParser parser;

    public OptimizerTestHelper() {
        final CSSParserFactory parserFactory = CSSParserFactory.getDefaultInstance();
        parser = parserFactory.createStrictParser();
    }

    public PropertyValues parseDeclarations(String css) {
        final MutablePropertyValues values = STYLING_FACTORY.createPropertyValues();
        final MutableStyleProperties properties = parser.parseDeclarations(css);
        properties.iteratePropertyValues(new PropertyValueIteratee() {
            public IterationAction next(PropertyValue propertyValue) {
                values.setComputedValue(propertyValue.getProperty(),
                        propertyValue.getValue());
                return IterationAction.CONTINUE;
            }
        });

        return values;
    }
}
