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
import com.volantis.shared.metadata.type.constraint.NumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * Implementation of {@link ImmutableNumberSubTypeConstraint}.
 */
public final class ImmutableNumberSubTypeConstraintImpl
        extends NumberSubTypeConstraintImpl
        implements ImmutableNumberSubTypeConstraint, Serializable {

    /**
     * Map from sub type name to sub type class. Used to deserialize sub types
     * with JiBX.
     */
    private static final Map SUB_TYPE_NAME_TO_CLASS;

    /**
     * Map from sub type class to sub type name. Used to serialize sub types
     * with JiBX.
     */
    private static final Map CLASS_TO_SUB_TYPE_NAME;

    static {
        SUB_TYPE_NAME_TO_CLASS = new HashMap();
        SUB_TYPE_NAME_TO_CLASS.put("byte", Byte.class);
        SUB_TYPE_NAME_TO_CLASS.put("short", Short.class);
        SUB_TYPE_NAME_TO_CLASS.put("int", Integer.class);
        SUB_TYPE_NAME_TO_CLASS.put("long", Long.class);
        SUB_TYPE_NAME_TO_CLASS.put("integer", BigInteger.class);
        SUB_TYPE_NAME_TO_CLASS.put("decimal", BigDecimal.class);
        SUB_TYPE_NAME_TO_CLASS.put("float", Float.class);
        SUB_TYPE_NAME_TO_CLASS.put("double", Double.class);
        CLASS_TO_SUB_TYPE_NAME = new HashMap();
        CLASS_TO_SUB_TYPE_NAME.put(Byte.class, "byte");
        CLASS_TO_SUB_TYPE_NAME.put(Short.class, "short");
        CLASS_TO_SUB_TYPE_NAME.put(Integer.class, "int");
        CLASS_TO_SUB_TYPE_NAME.put(Long.class, "long");
        CLASS_TO_SUB_TYPE_NAME.put(BigInteger.class, "integer");
        CLASS_TO_SUB_TYPE_NAME.put(BigDecimal.class, "decimal");
        CLASS_TO_SUB_TYPE_NAME.put(Float.class, "float");
        CLASS_TO_SUB_TYPE_NAME.put(Double.class, "double");
    }

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 3516434525301396164L;

    /**
     * Copy constructor.
     *
     * @param numberSubTypeConstraint The object to copy.
     */
    public ImmutableNumberSubTypeConstraintImpl(
            NumberSubTypeConstraint numberSubTypeConstraint) {
        super(numberSubTypeConstraint);
    }

    /**
     * Constructor which takes a Number.
     *
     * @param numberSubType The <code>Number</code> to set as the sub type.
     */
    public ImmutableNumberSubTypeConstraintImpl(Class numberSubType) {
        super(numberSubType);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableNumberSubTypeConstraintImpl() {
        super();
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutableInhibitor createImmutable() {
        return this;
    }

    /**
     * Serialize sub type with JiBX.
     */
    public static String serializer(final Class subType) {

        return (String) CLASS_TO_SUB_TYPE_NAME.get(subType);
    }

    /**
     * Deserialize sub type with JiBX.
     */
    public static Class deserializer(final String constraint) {

        return (Class) SUB_TYPE_NAME_TO_CLASS.get(constraint);
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_NUMBER_SUBTYPE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
