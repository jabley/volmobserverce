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

package com.volantis.styling.properties;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.styling.values.InitialValueSource;

import java.util.Set;

/**
 * Encapsulates information about a style properties characteristics.
 *
 * @mock.generate
 */
public interface StyleProperty {

    /**
     * Get the name of this property.
     *
     * <p>This must uniquely identify the property amongst all properties
     * within an individual style sheet. This is because it will be used as the
     * key when searching for information relating to the property, e.g. values
     * associated with a property.</p>
     *
     * @return The name of the property.
     */
    public String getName();

    /**
     * Get the index of this property.
     *
     * <p>This must uniquely identify the property amongst all properties
     * within an individual style sheet. This is because it will be used as the
     * key when searching for information relating to the property, e.g. values
     * associated with a property.</p>
     *
     * @return The index of the property.
     */
    public int getIndex();

    /**
     * Get the standard information about this property.
     *
     * @return The standard information for this property.
     */
    PropertyDetails getStandardDetails();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 30-Oct-05	9992/2	emma	VBM:2005101811 Adding new style property validation

 27-Oct-05	9965/1	ianw	VBM:2005101811 interim commit

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/4	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
