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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.TimeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for {@link MCSEffectDurationParser}.
 */
public class MCSEffectDurationParserTestCase
    extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Test a simple mcs-effect-duration value.
     */
    public void testSimpleEffectDuraion() throws Exception {

        List list = new ArrayList();
        list.add(STYLE_VALUE_FACTORY.getTime(null, 1.0, TimeUnit.S));
        list.add(STYLE_VALUE_FACTORY.getTime(null, 2.0, TimeUnit.S));
        list.add(STYLE_VALUE_FACTORY.getTime(null,3.0, TimeUnit.S));
        StyleValue expectedEffectDuration = STYLE_VALUE_FACTORY.getList(list);
        expectSetProperty(StylePropertyDetails.MCS_EFFECT_DURATION, expectedEffectDuration);
        
        parseDeclarations("mcs-effect-duration: 1.0s,2.0s,3.0s");
    }

}
