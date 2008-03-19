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
 * (c) Volantis Systems Ltd 2006. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.integration.optimizer;


import com.volantis.mcs.dom2theme.impl.optimizer.HeterogeneousShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.OptimizerHelper;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyStatus;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.BackgroundAttachmentKeywords;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;


/**
 *  Test the Background Optimizer
 */

public class HeterogeneousShorthandOptimizerTestCase
        extends OptimizerTestCaseAbstract {

    /**
     * Test that ANY required values are correctly removed.
     */
    public void testRequiredAnyValues() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================
        shorthandSetMock.expects.contains(StyleShorthands.BACKGROUND).
                returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ShorthandOptimizer optimizer = new HeterogeneousShorthandOptimizer(
                StyleShorthands.BACKGROUND, checkerMock,
                shorthandSetMock);

        setPropertyValue(inputValues,
                StylePropertyDetails.BACKGROUND_ATTACHMENT,
                OptimizerHelper.ANY,
                PropertyStatus.REQUIRED);

        setPropertyValue(inputValues,
                StylePropertyDetails.BACKGROUND_IMAGE,
                BackgroundImageKeywords.NONE,
                PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("background-attachment:scroll;background-image:none",
                outputValues.getStandardCSS());
    }

    /**
     * Ensure that if a shorthand could not be used but a property matched
     * its initial value that the property was cleared.
     */
    public void testPropertyClearedIfInitialAndNoShorthand() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================
        shorthandSetMock.expects.contains(StyleShorthands.BACKGROUND).
                returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ShorthandOptimizer optimizer = new HeterogeneousShorthandOptimizer(
                StyleShorthands.BACKGROUND, checkerMock,
                shorthandSetMock);

        setPropertyValue(inputValues,
                StylePropertyDetails.BACKGROUND_ATTACHMENT,
                BackgroundAttachmentKeywords.FIXED,
                PropertyStatus.CLEARABLE);

        setPropertyValue(inputValues,
                StylePropertyDetails.BACKGROUND_COLOR,
                StyleColorNames.RED,
                PropertyStatus.REQUIRED);

        MutableStyleProperties outputValues =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        optimizer.optimize(TargetEntity.ELEMENT, inputValues, outputValues,
                deviceValuesMock);

        assertEquals("background-color:red",
                outputValues.getStandardCSS());
    }
}
