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

package com.volantis.styling.expressions;


/**
 * The context within which styling functions are evaluated.
 *
 * @mock.generate
 */
public interface EvaluationContext {

    /**
     * Set a property on the evaluation context.
     *
     * @param key   The key to the property.
     * @param value The value of the property.
     */
    public void setProperty(Object key, Object value);

    /**
     * Get a property from the evaluation context.
     *
     * @param key The key to the property.
     * @return The value of the property, or null if it was not found.
     */
    public Object getProperty(Object key);

    int getCounterValue(String counterName);

    int[] getCounterValues(String counterName);

    /**
     * Get the value of the attribute.
     *
     * @param namespace The namespace of the attribute, null if the attribute
     *                  does not belong in a namespace, i.e. it belongs to the
     *                  element.
     * @param localName The local name of the attribute, may not be null.
     * @return The value of the attribute, or null if the attribute does not
     *         exist.
     */
    String getAttributeValue(String namespace, String localName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
