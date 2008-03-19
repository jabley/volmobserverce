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

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.interaction.sample.model.Address;
import com.volantis.mcs.interaction.sample.model.Person;

import java.util.ArrayList;
import java.util.List;

public class InteractionTestCase
        extends FlintstoneTestAbstract {

    public void testCreateModelFromProxies() {

        Person person = new Person();
        Address address;

        BeanProxy personProxy = (BeanProxy) createProxy(person);
        OpaqueProxy firstNameProxy = (OpaqueProxy) personProxy.getPropertyProxy(
                Person.FIRST_NAME);
        firstNameProxy.setModelObject("Fred");
        assertEquals("First name", "Fred", person.getFirstName());

        OpaqueProxy ageProxy = (OpaqueProxy) personProxy.getPropertyProxy(
                Person.AGE);
        ageProxy.setModelObject(new Integer(10030));
        assertEquals("Age", 10030, person.getAge());

        // Address should be null before it is modified.
        address = person.getAddress();
        assertNull("Address not null", address);

        BeanProxy addressProxy = (BeanProxy) personProxy.getPropertyProxy(
                Person.ADDRESS);
        ListProxy linesProxy = (ListProxy) addressProxy.getPropertyProxy(
                Address.LINES);

        // Address should still be null after its proxy and a nested proxy have
        // been added.
        address = person.getAddress();
        assertNull("Address not null", address);

        OpaqueProxy lineProxy;
        lineProxy = (OpaqueProxy) linesProxy.addItemProxy();
        lineProxy.setModelObject("301 Cobblestone Way");

        // Address should not be null now.
        address = person.getAddress();
        assertNotNull("Address null", address);

        lineProxy = (OpaqueProxy) linesProxy.addItemProxy();
        lineProxy.setModelObject("Bedrock");

        lineProxy = (OpaqueProxy) linesProxy.addItemProxy();
        lineProxy.setModelObject("70777");

        List expectedAddressLines = new ArrayList();
        expectedAddressLines.add("301 Cobblestone Way");
        expectedAddressLines.add("Bedrock");
        expectedAddressLines.add("70777");

        // Lines should not be null and should match expected.
        List actualAddressLines = address.getLines();
        assertNotNull("Address lines null", actualAddressLines);
        assertEquals(
                "Address lines mismatch", expectedAddressLines,
                actualAddressLines);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9961/6	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/4	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
