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

package com.volantis.mcs.interaction.impl.operation;

import com.volantis.mcs.interaction.impl.InternalProxy;

/**
 * An operation that will set the model object associated with a simple proxy.
 */
public class SetModelObjectOperation
        extends AbstractOperation {

    private final InternalProxy proxy;
    private final Object oldModelObject;
    private final Object newModelObject;

    /**
     * Initialise.
     *
     * @param proxy The proxy for which this operation was created.
     */
    public SetModelObjectOperation(InternalProxy proxy, Object newModelObject) {
        super(proxy);

        this.proxy = proxy;
        this.oldModelObject = proxy.getModelObject();
        this.newModelObject = newModelObject;
    }

    // Javadoc inherited.
    protected void executeImpl() {
        proxy.setModelObject(newModelObject);
    }

    // Javadoc inherited.
    protected void undoImpl() {
        proxy.setModelObject(oldModelObject);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/4	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
