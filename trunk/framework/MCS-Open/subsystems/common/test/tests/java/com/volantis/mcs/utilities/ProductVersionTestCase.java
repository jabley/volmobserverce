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

package com.volantis.mcs.utilities;

import com.volantis.synergetics.testtools.TestCaseAbstract;

public class ProductVersionTestCase extends TestCaseAbstract {

    public void testParsing() throws Exception {

        ProductVersion v1 = new ProductVersion(1, 0, 0);

        // version 1 should be equal to 1.0.0
        assertEquals(v1, ProductVersion.parse("1"));
        // version 1.0 should be equal to 1.0.0
        assertEquals(v1, ProductVersion.parse("1.0"));
        // version 1.0.0 should of course be equal to 1.0.0
        assertEquals(v1, ProductVersion.parse("1.0.0"));
        // patch level should be ignored
        assertEquals(v1, ProductVersion.parse("1.0.0 Patch Level 27"));
        // even if not separated from the last digit
        assertEquals(v1, ProductVersion.parse("1.0.0PatchLevel27"));
        // any additional digits should be igored
        assertEquals(v1, ProductVersion.parse("1.0.0.27"));
        // other separators are OK, too
        assertEquals(v1, ProductVersion.parse("1_0_0"));

        assertNull(ProductVersion.parse(null));
        assertNull(ProductVersion.parse(""));        
        assertNull(ProductVersion.parse("   "));
        assertNull(ProductVersion.parse("invalid"));
        assertNull(ProductVersion.parse("in.va.lid"));
    }

    public void testCompare() throws Exception {

        ProductVersion v132 = new ProductVersion(1, 3, 2);
        ProductVersion v312 = new ProductVersion(3, 1, 2);

        assertEquals(v132.compareTo(v312), -1 * v312.compareTo(v132));
        assertTrue(v132.compareTo(v312) < 0);
        assertTrue(v312.compareTo(v132) > 0);
        assertTrue(v312.compareTo(v312) == 0);                
    }

    public void testGreaterOrEqual() throws Exception {

        assertTrue(ProductVersion.parse("1").isGreaterOrEqual(ProductVersion.parse("0.9")));
        assertTrue(ProductVersion.parse("1.0").isGreaterOrEqual(ProductVersion.parse("0.9")));
        assertTrue(ProductVersion.parse("1.0.0").isGreaterOrEqual(ProductVersion.parse("0.9")));

        ProductVersion v0 = new ProductVersion(0, 0, 0);
        assertTrue(v0.equals(v0));
        assertTrue(v0.isGreaterOrEqual(v0));
        assertFalse(v0.isGreaterOrEqual(ProductVersion.parse("1")));
    }

    public void testHashCode() throws Exception {

        ProductVersion v1 = new ProductVersion(1, 0, 0);

        // version 1 should be equal to 1.0.0
        assertEquals(v1.hashCode(), ProductVersion.parse("1").hashCode());
        // version 1.0 should be equal to 1.0.0
        assertEquals(v1.hashCode(), ProductVersion.parse("1.0").hashCode());
        // version 1.0.0 should of course be equal to 1.0.0
        assertEquals(v1.hashCode(), ProductVersion.parse("1.0.0").hashCode());
        // patch level should be ignored
        assertEquals(v1.hashCode(), ProductVersion.parse("1.0.0 Patch Level 27").hashCode());
        // even if not separated from the last digit
        assertEquals(v1.hashCode(), ProductVersion.parse("1.0.0PatchLevel27").hashCode());
        // any additional digits should be igored
        assertEquals(v1.hashCode(), ProductVersion.parse("1.0.0.27").hashCode());
        // other separators are OK, too
        assertEquals(v1.hashCode(), ProductVersion.parse("1_0_0").hashCode());
    }

    public void testToString() throws Exception  {

        ProductVersion v = null;

        v = new ProductVersion(1, 0, 0);
        assertEquals(v, ProductVersion.parse(v.toString()));

        v = new ProductVersion(14, 3, 124);
        assertEquals(v, ProductVersion.parse(v.toString()));

        v = new ProductVersion(14, 0, 1);
        assertEquals(v, ProductVersion.parse(v.toString()));

        v = new ProductVersion(14, 0, 0);
        assertEquals(v, ProductVersion.parse(v.toString()));
    }

    public void testCreation()  throws Exception {

        ProductVersion v = new ProductVersion(3, 1, 2);
        assertEquals(3, v.getMajor());
        assertEquals(1, v.getMinor());
        assertEquals(2, v.getRevision());

        v = new ProductVersion(-3, 1, 2);
        assertEquals(v, new ProductVersion(0, 1, 2));
    }
}
