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

import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.ImmutablePropertyValues;

/**
 * Implementation of {@link com.volantis.styling.values.ImmutablePropertyValues}.
 */
final class ImmutablePropertyValuesImpl
        extends PropertyValuesImpl
        implements ImmutablePropertyValues {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public ImmutablePropertyValuesImpl(MutablePropertyValuesImpl value) {
        super(value, false);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutablePropertyValuesImpl(StylePropertyDefinitions definitions) {
        super(definitions);
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutablePropertyValues createImmutablePropertyValues() {
        return this;
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutablePropertyValues createImmutableExtendedPropertyValues() {
        return this;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
