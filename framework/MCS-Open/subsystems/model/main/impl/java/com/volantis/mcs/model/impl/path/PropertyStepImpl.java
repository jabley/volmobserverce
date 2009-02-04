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

package com.volantis.mcs.model.impl.path;

import com.volantis.mcs.model.path.PropertyStep;


/**
 * Moves from the bean to the proxy associated with a property of that bean.
 */
public class PropertyStepImpl
        implements PropertyStep, InternalStep {

    private final String property;

    public PropertyStepImpl(String property) {
        this.property = property;
    }

    public String getAsString() {
        return property;
    }

    public String getProperty() {
        return property;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/3	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Added path support

 ===========================================================================
*/
