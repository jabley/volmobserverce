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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.value;

import com.volantis.shared.metadata.value.immutable.ImmutableUnitValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Enumerations of all the possible {@link UnitValue} objects.
 *
 * <p>The units can be split into the following equivalence groups. An
 * equivalence group is the largest set of units such that for all units in
 * the set quantities in one unit within the set can be converted to a quantity
 * in any other unit within the set.</p>
 *
 * <p>Conversion within an equivalence group is typically performed by
 * picking one unit as the <em>conversion unit</em> and converting all other
 * units to and from that unit. e.g. to convert a quantity in
 * <code>inches</code> to one in <code>millimeters</code> the magnitude of the
 * source quantity is multiple by <code>2.54</code> to get the magnitude in
 * <code>centimeters</code> which is then divided by <code>10</code> to get the
 * magnitude in <code>millimeters</code>.</p>
 *
 * <dl>
 *     <dt>{@link #PIXELS}</dt>
 *     <dd>This is a relative length that cannot be converted to any other
 * length.</dd>
 *
 *     <dt><b>Absolute Lengths</b></dt>
 *     <dd>This includes any length that can be converted to and from a
 * <code>centimeter</code>.<ul>
 * <li>{@link #CENTIMETERS}</li>
 * <li>{@link #MILLIMETERS}</li>
 * <li>{@link #INCHES}</li>
 * </ul></dd>
 * </dl>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class QuantityUnits {


    /**
     * Private constructor to prevent being created.
     */
    private QuantityUnits() {
        // The fields within this should be initialised using reflection.
        // e.g. find the implementation class, and then retrieve the values of
        // the appropriate named fields that correspond to the ones in this
        // class.
    }

    /**
     * Relative length that cannot be converted to any other length.
     */
    public static final ImmutableUnitValue PIXELS;

    /**
     * Absolute length.
     */
    public static final ImmutableUnitValue CENTIMETERS;

    /**
     * Absolute length.
     *
     * <p>1 <code>millimeter</code> is 1/10th of a <code>centimeter</code>.</p>
     */
    public static final ImmutableUnitValue MILLIMETERS;

    /**
     * Absolute length.
     *
     * <p>1 <code>inch</code> is 2.54 <code>centimeters</code>.</p>
     */
    public static final ImmutableUnitValue INCHES;

    static {

        Class implementationClass = null;
        try {
            implementationClass = Class.forName("com.volantis.shared.metadata.impl.value.ImmutableUnitValueImpl");
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        }

        Class[] parameters = new Class[]{String.class};
        try {
            Constructor constructor = implementationClass.getConstructor(parameters);

            Object[] args = new Object[]{"pixels"};
            PIXELS = (ImmutableUnitValue) constructor.newInstance(args);

            args = new Object[]{"millimeters"};
            MILLIMETERS = (ImmutableUnitValue) constructor.newInstance(args);

            args = new Object[]{"inches"};
            INCHES = (ImmutableUnitValue) constructor.newInstance(args);

            args = new Object[]{"centimeters"};
            CENTIMETERS = (ImmutableUnitValue) constructor.newInstance(args);

        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalArgumentException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InvocationTargetException e) {
            throw new UndeclaredThrowableException(e);
        } catch (NoSuchMethodException e) {
            throw new UndeclaredThrowableException(e);
        } catch (SecurityException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 14-Jan-05	6560/5	tom	VBM:2004122401 Completed Metadata API Implementation

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
