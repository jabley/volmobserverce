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
package com.volantis.mcs.interaction.event;

import com.volantis.mcs.interaction.Proxy;

/**
 * Event notifying a change of read only state.
 */
public class ReadOnlyStateChangedEvent extends InteractionEvent {
    /**
     * The new read only state.
     */
    private boolean readOnly;

    /**
     * Initialise.
     *
     * @param source The proxy that was the source of the event.
     */
    public ReadOnlyStateChangedEvent(Proxy source,
                                     boolean isReadOnly,
                                     boolean originator) {
        super(source, originator);
        this.readOnly = isReadOnly;
    }

    // Javadoc not required
    public boolean isReadOnly() {
        return readOnly;
    }

    // Javadoc inherited
    public void dispatch(InteractionEventListener listener) {
        listener.readOnlyStateChanged(this);
    }
}
