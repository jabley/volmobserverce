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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps;

import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.testtools.application.AppContext;
import com.volantis.mcs.testtools.application.AppExecutor;
import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.synergetics.testtools.Executor;
import com.volantis.synergetics.testtools.HypersonicManager;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.config.MPSAppConfigurator;
import com.volantis.testtools.config.MPSPluginConfigBuilder;
import com.volantis.testtools.config.MPSPluginConfigValue;
import com.volantis.testtools.config.PluginConfigFileBuilder;
import com.volantis.testtools.stubs.ServletContextStub;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * This class is for tests requiring the AppManager and HyperSonicManager in
 * order to run.
 * @todo  Maybe this should be just a 'helper' class and not extend TestCase
 * If I had the time....
 *
 * @author mat
 *
 */
public class MPSTestAbstract extends TestCaseAbstract {

    protected HypersonicManager hypersonicManager;

    protected Volantis volantisBean = null;

    protected Connection conn;

    protected String messageRecipientInfo = null;

    public MPSTestAbstract(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        hypersonicManager =
                new HypersonicManager(HypersonicManager.IN_MEMORY_SOURCE);
    }

    public void tearDown() throws Exception {
        volantisBean = null;
        if (!conn.isClosed()) {
            conn.close();
        }
        conn = null;
        hypersonicManager = null;
        super.tearDown();
    }

    public String getMessageRecipientInfo() {
        return messageRecipientInfo;
    }

    public void setMessageRecipientInfo(String messageRecipientInfo) {
        this.messageRecipientInfo = messageRecipientInfo;
    }

    /**
     * Run the given executor inside the AppManager.
     *
     * @param executor The executor to run
     * @throws Exception
     */
    public void runWithAppManager(AppExecutor executor) throws Exception {
        final MPSPluginConfigValue mps = new MPSPluginConfigValue();
        if (messageRecipientInfo != null) {
            mps.messageRecipientInfo = messageRecipientInfo;
        }
        MPSAppConfigurator appConf = new MPSAppConfigurator();

        appConf.setPluginConfigValue(mps);
        // Set appConf in AppManager
        volantisBean = new Volantis();
        ServletContextStub servletContext = new ServletContextStub();
        AppManager am = new AppManager(volantisBean, servletContext);
        am.setAppConf(appConf);
        PluginConfigFileBuilder builder = new MPSPluginConfigBuilder();
        am.registerPluginConfigFileBuilder(builder, mps.getClass());
        am.useAppWith(executor);
    }



    /** Build a HSQL repository */
    protected void createRepository() throws Exception {

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println(
                "Unable to load org.hsqldb.jdbcDriver. "
                    + "Set the classpath correctly");
            return;
        }

        conn = DriverManager.getConnection("jdbc:hsqldb:" +
                                           HypersonicManager.IN_MEMORY_SOURCE,
                                           HypersonicManager.DEFAULT_USERNAME,
                                           HypersonicManager.DEFAULT_PASSWORD);
    }

    /**
     * Run the given executor inside the HypersonicManager.
     * Note that the HypersonicManager will itself be run inside
     * the AppManager.
     *
     * @param executor The code to run.
     */
    public void runWithHyperSonicManager(Executor executor) throws Exception {
        final Executor hypersonicExecutor = executor;
        runWithAppManager(new AppExecutor() {
            public void execute(AppContext appContext) throws Exception {
                hypersonicManager.useCleanupWith(hypersonicExecutor);
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 24-Nov-04	236/1	philws	VBM:2004111209 Re-worked MPS to use new build

 24-Sep-04	140/1	claire	VBM:2004070704 Fixing testcases and replacing some hypersonic usage with stubs

 14-Jul-04	136/1	claire	VBM:2004070301 Implementing failed recipients management so code adheres to API and JavaDoc

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 24-Oct-03	45/1	mat	VBM:2003101502 Rework tests to use AppManager properly

 23-Oct-03	45/1	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
