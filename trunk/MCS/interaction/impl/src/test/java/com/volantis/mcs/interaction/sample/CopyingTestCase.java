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

package com.volantis.mcs.interaction.sample;

import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.sample.model.Address;
import com.volantis.mcs.interaction.sample.model.Person;

import java.util.List;
import java.util.Iterator;

/**
 * Test that copying works.
 */
public class CopyingTestCase
 extends FlintstoneTestAbstract {

    public void testCopying() {

        Address address = createFlintStoneAddress();
        Person person = createFredFlintstone(address);

        Proxy proxy = createProxy(person);

        Person personCopy = (Person) proxy.copyModelObject();
        assertEquals("age", person.getAge(), personCopy.getAge());
        assertSame("firstName", person.getFirstName(), personCopy.getFirstName());
        assertSame("lastName", person.getLastName(), personCopy.getLastName());

        Address addressCopy = personCopy.getAddress();
        assertNotSame("address", address, addressCopy);

        List lines = address.getLines();
        List linesCopy = addressCopy.getLines();
        assertNotSame("lines", lines, linesCopy);

        assertEquals("lines", lines, linesCopy);

        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            String lineCopy = (String) linesCopy.get(i);
            assertSame("line " + i, line, lineCopy);
            assertEquals("line " + i, line, lineCopy);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 ===========================================================================
*/
