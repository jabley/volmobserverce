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
package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.values.PropertyValues;

/**
 * Performs additional analysis specific to those properties that specify styles
 * for rounded corners (mcs-border-radius) of boxes.
 */
public class CornerCharacteristicAnalyser
        extends BasicShorthandAnalyzer {

    private final StyleValue[] corners;
    private int cornerCount;
    
    // indicate if all corners style property has the same value
    private boolean canUseShorthand;
    private final StyleValue anyValue;

    public CornerCharacteristicAnalyser(
            StyleShorthand shorthand,
            PropertyClearerChecker checker,
            StyleValue anyValue,
            ShorthandSet supportedShorthands) {

        super(shorthand, checker, supportedShorthands);
        this.anyValue = anyValue;

        this.corners = new StyleValue[group.length];
        
    }
    
    // java inherited
    // Javadoc inherited.
    public void analyze(
            TargetEntity target, PropertyValues inputValues,
            DeviceValues deviceValues) {

        cornerCount = 0;

        super.analyze(target, inputValues, deviceValues);

        // This shorthand can only be used if all the following conditions
        // hold, the first two of which are tested in the super class.
        // 1) it is supported.
        // 2) all the properties are set.
        // 3) all 4 of the properties has the same value

        // If these conditions are not met then individual properties have to
        // be used.
        canUseShorthand = super.canUseShorthand();
        if (canUseShorthand) {

            StyleValue topLeft = getCornerValue(inputValues, 0);
            StyleValue topRight = getCornerValue(inputValues, 1);
            StyleValue bottomRight = getCornerValue(inputValues, 2);
            StyleValue bottomLeft = getCornerValue(inputValues, 3);

            if(areEqual(topLeft, topRight, bottomRight, bottomLeft)) {
                cornerCount = 1;
                corners[0] = topLeft;
            } else {
                cornerCount = 4;
                corners[0] = topLeft;
                corners[1] = topRight;
                corners[2] = bottomRight;
                corners[3] = bottomLeft;
                canUseShorthand = false;
            }
        } else {            
            cornerCount = 0;
        }
    }

    /**
     * Indicates whether the shorthand can be used.
     *
     * @return True if the shorthand can be used false otherwise.
     */
    public boolean canUseShorthand() {
        return canUseShorthand;
    }
    /**
     * Return value of border radius property for corner indicated by index,  
     * 
     * @param values set of style properties
     * @param index index of corner, 0 - topleft, 1 - topright, 2 - bottomright, 3 - bottomleft
     */
    private StyleValue getCornerValue(StyleValues values, int index) {
        return values.getStyleValue(group[index]);
    }

    // java inherit
    protected StyleValue[] getShorthandValues() {
        StyleValue[] values = new StyleValue[cornerCount];
        System.arraycopy(corners, 0, values, 0, cornerCount);
        return values;
    }

    // java inherit
    protected int getShorthandValuesCost() {
        int cost = 0;
        int separatorCost = 0;
        for (int i = 0; i < cornerCount; i++) {
            StyleValue edge = corners[i];
            cost += separatorCost;
            cost += edge.getStandardCost();
            separatorCost = 1; // The ' '.
        }
        return cost;
    }    

    /**
     * Indicates if all corners property has the same value
     * 
     * @param topLeft StyleValue of mcs-border-top-left-radius property
     * @param topRight StyleValue of mcs-border-top-right-radius property
     * @param bottomLeft StyleValue of mcs-border-bottom-left-radius property
     * @param bottomRight StyleValue of mcs-border-bottom-right-radius property
     * @return true if style property have the same value  
     */ 
    private boolean areEqual(StyleValue topLeft, StyleValue topRight,
                             StyleValue bottomRight, StyleValue bottomLeft) {
        if(topLeft.equals(topRight))
            if(topRight.equals(bottomRight))
                if(bottomRight.equals(bottomLeft))
                    return true;                
        return false;
    }

    // Javadoc inherited.
    protected StyleValue getRequiredInitialValue(int index) {
        return anyValue;
    }
}
