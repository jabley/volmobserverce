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

package com.volantis.styling.impl.values;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class MutablePropertyValuesTestCase
        extends TestCaseAbstract {

    private StylePropertyDefinitions definitions;
    private StyleValue styleValue1;
    private StyleValue styleValue2;
    private StyleProperty property1;
    private StyleProperty property2;
    private MutablePropertyValues mutable;
    private ImmutablePropertyValues immutable;

    protected void setUp() throws Exception {
        super.setUp();

        definitions = StylePropertyDetails.getDefinitions();

        styleValue1 = StyleKeywords.ABSOLUTE;
        styleValue2 = StyleColorNames.RED;
        property1 = StylePropertyDetails.COLOR;
        property2 = StylePropertyDetails.TEXT_ALIGN;
    }

    /**
     * Test that an immutable object created from a mutable one is not affected
     * when the mutable one is modified by setting new values.
     */
    public void testCopyOnWriteSet() {

        prepareForCopyOnWrite();

        // Modify the mutable one and make sure that the immutable one has
        // not changed.
        mutable.setComputedValue(property1, null);
        mutable.setSpecifiedValue(property2, null);

        checkImmutableForCopyOnWrite(immutable);
    }

    private void prepareForCopyOnWrite() {
        mutable = new MutablePropertyValuesImpl(definitions);

        prepareMutableForCopyOnWrite(mutable);

        immutable = mutable.createImmutablePropertyValues();

        // Make sure that the immutable has the correct values in.
        checkImmutableForCopyOnWrite(immutable);
    }

    /**
     * Test that an immutable object created from a mutable one is not affected
     * when the mutable one is cleared.
     */
    public void testCopyOnWriteClear() {

        prepareForCopyOnWrite();

        // Modify the mutable one and make sure that the immutable one has
        // not changed.
        mutable.clear();

        checkImmutableForCopyOnWrite(immutable);
    }

    private void checkImmutableForCopyOnWrite(
            ImmutablePropertyValues immutable) {
        // Immutable should have the same values as mutable immediately
        // after it has been created.
        assertSame("Property 1", styleValue1,
                immutable.getComputedValue(property1));
        assertSame("Property 2", styleValue2,
                immutable.getSpecifiedValue(property2));
    }

    private void prepareMutableForCopyOnWrite(
            MutablePropertyValues mutable) {

        mutable.setComputedValue(property1, styleValue1);
        mutable.setSpecifiedValue(property2, styleValue2);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/7	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/5	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 ===========================================================================
*/
