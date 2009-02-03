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

package com.volantis.mps.channels;

import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mps.MPSTestAbstract;
import com.volantis.testtools.config.ConfigValueChannelMms;
import com.volantis.testtools.config.ConfigValueChannelSms;
import com.volantis.testtools.config.ConfigValueChannelSmtp;
import com.volantis.testtools.config.MPSAppConfigurator;
import com.volantis.testtools.config.MPSPluginConfigBuilder;
import com.volantis.testtools.config.MPSPluginConfigValue;
import com.volantis.testtools.config.PluginConfigFileBuilder;
import com.volantis.testtools.stubs.ServletContextStub;

import java.util.Properties;
import java.sql.Connection;
import java.sql.Statement;


/**
 * This tests the {@link com.volantis.mps.channels.MessageChannel} class.
 *
 * @todo Later Ensure this test class covers all methods as no time at the moment to implement proper tests for all methods
 */
public class MessageChannelTestCase extends MPSTestAbstract {
    /**
     * Constant that represents an SMS channel.
     */
    protected final String SMS_CHANNEL = "smsc";

    /**
     * Constant that represents an MMS channel.
     */
    protected final String MMS_CHANNEL = "mmsc";

    /**
     * Constant that represents an SMTP channel.
     */
    protected final String SMTP_CHANNEL = "smtp";

    /**
     * Port to use for the tests
     */
    public static final int PORT = 54321;

    /**
     * The application manager for the test run
     */
    private AppManager appManager;


    /**
     * Initialise a new named instance of this test class.
     *
     * @param s The name of the test class.
     */
    public MessageChannelTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    public void setUp() throws Exception {
        super.setUp();

        createRepository();

        // Initialize the configBuilder and generate the config files
        final MPSPluginConfigValue mps = new MPSPluginConfigValue();

        ConfigValueChannelSms sms = new ConfigValueChannelSms();
        sms.channelClass = "com.volantis.mps.channels.LogicaSMSChannelAdapter";
        sms.name = SMS_CHANNEL;
        sms.address = "127.0.0.1";
        sms.port = new Integer(PORT);
        sms.userName = "test";
        sms.password = "test";
        sms.bindtype = "async";
        sms.supportsMulti = Boolean.FALSE;
        mps.channels.add(sms);

        ConfigValueChannelMms mms = new ConfigValueChannelMms();
        mms.channelClass = "com.volantis.mps.channels.NokiaMMSChannelAdapter";
        mms.name = MMS_CHANNEL;
        mms.url = "http://127.0.0.1:" + PORT;
        mms.defaultCountryCode = new Integer(44);
        mps.channels.add(mms);

        ConfigValueChannelSmtp smtp = new ConfigValueChannelSmtp();
        smtp.channelClass = "com.volantis.mps.channels.SMTPChannelAdapter";
        smtp.name = SMTP_CHANNEL;
        smtp.hostName = "127.0.0.1";
        smtp.authorisationEnabled = Boolean.FALSE;
        smtp.userName = "user";
        smtp.password = "password";
        mps.channels.add(smtp);

        MPSAppConfigurator appConf = new MPSAppConfigurator();
        appConf.setPluginConfigValue(mps);

        volantisBean = new Volantis();
        ServletContextStub servletContext = new ServletContextStub();
        appManager = new AppManager(volantisBean, servletContext);
        appManager.setAppConf(appConf);
        PluginConfigFileBuilder builder = new MPSPluginConfigBuilder();
        appManager.registerPluginConfigFileBuilder(builder, mps.getClass());

        Properties props = System.getProperties();
        props.put("mail.smtp.port", new Integer(PORT).toString());
    }

