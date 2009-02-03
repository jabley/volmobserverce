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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl;

/**
 * Extenders of the MetricGroupProxy class should implement this to perform the
 * action required. A binding is created for each method defined in each
 * interface. It is passed the MetricGroupProxy instance through which the call
 * came and can therefore access instance based information that is not
 * available when the binding is created
 */
public interface Binding {

    /**
     * Subclasses must implement this method to perform appropriate actions on
     * the provided target object.
     *
     * @param target the target of the binding (the MetricGroupProxy instance
     *               the method was called on).
     * @param args   the method arguments to use when performing operations on
     *               the target object
     * @return the result of performing the operation on the target object
     */
    public Object invoke(MetricGroupProxy target, Object[] args);

}
