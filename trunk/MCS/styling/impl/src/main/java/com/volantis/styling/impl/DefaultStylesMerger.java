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
package com.volantis.styling.impl;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.Iterator;

/**
 * A simple implementation of a property value merger which merges two Styles
 * together, with one set of property values always 'winning' over the other
 * if there are conflicts.
 */
public class DefaultStylesMerger implements StylesMerger {

    /**
     * The styling factory to use to create Styles.
     */
    protected StylingFactory factory;

    /**
     * Initialize a new instance using the given parameters.
     */
    public DefaultStylesMerger(StylingFactory stylingFactory) {
        factory = stylingFactory;
    }

    /**
     * Merge the two Styles together, where the property values from the winner
     * 'win' if specified in both sets.
     *
     * @param winner    Styles to merge, may be null.
     * @param luser     Styles to merge, may be null.
     * @return merged Styles. Will be null if both parameters are null.
     */
    public Styles merge(Styles winner, Styles luser) {
        Styles result = winner;

        if (winner != null && luser != null) {

            result = factory.createStyles(null);
            mergePropertyValues(result.getPropertyValues(),
                    winner.getPropertyValues(), luser.getPropertyValues());

        } else if (luser != null) {
            result = luser;
        }

//        if (result.getPropertyValues().getComputedValue(StylePropertyDetails.DISPLAY) == DisplayKeywords.BLOCK) {
//            new Exception().printStackTrace(System.out);
//        }

        return result;
    }

    /**
     * Merges two style property values together, creating a new merged style
     * property values where the property values from winner "win" if
     * specified in both sets.
     *
     * @param winner    property values to merge, may be null.
     * @param luser     property values to merge, may be null.
     * @param result    resulting property values.
     */
    public void mergePropertyValues(MutablePropertyValues result,
                                    MutablePropertyValues winner,
                                    MutablePropertyValues luser) {

        for (Iterator i = result.stylePropertyIterator(); i.hasNext();) {
            StyleProperty property = (StyleProperty) i.next();
            StyleValue initial = property.getStandardDetails().getInitialValue();

            boolean specified = false;
            StyleValue mergedValue = winner.getComputedValue(property);
            boolean ignored = winner.shouldExcludeFromCSS(property);

            StyleValue winnerComputed = winner.getComputedValue(property);
            StyleValue winnerSpecified = winner.getSpecifiedValue(property);
            StyleValue luserComputed = luser.getComputedValue(property);
            StyleValue luserSpecified = luser.getSpecifiedValue(property);

            if (winnerComputed != null && initial != null) {
                if (initial.equals(winnerComputed)
                        && winnerSpecified == null) {
                    winnerComputed = null;
                }
            }
            if (winnerComputed == null && luserComputed != null) {
                mergedValue = luserComputed;
                ignored = luser.shouldExcludeFromCSS(property);
            }
            if (winnerSpecified == null && luserSpecified != null) {
                mergedValue = luserComputed;
                specified = true;
                ignored = luser.shouldExcludeFromCSS(property);
            } else if (winnerSpecified != null) {
                specified = true;
            }
            if (mergedValue != null) {
                result.setComputedValue(property, mergedValue);
                if (specified) {
                    result.setSpecifiedValue(property,mergedValue);
                }
                if (ignored) {
                    result.excludeFromCSS(property);
                }
            }
//            if (property == StylePropertyDetails.DISPLAY && winnerComputed == DisplayKeywords.BLOCK) {
//                new Exception().printStackTrace(System.out);
//            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10647/1	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 06-Dec-05	10628/1	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/8	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/4	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (3)

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 28-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 ===========================================================================
*/
