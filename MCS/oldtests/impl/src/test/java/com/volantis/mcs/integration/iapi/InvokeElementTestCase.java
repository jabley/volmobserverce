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
 
package com.volantis.mcs.integration.iapi;

import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.integration.AbstractMarkupPlugin;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.TestableVolantis;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ArgumentConfiguration;
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;
import com.volantis.mcs.testtools.application.AppContext;
import com.volantis.mcs.testtools.application.AppExecutor;
import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.mcs.testtools.application.MinimalXmlRepositoryAppConfigurator;
import com.volantis.synergetics.BooleanWrapper;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.testtools.stubs.ServletContextStub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class tests InvokeElement.
 */
public class InvokeElementTestCase extends TestCaseAbstract {

    /**
     * This is nasty but we have to have a member field to be able to
     * determine that MarkupPlugin initialize method has been invoked.
     */
    private static BooleanWrapper initializeTestSuccess = new BooleanWrapper(false);

    /**
     * This is nasty but we have to have a member field to be able to
     * determine that MarkupPlugin process method has been invoked.
     */
    private static BooleanWrapper processTestSuccess = new BooleanWrapper(false);

    /**
     * This is nasty but we have to have a member field to be able to
     * determine that MarkupPlugin release method has been invoked.
     */
    private static BooleanWrapper releaseTestSuccess = new BooleanWrapper(false);

    /**
     * Create a new instance of InvokeElementTestCase
     * @param name The name of the testcase.
     */ 
    public InvokeElementTestCase(String name) {
        super(name);
    }

    // javadoc inherited.
    protected void setUp() throws Exception {
        initializeTestSuccess.setValue(false);
        processTestSuccess.setValue(false);
        releaseTestSuccess.setValue(false);
    }
    
    /**
     * Test doPluginInitialize.     
     */ 
    public void testDoPluginInitialize() throws Exception {
        final BooleanWrapper success = new BooleanWrapper(false);
        final HashMap argMap = new HashMap();
        
        MarkupPlugin plugin = new AbstractMarkupPlugin() {
            // javadoc inherited from superclass.
            public void initialize(Map arguments) {
                success.setValue(true);
                assertSame("Unexpected value for arguments.", 
                        argMap, arguments);
            }
        };
        
        InvokeElement invoke = new InvokeElement();
        invoke.doPluginInitialize(plugin, argMap, "name");
        
        assertTrue("MarkupPlugin.initialize should have been invoked.",
                success.getValue());
    }

    /**
     * Test the method doPluginProcess.     
     */ 
    public void testDoPluginProcess() throws Exception {
        final BooleanWrapper success = new BooleanWrapper(false);
        final HashMap argMap = new HashMap();
        final TestMarinerRequestContext requestContext = 
                new TestMarinerRequestContext();

        MarkupPlugin plugin = new AbstractMarkupPlugin() {
            // javadoc inherited from superclass.
            public void process(MarinerRequestContext context, Map arguments) {
                success.setValue(true);
                assertSame("Unexpected value for arguments.",
                        argMap, arguments);
                assertSame("Unexpected value for context.", 
                        requestContext, context);
            }
        };

        InvokeElement invoke = new InvokeElement();
        invoke.doPluginProcess(requestContext, plugin, argMap, "name");

        assertTrue("MarkupPlugin.process should have been invoked.",
                success.getValue());    
    }

    /**
     * Test the method doPluginRelease     
     */ 
    public void testDoPluginRelease() throws Exception {
        final BooleanWrapper success = new BooleanWrapper(false);        

        MarkupPlugin plugin = new AbstractMarkupPlugin() {
            // javadoc inherited from superclass.
            public void release() {
                success.setValue(true);
            }
        };

        InvokeElement invoke = new InvokeElement();
        invoke.doPluginRelease(plugin, "name");

        assertTrue("MarkupPlugin.initialize should have been invoked.",
                success.getValue());    
    }

    /**
     * Test the method elementStart.     
     */ 
    public void testElementStart() throws Exception {
        // Set up the contexts
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        ContextInternals.setMarinerPageContext(requestContext, pageContext);

        InvokeElement element = new InvokeElement();        
        
        int result = element.elementStart(requestContext, null);

        assertEquals("Unexpected result from elementStart.",
                IAPIConstants.PROCESS_ELEMENT_BODY, result);
        
        assertSame("InvokeElement should have been pushed onto the stack.",
                element, pageContext.peekIAPIElement());
    }

    /**
     * Test that we can invoke {@link com.volantis.mcs.integration.MarkupPlugin#initialize} from 
     * {@link com.volantis.mcs.integration.iapi.InvokeElement#elementEnd}     
     */ 
    public void testElementEndInvokeInitialize() throws Exception {
        doTestElementEnd("initialize");
        assertTrue("Expected initialize method to be invoked.",
                initializeTestSuccess.getValue());
    }
    
