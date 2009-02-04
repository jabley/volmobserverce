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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/AppManager.java,v 1.4 2003/04/23 13:08:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Feb-03    Geoff           VBM:2003022004 - Created.
 * 25-Feb-03    Geoff           VBM:2003022506 - Modified useAppWith to use a
 *                              ConsoleOutputManager to collect console output
 *                              and report contents of Stderr if problems 
 *                              arise during initialisation.
 * 28-Feb-03    Geoff           VBM:2003010904 - Add another explanatory 
 *                              comment.
 * 06-Mar-03    Geoff           VBM:2003101904 - Refactor to use new 
 *                              ConfigValue and ReflectionManager.
 * 25-Mar-03    Geoff           VBM:2003042306 - Use new AppExecutor and 
 *                              AppContext which allows us to access the 
 *                              context properly.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

import com.volantis.testtools.config.PluginConfigFileBuilder;
import com.volantis.testtools.config.ConfigFileBuilder;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.synergetics.testtools.Executor;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.testtools.ConsoleOutputManager;
import com.volantis.testtools.reflection.ReflectionManager;
import com.volantis.testtools.reflection.ReflectionExecutor;
import com.volantis.testtools.stubs.ServletContextStub;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.servlet.ServletConfigContext;
import com.volantis.mcs.servlet.ServletExternalPathToInternalURLMapper;
import com.volantis.mcs.repository.RepositoryException;

import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;

import junit.framework.Assert;

import javax.servlet.ServletException;

import junitx.util.PrivateAccessor;

/**
 * A class to manage the initialisation of Mariner applications (aka Volantis).
 * <p>
 * Uses the Command pattern to allow clients to provide executors which run
 * within the context a valid Volantis.
 */
public class AppManager {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    private Volantis volantis;
    
    private ServletContextStub servletContext;
    
    private AppConfigurator appConf;
    
    private ConsoleOutputManager consoleOutput;
    
    private boolean expectSuccess;

    private ConfigValue configValue;
    
    private ConfigFileBuilder configFileBuilder;

    public AppManager(Volantis volantis, ServletContextStub servletContext) {
        this.volantis = volantis;
        this.servletContext = servletContext;
        this.appConf = new DefaultAppConfigurator();
        this.consoleOutput = new ConsoleOutputManager();
        this.expectSuccess = true;
        this.configFileBuilder = new ConfigFileBuilder();
    }

    public void setAppConf(AppConfigurator appConf) {
        this.appConf = appConf;
    }

    public void setExpectSuccess(boolean expectSuccess) {
        this.expectSuccess = expectSuccess;
    }

     /**
      * Register a plugin configuration builder with the ConfigFileBuilder,
      * using the pluginConfigValueClass as the key.
      *
      * @param pluginConfigFileBuilder The builder
      * @param pluginConfigValueClass The pluginConfigValueClass
      */
     public void registerPluginConfigFileBuilder(
         PluginConfigFileBuilder pluginConfigFileBuilder,
         Class pluginConfigValueClass) {

             configFileBuilder.registerPluginBuilder(pluginConfigFileBuilder,
                 pluginConfigValueClass);
      }

    public void useAppWith(final AppExecutor executor) throws Exception {
        configValue = null;


        ReflectionManager reflector = new ReflectionManager(
                com.volantis.mcs.runtime.Volantis.class.getDeclaredField(
                        "initializationComplete"));
        // Set up for introspection...
        reflector.useAsAccessible(new ReflectionExecutor() {
            public Object execute(AccessibleObject object)
                    throws Exception {
                Field complete = (Field) object;
                // Ensure Volantis has not already been initialised.
                Assert.assertTrue(complete + " should not be true",
                        !complete.getBoolean(volantis));

                // Initialise Volantis.
                configValue = new ConfigValue();
                // Set up the configuration
                appConf.setUp(configValue);
                try {
                    configFileBuilder.buildConfigDocument(configValue);
                    // @todo NO POINT TO PASS IN SERVLET CONTEXT HERE
                    // all we are doing is setting the context path -
                    // if we passed in the real ServletContext, then
                    // we could create the config file in the real
                    // path provided by that ServletContext, this would
                    // be both more useful and more portable than what
                    // is here currently.
                    servletContext.setRealPath(
                            configFileBuilder.getConfigFileDir());
                    servletContext.setMCSConfigFile("/tmp/mcs-config.xml");
                    // Collect System.err output
                    consoleOutput.useCollectionWith(new Executor() {
                        public void execute() throws Exception {
                            synchronized(servletContext) {
                                ConfigContext cc = new ServletConfigContext(servletContext);

                                ServletExternalPathToInternalURLMapper mapper =
                                        new ServletExternalPathToInternalURLMapper(servletContext);
                                volantis.initializeInternal(mapper, cc,
                                        new MarinerServletApplication());
                                servletContext.setAttribute("volantis",
                                        volantis);
                            }
                        }
                    });
                } finally {
                    // Ensure the configuration is cleaned up.
                    appConf.tearDown(configValue);
                }

                // Check using introspection that Volantis
                // initialisation succeeded.
                boolean success = complete.getBoolean(volantis);
                String err = consoleOutput.getErr();
                if (expectSuccess) {
                    // Check for unexpected initialisation failure.
                    // Failures are reported via the log if set up or
                    // else via stderr.
                    // Here we can collect stderr, but there is no
                    // guaranteed way to collect log output (since it
                    // is configurable).
                    // The JUnit XML report files also contain stderr
                    // and stdout, but not really in a useful fashion,
                    // and they are not included in the HTML reports
                    // by default.
                    Assert.assertTrue(
                            "Volantis initialisation failure " +
                            "(check the log/stdout/stderr): " +
                            "Stderr was:\n" + err, success);
                } else {
                    // Check for unexpected success
                    Assert.assertTrue("Volantis initialisation success"
                            , !success);
                }

                try {
                    // Execute the user's code, providing some context so
                    // that it can see a bit about why things are the way
                    // they are.
                    executor.execute(new AppContext() {
                        public ConsoleOutputManager getConsoleOutput() {
                            return consoleOutput;
                        }

                        public ConfigValue getConfigValue() {
                            return configValue;
                        }
                    });
                } finally {
                    // Need to shut down volantis or if we are running with a
                    // JDBC repository we leave a connection pool running.
                    volantis.shutdown();
                }

                return null;
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-May-05	8200/2	trynne	VBM:2005050412 Moved classes from oldtests to testtools-runtime and added testtools-runtime classes into testtools.jar so that MPS need only depend on testtools

 11-Mar-05	6842/1	emma	VBM:2005020302 Making file references in config files relative to those files

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/2	ianw	VBM:2004090605 New Build system

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 24-Jun-04	4737/1	allan	VBM:2004062202 Restrict volantis initialization.

 31-Oct-03	1593/3	mat	VBM:2003101502 Adding pluginconfig to test file build

 31-Oct-03	1593/1	mat	VBM:2003101502 Adding pluginconfigvalue

 23-Oct-03	1585/3	mat	VBM:2003101502 Add plugin config builders to ConfigFileBuilder

 13-Oct-03	1517/3	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 10-Jun-03	356/1	allan	VBM:2003060907 Moved some common testtools into Synergetics

 ===========================================================================
*/
