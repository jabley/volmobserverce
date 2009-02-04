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

import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.testtools.application.MinimalXmlRepositoryAppConfigurator;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.config.ConfigFileBuilder;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.testtools.io.IOUtils;
import com.volantis.testtools.stubs.ServletContextStub;

import java.io.File;
import java.io.InputStream;
import javax.servlet.ServletContext;

import junitx.util.PrivateAccessor;

/**
 * Test case from MarinerServletApplication.
 */
public class MarinerServletApplicationTestCase extends TestCaseAbstract {

    /**
     * ServletContext to use with MarinerServletApplication tests.
     */
    private ServletContext context;

    /**
     * Configurator that is used to set up the ServletContext and must be
     * torn down after use.
     */
    private MinimalXmlRepositoryAppConfigurator configurator;

    /**
     * ConfigValue used by the configurator and required to tear it down.
     */
    private ConfigValue configValue;

    /**
     * Set up a ServletContext that can be used for MarinerServletApplication
     * testing.
     * @throws Exception if there is a problem when creating the configuration.
     */
    private void setUpContext() throws Exception {

        final ConfigFileBuilder builder = new ConfigFileBuilder();
        configValue = new ConfigValue();
        configurator =
                new MinimalXmlRepositoryAppConfigurator();

        configurator.setUp(configValue);

        builder.buildConfigDocument(configValue);

        context = new ServletContextStub() {
            public InputStream getResourceAsStream(String resourceURL) {
                InputStream is = null;

                if (resourceURL.endsWith(".xsd")) {
                    is = super.getResourceAsStream("/tmp/mcs-config.xsd");
                } else if (resourceURL.endsWith(".xml")) {
                    is = super.getResourceAsStream("/tmp/mcs-config.xml");
                } else {
                    throw new UndeclaredThrowableException(
                            new Exception("Cannot handle resourceURL: " +
                            resourceURL));
                }

                return is;
            }
        };
    }

    /**
     * Release any resources associated with the ServletContext that was
     * set up.
     */
    private void tearDownContext() {
        configurator.tearDown(configValue);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        setUpContext();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        tearDownContext();
    }

    /**
     * Test getInstance().
     */
    public void testGetInstance() throws Exception {
        MarinerServletApplication application =
            MarinerServletApplication.getInstance(context);
        assertNotNull("A MarinerServiceApplication instance should " +
                "have been created.", application);

        assertNotNull("Volantis should have been initialized.",
                MarinerServletApplication.findVolantisBean(context));

    }

    /**
     * Test getInstance() that can optionally create an application /
     * volantis instance.
     */
    public void testModalGetInstance() throws Throwable {
        MarinerServletApplication application =
                MarinerServletApplication.getInstance(context, false);
        assertNull("A MarinerServiceApplication instance should not " +
                "have been created.", application);

        assertNull("Volantis should not have been initialized.",
                MarinerServletApplication.findVolantisBean(context));

        application =
                MarinerServletApplication.getInstance(context, true);

        assertNotNull("A MarinerServiceApplication instance should " +
                "have been created.", application);

        assertNotNull("Volantis should have been initialized.",
                MarinerServletApplication.findVolantisBean(context));

        // Reset the context since the previous test will have changed it.
        setUpContext();

        PrivateAccessor.invoke(application,"getVolantisBean",
                new Class [] {ServletContext.class},
                new Object [] {context});

        // Test the strange but public behaviour that a
        // MarinerServletAppliction is created when the create flag is
        // false but the volantis bean already exists.
        application =
                MarinerServletApplication.getInstance(context, false);

        assertNotNull("A MarinerServiceApplication instance should " +
                "have been created.", application);
    }

    /**
     * Test that the modal version of getInstance() calls initialize() when
     * it should.
     */
    public void testModelGetInstanceInitialize() throws Throwable {
        MarinerServletApplication application =
                MarinerServletApplication.getInstance(context, true);

        assertTrue("Application should have been initialized.",
                ((Boolean) PrivateAccessor.
                getField(application, "initialized")).booleanValue());

        // Reset the context since the previous test will have changed it.
        setUpContext();

        PrivateAccessor.invoke(application,"getVolantisBean",
                new Class [] {ServletContext.class},
                new Object [] {context});

        application =
                MarinerServletApplication.getInstance(context, false);

        assertTrue("Application should have been initialized.",
                ((Boolean) PrivateAccessor.
                getField(application, "initialized")).booleanValue());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4733/1	allan	VBM:2004062105 Use a minimal device repository for Volantis.initialization

 24-Jun-04	4737/2	allan	VBM:2004062202 Restrict volantis initialization.

 ===========================================================================
*/