    /**
     * Test that we can invoke {@link com.volantis.mcs.integration.MarkupPlugin#process} from 
     * {@link com.volantis.mcs.integration.iapi.InvokeElement#elementEnd}     
     */ 
    public void testElementEndInvokeProcess() throws Exception{
        doTestElementEnd("process");
        assertTrue("Expected process method to be invoked.",
                processTestSuccess.getValue());
    }
    
    /**
     * Test that we can invoke {@link com.volantis.mcs.integration.MarkupPlugin#release} from 
     * {@link com.volantis.mcs.integration.iapi.InvokeElement#elementEnd}     
     */ 
    public void testElementEndInvokeRelease() throws Exception {
        doTestElementEnd("release");
        assertTrue("Expected release method to be invoked.",
                releaseTestSuccess.getValue());
    }
    
    /**
     * Test the method elementEnd.     
     */ 
    public void doTestElementEnd(String invokeMethod) throws Exception {
        Volantis volantis = new TestableVolantis();
        ServletContextStub contextStub = new ServletContextStub();
        
        AppManager appManager = new AppManager(volantis, contextStub);
        appManager.setAppConf(new MinimalXmlRepositoryAppConfigurator() {
            public void setUp(ConfigValue config) throws Exception {
                super.setUp(config);

                ArgumentConfiguration ac = new ArgumentConfiguration();
                ac.setName("argName");
                ac.setValue("argValue");
                ArrayList argsList = new ArrayList(1);
                argsList.add(ac);
                
                MarkupPluginConfiguration mpc = 
                        new MarkupPluginConfiguration();
                mpc.setName("myPlugin");
                mpc.setClassName("com.volantis.mcs.integration.iapi." +
                        "InvokeElementTestCase$InvokeTestMarkupPlugin");
                mpc.setScope("application");                
                ArrayList pluginList = new ArrayList(1);
                pluginList.add(mpc);
                
                config.markupPlugins = pluginList;
            }
        });
        
        AppExecutor executor = new InvokeAppExecutor(volantis, invokeMethod);
        appManager.useAppWith(executor);
    }

    /**
     * AppExecutor to all us to test MarkupPlugin invocation.
     */ 
    protected class InvokeAppExecutor implements AppExecutor {
        
        /**
         * The MarkupPlugin method we wish to invoke.
         */ 
        private String invokeMethod;
        
        /**
         * This is a bit hacky but we have to pass in our volantis instance
         * to be able to set it on our TestMarinerPageContext.
         */ 
        private Volantis volantis;
        
        /**
         * Create a new InvokeAppExecutor with the name of the method we want
         * to invoke on the MarkupPlugin.
         * @param invokeMethod The name of the method to invoke.
         * @param volantis The instance of Volantis to set on our
         * TestMarinerPageContext.
         */ 
        public InvokeAppExecutor(Volantis volantis, String invokeMethod) {
            this.invokeMethod = invokeMethod;
            this.volantis = volantis;
        }
        
        // javadoc inherited from interface.
        public void execute(AppContext context) throws Exception {
            // Set up the contexts

            // Connect application to volantis bean.
            MarinerApplication application = new MarinerApplication() {};
            ApplicationInternals.setVolantisBean(application, volantis);

            // Connect request to application.
            TestMarinerRequestContext requestContext =
                    new TestMarinerRequestContext();
            ContextInternals.setMarinerApplication(requestContext,
                                                   application);

            // Connect page to application and request.
            TestMarinerPageContext pageContext =
                    new TestMarinerPageContext();
            pageContext.setVolantis(volantis);
            ContextInternals.setMarinerPageContext(
                    requestContext, pageContext);

            InvokeElement element = new InvokeElement();
            element.elementStart(requestContext, null);

            InvokeAttributes attrs = new InvokeAttributes();
            attrs.setMethodName(invokeMethod);
            attrs.setName("myPlugin");

            element.elementEnd(requestContext, attrs);
        }
    }

    /**
     * Test implementation of MarkupPlugin.
     */ 
    public static class InvokeTestMarkupPlugin implements MarkupPlugin {
        
        /**
         * Public no-arg constructor.
         */ 
        public InvokeTestMarkupPlugin() {            
        }
        
        // javadoc inherited from MarkupPlugin interface.
        public void initialize(Map arguments) {
            initializeTestSuccess.setValue(true);
        }

        // javadoc inherited from MarkupPlugin interface.
        public void process(MarinerRequestContext context, Map arguments) {
            processTestSuccess.setValue(true);
        }

        // javadoc inherited from MarkupPlugin interface.
        public void release() {
            releaseTestSuccess.setValue(true);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 18-Aug-03	1146/1	geoff	VBM:2003042305 fix merge problems

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
