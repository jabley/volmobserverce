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
package com.volantis.mcs.css.renderer;

import java.util.ArrayList;
import java.util.List;

import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;

/**
 * An abstract test case for instances of {@link GenericCounterRenderer}.
 */
public abstract class GenericCounterRendererTestAbstract
        extends GenericRendererTestAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Test that we can render multiple counters without associated integers.
     *
     * @throws Exception
     */
    public void testCountersWithoutInt() throws Exception {
        PropertyRenderer renderer = createRenderer();

        List list = new ArrayList();

        addCounter(1, false, list);
        addCounter(2, false, list);
        addCounter(3, false, list);

        StyleList styleList = STYLE_VALUE_FACTORY.getList(list);
        checkRenderValue(renderer, styleList,
                "counter1 counter2 counter3");
    }

    /**
     * Test that we can render multiple counters with associated integers.
     *
     * @throws Exception
     */
    public void testCountersWithInt() throws Exception {
        PropertyRenderer renderer = createRenderer();

        List list = new ArrayList();

        addCounter(1, true, list);
        addCounter(2, true, list);
        addCounter(3, true, list);

        StyleList styleList = STYLE_VALUE_FACTORY.getList(list);

        checkRenderValue(renderer, styleList,
                "counter1 1 counter2 2 counter3 3");
    }

    /**
     * Add a StylePair containing a counter to the list provided, using id to
     * identify the counter, adding integer values as appropriate.
     *
     * @param id identifies the counter to add
     * @param addInt if true, add integer values for each counter.
     * @param list the list of style values to add to.
     */
    private void addCounter(int id, boolean addInt, List list) {
        StyleValue first =
            STYLE_VALUE_FACTORY.getIdentifier(null, "counter" + id);
        StyleValue second;
        if (addInt) {
            second = STYLE_VALUE_FACTORY.getInteger(null, id);
        } else {
            second = null;
        }

        StylePair pair = STYLE_VALUE_FACTORY.getPair(first, second);
        list.add(pair);
    }

    /**
     * Create the renderer to test.
     *
     * @return the created renderer.
     */
    abstract GenericCounterRenderer createRenderer();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10581/3	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, also fixed string rendering as well

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 29-Jul-05	9114/2	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 ===========================================================================
*/
