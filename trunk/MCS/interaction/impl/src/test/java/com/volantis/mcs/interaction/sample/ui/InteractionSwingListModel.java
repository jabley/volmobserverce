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

package com.volantis.mcs.interaction.sample.ui;

import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.event.InsertedIntoListEvent;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;

import javax.swing.AbstractListModel;

class InteractionSwingListModel
        extends AbstractListModel {

    private ListProxy listProxy;

    public InteractionSwingListModel(final ListProxy listProxy) {
        this.listProxy = listProxy;

        InteractionEventListener listener = new InteractionEventListenerAdapter() {

            protected void interactionEvent(InteractionEvent event) {
                fireContentsChanged(
                        InteractionSwingListModel.this, 0,
                        listProxy.size() - 1);
            }

            public void addedToList(InsertedIntoListEvent event) {
                int index = event.getFirstItemIndex();
                fireIntervalAdded(
                        InteractionSwingListModel.this, index, index);
            }
        };

        listProxy.addListener(listener, true);
    }

    public Object getElementAt(int index) {
        return listProxy.getItemProxy(index);
    }

    public int getSize() {
        return listProxy.size();
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added basic list operations

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
