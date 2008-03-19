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

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.DOMFactory;

/**
 * Provides a mechanism for a protocol to obtain the supporting objects that
 * it needs.
 *
 * <p>The driving force behind this class is to make the protocol more testable
 * by providing them with all the information that they need, rather than
 * expecting them to get it.</p>
 *
 * <p>This class contains information necessary to initialise a protocol for a
 * specific request whereas the {@link ProtocolConfiguration} contains
 * information necessary to initialise a protocol for a specific device.</p>
 *
 * @mock.generate
 */
public interface ProtocolSupportFactory {

    DOMFactory getDOMFactory();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
