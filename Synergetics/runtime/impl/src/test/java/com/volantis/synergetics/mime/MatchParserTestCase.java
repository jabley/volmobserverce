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
package com.volantis.synergetics.mime;

import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Learning test about conversions
 */
public class MatchParserTestCase extends TestCase {

    public void testConvertHexals() {
        String str = "0xfdc4";
        long l = Long.decode(str).longValue();
        ByteBuffer b = ByteBuffer.allocate(8);
        b.order(ByteOrder.LITTLE_ENDIAN);
        b.putLong(l);
        assertEquals("fdc4", Long.toHexString(l));
    }

}
