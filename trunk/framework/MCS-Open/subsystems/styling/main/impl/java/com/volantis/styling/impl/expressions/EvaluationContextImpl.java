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

package com.volantis.styling.impl.expressions;

import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.impl.counter.CounterEngine;
import com.volantis.styling.engine.Attributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link EvaluationContext}.
 */
public class EvaluationContextImpl
        implements EvaluationContext {

    private final CounterEngine counterEngine;

    private final Map properties;

    private Attributes attributes;

    public EvaluationContextImpl(CounterEngine counterEngine) {
        this.counterEngine = counterEngine;
        this.properties = new HashMap();
    }


    // Javadoc inherited.
    public void setProperty(Object key, Object value) {
        properties.put(key, value);
    }

    // Javadoc inherited.
    public Object getProperty(Object key) {
        return properties.get(key);
    }

    // Javadoc inherited.
    public int getCounterValue(String counterName) {
        return counterEngine.getCounter(counterName, true).intValue();
    }

    // Javadoc inherited.
    public int[] getCounterValues(String counterName) {
        return counterEngine.getCounterValues(counterName, true);
    }

    // Javadoc inherited.
    public String getAttributeValue(String namespace, String localName) {
        return attributes.getAttributeValue(namespace, localName);
    }

    /**
     * Set the attributes for use by any of the compiled expressions.
     *
     * @param attributes The attributes.
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
