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
package com.volantis.mcs.dom2theme.integration.optimizer;

import com.volantis.mcs.dom2theme.impl.optimizer.EdgeCharacteristicAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.OptimizerHelper;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyStatus;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * last change:  $Date$
 * by:           $Author$
 * revision:     $Revision$
 */
public class EdgeCharacteristicAnalyserTestCase
        extends OptimizerTestCaseAbstract {

    /**
     * Test in wchich ANY required values are rendered properly 
     */
    public void testRequiredAnyValues() {
        // Set expectations
        shorthandSetMock.expects.contains(StyleShorthands.BORDER)
            .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_COLOR)
            .returns(true).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_BOTTOM)
            .returns(false).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_LEFT)
            .returns(false).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_RIGHT)
            .returns(false).any();
        shorthandSetMock.expects.contains(StyleShorthands.BORDER_TOP)
            .returns(false).any();
        
        setBoxProperties(inputValues,
                StylePropertyDetails.BORDER_BOTTOM_COLOR,
                OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_LEFT_COLOR,
                OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_RIGHT_COLOR,
                OptimizerHelper.ANY, PropertyStatus.REQUIRED,
                StylePropertyDetails.BORDER_TOP_COLOR,
                OptimizerHelper.ANY, PropertyStatus.REQUIRED);
        final MutableStylePropertySet propertySet =
                new MutableStylePropertySetImpl(); // number of properties
        
        ShorthandAnalyzer analyzer;
        // check for null default value
        try {
             analyzer = new EdgeCharacteristicAnalyzer(
                    StyleShorthands.BORDER_COLOR, checkerMock, null, shorthandSetMock);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        final StyleValue defaultStyleValue =
            StyleValueFactory.getDefaultInstance().getColorByRGB(null, 100);
        analyzer = new EdgeCharacteristicAnalyzer(
                StyleShorthands.BORDER_COLOR, checkerMock, defaultStyleValue,
                shorthandSetMock);
        analyzer.analyze(TargetEntity.ELEMENT, inputValues, deviceValuesMock);
        
        // verify test 
        // 2 is because of ':' and ';' in getShorthandCost()
        assertEquals(StyleShorthands.BORDER_COLOR.getName().length() +
                defaultStyleValue.getStandardCost() + 2,
                analyzer.getShorthandCost(propertySet, propertySet));
    }
    
    private void setBoxProperties(MutablePropertyValues inputValues,
            final StyleProperty bottomProperty, final StyleValue bottomValue,
            final PropertyStatus bottomStatus,
            final StyleProperty leftProperty, final StyleValue leftValue,
            final PropertyStatus leftStatus,
            final StyleProperty rightProperty, final StyleValue rightValue,
            final PropertyStatus rightStatus,
            final StyleProperty topProperty, final StyleValue topValue,
            final PropertyStatus topStatus) {

        setPropertyValue(inputValues, bottomProperty, bottomValue, bottomStatus);
        setPropertyValue(inputValues, leftProperty, leftValue, leftStatus);
        setPropertyValue(inputValues, rightProperty, rightValue, rightStatus);
        setPropertyValue(inputValues, topProperty, topValue, topStatus);
    }

}
