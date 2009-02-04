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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/DefaultAssetURLRewriterTestCase.java,v 1.8 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jan-03    Byron           VBM:2003012712 - Created
 * 07-Feb-03    Sumit           VBM:2003020603 - Made all common atributes
 *                              static to prevent NPE in testR..CacheEnabled
 * 12-Feb-03    Geoff           VBM:2003021110 - Move from single license to
 *                              per fixture license generation, and use the
 *                              new LicenseManager useLicenseWith() method.
 * 19-Feb-03    Byron           VBM:2003012712 - Added and updated urlRewriting
 *                              tests case.
 * 20-Feb-03    Geoff           VBM:2003021903 - Add to do comment.
 * 20-Feb-03    Byron           VBM:2003021809 - Added a test case for Chart
 *                              Assets.
 * 21-Feb-03    Geoff           VBM:2003022004 - Use refactored LicenseManager.
 * 06-Mar-03    Geoff           VBM:2003010904 - Use new ConfigValue stuff.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.DefaultApplicationContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.testtools.application.AppContext;
import com.volantis.mcs.testtools.application.AppExecutor;
import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;
import com.volantis.testtools.stubs.ServletContextStub;
import junitx.util.PrivateAccessor;

import java.util.HashMap;

/**
 * Test the default asset URL rewriter.
 * @todo better this class tests the core functionality and should be updated
 * to test for more permutations of asset url rewriting.
 */
public class DefaultAssetURLRewriterTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private static String tvPrefix = "http://tvprefix/";
    private static String deviceName = "DummyDeviceName";

    private ConfigValue configValue = new ConfigValue();

    private TestMarinerRequestContext requestContext =
            new TestMarinerRequestContext();
    private Volantis volantis;
    ProtocolBuilder builder = new ProtocolBuilder();
    VolantisProtocol protocol = builder.build(
            new TestProtocolRegistry.TestDOMProtocolFactory(),
            InternalDeviceTestHelper.createTestDevice());


    /**
     * Specialisation of MarinerPageContext to allow us to return known
     * Layout and MarinerRequestContext
     * @todo merge this back into {@link TestMarinerRequestContext}
     */
    private class MyMarinerPageContext extends MarinerPageContext {

        private DeviceLayoutContext deviceLayoutContext =
            new DeviceLayoutContext();

        /**
         * Construct a new MyMarinerPageContext().
         * @throws Exception
         */
        public MyMarinerPageContext() throws Exception {
            PrivateAccessor.setField(this, "applicationContext",
                    new DefaultApplicationContext(requestContext));
        }

        /**
         * Return a known static implementation of MarinerRequestContext
         * for test purposes.
         * @return A known implementation of MarinerRequestContext.
         */
        public MarinerRequestContext getRequestContext() {
            return requestContext;
        }

        public ReusableStringBuffer allocateRSB() {
            return new ReusableStringBuffer();
        }

        public String getDeviceName() {
            return "Master";
        }

        public DeviceLayoutContext getDeviceLayoutContext() {
            return deviceLayoutContext;
        }

        public InternalDevice getDevice() {
            HashMap policies = new HashMap();
            policies.put("tvChannelPrefix", tvPrefix);
            policies.put("preferredimagetype", ImageAsset.PNG_NAME);
            policies.put("rendermode", "greyscale");
            policies.put("bmpinpage", "true");
            policies.put("gifinpage", "false");
            policies.put("jpeginpage", "true");
            policies.put("pnginpage", "true");
            policies.put("maximagesize", "1024");
            policies.put("pixeldepth", "16");
            policies.put("pixelsx", "640");
            policies.put("gpng16rule", "gp16");

            return INTERNAL_DEVICE_FACTORY.createInternalDevice(
                new DefaultDevice(deviceName, policies, null));
        }

        public Volantis getVolantisBean() {
            return volantis;
        }

        public VolantisProtocol getProtocol() {
            return protocol;
        }

        public Pane getCurrentPane() {
            Pane pane = new Pane(null);
            pane.setWidth("100");
            pane.setHeight("100");
            return pane;
        }

    }

    protected void setUp() throws Exception {
        super.setUp();

        configValue.remoteRepositoryTimeout = null;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Construct resource-hungry (time, memory, etc.) objects once in this
     * constructor so that they may be used for any test within this class.
     * <p>
     * Currently a temporary licence file, the configuration file and the
     * volantis bean itself is created and initialized in this constructor.
     *
     * @param      name the name of this test.
     */
    public DefaultAssetURLRewriterTestCase(String name) throws Exception {
        super(name);
    }

    /**
     * This method tests the method public MarinerURL rewriteAssetURL (
     * MarinerRequestContext,Asset,AssetGroup,MarinerURL ) for the
     * com.volantis.mcs.runtime.DefaultAssetURLRewriter class.
     */
    public void testRewriteAssetURLDynamicVisual() throws Exception {
        AssetGroup assetGroup = new AssetGroup("Default Group");
        MarinerURL url = new MarinerURL("http://test.com:8080/this=that");

        // Asset construction
        String value = "Dynamic Value";
        DynamicVisualAsset asset = new DynamicVisualAsset();
        asset.setEncoding(DynamicVisualAsset.TV);
        asset.setValue(value);

        String expected = tvPrefix + value;
        doRewriterTest(asset, assetGroup, url, expected);
    }

    /**
     * Test the rewriting with a text asset and complete url
     */
    public void testRewriteAssetURLTextAsset() throws Exception {
        AssetGroup assetGroup = new AssetGroup("Default Group");
        assetGroup.setProject(createProject("/base"));
        MarinerURL url = new MarinerURL("http://test.com:8080/this=that");

        TextAsset asset = new TextAsset("TestText");
        String expected = url.getExternalForm();

        doRewriterTest(asset, assetGroup, url, expected);
    }

    /**
     * Test the rewriting with a valid assetgroup without an external repository.
     */
    public void testRewriteAssetURLComputeURLValidAssetGroup()
            throws Exception {
        // Asset group must not be null!
        AssetGroup assetGroup = new AssetGroup("Default Group");
        assetGroup.setProject(createProject("/base"));
        assetGroup.setPrefixURL("http://prefix/");

        MarinerURL url = new MarinerURL("this=that");
        TextAsset asset = new TextAsset("TestText");
        String expected = assetGroup.getPrefixURL() + url.getExternalForm();

        doRewriterTest(asset, assetGroup, url, expected);
    }

    /**
     * Test the rewriting with a valid assetgroup without an external repository
     * and with various combination of server/client side url's.
     */
    public void testRewriteAssetURLCompleteURLNoBaseURL() throws Exception {
        // Asset group must not be null!
        AssetGroup assetGroup = new AssetGroup("Default Group");
        assetGroup.setProject(createProject("/base"));

        MarinerURL url = new MarinerURL("http://test.com/this=that");
        TextAsset asset = new TextAsset("TestText");
        String expected = url.getExternalForm();

        // Test if this is a server side URL.
        assetGroup.setLocationType(AssetGroup.ON_SERVER);
        doRewriterTest(asset, assetGroup, url, expected);

        // Test non-relative path
        assetGroup.setLocationType(AssetGroup.ON_DEVICE);
        url = new MarinerURL("http://test.com/this=that");
        doRewriterTest(asset, assetGroup, url, expected);

        // Test if this is a client side URL.
        // This has been changed to being a device to ensure it is tested
        // as client side and not resolved to a full URL.  The test mcs-config
        // includes an absolute asset base URL for testing combinations of
        // rewrites and the previous code of using ON_SERVER failed. For
        // reader info rather than JavaDoc as this is a normal comment:
        // @see testRelativeAndAbsolutePaths()
        // @see com.volantis.testtools.config.ConfigFileBuilder.DEFAULT_ASSET_BASE_URL
        assetGroup.setLocationType(AssetGroup.ON_DEVICE);
        url = new MarinerURL("/test/this=that");
        expected = url.getExternalForm();
        doRewriterTest(asset, assetGroup, url, expected);
    }

    /**
     * Test the rewriting with a valid assetgroup without an external repository.
     */
    public void testRewriteAssetURLConvertibleImage() throws Exception {
        AssetGroup assetGroup = new AssetGroup("Default Group");
        assetGroup.setProject(createProject("/base"));

        MarinerURL url = new MarinerURL("http://host.com/this=that");
        ConvertibleImageAsset asset = new ConvertibleImageAsset("Test");
        asset.setEncoding(ConvertibleImageAsset.PNG);
        asset.setValue("/image.gif");
        asset.setPixelDepth(16);
        String expected = "http://host.com/gp16/this=that?" +
            "v.maxSize=1024&" +
            "v.width=640";

        doRewriterTest(asset, assetGroup, url, expected);
    }

    /**
     * Test the rewriting with a chart asset.
     */
    public void testRewriteAssetURLChartAsset() throws Exception {

        String strURL = "http://host.com/this=that";
        MarinerURL url = new MarinerURL(strURL);
        ChartAsset asset = new ChartAsset("Test");
        String expected = strURL;

        doRewriterTest(asset, null, url, expected);
    }

    /**
     * Test the various rewrites of URLs against expected values that are
     * created based on architecture decisions of how URLs should be
     * resolved.
     * @throws Exception If a test fails.
     */
    public void testRelative() throws Exception {

        // Default Strings
        String assetURL = "a/b/c.png";
        String assetGroupURL = "x/y/z/";
        String expected = assetGroupURL + assetURL;

        // Test Project
        final RuntimeProjectMock projectMock = createProject("/blah/");
//                new RuntimeProjectMock("projectMock", expectations);
//        projectMock.expects.getAssetsBaseURL()
//                .returns(new MarinerURL("/blah/")).any();
//        projectMock.expects.getContainsOrphans().returns(false).any();

        doRelativeOrAbsoluteTest(assetURL, projectMock, assetGroupURL, expected);
    }

    private void doRelativeOrAbsoluteTest(
            String assetURL,
            final RuntimeProject project,
            String assetGroupURL,
            String expected)
            throws Exception {

        // Asset Prefix
        TextAsset asset = new TextAsset("Default Asset");
        asset.setValue(assetURL);
        asset.setProject(project);

        // AssetGroup Prefix
        AssetGroup assetGroup = new AssetGroup("Default Group");
        assetGroup.setPrefixURL(assetGroupURL);
        assetGroup.setLocationType(AssetGroup.ON_SERVER);
        assetGroup.setProject(project);

        MarinerURL url = new MarinerURL(assetURL);

        doRewriterTest(asset, assetGroup, url, expected);
    }

    RuntimeProjectMock createProject(final String baseUrl) {

        final RuntimeProjectMock projectMock =
                new RuntimeProjectMock("projectMock", expectations);
        MarinerURL baseURL = new MarinerURL(baseUrl);
        baseURL.makeReadOnly();
        projectMock.expects.getAssetsBaseURL().returns(baseURL).any();
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.getPolicySource().returns(null).any();
        projectMock.expects.isPortable().returns(false).any();

        return projectMock;
    }

    /**
     * Test the a malformed completed URL is detected.
     *
     * @throws Exception If a test fails.
     */
    public void testMalformedCompletedURL() throws Exception {

        // Default Strings
        String assetURL = "a/b/c.png";
        String assetGroupURL = "/x/y/z/";
        String expected = "Anything :-)";

        // Test case 2 - Trigger exception for malformed completed URL
//        assetGroupURL = "/" + assetGroupURL;
        RuntimeProject projectMock = createProject("volantis/");

        try {
            doRelativeOrAbsoluteTest(assetURL, projectMock, assetGroupURL,
                    expected);
            fail("There should have been an exception from completeURL");
        } catch (Exception e) {
            // Check here for a RuntimeException with a message starting
            // with the string in the code since it is known that this is
            // the exception being tested for.
            if (!e.getClass().equals(RuntimeException.class) ||
                    !e.getMessage().startsWith("The rewritten URL")) {
                // Rethrow the exception as it was not an expected one!
                throw e;
            }
        }
    }

    /**
     * Test host relative.
     *
     * @throws Exception If a test fails.
     */
    public void testHostRelative() throws Exception {

        // Default Strings

        // Test Project
        RuntimeProject projectMock = createProject("http://www.volantis.com/webapp/");

        // Test case 3 - Host Relative
        doRelativeOrAbsoluteTest("a/b/c.png", projectMock, "/x/y/z/",
                "http://www.volantis.com/webapp/x/y/z/a/b/c.png");
    }

    /**
     * Test that an absolute asset group prefix overrides base.
     *
     * @throws Exception If a test fails.
     */
    public void testAbsoluteAssetGroupPrefixOverridingBase() throws Exception {

        // Default Strings

        // Test Project
        RuntimeProject projectMock = createProject("http://www.volantis.com/webapp/");

        // Test case 4 - Absolute AssetGroup prefix overriding base
        doRelativeOrAbsoluteTest("a/b/c.png", projectMock, "http://www.ibm.com/",
                "http://www.ibm.com/a/b/c.png");
    }

    /**
     * Test a host relative asset.
     *
     * @throws Exception If a test fails.
     */
    public void testHostRelativeAsset() throws Exception {

        // Default Strings

        // Test Project
        RuntimeProject projectMock = createProject("http://www.volantis.com/webapp/");

        // Test case 5 - Host relative Asset
        doRelativeOrAbsoluteTest("/a/b/c.png", projectMock, "",
                "http://www.volantis.com/webapp/a/b/c.png");
    }

    /**
     * Test a host relative asset overrides the asset group.
     *
     * @throws Exception If a test fails.
     */
    public void testHostRelativeAssetOverridingAssetGroup() throws Exception {

        // Test Project
        RuntimeProject projectMock = createProject("http://www.volantis.com/webapp/");

        doRelativeOrAbsoluteTest("/a/b/c.png", projectMock, "/x/y/",
                "http://www.volantis.com/webapp/a/b/c.png");
    }

    /**
     * Test the absolute asset prefix overrides base.
     *
     * @throws Exception If a test fails.
     */
    public void testAbsoluteAssetPrefixOverridingBase() throws Exception {

        // Test Project
        RuntimeProject projectMock = createProject("http://www.volantis.com/webapp/");

        doRelativeOrAbsoluteTest("http://www.volantis.com/", projectMock, "/x/y/",
                "http://www.volantis.com/");
    }

    /**
     * Support method for testRewriteAssetURL.
     *
     * @param asset      the asset
     * @param assetGroup the asset group
     * @param marinerURL the mariner URL
     * @param expected   the expected result
     */
    private void doRewriterTest(final Asset asset,
                                final AssetGroup assetGroup,
                                final MarinerURL marinerURL,
                                final String expected) throws Exception {
        volantis = new TestableVolantis();
        ServletContextStub servletContext = new ServletContextStub();
        AppManager manager = new AppManager(volantis, servletContext);

        manager.useAppWith(new AppExecutor() {
            public void execute(AppContext context) throws Exception {
                MyMarinerPageContext pageContext = new MyMarinerPageContext();
                requestContext.pushProject(volantis.getDefaultProject());
                ContextInternals.setMarinerPageContext(requestContext,
                                                       pageContext);
                protocol.setMarinerPageContext(pageContext);

                DefaultAssetURLRewriter rewriter = new DefaultAssetURLRewriter();
                MarinerURL url = rewriter.rewriteAssetURL(requestContext,
                                                          asset,
                                                          assetGroup,
                                                          marinerURL);
                assertNotNull(url);
                assertEquals("ExternalForm should match; expected=" +
                        expected + ", actual=" + url.getExternalForm() + ".",
                             expected,
                             url.getExternalForm());
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Sep-05	9391/3	emma	VBM:2005082604 Supermerge required

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 17-Jan-05	6693/1	allan	VBM:2005011403 Remove MPS specific image url parameters

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 21-Sep-04	5559/1	geoff	VBM:2004091506 Support GIF as transcoded image type in MCS and ICS

 30-Jul-04	4713/8	geoff	VBM:2004061004 Support iterated Regions (supermerge again)

 02-Jul-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 29-Jul-04	4991/3	byron	VBM:2004070510 VTS classes need renaming in MCS to ICS - removed constants usage in TC

 29-Jul-04	4991/1	byron	VBM:2004070510 VTS classes need renaming in MCS to ICS

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 30-Apr-04	4128/1	ianw	VBM:2004042905 Changed v.maxsize to v.maxSize

 18-Feb-04	3041/1	claire	VBM:2004021208 Updated project/config code for testing paths

 17-Feb-04	3037/3	claire	VBM:2004021206 Changed exception testing logic

 16-Feb-04	3037/1	claire	VBM:2004021206 Improve isolation of rewriting URL test cases

 11-Feb-04	2846/11	claire	VBM:2004011915 Added more rewrite URL testcases

 11-Feb-04	2846/9	claire	VBM:2004011915 Ensured asset url rewriting works as specified, with testcases

 09-Feb-04	2846/6	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 09-Feb-04	2846/4	claire	VBM:2004011915 Refactoring URL handling

 05-Feb-04	2846/1	claire	VBM:2004011915 Asset URL computation based on base and prefix

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 10-Jun-03	356/1	allan	VBM:2003060907 Moved some common testtools into Synergetics

 ===========================================================================
*/
