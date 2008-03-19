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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.DeckPageAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;

/**
 * Deck XDIME element.
 */
public class DeckPageElement extends WidgetElement {

    /**
     * Creates and initialises new instance of Deck element.
     * @param context
     */
    public DeckPageElement(XDIMEContextInternal context) {
        super(WidgetElements.DECK_PAGE, context);

        protocolAttributes = new DeckPageAttributes();
    }
}
