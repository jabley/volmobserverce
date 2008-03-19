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
package com.volantis.mcs.eclipse.builder.common;

import com.volantis.mcs.interaction.diagnostic.DiagnosticListener;
import com.volantis.mcs.interaction.diagnostic.DiagnosticEvent;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.ProxyEvent;

/**
 * Adapter for event listeners that provides a single common method for
 * responding to any changes in the interaction model that might result in a
 * GUI change (either model changes or diagnostic changes).
 */
public class InteractionChangeAdapter extends InteractionEventListenerAdapter
        implements DiagnosticListener {
    public void diagnosticsChanged(DiagnosticEvent event) {
        interactionChangeEvent(event);
    }

    public void interactionEvent(InteractionEvent event) {
        interactionChangeEvent(event);
    }

    public void interactionChangeEvent(ProxyEvent event) {
        // Do nothing by default
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 ===========================================================================
*/
