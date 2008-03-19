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

package com.volantis.shared.metadata.impl;

import com.volantis.shared.inhibitor.TestInhibitorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Test case for {@link ImmutableGeneratingTypedMap}.
 */
public class ImmutableGeneratingTypedMapTestCase
        extends TypedMapTestCase {

    private TestInhibitorFactory factory;

    /**
     * Constructor
     */
    public ImmutableGeneratingTypedMapTestCase() {
        factory = TestInhibitorFactory.getDefaultInstance();
    }

    /**
     * Override to create instance of {@link ImmutableGeneratingTypedMap}.
     */
    protected TypedMap createTypedMap(Map delegate,
                                      Class allowableKeyClass,
                                      boolean allowNullKey,
                                      Class allowableValueClass,
                                      boolean allowNullValue) {
        return new ImmutableGeneratingTypedMap(
                delegate,
                allowableValueClass, allowNullValue);
    }

    public void testNullInhibitorValuesAccepted() {
        Object value = factory.createTestInhibitor();
        String key = "key";
        // create the map
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           key.getClass(), false,
                                           value.getClass(), true);
        typedMap.put(key, null);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 ===========================================================================
*/
