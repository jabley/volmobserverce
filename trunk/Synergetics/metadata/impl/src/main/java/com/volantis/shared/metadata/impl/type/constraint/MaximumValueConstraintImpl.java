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

package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.constraint.MaximumValueConstraint;

/**
 * Implementation of {@link MaximumValueConstraint}.
 */
abstract class MaximumValueConstraintImpl extends
        RangeConstraintImpl
        implements MaximumValueConstraint {

    /**
     * Copy constructor.
     *
     * @param constraint The object to copy.
     */
    protected MaximumValueConstraintImpl(MaximumValueConstraint constraint) {
        super(constraint);
    }

    /**
     * Protected constructor for sub classes.
     */
    protected MaximumValueConstraintImpl() {
        super();
    }

    public ImmutableInhibitor createImmutable() {
        return new ImmutableMaximumValueConstraintImpl(this);
    }

    public MutableInhibitor createMutable() {
        return new MutableMaximumValueConstraintImpl(this);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof MaximumValueConstraint)
                ? equalsRangeConstraint((MaximumValueConstraint) other)
                : false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
