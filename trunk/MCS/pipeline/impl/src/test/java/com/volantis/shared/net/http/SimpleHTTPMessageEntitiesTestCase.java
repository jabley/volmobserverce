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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.http;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.net.http.headers.HeaderImpl;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import junitx.util.PrivateAccessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleHTTPMessageEntitiesTestCase extends TestCaseAbstract {

    /**
     * Construct a new SimpleHTTPMessageEntitiesTestCase.
     * @param name The name of this test.
     */
    public SimpleHTTPMessageEntitiesTestCase(String name) {
        super(name);
    }

    /**
     * Test that put puts a HTTPMessageEntity into the ..Entities. In
     * doing so this test also tests contains and retrieve as a side-effect.
     */
    public void testPutContainsRetreive() {
        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();
        String headerName = "header";

        HeaderImpl header = new HeaderImpl(headerName);

        entities.put(header);

        assertTrue("The header that was put is not contained in this entities",
                entities.containsIdentity(header.getIdentity()));


        HTTPMessageEntity retrieved [] =
                entities.retrieve(header.getIdentity());

        assertEquals("There can be only one", 1, retrieved.length);

        assertEquals("The retreived HTTPMessageEntity is not equal to the" +
                " entity that was put", header, retrieved[0]);
    }

    /**
     * Test that the put method replaces an existing entity in the entities
     * with the same name.
     */
    public void testPutDoesReplace() {
        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();

        String headerName = "header";

        String value1 = "value1";
        String value2 = "value2";

        HeaderImpl header1 = new HeaderImpl(headerName);
        header1.setValue(value1);

        entities.put(header1);

        HTTPMessageEntity retrieved [] = entities.retrieve(header1.getIdentity());

        assertEquals(value1, retrieved[0].getValue());

        HeaderImpl header2 = new HeaderImpl(headerName);
        header2.setValue(value2);

        entities.put(header2);

        retrieved = entities.retrieve(header1.getIdentity());

        assertEquals("There can be only one", 1, retrieved.length);

        assertEquals("The previous entry should have been replaced", value2,
                retrieved[0].getValue());
    }

    /**
     * Test array put i.e. the put that takes an array of entities to
     * put.
     */
    public void testArrayPut() throws Exception {

        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();

        String name1 = "name1";
        String name2 = "name2";
        String name3 = "name3";

        HeaderImpl header1 = new HeaderImpl(name1);
        HeaderImpl header2 = new HeaderImpl(name1);
        HeaderImpl header3 = new HeaderImpl(name2);
        HeaderImpl header4 = new HeaderImpl(name3);

        entities.add(header1);
        header1.setValue("value1");
        entities.add(header2);
        header2.setValue("value2");
        entities.add(header3);
        entities.add(header4);

        Header headers [] = new Header[3];
        headers[0] = new HeaderImpl(name1);
        headers[1] = new HeaderImpl(name1);
        headers[2] = new HeaderImpl(name2);

        // Putting the headers array should replace header1, header2 and
        // header3 and return these three replaced entities. In addition
        // entities should consist of the three headers in the headers array
        // and header4.

        HTTPMessageEntity replaced [] = entities.put(headers);

        assertEquals("Expected 3 replaced entities", 3, replaced.length);

        assertEquals("Expected there to be 4 entities in entities", 4,
                entities.size());

        // Check the replaced entities.
        List replacedList = Arrays.asList(replaced);
        List expectedList = new ArrayList();
        expectedList.add(header1);
        expectedList.add(header2);
        expectedList.add(header3);
        assertCollectionsSame(expectedList, replacedList);

        // Check that entities is as expected.
        Collection entityCollection = (Collection)
                PrivateAccessor.getField(entities, "entities");
        expectedList = new ArrayList(Arrays.asList(headers));
        expectedList.add(header4);
        assertCollectionsSame(expectedList, entityCollection);
    }

    /**
     * Assert that two Collections contain the same objects. Objects are
     * considered to be the same when they have the same
     * System.identityHashCode.
     * @param c1 A Collection.
     * @param c2 Another Collection.
     * @todo move this into Synergetics TestCaseAbstract
     */
    private void assertCollectionsSame(Collection c1, Collection c2) {

        if (c1 != c2) {
            assertNotNull(c1);
            assertNotNull(c2);
            assertEquals(c1.size(), c2.size());


            Object array1 [] = c1.toArray();
            Object array2 [] = c2.toArray();

            for (int i1 = 0; i1 < array1.length; i1++) {
                Object current = array1[i1];
                int currentHashCode = System.identityHashCode(current);
                Object o = null;
                int i2 = 0;
                do {
                    o = array2[i2];
                    i2++;
                } while (i2 < array2.length && System.identityHashCode(o) !=
                        currentHashCode);
                assertEquals("Objects not same: " + current + ", " + o,
                        currentHashCode, System.identityHashCode(o));
            }
        }
    }

    /**
     * Test remove.
     */
    public void testRemove() {
        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();

        String name1 = "name1";
        String name2 = "name2";

        HeaderImpl header1 = new HeaderImpl(name1);
        HeaderImpl header2 = new HeaderImpl(name1);
        HeaderImpl header3 = new HeaderImpl(name2);

        entities.add(header1);
        header1.setValue("value1");
        entities.add(header2);
        header2.setValue("value2");
        entities.add(header3);

        // Removing name1 headers should remove header1 and header2
        // leaving only header3.

        HTTPMessageEntity removed [] = entities.remove(header1.getIdentity());

        assertEquals("Two entities should have been removed", 2,
                removed.length);

        assertEquals("One entity should remain", 1, entities.size());

        // Use identityHashCode in case equals changes later.
        if (System.identityHashCode(removed[0]) ==
                System.identityHashCode(header1)) {
            assertEquals("Expected to find header2",
                    System.identityHashCode(header2),
                    System.identityHashCode(removed[1]));
        } else {
            assertEquals("Expected to find header1",
                    System.identityHashCode(header1),
                    System.identityHashCode(removed[1]));

            assertEquals("Expected to find header2",
                    System.identityHashCode(header2),
                    System.identityHashCode(removed[0]));
        }
    }

    /**
     * Test remove.
     */
    public void testRetrieve() {
        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();

        String name1 = "name1";
        String name2 = "name2";

        HeaderImpl header1 = new HeaderImpl(name1);
        HeaderImpl header2 = new HeaderImpl(name1);
        HeaderImpl header3 = new HeaderImpl(name2);

        entities.add(header1);
        header1.setValue("value1");
        entities.add(header2);
        header2.setValue("value2");
        entities.add(header3);

        HTTPMessageEntity retrieved [] =
                entities.retrieve(header1.getIdentity());

        assertEquals("Two entities should have been retrieved", 2,
                retrieved.length);

        assertEquals("Three entities should remain", 3, entities.size());

        // Use identityHashCode in case equals changes later.
        if (System.identityHashCode(retrieved[0]) ==
                System.identityHashCode(header1)) {
            assertEquals("Expected to find header2",
                    System.identityHashCode(header2),
                    System.identityHashCode(retrieved[1]));
        } else {
            assertEquals("Expected to find header1",
                    System.identityHashCode(header1),
                    System.identityHashCode(retrieved[1]));

            assertEquals("Expected to find header2",
                    System.identityHashCode(header2),
                    System.identityHashCode(retrieved[0]));
        }
    }

    /**
     * Test contains.
     */
    public void testContainsPositive() {
        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();

        String name1 = "name1";
        String name2 = "name2";

        HeaderImpl header1 = new HeaderImpl(name1);
        HeaderImpl header2 = new HeaderImpl(name1);
        HeaderImpl header3 = new HeaderImpl(name2);

        entities.put(header1);
        entities.put(header2);
        entities.put(header3);

        assertTrue("Expected a name1 entity to be contained in entities",
                entities.containsIdentity(header1.getIdentity()));

        assertFalse("Did not expect an entity named bibble to be contained " +
                "in entities",
                entities.containsIdentity(new HeaderImpl("bibble").getIdentity()));
    }

    /**
     * Test clear.
     */
    public void testClear() {
        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();

        String name1 = "name1";
        String name2 = "name2";

        HeaderImpl header1 = new HeaderImpl(name1);
        header1.setValue("value1");
        HeaderImpl header2 = new HeaderImpl(name1);
        header2.setValue("value2");
        HeaderImpl header3 = new HeaderImpl(name2);

        entities.add(header1);
        entities.add(header2);
        entities.add(header3);

        assertEquals(3, entities.size());
        entities.clear();
        assertEquals(0, entities.size());
    }

    /**
     * Test add.
     * @throws java.lang.Exception
     */
    public void testAdd() throws Exception {

        SimpleHTTPMessageEntities entities =
                new SimpleHTTPMessageEntities();

        String name1 = "name1";
        String name2 = "name2";

        HeaderImpl header1 = new HeaderImpl(name1);
        header1.setValue("value1");
        HeaderImpl header2 = new HeaderImpl(name1);
        header2.setValue("value2");
        HeaderImpl header3 = new HeaderImpl(name1);
        header3.setValue("value2");
        HeaderImpl header4 = new HeaderImpl(name2);

        entities.add(header1);
        entities.add(header2);
        entities.add(header3);
        entities.add(header4);

        assertEquals(3, entities.size());


        // Check that entities is as expected.
        Collection entityCollection = (Collection)
                PrivateAccessor.getField(entities, "entities");
        ArrayList expectedList = new ArrayList();
        expectedList.add(header1);
        expectedList.add(header2);
        expectedList.add(header4);
        assertCollectionsSame(expectedList, entityCollection);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	217/12	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/10	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/8	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 28-Jul-03	217/6	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 ===========================================================================
*/
