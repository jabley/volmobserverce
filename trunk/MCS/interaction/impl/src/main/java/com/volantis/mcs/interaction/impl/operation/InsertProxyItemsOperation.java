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

import java.util.Collections;
import java.util.List;

public class InsertProxyItemsOperation
        extends AbstractOperation {

    private final ListProxyImpl listProxy;
    private final int index;
    private final List proxyItems;

    public InsertProxyItemsOperation(
            ListProxyImpl listProxy, int index, List proxyItems) {
        super(listProxy);

        this.listProxy = listProxy;
        this.index = index;
        this.proxyItems = Collections.unmodifiableList(proxyItems);
    }

    protected void executeImpl() {
        listProxy.insertItems(index, proxyItems);
    }

    protected void undoImpl() {
        listProxy.removeItems(index, index + proxyItems.size() - 1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added basic list operations

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
