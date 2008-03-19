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

import com.volantis.shared.inhibitor.ImmutableInhibitor;

/**
 * Implementation of {@link ImmutableStylePropertySet}.
 */
final class ImmutableStylePropertySetImpl
        extends StylePropertySetImpl
        implements ImmutableStylePropertySet {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public ImmutableStylePropertySetImpl(StylePropertySet value) {
        super(value);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableStylePropertySetImpl() {
    }

    /**
     * Override to return this object rather than create a new one.
     * <p/>
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutableStylePropertySet createImmutableStylePropertySet() {
        return this;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
