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

package com.volantis.mcs.unit.layouts.visitor;

import com.volantis.mcs.layouts.FormatVisitor;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

import java.lang.reflect.Method;

/**
 * An action that will visit the spatial iterator method on the format visitor.
 */
public class VisitFormatMethodAction
        implements MethodAction {

    /**
     * The reflected method.
     */
    protected final Method method;

    /**
     * Initialise.
     * @param formatClass The class whose specialised method in the visitor
     * is to be invoked.
     */
    public VisitFormatMethodAction(Class formatClass) {
        Class [] parameters = new Class[] {
            formatClass, Object.class
        };
        try {
            method = FormatVisitor.class.getMethod("visit", parameters);
        } catch (NoSuchMethodException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Invoke the reflected method. 
     */
    public Object perform(MethodActionEvent event)
            throws Throwable {

        FormatVisitor visitor = (FormatVisitor)
                event.getArgument(FormatVisitor.class);

        Object object = event.getArguments()[1];

        Object [] args = new Object[] {
            event.getSource(), object
        };
        Boolean result = (Boolean) method.invoke(visitor, args);
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
