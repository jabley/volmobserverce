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

package com.volantis.xml.pipeline.sax.convert;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.NameValuePair;
import junitx.util.PrivateAccessor;

import java.util.TreeSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Test the ElementCase operation process.
 */
public class ElementCaseOperationProcessTestCase
    extends TestCaseAbstract {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The operation process.
     */
    private ElementCaseOperationProcess operationProcess;

    // javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        operationProcess = new ElementCaseOperationProcess();
    }

    /**
     * Test that the static list is sorted and that the key is in uppercase
     * and the value is in lowercase.
     */
    public void testStaticCacheConstraints() throws Exception {
        Object array[] = (Object[]) PrivateAccessor.getField(
            operationProcess, "cacheArray");
        assertNotNull(array);

        TreeSet set = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                NameValuePair value1 =
                    (NameValuePair) o1;
                NameValuePair value2 =
                    (NameValuePair) o2;

                return value1.getName().compareTo(value2.getName());
            }
        });

        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }
        assertEquals("Set size matches", array.length, set.size());

        int index = 0;
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            NameValuePair o =
                (NameValuePair) iterator.next();

            assertTrue("Static cache is not sorted by name: " + o.getName(),
                       o == array[index]);

            assertEquals("Static cache name is not uppercase: " + o.getName(),
                         o.getName().toUpperCase(),
                         o.getName());

            assertEquals("Static cache value is not lowercase: " + o.getValue(),
                         o.getValue().toLowerCase(),
                         o.getValue());
            ++index;
        }
    }

    /**
     * Test the dynamic cache usage
     */
    public void testDynamicCacheUsage() throws Exception {

        operationProcess.setMode("lower");

        List cache = (List) PrivateAccessor.getField(
            operationProcess, "dynamicCache");

        assertNotNull("Cache should not be null.", cache);
        assertEquals("Cache should have no entries", 0, cache.size());

        String localName = "elementNotInStaticCache";
        operationProcess.startElement("#1", localName, localName, null);
        assertEquals("Cache should have 1 entry", 1, cache.size());
        doesResultMatch(cache.get(0), localName, localName.toLowerCase());

        localName = "anotherElementNotInStaticCache";
        operationProcess.startElement("#2", localName, localName, null);
        assertEquals("Cache should have 2 entries", 2, cache.size());
        doesResultMatch(cache.get(1), localName, localName.toLowerCase());

        // Should find localName and not add another item to the cache.
        operationProcess.startElement("#3", localName, localName, null);
        assertEquals("Cache should have 2 entries", 2, cache.size());
        doesResultMatch(cache.get(1), localName, localName.toLowerCase());
    }

    /**
     * Test the dynamic cache usage with a QName.
     */
    public void testDynamicCacheUsageWithQName() throws Exception {
        operationProcess.setMode("upper");

        List cache = (List) PrivateAccessor.getField(
            operationProcess, "dynamicCache");

        assertNotNull("Cache should not be null.", cache);
        assertEquals("Cache should have no entries", 0, cache.size());

        String localName = "notInDynamicCache";
        String qNameValue = "qnameValue";
        String qName = qNameValue + ":" + localName;

        // This should add the qName and localName to the dynamic cache.
        operationProcess.startElement("#1", localName, qName, null);
        assertEquals("Cache should have correct size:", 2, cache.size());
        doesResultMatch(cache.get(0), localName, localName.toUpperCase());
        doesResultMatch(cache.get(1), qName, qNameValue + ":" +
                                             localName.toUpperCase());

        // Start element again - should return use the cached value.
        operationProcess.startElement("#2", localName, qName, null);
        assertEquals("Cache should have correct size:", 2, cache.size());
        doesResultMatch(cache.get(0), localName, localName.toUpperCase());
        doesResultMatch(cache.get(1), qName, qNameValue + ":" +
                                             localName.toUpperCase());

        // Start element again - with EMPTY qname.
        operationProcess.startElement("#3", localName, "", null);
        assertEquals("Cache should have correct size:", 2, cache.size());
    }

    /**
     * Helper method to reduce code bloat.
     */
    private void doesResultMatch(Object nameValuePair,
                                 String expectedName,
                                 String expectedValue) {
        assertEquals("Cache name should match",
                     expectedName,
                     ((NameValuePair) nameValuePair).getName());

        assertEquals("Cache value should match",
                     expectedValue,
                     ((NameValuePair) nameValuePair).getValue());


    }

    /**
     * Test the static cache usage.
     */
    public void testStaticCacheUsage() throws Exception {

        List cache = (List) PrivateAccessor.getField(
            operationProcess, "dynamicCache");

        assertNotNull("Cache should not be null.", cache);
        assertEquals("Cache should have no entries", 0, cache.size());

        final String localName = "HTML";
        operationProcess.startElement("#", localName, localName, null);
        assertEquals("Cache should have no entries", 0, cache.size());
    }

    /**
     * Helper method to retrieve invoke the private method
     * getConvertedLocalName on the operation process.
     *
     * @param  name the name.
     * @return      the result of the conversion (if not cached) or the cached
     *              value.
     */
    private String getConvertedLocalName(String name) {
        String result = null;
        try {
            result = (String) PrivateAccessor.invoke(
                operationProcess,
                "getConvertedLocalName",
                new Class[]{String.class},
                new Object[]{name});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            fail(throwable.getMessage());
        }
        return result;
    }

    /**
     * Helper method to retrieve invoke the private method getConvertedQName on
     * the operation process.
     *
     * @param  name               the name
     * @param  convertedLocalName the converted local name.
     * @return                    the result of the conversion (if not cached)
     *                            or the cached value.
     */
    private String getConvertedQName(String name, String convertedLocalName) {
        String result = null;
        try {
            result = (String) PrivateAccessor.invoke(
                operationProcess,
                "getConvertedQName",
                new Class[]{String.class, String.class},
                new Object[]{name, convertedLocalName});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            fail(throwable.getMessage());
        }
        return result;
    }

    /**
     * Helper method for testSetMode.
     *
     * @param mode the mode
     */
    private void doTestModeWithExpectedException(String mode) {
        Exception exception = null;
        try {
            operationProcess.setMode(mode);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull("Should have thrown an IllegalArgumentException",
                      exception);
    }

    /**
     * Test the set mode method. This method should only permit the setting
     * of the mode to 'lower' and 'upper'. Anything else should throw an
     * IllegalArgumentException.
     */
    public void testSetMode() throws Exception {
        operationProcess.setMode("lower");
        Object mode = PrivateAccessor.getField(operationProcess,
                                               "convertToLowerCase");
        assertEquals("Mode should match", Boolean.TRUE, mode);

        operationProcess.setMode("upper");
        mode = PrivateAccessor.getField(operationProcess,
                                        "convertToLowerCase");
        assertEquals("Mode should match", Boolean.FALSE, mode);

        doTestModeWithExpectedException("UPPER");
        doTestModeWithExpectedException("LOWER");
        doTestModeWithExpectedException("");
        doTestModeWithExpectedException(null);
    }

    /**
     * Helper method for getting the converted local name
     *
     * @param  cache             the dynamic cache
     * @param  name              the name
     * @param  expectedCacheSize the expected cache size after the get.
     * @param  index             the index to verify the equality of items in
     *                           the cache.
     */
    private void doTestGetConvertedLocalName(final List cache,
                                             final String name,
                                             int expectedCacheSize,
                                             int index)
        throws Exception {

        String result = getConvertedLocalName(name);
        assertEquals("Result should match", name.toLowerCase(), result);

        assertEquals("Cache should have correct size:",
                     expectedCacheSize,
                     cache.size());

        if (expectedCacheSize > 0) {
            assertEquals("Item in the cache should have the correct value:",
                         ((NameValuePair) cache.get(index)).getValue(),
                         result);
        }
    }

    /**
     * Test the getting of the converted local name.
     */
    public void testGetConvertedLocalName() throws Exception {

        List cache = (List) PrivateAccessor.getField(
            operationProcess, "dynamicCache");

        operationProcess.setMode("lower");

        // Should use static cache...
        doTestGetConvertedLocalName(cache, "HTML", 0, -1);

        // Should use static cache...
        doTestGetConvertedLocalName(cache, "HtMl", 0, -1);

        // Should use dynamic cache...
        doTestGetConvertedLocalName(cache, "notInStaticCache", 1, 0);

        // Should use dynamic cache and not re-add same item...
        doTestGetConvertedLocalName(cache, "notInStaticCache", 1, 0);

        // Add a second item to the cache.
        doTestGetConvertedLocalName(cache, "SecondItemNotInStaticCache", 2, 1);
    }

    /**
     * Helper method for getting the converted local name
     *
     * @param cache             the dynamic cache
     * @param qName             the qualified name
     * @param                   convertedLocalName the converted local name
     * @param expectedCacheSize the expected cache size after the get.
     * @param index             the index to verify the equality of items in
     *                          the cache.
     */
    private void doTestGetConvertedQName(final List cache,
                                         final String qName,
                                         final String convertedLocalName,
                                         int expectedCacheSize,
                                         int index,
                                         String expected)
        throws Exception {

        String result = getConvertedQName(qName, convertedLocalName);
        assertEquals("Result should match", expected, result);

        assertEquals("Cache should have correct size:",
                     expectedCacheSize,
                     cache.size());

        if (expectedCacheSize > 0) {
            assertEquals("Item in the cache should have the correct value:",
                         ((NameValuePair) cache.get(index)).getValue(),
                         result);
        }
    }

    /**
     * Test the getting of the converted local name.
     */
    public void testGetConvertedQName() throws Exception {

        List cache = (List) PrivateAccessor.getField(
            operationProcess, "dynamicCache");

        operationProcess.setMode("upper");

        // Should use static cache...
        doTestGetConvertedQName(cache, "", "HTML", 0, -1, "");

        // Should use static cache...
        doTestGetConvertedQName(cache, "abc:html", "HTML", 1, 0, "abc:HTML");

        // Should use dynamic cache...
        doTestGetConvertedQName(cache, "abc:hTmL", "HTML", 2, 1, "abc:HTML");

        // Should use dynamic cache...
        doTestGetConvertedQName(cache, "def:BoDy", "BODY", 3, 2, "def:BODY");

        // Should use dynamic cache and not re-add same item...
        doTestGetConvertedQName(cache, "def:boDY", "BODY", 4, 3, "def:BODY");

        // Are namespace prefixes case-sensitve? Yes they are.
        doTestGetConvertedQName(cache, "DEF:boDY", "BODY", 5, 4, "DEF:BODY");

        // Test to see if case-senstive match in cache works?
        doTestGetConvertedQName(cache, "DEF:boDY", "BODY", 5, 4, "DEF:BODY");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	323/3	byron	VBM:2003080802 Provide ConvertElementCase pipeline process - bugfixes and additional test cases

 12-Aug-03	323/1	byron	VBM:2003080802 Provide ConvertElementCase pipeline process

 ===========================================================================
*/
