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
package com.volantis.mcs.ibm.ucp;

import com.ibm.ucp.IProvider;
import com.ibm.ucp.Profile;
import com.ibm.ucp.ProviderFactory;
import com.ibm.ucp.util.Environment;
import com.volantis.devrep.repository.accessors.EclipseDeviceRepository;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.repository.xml.XMLRepository;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.testtools.Executor;
import com.volantis.synergetics.testtools.HypersonicManager;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.io.IOUtils;
import org.jdom.input.DefaultJDOMFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.jdom.Namespace;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Test case for {@link MCSDeviceRepositoryProvider}.
 *
 * @todo this points out the need for more infrastructure for repository tests.
 * We could have a RepositoryExecutor interface with a
 *  - execute(RespositoryManagerContext)
 * method. Then we could have an abstract RepositoryManager class which has a
 *  - setInitialisor(RepositoryInitialisor)
 *  - doExecuteWith(RepositoryExecutor).
 * The RepositoryInitialisor interface would have a single method
 *  - doInitialisationWith(RepositoryManagerContext)
 * RepositoryManager would use the initialisor instance to initialise the
 * repository before using the executor to run the test when doExecuteWith was
 * called. Then we could have concrete subclasses XMLRepositoryManager and
 * JDBCRepositoryManager. JDBCRepositoryManager would add the
 *  - setCreator(JDBCRepositoryCreator)
 * method, where JDBCRepositoryCreator interface would have the method
 *  - doCreateWith(JDBCRepositoryManagerContext)
 * JDBCRepositoryManager would use the creator instance to create the database
 * tables it needs. XMLRepositoryManager doesn't need to create tables, but it
 * would need to call XMLRepository.write() after the initialisation stage.
 * After all that was done, we could write instances of RepositoryExecutor
 * which could implement repository tests which could run against either of
 * the repository types by executing them with different manager instances.
 */
public class MCSDeviceRepositoryProviderTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to create XSL transformers.
     */
    private static final TransformerMetaFactory transformerMetaFactory =
            new TestTransformerMetaFactory();

    private static final Namespace DEVICE_NAMESPACE = Namespace.getNamespace("",
            "http://www.volantis.com/xmlns/device-repository/device");

    private static final Namespace HIERARCHY_NAMESPACE =
            Namespace.getNamespace("",
            "http://www.volantis.com/xmlns/device-repository/device-hierarchy");

    /**
     * The name of an empty repository file which we can use for testing -
     * we need a valid repository to add stuff to.
     */
    private static final String EMPTY_REPOSITORY_FILE = "repository.zip";

    public void testDefaultConstructorFailure() throws Exception {

        // Load up a testing UCP configuration.
        // This is required because Adaptor tries to initialise Logging.
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");

        try {
            // Create the provider manually.
            new MCSDeviceRepositoryProvider("provider name");
            fail("default constructor should throw exception");
        } catch (UnsupportedOperationException e) {
            // success, fall through...
        }

    }

    public void testNoParameters() throws Exception {

        // Load up a testing UCP configuration.
        // This is required because Adaptor tries to initialise Logging.
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");

        Element parameters = createParameters(null);
        try {
            // Create the provider manually.
            new MCSDeviceRepositoryProvider("provider name", parameters);
            fail("construction with no parameters tag should fail");
        } catch (RuntimeException e) {
            // success, fall though...
        }

    }

    public void testEmptyParameters() throws Exception {

        // Load up a testing UCP configuration.
        // This is required because Adaptor tries to initialise Logging.
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");

        Element parameters = createParameters(new HashMap());
        try {
            // Create the provider manually.
            new MCSDeviceRepositoryProvider("provider name", parameters);
            fail("construction with empty parameters tag should fail");
        } catch (RuntimeException e) {
            // success, fall though...
        }

    }

    public void testXmlDeviceNone() throws Exception {

        // Load up a testing UCP configuration.
        // This is required because Adaptor tries to initialise Logging.
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");

        // Create an empty repository to test against
        File deviceRepository =
            IOUtils.extractTempZipFromJarFile(
                getClass(),
                EMPTY_REPOSITORY_FILE,
                FileExtension.DEVICE_REPOSITORY.getExtension());

        HashMap params = new HashMap();
        params.put("repository-type", "xml");
        params.put("file", deviceRepository.getPath());
        Element parameters = createParameters(params);
        String providerName = "provider name";
        String deviceName = "uNkNoWn_dEvIcE";

        // Create the provider manually.
        MCSDeviceRepositoryProvider provider =
            new MCSDeviceRepositoryProvider(providerName, parameters);
        assertEquals("", providerName, provider.getName());
        assertNull("", provider.getProfile(deviceName));
        assertNull("", provider.getProfileDescription(deviceName));
    }

    /**
     * Test a single device with no values in a XML repository.
     * <p>
     * This should test basic device retrieval and null value handling for
     * the values we are adapting.
     */
    public void testXMLDeviceEmpty() throws Exception {

        HashMap deviceToProfile = new HashMap();
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        checkXMLDeviceToProfileAdaption(deviceToProfile);

    }

    /**
     * Test a set of devices with no values in an XML repository.
     * <p>
     * This should test the device name set handling methods.
     */
    public void testXMLDeviceEmptySet() throws Exception {

        HashMap deviceToProfile = new HashMap();
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        deviceToProfile.put(new DeviceValue(), new ProfileValue());
        checkXMLDeviceToProfileAdaption(deviceToProfile);

    }

    /**
     * Test a single device, with values defined for those properties which
     * require adaption, in an XML repository.
     * <p>
     * This should test the adaption of straightforward properties.
     */
    public void testXMLDeviceFull() throws Exception {

        HashMap deviceToProfile = new HashMap();
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.pixeldepth = "8";
        deviceValue.rendermode = "greyscale";
        deviceValue.modelnum = "Exploding Phone 101";
        deviceValue.pixelsx = "64";
        deviceValue.pixelsy = "32";
        deviceValue.charactersx = "10";
        deviceValue.charactersy = "4";
        deviceValue.mfg = "Acme";
        deviceValue.uaprofCcppAccept = "text/html, text/plain";
        deviceValue.brwsrname = "Acme WML Browser";
        deviceValue.brwsrvers = "2.01";
        deviceValue.uaprofWmlVersion = "1.1, 1.3";
        ProfileValue profileValue = new ProfileValue();
        profileValue.bitsPerPixel = "8";
        profileValue.colorCapable = "No";
        profileValue.model = "Exploding Phone 101";
        profileValue.screenSize = "64x32";
        profileValue.screenSizeChar = "10x4";
        profileValue.vendor = "Acme";
        profileValue.ccppAccept = "[text/html, text/plain]";
        profileValue.browserName = "Acme WML Browser";
        profileValue.browserVersion = "2.01";
        profileValue.wmlVersion = "[1.1, 1.3]";
        deviceToProfile.put(deviceValue, profileValue);
        checkXMLDeviceToProfileAdaption(deviceToProfile);

    }

    /**
     * Test the adaption of the rendermode=rgb property.
     */
    public void testXMLDeviceRenderModeRgb() throws Exception {

        HashMap deviceToProfile = new HashMap();
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.rendermode = "rgb";
        deviceValue.pixeldepth = "2";
        ProfileValue profileValue = new ProfileValue();
        profileValue.colorCapable = "Yes";
        profileValue.bitsPerPixel = "2";
        deviceToProfile.put(deviceValue, profileValue);
        checkXMLDeviceToProfileAdaption(deviceToProfile);

    }

    /**
     * Test the adaption of the rendermode=pallette property.
     */
    public void testXMLDeviceRenderModePallette() throws Exception {

        HashMap deviceToProfile = new HashMap();
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.rendermode = "pallette";
        deviceValue.pixeldepth = "2";
        ProfileValue profileValue = new ProfileValue();
        profileValue.colorCapable = "Yes";
        profileValue.bitsPerPixel = "2";
        deviceToProfile.put(deviceValue, profileValue);
        checkXMLDeviceToProfileAdaption(deviceToProfile);

    }

    /**
     * Check the results of adapting a set of devices into Profiles.
     * <p>
     * This will take a mapping of device to profile values, create a set of
     * synthetic device names for each entry in the map, add the devices into
     * a temporary repository with the synthetic names.
     * <p>
     * It will then create a Provider against that temporary repository, and
     * attempt to retreive Profiles from the Provider using the set of
     * synthetic names, checking that the Profiles returned match the profile
     * values passed in the original map.
     *
     * @param deviceToProfile a map of DeviceValues to ProfileValues.
     * @throws Exception
     */
    public void checkXMLDeviceToProfileAdaption(Map deviceToProfile)
            throws Exception {

        // Load up a testing UCP configuration.
        // This is required because adding Properties to Categories requires a
        // Schema Parser in SchemaFactory, even though the provider has turned
        // have validation "off". Not quite sure what is going on here.
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");

        // Create an empty repository to add to.
        File deviceRepository =
            IOUtils.extractTempZipFromJarFile(
                getClass(),
                EMPTY_REPOSITORY_FILE,
                FileExtension.DEVICE_REPOSITORY.getExtension());

        // Construct a bunch of testable devices using the underlying
        // accessor
        // @todo later refactor so it doesn't use the internal device API
        EclipseDeviceRepository accessor =
            new EclipseDeviceRepository(deviceRepository.getPath(),
                    transformerMetaFactory, new DefaultJDOMFactory(), null);

        int i = 0;
        HashMap nameToProfile = new HashMap();
        for (Iterator keys = deviceToProfile.keySet().iterator();
             keys.hasNext();) {
            // Extract the next deviceValue.
            DeviceValue deviceValue = (DeviceValue)keys.next();

            // Create a synthetic name for it.
            i += 1;
            String deviceName = "mytest-device-" + i;

            // Add the expected profile value for this device value to the
            // results map.
            nameToProfile.put(deviceName, deviceToProfile.get(deviceValue));

            accessor.addHierarchyDeviceElement(
                    deviceName, accessor.getRootDeviceName());

            // Add the created device for this device value to the
            // repository.
            HashMap deviceProps = new HashMap();
            deviceProps.put("pixeldepth", deviceValue.pixeldepth);
            deviceProps.put("rendermode", deviceValue.rendermode);
            deviceProps.put("modelnum", deviceValue.modelnum);
            deviceProps.put("pixelsx", deviceValue.pixelsx);
            deviceProps.put("pixelsy", deviceValue.pixelsy);
            deviceProps.put("charactersx", deviceValue.charactersx);
            deviceProps.put("charactersy", deviceValue.charactersy);
            deviceProps.put("mfg", deviceValue.mfg);
            deviceProps.put("UAProf.CcppAccept", deviceValue.uaprofCcppAccept);
            deviceProps.put("brwsrname", deviceValue.brwsrname);
            deviceProps.put("brwsrvers", deviceValue.brwsrvers);
            deviceProps.put("UAProf.WmlVersion", deviceValue.uaprofWmlVersion);

            org.jdom.Element deviceElement = createDeviceElement(deviceProps);
            Map devices = new HashMap(1);
            devices.put(deviceName, deviceElement);
            accessor.writeDeviceElements(devices);
            accessor.writeHierarchy();
        }

        accessor.saveRepositoryArchive();

        // Connect to a single file XML repository.
        HashMap repositoryProps = new HashMap();
        repositoryProps.put(XMLRepository.DEVICE_REPOSITORY_PROPERTY,
                            deviceRepository.getPath());

        // Create a Provider "wrapping" the temporary XML repository.
        HashMap params = new HashMap();
        params.put("repository-type", "xml");
        params.put("file", deviceRepository.getPath());

        Element parameters = createParameters(params);
        MCSDeviceRepositoryProvider provider =
            new MCSDeviceRepositoryProvider("provider", parameters);

        // Check that the available set of device names is correct.
        Set nameSet = nameToProfile.keySet();

        System.out.println("expecting: " + nameSet);
        System.out.println("and got  : " + provider.getProfileKeys());

        // Iterator over the names, ensuring each profile value is correct.
        for (Iterator names = nameSet.iterator(); names.hasNext();) {
            String deviceName = (String)names.next();
            ProfileValue profileValue = (ProfileValue)
                nameToProfile.get(deviceName);

            // Ensure provider returns test data we created in repository.
            Profile profile = provider.getProfile(deviceName);
            assertNotNull("cannot retrieve profile for empty device",
                          profile);
            assertEquals("profile description should equal device name",
                         deviceName,
                         provider.getProfileDescription(deviceName));

            // Test the results of the adapted device values.
            // NOTE: we only test getPropertyString since this is the only
            // method of Profile that the example code IBM gave us called.
            assertEquals("bitsPerPixel",
                         profileValue.bitsPerPixel,
                         profile.getPropertyString("BitsPerPixel"));
            assertEquals("colorCapable",
                         profileValue.colorCapable,
                         profile.getPropertyString("ColorCapable"));
            assertEquals("model",
                         profileValue.model,
                         profile.getPropertyString("Model"));
            assertEquals("screenSize",
                         profileValue.screenSize,
                         profile.getPropertyString("ScreenSize"));
            assertEquals("screenSizeChar",
                         profileValue.screenSizeChar,
                         profile.getPropertyString("ScreenSizeChar"));
            assertEquals("vendor",
                         profileValue.vendor,
                         profile.getPropertyString("Vendor"));
            assertEquals("ccppAccept",
                         profileValue.ccppAccept,
                         profile.getPropertyString("CcppAccept"));
            assertEquals("browserName",
                         profileValue.browserName,
                         profile.getPropertyString("BrowserName"));
            assertEquals("browserVersion",
                         profileValue.browserVersion,
                         profile.getPropertyString("BrowserVersion"));
            assertEquals("wmlVersion",
                         profileValue.wmlVersion,
                         profile.getPropertyString("WmlVersion"));
        }

        assertTrue("profile keys should contain all explicitly " +
                   "created device names",
                   provider.getProfileKeys().containsAll(nameSet));
    }

    /**
     * Create a device element with the specified policy child elements
     * @param policies A map of name value pairs to add to the device element
     * as policy elements
     * @return a device element with the specified policy child elements
     */
    private org.jdom.Element createDeviceElement(Map policies) {
        org.jdom.Element deviceElement =
                new org.jdom.Element("device", DEVICE_NAMESPACE);
        org.jdom.Element policiesElement =
                new org.jdom.Element("policies", DEVICE_NAMESPACE);
        deviceElement.addContent(policiesElement);

        Set keys = policies.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            String value = (String)policies.get(name);
            if (name != null && value != null) {
                org.jdom.Element policy =
                        new org.jdom.Element("policy", DEVICE_NAMESPACE);
                policy.setAttribute("name", name);
                policy.setAttribute("value", value);
                policiesElement.addContent(policy);
            }
        }

        return deviceElement;
    }

    public void testJDBCDeviceNone() throws Exception {

        // Load up a testing UCP configuration.
        // This is required because adding Properties to Categories requires a
        // Schema Parser in SchemaFactory, even though we have validation off.
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");

        // Use the in-memory hypersonic db - no background threads!
        final HypersonicManager hypersonicMgr = new HypersonicManager(
                HypersonicManager.IN_MEMORY_SOURCE);
        hypersonicMgr.useCleanupWith(new Executor() {
            public void execute() throws Exception {

                // Create the database tables.
                // todo: this should be refactored to be re-usable, see the
                // to do comment in the class comment.
                Connection connection = DriverManager.getConnection(
                        hypersonicMgr.getUrl(),
                        HypersonicManager.DEFAULT_USERNAME,
                        HypersonicManager.DEFAULT_PASSWORD);
                Statement statement = connection.createStatement();
                // Create the device patterns table
                String sql = "CREATE TABLE VMDEVICE_PATTERNS ( " +
                        "PROJECT   VARCHAR (255)  NOT NULL," +
                        "NAME      VARCHAR (20)  NOT NULL," +
                        "PATTERN   VARCHAR (255)  NOT NULL," +
                        "REVISION  NUMERIC (9)   DEFAULT 0" +
                        " ) ";
                statement.execute(sql);
                statement.close();
                statement = connection.createStatement();
                // Create the device patterns table
                sql = "CREATE TABLE VMPOLICY_VALUES ( " +
                        "PROJECT   VARCHAR (255)  NOT NULL," +
                        "NAME      VARCHAR (20)  NOT NULL," +
                        "POLICY    VARCHAR (200)  NOT NULL," +
                        "VALUE     VARCHAR (1024)," +
                        "REVISION  NUMERIC (9)   DEFAULT 0" +
                        " ) ";
                statement.execute(sql);
                statement.close();
                // Note: must leave connection open so that subsequent code
                // can find the tables we just created.

                final HashMap params = new HashMap();
                params.put("repository-type", "jdbc");
                // todo: all these values should come from the db manager class.
                params.put("odbc-user", HypersonicManager.DEFAULT_USERNAME);
                params.put("odbc-password", HypersonicManager.DEFAULT_PASSWORD);
                params.put("odbc-source", hypersonicMgr.getSource());
                params.put("odbc-vendor", "hypersonic");
                // NOTE: our old style devices don't currently use default
                // project but we need to provide it or the test fails...
                params.put("project", "#DefaultProject");
//                params.put("odbc-host", "not used");
//                params.put("odbc-port", "-1");
                Element parameters = createParameters(params);
                MCSDeviceRepositoryProvider provider =
                        new MCSDeviceRepositoryProvider("provider name", parameters);
                Profile profile = provider.getProfile("unknown device");
                assertNull(profile);

            }
        });

    }

    /**
     * Try creating our provider via the hardcoded UCP config file rather than
     * simulating it's creation manually.
     *
     * @todo later this cannot be done since the ucp-config.xml doesn't contain a reference to a valid and existing device repository
     */
    public void noTestFullInitialisationFromUcpConfig() throws Exception {

        // Load up a testing UCP configuration.
        // This contains an Adaptor definition for our provider, etc.
        Environment.init("/com/volantis/mcs/ibm/ucp/ucp-config.xml");

        // Create the provider.
        IProvider provider = ProviderFactory.getProvider();

        // Check it is the type we expect.
        assertTrue(provider instanceof MCSDeviceRepositoryProvider);

    }

    /**
     * Create a DOM tree containing parameters as would be passed into the
     * constructor by UCP. This has the same structure as the <parameters>
     * tree defined in the UCP XML config file.
     *
     * @param parameters
     * @return a DOM parameters element
     * @throws Exception
     */
    private Element createParameters(Map parameters) throws Exception {

        Element root = null;
        if (parameters != null) {
            String xml = "";
            xml +=
                    "<?xml version='1.0'?>";
            xml +=
                    "<parameters>";
            for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
                String name = (String) i.next();
                String value = (String) parameters.get(name);
                xml +=
                        "<param name='" + name + "' value='" + value + "'/>";
            }

            xml +=
                    "</parameters>";
            root = parse(xml);
        }
        return root;

    }

    /**
     * Parse a valid snippet of XML, returning the root element of a DOM tree.
     *
     * @param xml the xml snippet to parse.
     * @return the root element.
     * @throws Exception if there was a problem with the parse.
     */
    private Element parse(String xml) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
        Document document = db.parse(input);
        return document.getDocumentElement();

    }

    /**
     * Stores the values we adapt into a Profile.
     */
    private static class ProfileValue {

        // pixeldepth -> BitsPerPixel
        String bitsPerPixel;
        // rendermode -> ColorCapable
        String colorCapable;
        // modelnum -> Model
        String model;
        // pixelsx,y -> ScreenSize
        String screenSize;
        // charactersx,y -> ScreenSizeChar
        String screenSizeChar;
        // mfg -> Vendor
        String vendor;
        // UAProf.CcppAccept -> CcppAccept
        String ccppAccept;
        // brwsrname -> BrowserName
        String browserName;
        // brwsrvers -> BrowserVersion
        String browserVersion;
        // UAProf.WmlVersion -> WmlVersion
        String wmlVersion;

    }

    /**
     * Stores the value we adapt out of a InternalDevice.
     */
    private static class DeviceValue {

        // pixeldepth -> BitsPerPixel
        String pixeldepth;
        // rendermode -> ColorCapable
        String rendermode;
        // modelnum -> Model
        String modelnum;
        // pixelsx,y -> ScreenSize
        String pixelsx;
        String pixelsy;
        // charactersx,y -> ScreenSizeChar
        String charactersx;
        String charactersy;
        // mfg -> Vendor
        String mfg;
        // UAProf.CcppAccept -> CcppAccept
        String uaprofCcppAccept;
        // brwsrname -> BrowserName
        String brwsrname;
        // brwsrvers -> BrowserVersion
        String brwsrvers;
        // UAProf.WmlVersion -> WmlVersion
        String uaprofWmlVersion;

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 26-Aug-04	5294/2	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 04-May-04	4113/1	doug	VBM:2004042906 Fixed migration problem with the device repository

 21-Apr-04	3016/2	adrian	VBM:2004021301 Fixed merge problems with updated XMLDeviceRepositoryAccessor

 03-Mar-04	3018/1	adrian	VBM:2004021302 Updated XMLDeviceRepositoryAccessor with new write methods

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 25-Feb-04	3136/1	philws	VBM:2004021908 Remove accessor manager singletons and make MCSDeviceRepositoryProvider and its test case use the runtime device accessor correctly

 30-Jan-04	2807/4	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 30-Jan-04	2807/2	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 15-Jan-04	2595/3	andy	VBM:2004011404 changed internal representation of policy names and partially implemented legacy mode to support old gui

 15-Jan-04	2595/1	andy	VBM:2004011404 changed internal representation of policy names

 13-Jan-04	2573/4	andy	VBM:2003121907 renamed file variables to directory

 13-Jan-04	2573/1	andy	VBM:2003121907 removed remnants of single file support

 04-Jan-04	2360/1	andy	VBM:2003121710 added PROJECT column to all tables

 23-Dec-03	2252/4	andy	VBM:2003121703 change to default name for non existant repository in test suite

 23-Dec-03	2252/2	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 03-Nov-03	1529/7	geoff	VBM:2003100904 Implement IBM UCP IProvider to read MCS devices for Eclipse (fix bug in rendermode support)

 17-Oct-03	1529/5	geoff	VBM:2003100904 Implement IBM UCP IProvider to read MCS devices for Eclipse (fix small rework things from mat)

 14-Oct-03	1529/3	geoff	VBM:2003100904 Implement IBM UCP Adaptor/IProvider to read MCS devices for Eclipse (finished apart from test case infrastructure)

 ===========================================================================
*/
