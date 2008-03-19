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

import junitx.util.PrivateAccessor;

import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.drivers.web.ScriptFilter;
import com.volantis.xml.pipeline.sax.drivers.web.SimpleScriptModule;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfigurationImpl;
import com.volantis.xml.pipeline.sax.proxy.DefaultProxy;
import com.volantis.xml.pipeline.sax.proxy.Proxy;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Test case for the web driver configuration.
 */
public class WebDriverRuleSetTestCase extends TestCaseAbstract {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Test the WebDriverRuleSet
     */
    public WebDriverRuleSetTestCase(String string) {
        super(string);
    }

    /**
     * Test the adding of rule instances for the the WebDriverConfiguration.
     */
    public void testAddRuleInstances() throws Exception {
        doTest(null);

        MockWebDriverConfiguration config = new MockWebDriverConfiguration();
        doTest(config);
    }

    /**
     * Test the adding of rule instances for the the WebDriverConfiguration.
     */
    public void testWebDriverTimeout() throws Exception {
        doTest(null);

        MockWebDriverConfiguration config = new MockWebDriverConfiguration();
        config.setTimeout(100);
        doTest(config);
    }

    /**
     * Test the adding of rule instances for the Proxy WebDriverConfiguration.
     */
    public void testAddProxyRuleInstances() throws Exception {
        MockWebDriverConfiguration config = new MockWebDriverConfiguration();
        config.putProxy(new MockProxy("id", "localhost", 8080));
        doTest(config);

    }

    /**
     * Test the adding of rule instances for the Proxy WebDriverConfiguration.
     */
    public void testAddProxiesRuleInstances() throws Exception {
        MockWebDriverConfiguration config = new MockWebDriverConfiguration();
        config.putProxy(new MockProxy("id", "localhost", 8080));
        doTest(config);

        config.putProxy(new MockProxy("id2", "anotherLocalhost", 8081));
        doTest(config);
    }

    private void updateScriptModule (MockScriptModule scriptModule,
                                     String className,
                                     String contentType) throws Exception {

        ScriptFilter scriptFilter = new ScriptFilter();
        scriptFilter.setContentType(contentType);
        scriptFilter.setScriptClass(Class.forName(className));
        scriptModule.putScriptFilter(scriptFilter);
    }

    /**
     * Test the adding of rule instances for the Script WebDriverConfiguration.
     */
    public void testAddScriptModuleRuleInstances() throws Exception {

        MockWebDriverConfiguration config = new MockWebDriverConfiguration();
        final MockScriptModule scriptModule = new MockScriptModule("moduleID");

        String className = "com.volantis.xml.pipeline.sax.XMLPipelineFilterAdapter";
        updateScriptModule(scriptModule, className, "contentType");
        config.putScriptModule(scriptModule);
        doTest(config);
    }

    /**
     * Test the adding of rule instances for the Script WebDriverConfiguration.
     */
    public void testAddScriptModulesRuleInstances() throws Exception {

        MockWebDriverConfiguration config = new MockWebDriverConfiguration();
        final MockScriptModule scriptModule = new MockScriptModule("moduleID");

        String className = "com.volantis.xml.pipeline.sax.XMLPipelineFilterAdapter";
        updateScriptModule(scriptModule, className, "contentType");
        config.putScriptModule(scriptModule);
        doTest(config);

        updateScriptModule(scriptModule, className, "contentType2");
        config.putScriptModule(scriptModule);
        doTest(config);
    }

    /**
     * Utility method for creating a dummy xml configuration cache file.
     *
     * @param mock an object encapsulating test values for the configuration
     * @return a StringBuffer representing an xml configuratino file.
     */
    private StringBuffer createXMLCache(MockWebDriverConfiguration mock) {
        StringBuffer doc = new StringBuffer();
        doc.append("<pipeline-configuration>");
        if (mock != null) {
            doc.append("<web-driver");
            if (mock.getTimeoutInMillis() != -1) {
                doc.append(" connection-timeout='")
                        .append(mock.getTimeoutInMillis())
                        .append("'");
            }
            doc.append(">");    
            doc.append(mock.getProxyConfigBuffer());
            doc.append(mock.getScriptConfigBuffer());
            doc.append("</web-driver>");
        }

        doc.append("</pipeline-configuration>");

        //System.out.println(doc.toString());
        return doc;
    }

    /**
     * Do the test.
     *
     * @param mock the mock configuration object.
     */
    private void doTest(MockWebDriverConfiguration mock) throws Exception {
        StringBuffer doc = createXMLCache(mock);

        String input = doc.toString();
        //System.out.println(input);

        TestXmlConfigurationBuilder configBuilder =
            new TestXmlConfigurationBuilder(input);

        MarinerConfiguration actualConfig = configBuilder.buildConfiguration();
        assertNotNull(actualConfig);

        final WebDriverConfiguration config =
            actualConfig.getPipelineConfiguration().getWebDriverConfiguration();


        if (mock != null) {
            assertNotNull("Actual configuration should not be null",
                          config);

            Map expectedMap = (Map)getPrivateObject(mock, "proxies");
            Map actualMap = (Map)getPrivateObject(
                actualConfig.getPipelineConfiguration().
                    getWebDriverConfiguration(),
                "proxies");

            String result = verifyContentsOfMap(expectedMap, actualMap);
            if (result != null) {
                fail(result);
            }

            expectedMap = (Map)getPrivateObject(mock, "scriptModules");
            actualMap = (Map)getPrivateObject(
                actualConfig.getPipelineConfiguration().
                    getWebDriverConfiguration(),
                "scriptModules");
            result = verifyContentsOfMap(expectedMap, actualMap);
            if (result != null) {
                fail(result);
            }
        } else {
            assertNull("There should be no items in the dataSource list", config);
        }
    }

