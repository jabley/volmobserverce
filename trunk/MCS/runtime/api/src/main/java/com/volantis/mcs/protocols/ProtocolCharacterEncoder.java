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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ProtocolCharacterEncoder.java,v 1.1 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-May-02    Paul            VBM:2002042202 - Created to use protocol
 *                              information to encode entity references.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.output.AbstractCharacterEncoder;

import java.io.IOException;
import java.io.Writer;

/**
 * Class for doing protocol specific encoding of characters.
 */
public final class ProtocolCharacterEncoder
        extends AbstractCharacterEncoder {

    private final VolantisProtocol protocol;

    public ProtocolCharacterEncoder(VolantisProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Return the entity reference to use for the character, or null if there
     * isn't one.
     *
     * @param c The character whose entity reference is required.
     * @return The name of the entity reference, or null.
     */
    private String getEntityRef(int c) {
        return protocol.encodeCharacter(c);
    }

    /**
     * Convert some characters into entity references.
     */
    public void encode(int c, Writer out)
            throws IOException {

        String entityRef = getEntityRef(c);
        if (entityRef != null) {
            out.write(entityRef);
        } else {
            // TODO we really need to add some stuff in here to cope with
            // unprintable characters. Unfortunately this means that we have to
            // a lot of information about the encoding and Java does not currently
            // have a public API to get access to that information hence we just
            // leave it for now.
            out.write(c);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
