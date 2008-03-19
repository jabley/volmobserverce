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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.descriptorstore;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

public class ResourceDescriptorStoreTestCase extends TestCaseAbstract {

    private static final ResourceDescriptorStoreFactory FACTORY =
        ResourceDescriptorStoreFactory.getDefaultInstance();

    private ResourceDescriptorStore store;

    protected void setUp() throws Exception {
        initializeModelOperations();
        super.setUp();

    }

    protected void tearDown() throws Exception {
        store.shutdown();
        super.tearDown();
    }

    public void initializeModelOperations() throws IOException {
        final Properties props = new Properties();
        final String properties =
            "javax.jdo.PersistenceManagerFactoryClass=" +
            "org.jpox.PersistenceManagerFactoryImpl\n" +
            "javax.jdo.option.ConnectionDriverName=org.hsqldb.jdbcDriver\n" +
            "javax.jdo.option.ConnectionURL=jdbc:hsqldb:mem:" + getName() +
            "\n" +
            "javax.jdo.option.ConnectionUserName=sa\n" +
            "javax.jdo.option.ConnectionPassword=\n" +
            "\n" +
            "org.jpox.autoCreateSchema=true\n" +
            "org.jpox.validateTables=false\n" +
            "org.jpox.validateConstraints=false";
        props.load(new ByteArrayInputStream(properties.getBytes()));
        store= FACTORY.getResourceDescriptorStore(props);
    }


    public void testCreateSameConfigurationItem() throws Exception {
        String type = "wibble";
        Parameters params = store.createParameters();
        params.setParameterValue("bob", "jane");
        params.setParameterValue("scooby", "scrappy");

        ParameterNames names = store.createParameterNames();

        ResourceDescriptor item1 =
            store.createDescriptor(type, params, names, 600);

        ResourceDescriptor item2 =
            store.createDescriptor(type, params, names, 600);

        assertEquals("items should have the same external ID",
                     item2.getExternalID(), item1.getExternalID());

        ResourceDescriptor item3 = store.getDescriptor(item1.getExternalID());
        assertEquals("items should have the same ID", item3, item1);
    }

    public void testCreateDifferentConfigurationItem() throws Exception {
        String type = "wibble";

        Parameters params = store.createParameters();
        params.setParameterValue("bob", "jane");
        params.setParameterValue("scooby", "scrappy");

        Parameters params2 = store.createParameters();
        params2.setParameterValue("bob", "jane");
        params2.setParameterValue("scooby", "scrappy2");


        ParameterNames names = store.createParameterNames();

        ResourceDescriptor item1 =
            store.createDescriptor(type, params, names, 600);

        ResourceDescriptor item2 =
            store.createDescriptor(type, params2, names, 600);

        assertNotEquals(item2.getExternalID(), item1.getExternalID());

        assertEquals(item1, store.getDescriptor(item1.getExternalID()));
        assertEquals(item2, store.getDescriptor(item2.getExternalID()));
        try {
            store.getDescriptor("This is not a real external ID");

        } catch (ResourceDescriptorStoreException cse) {
            // success
        }
    }

    public void testUpdate() throws Exception {
        String type = "wibble";

        Parameters params = store.createParameters();
        params.setParameterValue("bob", "jane");
        params.setParameterValue("scooby", "scrappy");

        ParameterNames names = store.createParameterNames();

        ResourceDescriptor item1 =
            store.createDescriptor(type, params, names, -1);

        //TTL should be -1
        assertEquals(-1, item1.getTimeToLive());

        item1.setTimeToLive(100);
        store.updateDescriptor(item1);

        ResourceDescriptor item2 = store.getDescriptor(item1.getExternalID());
        assertTrue(100 >= item2.getTimeToLive());
    }

    public void testDifferentTypesCauseDifferentObjects() throws Exception {
        String type1 = "wibble";
        String type2 = "wobble";

        Parameters params = store.createParameters();
        params.setParameterValue("bob", "jane");
        params.setParameterValue("scooby", "scrappy");

        ParameterNames names = store.createParameterNames();

        ResourceDescriptor item1 =
            store.createDescriptor(type1, params, names, 600);
        ResourceDescriptor item2 =
            store.createDescriptor(type2, params, names, 600);

        assertNotEquals(item2.getExternalID(), item1.getExternalID());

        assertEquals(item1, store.getDescriptor(item1.getExternalID()));
        assertEquals(item2, store.getDescriptor(item2.getExternalID()));
    }

}
