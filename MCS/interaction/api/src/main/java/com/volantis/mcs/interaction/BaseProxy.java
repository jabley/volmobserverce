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

package com.volantis.mcs.interaction;

import com.volantis.mcs.model.descriptor.BaseClassDescriptor;

/**
 * A proxy for an abstract class within the model.
 *
 * <p>A base proxy is used when the underlying model can reference (through an
 * abstract class) an instance of a number of different concrete classes. Each
 * different concrete class could require a different proxy to represent it and
 * over the lifetime of the model the instance (and the class of that instance)
 * may change a number of times. When this happens new proxies for these objects
 * would need to be created. As the user interface typically has
 * references to the proxies it would need to listen for changes in the
 * containing proxy and update itself accordingly. This could be very awkward
 * to do and so would likely not be done correctly and would very easily lead
 * to problems and instability within the user interface. The base proxy aims
 * to simplify the problem.</p>
 *
 * <p>Once it has been created from a containing class a base proxy never
 * changes (unless parent proxy is changed by the user interface). It also
 * will never create an underlying model object as it does not know what type
 * to create. However, when the underlying model object is set then it will
 * automatically create the necessary proxy objects for it. The base proxy
 * also provides a stable place to register listeners.</p>
 */
public interface BaseProxy
        extends ParentProxy {

    /**
     * Get the descriptor of the underlying model object.
     *
     * @return The descriptor of the underlying model object.
     */
    BaseClassDescriptor getBaseClassDescriptor();

    Proxy getConcreteProxy();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/4	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
