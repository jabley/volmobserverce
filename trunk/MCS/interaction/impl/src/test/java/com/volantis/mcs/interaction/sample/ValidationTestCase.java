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
import com.volantis.mcs.interaction.InteractionFactory;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.DiagnosticEvent;
import com.volantis.mcs.interaction.diagnostic.DiagnosticListenerMock;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import com.volantis.mcs.interaction.impl.validation.Prepare4Validation;
import com.volantis.mcs.interaction.impl.validation.ProxyDiagnosticImpl;
import com.volantis.mcs.interaction.sample.model.Address;
import com.volantis.mcs.interaction.sample.model.Contacts;
import com.volantis.mcs.interaction.sample.model.Person;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.validation.DiagnosticLevel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidationTestCase
        extends FlintstoneTestAbstract {

    private ModelFactory modelFactory;

    protected void setUp() throws Exception {
        super.setUp();

        modelFactory = ModelFactory.getDefaultInstance();
    }

    public void testValidation() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final DiagnosticListenerMock diagnosticListenerMock =
                new DiagnosticListenerMock(
                        "diagnosticListenerMock", expectations);

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        Person person = new Person();
        person.setAge(-10);

        BeanProxy root = (BeanProxy) createProxy(person);

        // =====================================================================
        //   Set Test Object Specific Expectations
        // =====================================================================

        diagnosticListenerMock.expects
                .diagnosticsChanged(new DiagnosticEvent(root, true));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        root.addDiagnosticListener(diagnosticListenerMock);
        root.validate();

        final Proxy firstNameProxy = root.getPropertyProxy(Person.FIRST_NAME);
        final Proxy lastNameProxy = root.getPropertyProxy(Person.LAST_NAME);
        final Proxy ageProxy = root.getPropertyProxy(Person.AGE);
        final Proxy addressProxy = root.getPropertyProxy(Person.ADDRESS);

        final ProxyDiagnostic firstNameDiagnostic = new ProxyDiagnosticImpl(
                DiagnosticLevel.ERROR,
                firstNameProxy,
                modelFactory.createMessage("not.set", "firstName"));
        final ProxyDiagnostic lastNameDiagnostic = new ProxyDiagnosticImpl(
                DiagnosticLevel.ERROR,
                lastNameProxy,
                modelFactory.createMessage("not.set", "lastName"));
        final ProxyDiagnostic ageDiagnostic = new ProxyDiagnosticImpl(
                DiagnosticLevel.ERROR,
                ageProxy,
                modelFactory.createMessage(
                        "out.of.range",
                        new Object[]{
                            new Integer(0),
                            new Integer(100000),
                            new Integer(-10)
                        }));
        final ProxyDiagnostic addressDiagnostic = new ProxyDiagnosticImpl(
                DiagnosticLevel.ERROR,
                addressProxy,
                modelFactory.createMessage("not.set", "address"));

        // Check the diagnostics on the proxies for the properties.
        checkDiagnostics(
                firstNameProxy, Collections.singletonList(firstNameDiagnostic));
        checkDiagnostics(
                lastNameProxy, Collections.singletonList(lastNameDiagnostic));
        checkDiagnostics(ageProxy, Collections.singletonList(ageDiagnostic));
        checkDiagnostics(
                addressProxy, Collections.singletonList(addressDiagnostic));

        // Check the diagnostics on the root proxy.
        checkDiagnostics(
                root, Arrays.asList(
                        new Object[]{
                            firstNameDiagnostic,
                            lastNameDiagnostic,
                            ageDiagnostic,
                            addressDiagnostic
                        }));
    }

    private void checkDiagnostics(Proxy proxy, List expectedDiagnostics) {
        System.out.println("\nChecking diagnostics for " + proxy.getPathAsString());
        List diagnostics = proxy.getDiagnostics();
        for (int i = 0; i < diagnostics.size(); i++) {
            ProxyDiagnostic diagnostic = (ProxyDiagnostic) diagnostics.get(i);
            System.out.println(diagnostic);
        }

        assertEquals(
                "Mismatching diagnostics", expectedDiagnostics, diagnostics);
    }

    public void notestValidationDAG() {

        Contacts contacts = new Contacts();
        List contactsList = contacts.getContacts();

        // Share the address.
        Address flintstonesAddress = createFlintStoneAddress();
        Person fred = createFredFlintstone(flintstonesAddress);
        Person wilma = createWilmaFlintstone(flintstonesAddress);

        contactsList.add(fred);
        contactsList.add(wilma);

        Proxy proxy = createProxy(contacts);

        Path path2Fred = modelFactory.parsePath("/contacts/0/address");
        Proxy fredAddress = proxy.getProxy(path2Fred);

        Path path2Wilma = modelFactory.parsePath("/contacts/1/address");
        Proxy wilmaAddress = proxy.getProxy(path2Wilma);

        // Build up a mapping from the model object to the proxies.
        Prepare4Validation prepare = new Prepare4Validation();
        prepare.prepare(proxy);

        proxy.validate();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/7	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
