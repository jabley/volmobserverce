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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import java.util.Enumeration;
import java.util.Set;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.synergetics.testtools.io.NonExistantTemporaryFileCreator;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import junitx.util.PrivateAccessor;

import javax.servlet.ServletException;
import javax.servlet.Servlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

public class ServletConfigContextTestCase extends TestCaseAbstract {
    public ServletConfigContextTestCase(String name) {
        super(name);
    }

    /**
     * Tests that file names given will be correctly resolved relative to the
     * location of the main configuration file.
     * @throws Exception if there is a problem in the test
     */
    public void testGetConfigRelativeFile() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new NonExistantTemporaryFileCreator());
        manager.executeWith(new MyTemporaryFileExecutor() {
            public void execute(File file) throws Exception {

                try {
                    file.mkdirs();
                    String testFileName = "testFile";
                    File configFile = new File(file, "mcs-config.xml");
                    configFile.createNewFile();
                    File testFile = new File(file, testFileName);
                    testFile.createNewFile();
                    File webInfDir = new File(file, "WEB-INF");
                    webInfDir.mkdirs();
                    File configFile2 = new File(webInfDir, "mcs-config.xml");
                    configFile2.createNewFile();
                    String validAbsolutePath = testFile.getAbsolutePath();
                    String invalidAbsolutePath = "/a/b/c/abc";
                    String validRelativePath = validAbsolutePath.substring(
                            validAbsolutePath.lastIndexOf(testFileName));
                    String invalidRelativePath = "a/b/c/abc";

                    ServletConfigContext scc = getInitialisedSCCInstance(
                            configFile.getAbsolutePath(), null, null);
                    scc.getMainConfigInputSource();
                    // valid absolute config path where mcs-config.xml location known
                    assertNotNull(scc.getConfigRelativeFile(
                            validAbsolutePath, true));

                    // valid relative config path where mcs-config.xml location known
                    assertNotNull(scc.getConfigRelativeFile(validRelativePath, true));

                    // invalid absolute config path where mcs-config.xml location known
                    assertNull(scc.getConfigRelativeFile(invalidAbsolutePath, true));

                    // invalid relative config path where mcs-config.xml location known
                    assertNull(scc.getConfigRelativeFile(invalidRelativePath, true));

                    FileInputStream fis = new FileInputStream(configFile);
                    scc = getInitialisedSCCInstance(null, null, fis);
                    scc.getMainConfigInputSource();

                    // valid absolute config path where mcs-config.xml location
                    // only known as resource
                    assertNotNull(scc.getConfigRelativeFile(validAbsolutePath, true));

                    // valid relative config path where mcs-config.xml location
                    // only known as resource
                    try {
                        scc.getConfigRelativeFile(validRelativePath, true);
                        fail("Should not be possible to create a File from " +
                                "relative path (" + validRelativePath +
                                ") if absolute path to mcs-config.xml unknown)");
                    } catch (ConfigurationException e) {
                        // required behaviour
                    }
                } catch (Throwable t) {
                    fail("Unexpected exception thrown: " + t.toString());
                }
            }
        });
    }

    /**
     * Tests that configuration resource identifiers are correctly resolved
     * to either an absolute resource or path, or path relative to the main
     * configuration file.
     * @throws Exception if there is a problem in the test
     */
    public void testCreateConfigInputStream() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new NonExistantTemporaryFileCreator());
        manager.executeWith(new MyTemporaryFileExecutor() {
            public void execute(File file) throws Exception {
                try {
                    file.mkdirs();
                    File configFile = new File(file, "mcs-config.xml");
                    configFile.createNewFile();

                    String LOG4JXMLFILE = "mcs-log4j.xml";
                    String realPath = file.getAbsolutePath();
                    String validAbsoluteConfigRID =
                            realPath + "/" + LOG4JXMLFILE;
                    String validRelativeToConfigRID = LOG4JXMLFILE;
                    String invalidAbsoluteResourceID =
                            "/config/1x2x/" + LOG4JXMLFILE;
                    String invalidRelativeResourceID =
                            "config/1x2x/" + LOG4JXMLFILE;
                    String validRelativeToDefaultRID = "WEB-INF/" + LOG4JXMLFILE;
                    String invalidAbsolutePath = "/a/b/c/abc";

                    // SUCCESS CASES - REAL PATH VALID
                    FileInputStream fis  = new FileInputStream(configFile);
                    ServletConfigContext scc = getInitialisedSCCInstance(
                            validAbsoluteConfigRID, realPath, fis);
                    scc.getMainConfigInputSource();
                    // valid absolute resource
                    assertSame(fis, scc.createConfigInputStream(
                            validAbsoluteConfigRID));
                    // valid relative resource
                    assertSame(fis, scc.createConfigInputStream(
                            validRelativeToConfigRID));
                    // valid file path
                    assertNotNull(scc.createConfigInputStream(
                            configFile.getAbsolutePath()));
                    // valid resource relative to webapp context
                    assertSame(fis, scc.createConfigInputStream(
                            validRelativeToDefaultRID));

                    // FAILURE CASES
                    // invalid absolute resource
                    assertNull(scc.createConfigInputStream(
                            invalidAbsoluteResourceID));
                    // invalid relative resource
                    assertNull(scc.createConfigInputStream(
                            invalidRelativeResourceID));
                    // invalid file path
                    assertNull(scc.createConfigInputStream(
                            invalidAbsolutePath));

                } catch (Throwable t) {
                    fail("Unexpected exception thrown: " + t.toString());
                }
            }
        });
    }

    /**
     *  Verifies that a configuration file is found if the id given is a valid
     * resource or file identifier, or is not present. Also tests that it fails
     * if an invalid location is supplied.
     * @throws Exception if there is a problem in the test
     */
    public void testGetMainConfigInputSource() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new NonExistantTemporaryFileCreator());
        manager.executeWith(new MyTemporaryFileExecutor() {
            public void execute(File file) throws Exception {
                try {
                    file.mkdirs();
                    String realPath = file.getAbsolutePath();

                    File configFile = new File(file, "mcs-config.xml");
                    configFile.createNewFile();

                    File webInfDir = new File(file, "WEB-INF");
                    webInfDir.mkdirs();
                    File configFile2 = new File(webInfDir, "mcs-config.xml");
                    configFile2.createNewFile();

                    String validAbsolutePath = configFile.getAbsolutePath();
                    String invalidAbsolutePath = "/a/b/c/abc";
                    String validRelativePath = validAbsolutePath.substring(
                            validAbsolutePath.lastIndexOf("/"));
                    // this should not point to an existing file!!
                    String validAbsoluteResourceID =
                            realPath + "/mcs-log4j.xml";
                    String invalidRelativePath = "a/b/c/abc";
                    String relativeResourceID = "/mcs-log4j.xml";

                    // SUCCESS CASES
                    // no init param
                    ServletConfigContext scc = getInitialisedSCCInstance(
                            null, realPath, null);
                    assertNotNull(PrivateAccessor.invoke(scc,
                            "getMainConfigInputSource",
                            new Class[]{},
                            new Object[]{}));

                    // init param is valid absolute path
                    scc = getInitialisedSCCInstance(validAbsolutePath,
                            realPath, null);
                    assertNotNull(PrivateAccessor.invoke(scc,
                            "getMainConfigInputSource",
                            new Class[]{},
                            new Object[]{}));

                    // init param is valid relative path
                    scc = getInitialisedSCCInstance(validRelativePath,
                            realPath, null);
                    assertNotNull(PrivateAccessor.invoke(scc,
                            "getMainConfigInputSource",
                            new Class[]{},
                            new Object[]{}));

                    // init param is valid absolute resource
                    FileInputStream fis = new FileInputStream(configFile);
                    scc = getInitialisedSCCInstance(
                            validAbsoluteResourceID, realPath, fis);
                    // valid absolute resource
                    assertNotNull(PrivateAccessor.invoke(scc,
                            "getMainConfigInputSource",
                            new Class[]{},
                            new Object[]{}));

                    // FAILURE CASES
                    // init param is invalid absolute file path
                    scc = getInitialisedSCCInstance(invalidAbsolutePath,
                            realPath, null);
                    try {
                        PrivateAccessor.invoke(scc,
                                "getMainConfigInputSource",
                                new Class[]{},
                                new Object[]{});
                        fail("Should not be possible to retrieve main config " +
                                "file from an invalid path - check path " +
                                "doesn't exist :" + invalidAbsolutePath);
                    } catch (ConfigurationException e) {
                        // correct behaviour
                    }

                    // init param is invalid relative file path
                    scc = getInitialisedSCCInstance(invalidRelativePath,
                            realPath, null);
                    try {
                        PrivateAccessor.invoke(scc,
                                "getMainConfigInputSource",
                                new Class[]{},
                                new Object[]{});
                        fail("Should not be possible to retrieve main config " +
                                "file from an invalid path - check path " +
                                "doesn't exist :" + invalidRelativePath);
                    } catch (ConfigurationException e) {
                        // correct behaviour
                    }

                    // init param is relative resource
                    scc = getInitialisedSCCInstance(relativeResourceID,
                    realPath, null);
                    try {
                        PrivateAccessor.invoke(scc,
                        "getMainConfigInputSource",
                        new Class[]{},
                        new Object[]{});
                        fail("Should not be possible to retrieve main config " +
                                "file from an invalid path - check path " +
                                "doesn't exist :" + invalidRelativePath);
                    } catch (ConfigurationException e) {
                        // correct behaviour
                    }

                } catch (Throwable t) {
                    fail("Unexpected exception thrown: " + t.toString());
                }
            }
        });
    }

    /**
     * Utility class to allow test cases to call the same ServletConfigContext
     * set up method
     */
    private abstract class MyTemporaryFileExecutor implements TemporaryFileExecutor{

        /**
         * Utility method to avoid code duplication in test setup
         *
         * @param configLocation
         * @param realPath value to be returned by ServletContext.getRealPath()
         * @param is value to be returned by ServletContext.getResourceAsStream()
         * @return an initialised ServletConfigContext instance
         */
        public ServletConfigContext getInitialisedSCCInstance(
                final String configLocation,
                final String realPath,
                final InputStream is)
                throws ServletException, ConfigurationException {
            TestServletContext testContext = new TestServletContext();
            testContext.setConfigLocation(configLocation);
            testContext.setResourceAsStream(is);
            testContext.setRealPath(realPath);
            ServletConfigContext scc = new ServletConfigContext(testContext);
            return scc;
        }
    }

    /**
     * Utility class which allows test cases to exercise the
     * getConfigInputStream logic without requiring an actual Servlet
     * Container
     */
    private class TestServletContext implements ServletContext {

        InputStream dummyIS = null;
        String locationOfConfigResource = "";
        String realPath = null;

        public InputStream getResourceAsStream(String s) {
            if ((locationOfConfigResource != null &&
                    locationOfConfigResource.equals(s)) ||
                    s.startsWith("/WEB-INF/")) {
                return dummyIS;
            } else {
                return null;
            }
        }

        public void setResourceAsStream(InputStream is) {
            dummyIS = is;
        }

        public void setConfigLocation(String s) {
            locationOfConfigResource = s;
        }

        public void setRealPath(String path) {
            realPath = path;
        }

        public String getRealPath(String s) {
            return realPath;
        }

        public String getInitParameter(String s) {
            if (s.equals("config.file")) {
                return locationOfConfigResource;
            } else {
                return null;
            }
        }

        // ************************************************************
        // All methods below this point have default implementations
        // ************************************************************

        public ServletContext getContext(String s) {
            return null;
        }

        public int getMajorVersion() {
            return 0;
        }

        public int getMinorVersion() {
            return 0;
        }

        public String getMimeType(String s) {
            return null;
        }

        public Set getResourcePaths(String s) {
            return null;
        }

        public URL getResource(String s) throws MalformedURLException {
            return null;
        }


        public RequestDispatcher getRequestDispatcher(String s) {
            return null;
        }

        public RequestDispatcher getNamedDispatcher(String s) {
            return null;
        }

        public Servlet getServlet(String s) throws ServletException {
            return null;
        }

        public Enumeration getServlets() {
            return null;
        }

        public Enumeration getServletNames() {
            return null;
        }

        public void log(String s) {
        }

        public void log(Exception e, String s) {
        }

        public void log(String s, Throwable throwable) {
        }

        public String getServerInfo() {
            return null;
        }

        public Enumeration getInitParameterNames() {
            return null;
        }

        public Object getAttribute(String s) {
            return null;
        }

        public Enumeration getAttributeNames() {
            return null;
        }

        public void setAttribute(String s, Object o) {
        }

        public void removeAttribute(String s) {
        }

        public String getServletContextName() {
            return null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Mar-05	7401/1	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 11-Mar-05	6842/1	emma	VBM:2005020302 Making file references in config files relative to those files

 ===========================================================================
*/
