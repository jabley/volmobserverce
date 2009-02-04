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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.viewport;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.mcs.protocols.DOMProtocol;

public class IPhoneCompatibleViewportControlRenderer implements ViewportControlRenderer {

    public void renderMeta(DOMProtocol protocol, DOMOutputBuffer dom) {
        ProtocolConfiguration conf = protocol.getProtocolConfiguration();
        int width = conf.getDevicePixelsX();
        String name = "viewport";
        String content = "width=" + width + ", initial-scale=1.0";

        MetaAttributes metaAttributes = new MetaAttributes();
        metaAttributes.setName(name);
        metaAttributes.setContent(content);

        // Write meta element to the head.
        protocol.doMeta(dom, metaAttributes);

    }
}
