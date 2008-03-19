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

import com.volantis.shared.metadata.type.constraint.ConstraintFactory;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableUniqueMemberConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumValueConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumLengthConstraint;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Default Implementation of a {@link ConstraintFactory}.
 */
public class DefaultConstraintFactory extends ConstraintFactory {
    /**
     * Immutable number sub type constraint for {@link Number}.
     */
    private static final ImmutableNumberSubTypeConstraint NUMBER_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(Number.class);

    /**
     * Immutable number sub type constraint for {@link Byte}.
     */
    private static final ImmutableNumberSubTypeConstraint BYTE_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(Byte.class);

    /**
     * Immutable number sub type constraint for {@link Double}.
     */
    private static final ImmutableNumberSubTypeConstraint DOUBLE_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(Double.class);

    /**
     * Immutable number sub type constraint for {@link Float}.
     */
    private static final ImmutableNumberSubTypeConstraint FLOAT_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(Float.class);

    /**
     * Immutable number sub type constraint for {@link Integer}.
     */
    private static final ImmutableNumberSubTypeConstraint INTEGER_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(Integer.class);

    /**
     * Immutable number sub type constraint for {@link Long}.
     */
    private static final ImmutableNumberSubTypeConstraint LONG_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(Long.class);

    /**
     * Immutable number sub type constraint for {@link Short}.
     */
    private static final ImmutableNumberSubTypeConstraint SHORT_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(Short.class);

    /**
     * Immutable number sub type constraint for {@link BigDecimal}.
     */
    private static final ImmutableNumberSubTypeConstraint BIG_DECIMAL_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(BigDecimal.class);

    /**
     * Immutable number sub type constraint for {@link BigInteger}.
     */
    private static final ImmutableNumberSubTypeConstraint BIG_INTEGER_SUB_TYPE_CONSTRAINT =
            new ImmutableNumberSubTypeConstraintImpl(BigInteger.class);

    /**
     * A Map which stores all the valid different number sub type constraints,
     * keyed by their corresponding Class type.
     */
    private static Map numberSubTypeConstraintMap = new HashMap();

    // Initialize the number sub type constraint map.
    static {
        numberSubTypeConstraintMap.put(Number.class, NUMBER_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(Byte.class, BYTE_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(Double.class, DOUBLE_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(Float.class, FLOAT_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(Integer.class, INTEGER_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(Long.class, LONG_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(Short.class, SHORT_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(BigDecimal.class, BIG_DECIMAL_SUB_TYPE_CONSTRAINT);
        numberSubTypeConstraintMap.put(BigInteger.class, BIG_INTEGER_SUB_TYPE_CONSTRAINT);
    }

    // Javadoc inherited.
    public MutableEnumeratedConstraint createEnumeratedConstraint() {
        return new MutableEnumeratedConstraintImpl();
    }

    // Javadoc inherited.
    public MutableMemberTypeConstraint createMemberTypeConstraint() {
        return new MutableMemberTypeConstraintImpl();
    }

    // Javadoc inherited.
    public MutableMaximumValueConstraint createMaximumValueConstraint() {
        return new MutableMaximumValueConstraintImpl();
    }

    // Javadoc inherited.
    public MutableMinimumValueConstraint createMinimumValueConstraint() {
        return new MutableMinimumValueConstraintImpl();
    }

    // Javadoc inherited.
    public MutableMinimumLengthConstraint createMinimumLengthConstraint() {
        return new MutableMinimumLengthConstraintImpl();
    }

    // Javadoc inherited.
    public MutableMaximumLengthConstraint createMaximumLengthConstraint() {
        return new MutableMaximumLengthConstraintImpl();
    }

    // Javadoc inherited.
    public ImmutableNumberSubTypeConstraint getNumberSubTypeConstraint(
            Class numberSubType) {

        // try to get a valid constraint from the map
        ImmutableNumberSubTypeConstraint numberSubTypeConstraint =
                (ImmutableNumberSubTypeConstraint)
                numberSubTypeConstraintMap.get(numberSubType);

        // throw an exception if the provided key does not correspond to a
        // valid constraint
        if (numberSubTypeConstraint == null) {
            throw new IllegalArgumentException("Class is not a sub type of "
                    + Number.class + " : numberSubType");
        }

        // return the number sub type constraint
        return numberSubTypeConstraint;
    }

    /**
     * This exists only for persistence mechanisms. It should only be used
     * in such a context.
     *
     * @return an uninitialized ImmutableNumberSubTypeConstraint
     */
    public ImmutableNumberSubTypeConstraint 
          getUninitializedNumberSubtypeConstraint() {
              return new ImmutableNumberSubTypeConstraintImpl();
    }

    // Javadoc inherited.
    public ImmutableUniqueMemberConstraint getUniqueMemberConstraint() {
        return new ImmutableUniqueMemberConstraintImpl();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6670/1	adrianj	VBM:2005010506 Implementation of resource asset continued

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
