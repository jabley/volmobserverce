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

import com.volantis.mcs.interaction.impl.ListProxyImpl;

import java.util.List;

public class RemoveProxyItemsOperation
        extends AbstractOperation {

    private final ListProxyImpl listProxy;
    private final int firstItemIndex;
    private final int lastItemIndex;
    private List removedProxies;

    public RemoveProxyItemsOperation(
            ListProxyImpl listProxy, int firstItemIndex, int lastItemIndex) {
        super(listProxy);

        this.listProxy = listProxy;
        this.firstItemIndex = firstItemIndex;
        this.lastItemIndex = lastItemIndex;
    }

    protected void executeImpl() {
        removedProxies = listProxy.removeItems(firstItemIndex, lastItemIndex);
    }

    protected void undoImpl() {
        listProxy.insertItems(firstItemIndex, removedProxies);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added basic list operations

 ===========================================================================
*/