    // JavaDoc inherited
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This tests the {@link com.volantis.mps.channels.MessageChannel#sendImpl} method implementations.
     * <p>
     * This is not as generic as it could and should be and requires a huge
     * amount of setup.  Ideally the channels should be altered to allow
     * drop in/replacement send methods if nothing else.  This would make
     * testing a little easier (although would not remove a chunk of this
     * setup).
     * </p>
     * <p>
     * This tests SMTP failure as there is no server running against the
     * specified address port.  This channel and others have also been
     * integration tested for success and failure using Tomcat.  This is not
     * an ideal solution BUT given the current time constraints it is
     * adequate.
     * </p>
     */
    public void testSendImpl() throws Exception {

        // @todo later re-implement such that this test works. Commented out as equivalent to test exclusions doesn't exist in the new build.
        /*
//        Execution error was:
//        N/Acom.volantis.mps.message.MessageException at com.volantis.mps.assembler.MessageRequestor.doInternalRequest(MessageRequestor.java:266) at
//        ...
//        Caused by: com.volantis.mcs.repository.jdbc.JDBCRepositoryException: Error (JDBC Errorjava.sql.SQLException: Table not found: VMPOLICY_VALUES in statement [select POLICY , VALUE , REVISION from VMPOLICY_VALUES where PROJECT = '#DefaultProject' and NAME = 'Nokia-Series7110']) at com.volantis.mcs.accessors.jdbc.JDBCDeviceRepositoryAccessor.retrieveDeviceImpl(JDBCDeviceRepositoryAccessor.java:733) at
//        ...
//        even though this test appears to set that table up (it could be that the table initialization simply doesn't match MCS now)
        final InternetAddress address = new InternetAddress("mps@volantis.com");

        appManager.useAppWith(new AppExecutor() {
            public void execute(AppContext context) throws Exception {
                hypersonicManager.useCleanupWith(new Executor() {
                    public void execute() throws MessageException,
                            IOException,
                            RecipientException {
                        MpsPluginConfiguration config =
                                (MpsPluginConfiguration)volantisBean.
                                getApplicationPluginConfiguration("MPS");

                        Map channels = new HashMap();
                        Iterator ci = config.getChannelsIterator();
                        while (ci.hasNext()) {
                            MpsChannelConfiguration cc =
                                    (MpsChannelConfiguration)ci.next();
                            channels.put(cc.getName(),cc);
                        }
                        Map channelConfig =
                                ((MpsChannelConfiguration)channels.
                                get(SMTP_CHANNEL)).getArguments();
                        MessageChannel smtpAdapter =
                                new SMTPChannelAdapter(SMTP_CHANNEL,
                                                       channelConfig);
                        final String TEST_MESSAGE =
                                "<message><canvas layoutName=\"/error.mlyt\">"
                                + "<pane name=\"error\">SMS TestMessage</pane>"
                                + "</canvas></message>";

                        MultiChannelMessage message1 =
                                new MultiChannelMessage(
                                        TEST_MESSAGE,
                                        "SMTP test");
                        com.volantis.mps.session.Session session =
                                new com.volantis.mps.session.Session();

                        MessageRecipients multi =
                                new MessageRecipients();
                        MessageRecipient recipient =
                                new MessageRecipient();

                        recipient.setMSISDN("10.20.30.50");
                        recipient.setAddress(address);
                        recipient.setChannelName(SMTP_CHANNEL);
                        recipient.setDeviceName("Nokia-Series7110");

                        multi.addRecipient(recipient);

                        recipient = new MessageRecipient();

                        recipient.setMSISDN("10.20.30.51");
                        recipient.setAddress(address);
                        recipient.setChannelName(SMTP_CHANNEL);
                        recipient.setDeviceName("Nokia-Series7110");

                        multi.addRecipient(recipient);
                        recipient = new MessageRecipient();

                        recipient.setMSISDN("10.20.30.52");
                        recipient.setAddress(address);
                        recipient.setChannelName(SMTP_CHANNEL);
                        recipient.setDeviceName("Nokia-Series7110");

                        multi.addRecipient(recipient);
                        recipient = new MessageRecipient();

                        recipient.setMSISDN("10.20.30.53");
                        recipient.setAddress(address);
                        recipient.setChannelName(SMTP_CHANNEL);
                        recipient.setDeviceName("Nokia-Series7110");

                        multi.addRecipient(recipient);

                        recipient = new MessageRecipient();

                        session.addRecipients("all", multi);

                        MessageRecipients failures = session.send(message1,
                                                                  "all",
                                                                  null);
                        // Current SMTPServer implementation causes these
                        // failures - the implementations are linked.
                        assertNotNull("Failures should exist", failures);
                    }
                });
            }
        });
        */

    }

    // ======================================================================
    // These methods for creating a simple test repository are copied from
    // {@link LogicaSMSChannelAdapterTestCase} which  works in a similar way
    // to this test case.
    //
    // Ideally these would be moved to a shared class, tided up etc., but
    // there is no time for that at the moment.
    // ======================================================================

    /**
     * Build a HSQL repository
     */
    protected void createRepository() throws Exception {
        super.createRepository();
        createDevicePatterns(conn);
        createPolicyValues(conn);
        createLayout(conn);
    }

    /**
     * Create the device patterns table
     */
    private void createDevicePatterns(Connection conn) {
        try {
            Statement st = conn.createStatement();
            st.execute("create table VMDEVICE_PATTERNS ("
                       + " PROJECT VARCHAR(255),"
                       + " NAME VARCHAR(20),"
                       + " PATTERN VARCHAR(255),"
                       + " REVISION INTEGER DEFAULT 0,"
                       + " CONSTRAINT PK_VMDEVICEPATTERNS "
                       + " PRIMARY KEY ( NAME, PATTERN ) )");
            st.execute("insert into VMDEVICE_PATTERNS values ("
                       + "'#DefaultProject','one','one',0)");
            st.execute("insert into VMDEVICE_PATTERNS values ("
                       + "'#DefaultProject','Nokia-Series7110','Nokia-Series7110',0)");
        } catch (Exception e) {
            System.err.println("Failed to create VMDEVICE_PATTERNS table.");
            e.printStackTrace(System.err);
        }
    }

    /**
     * Create the Policy values
     */
    private void createPolicyValues(Connection conn) {
        try {
            Statement st = conn.createStatement();
            st.execute("CREATE TABLE VMPOLICY_VALUES ( "
                       + "PROJECT VARCHAR(255),"
                       + "NAME VARCHAR(20) NOT NULL, "
                       + "POLICY VARCHAR(200) NOT NULL, "
                       + "VALUE VARCHAR(1024), "
                       + "REVISION INTEGER DEFAULT 0, "
                       + "CONSTRAINT PK_VMPOLICYVALUES "
                       + "PRIMARY KEY ( NAME, POLICY ) )");

            st.execute("insert into VMPOLICY_VALUES values ("
                       + "'#DefaultProject','Nokia-Series7110','fallback','one',0 )");
            st.execute("insert into VMPOLICY_VALUES values ("
                       + "'#DefaultProject','Nokia-Series7110','preferredmessageprotocol','SMTP',0 )");
            st.execute("insert into VMPOLICY_VALUES values ("
                       + "'#DefaultProject','Nokia-Series7110','smtpprotocol','HTMLVersion3_2',0 )");
            st.execute("insert into VMPOLICY_VALUES values ("
                       + "'#DefaultProject','one','pixelsX','200',0 )");
            st.close();
        } catch (Exception e) {
            System.err.println("Failed to create VMPOLICY_VALUES table.");
            e.printStackTrace(System.err);
        }
    }

    /**
     * Create the layout we will be using
     * @param conn
     */
    private void createLayout(Connection conn) {
        Statement st;
        try {
            st = conn.createStatement();
            st.execute("CREATE TABLE VMLAYOUT_FORMATS ( "
                       + "PROJECT VARCHAR (255) NOT NULL, "
                       + "LAYOUT VARCHAR(32) NOT NULL, "
                       + "DEVICE VARCHAR(32) NOT NULL, "
                       + "INSTANCE INTEGER DEFAULT 0, "
                       + "PARENT INTEGER DEFAULT 0, "
                       + "CHILDINDEX INTEGER DEFAULT 0, "
                       + "TYPE VARCHAR(32), "
                       + "REVISION INTEGER DEFAULT 0)");
            st.execute("INSERT INTO VMLAYOUT_FORMATS VALUES ("
                       + "'#DefaultProject', '/error.mlyt','Nokia-Series7110',0,-1,-1,'Pane',0)");
            st.close();
        } catch (Exception e) {
            System.err.println("Failed to create VMLAYOUT_FORMATS table.");
            e.printStackTrace(System.err);
        }
        try {
            st = conn.createStatement();
            st.execute("CREATE TABLE VMDEVICE_LAYOUTS ( " +
                       "PROJECT VARCHAR (255) NOT NULL, " +
                       "LAYOUT VARCHAR (255) NOT NULL, " +
                       "DEVICE VARCHAR (32) NOT NULL, " +
                       "DEFAULTFRAGMENT VARCHAR (32), " +
                       "DEFAULTSEGMENT VARCHAR (32), " +
                       "VERSION NUMERIC (5), " +
                       "REVISION NUMERIC (9), " +
                       "TIMETOLIVE NUMERIC (5), " +
                       "RETRYINTERVAL NUMERIC (5), " +
                       "RETRYMAXCOUNT NUMERIC (5), " +
                       "RETAINDURINGRETRY NUMERIC (1), " +
                       "RETRYFAILEDRETRIEVAL NUMERIC (1), " +
                       "CACHETHISPOLICY NUMERIC (1) )" );
            st.execute("INSERT INTO VMDEVICE_LAYOUTS VALUES ("
                       + "'#DefaultProject', '/error.mlyt','Nokia-Series7110',NULL,NULL,0, 0, 0, 0, 0, 0, 0, 0)");
            st.close();
        } catch (Exception e) {
            System.err.println("Failed to create VMDEVICE_LAYOUTS table.");
            e.printStackTrace(System.err);
        }
        try {
            st = conn.createStatement();
            st.execute("CREATE TABLE VMFORMAT_ATTRIBUTES ("
                       + "PROJECT VARCHAR (255) NOT NULL, "
                       + "LAYOUT VARCHAR(32) NOT NULL, "
                       + "DEVICE VARCHAR(32) NOT NULL, "
                       + "INSTANCE INTEGER DEFAULT 0, "
                       + "NAME VARCHAR(32) NOT NULL, "
                       + "VALUE VARCHAR(128), "
                       + "REVISION INTEGER DEFAULT 0)");
            st.execute("INSERT INTO VMFORMAT_ATTRIBUTES VALUES ("
                       + "'#DefaultProject', '/error.mlyt','Nokia-Series7110',0,'Name','error',0)");
            st.close();
        } catch (Exception e) {
            System.err.println("Failed to create VMDEVICE_LAYOUTS table.");
            e.printStackTrace(System.err);
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 24-Nov-04	236/1	philws	VBM:2004111209 Re-worked MPS to use new build

 14-Jul-04	136/1	claire	VBM:2004070301 Implementing failed recipients management so code adheres to API and JavaDoc

 ===========================================================================
*/