    /**
     * Verify the contents of two Maps match.
     *
     * @param expectedMap the expected map.
     * @param actualMap   the actual map.
     * @return a string containing an error message or null if no error occurred.
     */
    private String verifyContentsOfMap(Map expectedMap, Map actualMap) {
        String result = null;
        if ((expectedMap == null) && (actualMap == null)) {
            // Leave as null.
        } else if ((expectedMap != null) && (actualMap != null)) {
            if (expectedMap.size() != actualMap.size()) {
                result = "Map sizes do not match. Expected " +
                    expectedMap.size() + " but was " +
                    actualMap.size();
            } else {
                Set keySet = expectedMap.keySet();
                Iterator iterator = keySet.iterator();

                while ((result == null) && iterator.hasNext()) {
                    String key = (String) iterator.next();
                    Object expected = expectedMap.get(key);
                    Object actual = actualMap.get(key);
                    if (actual == null) {
                        result = "Could not find expected key in actual map: " +
                            "'" + key + "'";
                    } else if ((expected instanceof Proxy) ) {
                        result = checkProxyMatch(expected, actual);
                    } else if (expected instanceof SimpleScriptModule) {
                        result = checkScriptModuleMatches(expected, actual);
                    }
                }
            }
        } else {
            result = "Expected is null and actual isn't (or vice versa)";
        }
        return result;
    }

