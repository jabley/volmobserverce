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
package com.volantis.mcs.protocols.widgets;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Widget module used within XHTMLFull protocol.
 */
public interface XHTMLFullWidgetModule extends WidgetModule {
    /**
     * Processes the specified body element, so that the onload attribute
     * contains an invocation of the Widget.startup(). This is required by the
     * 'vfc-base.js' library to work.
     * 
     * @param protocol The protocol used.
     * @param element The body element.
     */
    public void processBodyElementForStartup(VolantisProtocol protocol, Element element);
}
