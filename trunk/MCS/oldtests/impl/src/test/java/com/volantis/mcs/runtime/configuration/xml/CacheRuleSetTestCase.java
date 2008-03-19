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

package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.CacheConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;

import java.util.Iterator;

/**
 * Test the reading of the cache configuration.
 */
public class CacheRuleSetTestCase extends TestCaseAbstract {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    public CacheRuleSetTestCase(String s) {
        super(s);
    }

    /**
     * Test with a null value object
     */
    public void testNull() throws ConfigurationException {
        doTest((CacheValue)null);
    }

    /**
     * Test with an empty value object
     */
    public void testEmpty() throws ConfigurationException {
        CacheValue value = new CacheValue();
        // mandatory
        value.name = "";
        value.maxEntries = "1"; // 0 is invalid.
        // default
        value.strategy = "least-recently-used";
        value.maxAge = "unlimited";
        doTest(value);
    }

    /**
     * Test with all fields set in the value object
     */
    public void testFull() throws ConfigurationException {
        doTest(new CacheValue("Cache Name",
                              "least-used",
                              "1234",
                              "86400"));
    }

    /**
     * Test with all fields set in multiple value objects
     */
    public void testMultiple() throws ConfigurationException {
        CacheValue list[] = {
            new CacheValue("Name1", "least-used", "1", "86400"),
            new CacheValue("Name2", "least-recently-used", "2", "86400"),
            new CacheValue("Name3", "least-used", "3", "86400"),
            new CacheValue("Name4", "least-recently-used", "4", "86400"),
            new CacheValue("N4me5", "least-used", "5", "86400"),
        };
        doTest(list);
    }

    /**
     * Utility method used by createXMLCache to add values to the xml document.
     */
    private void addValue(StringBuffer buffer, Object value, String description) {
        if (value != null) {
            buffer.append("     ").append(description).append("=\"");
            buffer.append(value).append("\" \n");
        }
    }

    /**
     * Utility method to create a xml configuration file.
     */
    private StringBuffer createXMLCache(CacheValue[] values) {
        StringBuffer doc =
            new StringBuffer(
                   " <pipeline-configuration> \n" +
                   "    <caching-operation> \n");

        for (int i = 0; i < values.length; i++) {
            CacheValue value = values[i];
            if (value != null) {
                doc.append("      <cache \n");
                addValue(doc, value.name, "name");
                addValue(doc, value.strategy, "strategy");
                addValue(doc, value.maxEntries, "max-entries");
                addValue(doc, value.maxAge, "max-age");
                doc.append("      /> \n");
            }
        }
        doc.append("    </caching-operation> \n");
        doc.append(" </pipeline-configuration> \n");

        return doc;
    }

    /**
     * Create a subset of the mcs-config XML document from the values
     * supplied, parse it into a {@link CacheConfiguration} object,
     * and ensure that the values supplied are match those found.
     *
     * @param value
     * @throws ConfigurationException
     */
    public void doTest(CacheValue value) throws ConfigurationException {
        doTest(new CacheValue[]{value});
    }

    /**
     * Create a subset of the mcs-config XML document from the values
     * supplied, parse it into a {@link CacheConfiguration} object,
     * and ensure that the values supplied are match those found.
     *
     * @param values
     * @throws ConfigurationException
     */
    public void doTest(CacheValue[] values) throws ConfigurationException {
        StringBuffer doc = createXMLCache(values);

        TestXmlConfigurationBuilder configBuilder =
            new TestXmlConfigurationBuilder(doc.toString());
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        Iterator cacheList = config.getPipelineConfiguration().
            getCacheOperationConfiguration().getCacheConfigurations();
        if (values != null) {
            assertNotNull("cacheConfiguration", cacheList);
            int cacheSize = 0;
            int nonNullInputSize = 0;
            for (int i = 0; i < values.length; i++) {
                CacheValue value = values[i];
                if (value != null) {
                    ++nonNullInputSize;
                }
                if (cacheList.hasNext()) {
                    CacheConfiguration cacheConfiguration =
                        (CacheConfiguration) cacheList.next();
                    checkValue(values[i], cacheConfiguration);
                    ++cacheSize;
                }
            }
            assertEquals("Cache size mismatch", cacheSize, nonNullInputSize);

        } else {
            assertTrue("cacheConfiguration", !cacheList.hasNext());
        }
    }

    /**
     * Check that the cache values read match those expected.
     */
    private void checkValue(CacheValue value, CacheConfiguration cache) {
        assertEquals(value.name, cache.getName());
        assertEquals(value.strategy, cache.getStrategy());
        assertEquals(value.maxEntries, cache.getMaxEntries());
        assertEquals(value.maxAge, cache.getMaxAge());
    }


    /**
     * A private Value Object class for holding mariner agent values.
     */
    private class CacheValue {
        String name;
        String strategy;
        String maxEntries;
        String maxAge;

        public CacheValue() {

        }

        public CacheValue(String name,
                          String strategy,
                          String maxEntries,
                          String maxAge) {
            this.name = name;
            this.strategy = strategy;
            this.maxEntries = maxEntries;
            this.maxAge = maxAge;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 02-Jul-03	478/1	byron	VBM:2003061401 Unfinished issues of 2003060403

 ===========================================================================
*/
