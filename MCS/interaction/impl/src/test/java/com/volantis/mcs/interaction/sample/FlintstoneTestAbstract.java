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

import com.volantis.mcs.interaction.InteractionFactory;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.sample.descriptors.Descriptors;
import com.volantis.mcs.interaction.sample.model.Address;
import com.volantis.mcs.interaction.sample.model.AddressImpl;
import com.volantis.mcs.interaction.sample.model.Person;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;

public abstract class FlintstoneTestAbstract
        extends TestCaseAbstract {

    public Address createFlintStoneAddress() {
        Address address = new AddressImpl();
        List lines = address.getLines();
        lines.add("301 Cobblestone Way");
        lines.add("Bedrock");
        lines.add("70777");
        return address;
    }

    public Person createFredFlintstone(Address address) {
        Person person = new Person();
        person.setFirstName("Fred");
        person.setLastName("Flintstone");
        person.setAge(10040);
        person.setAddress(address);
        return person;
    }

    public Person createWilmaFlintstone(Address address) {
        Person person = new Person();
        person.setFirstName("Wilma");
        person.setLastName("Flintstone");
        person.setAge(10021);
        person.setAddress(address);
        return person;
    }

    protected Proxy createProxy(Object object) {
        ModelDescriptor descriptor = Descriptors.MODEL_DESCRIPTOR;
        InteractionFactory factory = InteractionFactory.getDefaultInstance();
        InteractionModel interactionModel =
                factory.createInteractionModel(descriptor);

        Proxy proxy = interactionModel.createProxyForModelObject(object);
        return proxy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/4	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9961/2	pduffin	VBM:2005101811 Committing restructuring

 ===========================================================================
*/
