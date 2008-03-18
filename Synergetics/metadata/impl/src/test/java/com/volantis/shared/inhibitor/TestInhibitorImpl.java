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

package com.volantis.shared.inhibitor;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;

/**
 * Base implementation of {@link TestInhibitor}.
 */
abstract class TestInhibitorImpl
        implements TestInhibitor {

    /**
     * The name.
     */
    private String name;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected TestInhibitorImpl(TestInhibitor value) {
        this.name = value.getName();
    }

    /**
     * Protected constructor for sub classes.
     */
    protected TestInhibitorImpl() {
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableTestInhibitorImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableTestInhibitorImpl(this);
    }

    public String getName() {
        return name;
    }

    /**
     * Mutator added here for convenience. This must never be called directly
     * on an instance of this class as that could break immutability.
     */
    public void setName(String name) {
        this.name = name;
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