    /**
     * Check that the script module object matches the actual result read in.
     *
     * @param expected the expected object.
     * @param actual   the actual object.
     * @return null if the objects match, or a string containing a desription of
     *         the failure.
     */
    private String checkScriptModuleMatches(Object expected, Object actual) {
        String result = null;
        if (!(actual instanceof SimpleScriptModule)) {
            result = "Objects do not match. Expected: " + expected +
                " vs actual: " + actual;
        } else {
            SimpleScriptModule eScript = (SimpleScriptModule) expected;
            SimpleScriptModule aScript = (SimpleScriptModule) actual;
            if (!eScript.getId().equals(aScript.getId())) {
                result = "Script module id value does not match";
            } else {
                Map expectedFilters = (Map)getPrivateObject(eScript, "scriptFilters");
                Map actualFilters = (Map)getPrivateObject(aScript, "scriptFilters");
                if ((expectedFilters == null) && (actualFilters == null)) {
                    // ignore
                } else if ((expectedFilters != null) && (actualFilters != null)) {
                    if (expectedFilters.size() != actualFilters.size()) {
                        result = "Script filter value does not match " +
                            "expected: " + expectedFilters.size() + " actual: " +
                            actualFilters.size();
                    } else {
                        Set keySet = expectedFilters.keySet();
                        for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                            String key = (String) iterator.next();
                            if (actualFilters.get(key) == null) {
                                result = "Key exists in expected, not in actual " +
                                    "result: " + key;
                            } else {
                                ScriptFilter actualFilter =
                                    (ScriptFilter)actualFilters.get(key);
                                ScriptFilter expectedFilter =
                                    (ScriptFilter)expectedFilters.get(key);
                                if (!checkFilterMatches(expectedFilter,
                                                        actualFilter)) {
                                    result = "Script filter value does not match " +
                                        "expected: " + expectedFilter + " actual: " +
                                        actualFilter;
                                }
                            }
                        }
                    }
                } else {
                    result = "Script filter value does not match " +
                        "(one value is null) expected: " +
                        expectedFilters + " actual: " +
                        actualFilters;
                }
            }
        }
        return result;
    }

    private boolean checkFilterMatches(ScriptFilter expectedFilter,
                                       ScriptFilter actualFilter) {

        boolean result = true;
        final String aContentType = actualFilter.getContentType();
        final String eContentType = expectedFilter.getContentType();
        final Class aScriptClass = actualFilter.getScriptClass();
        final Class eScriptClass = expectedFilter.getScriptClass();
        if ((aContentType == null && eContentType != null) ||
            (eContentType == null && aContentType != null) ||
            (aScriptClass == null && eScriptClass != null) ||
            (eScriptClass == null && aScriptClass != null) ||
            !aContentType.equals(eContentType) ||
            aScriptClass != eScriptClass) {
            result = false;
        }
        return result;
    }

    /**
     * Check that the proxy module object matches the actual result read in.
     *
     * @param expected the expected object.
     * @param actual   the actual object.
     * @return null if the objects match, or a string containing a desription of
     *         the failure.
     */
    private String checkProxyMatch(Object expected, Object actual) {
        String result = null;
        if (!(actual instanceof Proxy)) {
            result = "Objects do not match. Expected: " + expected +
                " vs actual: " + actual;
        } else {
            Proxy eProxy = (Proxy) expected;
            Proxy aProxy = (Proxy) actual;
            if (!eProxy.getId().equals(aProxy.getId())) {
                result = "Proxy id value does not match";
            } else if (!(eProxy.getPort() == aProxy.getPort())) {
                result = "Proxy port value does not match";
            } else if ((eProxy.getHost() != null) &&
                (aProxy.getHost() != null) &&
                !eProxy.getHost().equals(aProxy.getHost())) {
                result = "Proxy host value does not match";
            } else if (((aProxy.getHost() == null) ||
                (eProxy.getHost() == null))) {
                if (aProxy.getHost() != eProxy.getHost()) {
                    result = "Proxy host value does not match " +
                        "(one value is null) expected: " +
                        eProxy.getHost() + " actual: " +
                        aProxy.getHost();
                }
            }
        }
        return result;
    }

    /**
     * Get a private field from the specified object.
     *
     * @param objToExamine the object to examine for the field.
     * @param field        the field name.
     * @return the field, or null if it couldn't be found.
     */
    private Object getPrivateObject(Object objToExamine, String field) {
        Object result = null;
        try {
            result = PrivateAccessor.getField(objToExamine, field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Mock implementation for the WebDriverConfiguration object.
     */
    private class MockWebDriverConfiguration
                extends WebDriverConfigurationImpl {

        public String getProxyConfigBuffer() {
            StringBuffer result = new StringBuffer();
            Map proxies = (Map)getPrivateObject(this, "proxies");
            if (proxies != null && proxies.size() > 0) {
                Set keys = proxies.keySet();
                for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
                    Proxy proxy = (Proxy) proxies.get(iterator.next());
                    if (proxy != null) {
                        result.append("<proxy ");
                        String value;
                        if ((value = proxy.getId()) != null) {
                            result.append(" id=\"" + value + "\"");
                        }
                        result.append(" port=\"" + proxy.getPort() + "\"");

                        if ((value = proxy.getHost()) != null) {
                            result.append(" host=\"" + value + "\"");
                        }
                        result.append("></proxy>");
                    }
                }
            }
            return result.toString();
        }

        /**
         * Get the script configuration buffer as a string.
         * @return  the script configuration buffer as a string.
         */
        public String getScriptConfigBuffer() {
            StringBuffer result = new StringBuffer();
            Map scripts = (Map)getPrivateObject(this, "scriptModules");
            if (scripts != null && scripts.size() > 0) {
                Set keys = scripts.keySet();
                result.append("<script>");
                for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
                    SimpleScriptModule scriptModule = (SimpleScriptModule)
                        scripts.get(iterator.next());
                    if (scriptModule != null) {
                        result.append("<module");
                        Object value;
                        if ((value = scriptModule.getId()) != null) {
                            result.append(" id=\"" + value + "\"");
                        }
                        result.append(">");
                        Map scriptFilters = (Map)getPrivateObject(scriptModule,
                                                                  "scriptFilters");
                        if (scriptFilters != null) {
                            Set keySet = scriptFilters.keySet();
                            for (iterator = keySet.iterator(); iterator.hasNext();) {
                                String key = (String) iterator.next();
                                ScriptFilter filter = (ScriptFilter)scriptFilters.get(key);
                                result.append("<filter content-type=\"");
                                result.append(key).append("\"");;
                                result.append(" class=\"");
                                result.append(filter.getScriptClass().getName());
                                result.append("\"");
                                result.append("/>");
                            }
                        }
                        result.append("</module>");
                    }

                }
                result.append("</script>");
            }
            return result.toString();
        }
    }

    /**
     * Mock object for the Script.
     *
     *  <script>
     *      <module id='id_of_this_module'>
     *          <filter content-type="content_type_of_parent_document"
     *              class='class_implementing_script_filter'/>
     *          <filter content-type='content_type_of_parent_document'>
     *              class='another_class_implementing_script_filter'/>
     *      </module>
     * </script>
     */
    private class MockScriptModule extends SimpleScriptModule {

        public MockScriptModule(String id) {
            setId(id);
        }
    }

    /**
     * Mock object for the Proxy.
     */
    private class MockProxy extends DefaultProxy {

        public MockProxy(String id, String host, int port) {
            setId(id);
            setHost(host);
            setPort(port);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/2	matthew	VBM:2005092809 Allow proxy configuration via system properties

 01-Jun-05	8627/1	tom	VBM:2005052502 Added connection-timeout to mcs-properties.xml

 31-May-05	8611/1	tom	VBM:2005052502 Added connection-timeout to mcs-config.xml

 01-Apr-05	6798/3	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jul-04	4707/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 06-Aug-03	921/3	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy - rework issue

 05-Aug-03	921/1	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 ===========================================================================
*/
