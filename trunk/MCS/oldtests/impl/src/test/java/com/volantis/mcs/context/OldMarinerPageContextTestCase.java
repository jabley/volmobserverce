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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/context/MarinerPageContextTestCase.java,v 1.5 2003/04/25 10:26:08 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Dec-02    Allan           VBM:2002110102 - Testcase for
 *                              MarinerPageContext. Just tests
 *                              setCurrentFragment() at the moment.
 * 20-Feb-03    Geoff           VBM:2003021903 - Add comment.
 * 05-Mar-03    Chris W         VBM:2003022706 - Added test for
 *                              updateFormFragmentationState()
 * 31-Mar-03    Chris W         VBM:2003022706 - Added comment on needing a
 *                              separate inner class MyMarinerPageContext for
 *                              each test.
 * 22-Apr-03    Allan           VBM:2003041710 - Added tests for
 *                              isMarinerExpression and handleMarinerExpression
 *                              Made extend TestCase abstract and tidied a bit.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to
 *                              Synergetics.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.context;

import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.expression.Precept;
import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.DeviceLayoutReplicator;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.Packager;
import com.volantis.mcs.runtime.packagers.mime.MultipartApplicationContext;
import com.volantis.mcs.runtime.packagers.mime.MultipartPackageHandler;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.ServletContextStub;
import com.volantis.xml.expression.atomic.BooleanValue;
import junitx.util.PrivateAccessor;

import java.util.EmptyStackException;
import java.util.Stack;

// NOTE: InvocationTargetException import is necessary!

/**
 * Test case for MarinerPageContext.
 */
public class OldMarinerPageContextTestCase extends TestCaseAbstract {

    MyMarinerPageContext context;
    TestMarinerRequestContext testRequestContext;
    CanvasLayout canvasLayout;
    Volantis volantis;
    ServletContextStub servletContext;


    /**
     * Set up stuff for this testcase.
     */
    public void setUp() throws Exception {
        super.setUp();

        context = new MyMarinerPageContext();
        setUpContext(context);

    }

    /**
     * General set up for a MyMarinerPageContext allowing specific
     * tests to add their own specific behaviour to the context that they
     * create instead of using the member variable.
     * @param context the MyMarinerPageContext to set up.
     */
    private void setUpContext(MyMarinerPageContext context) {
        testRequestContext = new TestMarinerRequestContext();
        volantis = new Volantis();
        servletContext = new ServletContextStub();
        testRequestContext = new TestMarinerRequestContext();

        context.setVolantisBean(volantis);
        // Register a dummy EnvironmentContext against the request context
        ContextInternals.setEnvironmentContext(testRequestContext,
                new TestEnvironmentContext());
        testRequestContext.setMarinerPageContext(context);
        context.setRequestContext(testRequestContext);

        // Create a project to push onto the stack.
        final RuntimeProjectMock projectMock =
                new RuntimeProjectMock("projectMock", expectations);
        testRequestContext.pushProject(projectMock);
        context.pushDeviceLayoutContext(new DeviceLayoutContext());
        context.setRequestContext(testRequestContext);
        canvasLayout = new CanvasLayout();
    }

    /**
     * Tear down everything that was set up.
     */
    public void tearDown() {
        context = null;
    }


    /**
     * Test that retrieveImageAssetURLAsString returns an array containing a singe url
     * when it is passed a <code>ImageAsset</code> which is a single image.
     */
    public void testRetrieveImageAssetURLAsStringNoSequence() throws Throwable {

        // perform our own set up because the standard one does not set up the contexts
        // correctly
        MultipartPackageHandler handler =
            new MultipartPackageHandler();
        ApplicationContext applicationContext = new ApplicationContext(testRequestContext);
        applicationContext.setAssetURLRewriter(handler);
        PackageResources packageResources = new MultipartApplicationContext(testRequestContext);
        applicationContext.setPackageResources(packageResources);
        Packager packager = new MultipartPackageHandler();
        applicationContext.setPackager(packager);

        MarinerPageContext context = new MarinerPageContext();
        testRequestContext.setMarinerPageContext(context);

        PrivateAccessor.setField(context, "applicationContext", applicationContext);
        ContextInternals.setApplicationContext(testRequestContext, applicationContext);

        testRequestContext.setApplicationContext(applicationContext);

        context.pushRequestContext(testRequestContext);

        final AssetResolverMock assetResolverMock =
                new AssetResolverMock("assetResolverMock", expectations);
        PrivateAccessor.setField(context, "assetResolver", assetResolverMock);

        // set up the ImageAsset
        ImageAsset imageAsset = new ConvertibleImageAsset();
        imageAsset.setValue("rotatingWorld.gif");
        imageAsset.setSequence(false);

        assetResolverMock.expects.computeURLAsString(imageAsset)
                .returns("rotatingWorld.gif").any();

        // perform the tests
        String[] result = context.retrieveImageAssetURLAsString(imageAsset);

        assertEquals("The array is not of the correct size ", 1, result.length);

        String expectedString0 = "rotatingWorld.gif";
        assertEquals("The image asset url at index 0 is wrong ",
                expectedString0, result[0]);
    }

    /**
     * Test that retrieveImageAssetURLAsString returns an array of urls when it is passed
     * an <code>ImageAsset</code> which is a sequence of images.
     */
    public void testRetrieveImageAssetURLAsStringWithSequence() throws Throwable {

        // perform our own set up because the standard one does not set up the contexts
        // correctly
        MultipartPackageHandler handler =
            new MultipartPackageHandler();
        ApplicationContext applicationContext = new ApplicationContext(testRequestContext);
        applicationContext.setAssetURLRewriter(handler);
        PackageResources packageResources = new MultipartApplicationContext(testRequestContext);
        applicationContext.setPackageResources(packageResources);
        Packager packager = new MultipartPackageHandler();
        applicationContext.setPackager(packager);

        MarinerPageContext context = new MarinerPageContext();
        testRequestContext.setMarinerPageContext(context);

        PrivateAccessor.setField(context, "applicationContext", applicationContext);
        ContextInternals.setApplicationContext(testRequestContext, applicationContext);

        testRequestContext.setApplicationContext(applicationContext);

        context.pushRequestContext(testRequestContext);

        final AssetResolverMock assetResolverMock =
                new AssetResolverMock("assetResolverMock", expectations);
        PrivateAccessor.setField(context, "assetResolver", assetResolverMock);

        // set up the ImageAsset
        ImageAsset imageAsset = new ConvertibleImageAsset();
        imageAsset.setValue("rotatingWorld{$index}.gif");
        imageAsset.setSequence(true);
        imageAsset.setSequenceSize(2);

        ImageAsset imageAsset0 = (ImageAsset) imageAsset.clone();
        imageAsset0.setValue("rotatingWorld0.gif");

        ImageAsset imageAsset1 = (ImageAsset) imageAsset.clone();
        imageAsset1.setValue("rotatingWorld1.gif");

        assetResolverMock.expects.computeURLAsString(imageAsset0)
                .returns("rotatingWorld0.gif").any();
        assetResolverMock.expects.computeURLAsString(imageAsset1)
                .returns("rotatingWorld1.gif").any();

        // perform the tests
        String[] result = context.retrieveImageAssetURLAsString(imageAsset);

        assertEquals("The array is not of the correct size ", 2, result.length);

        String expectedString0 = "rotatingWorld0.gif";
        assertEquals("The image asset url at index 0 is wrong ",
                expectedString0, result[0]);

        String expectedString1 = "rotatingWorld1.gif";
        assertEquals("The image asset url at index 1 is wrong ",
                expectedString1, result[1]);
    }

    /**
     * Test the getPane() method.
     */
    public void testGetPane() throws RepositoryException {
        Format pane = new Pane(canvasLayout);
        pane.setName("pane");

        activateAndSetDeviceLayout();

        assertSame(pane, context.getPane("pane"));

        Fragment fragment = new Fragment(canvasLayout);
        Format dp = new DissectingPane(canvasLayout);
        dp.setName("dissectingPane");
        canvasLayout.removeFormat(dp);
        fragment.addFormat(dp);

        activateAndSetDeviceLayout();

        context.setCurrentFragment(fragment);
        assertSame(dp, context.getPane("dissectingPane"));
    }

    private void activateAndSetDeviceLayout() {
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        DeviceLayoutContext dlc = new DeviceLayoutContext();
        dlc.setDeviceLayout(runtimeDeviceLayout);
        context.pushDeviceLayoutContext(dlc);
    }

    /**
     * Test the setCurrentFragment() method.
     */
    public void testSetCurrentFragment() throws RepositoryException {
        Fragment fragment = new Fragment(canvasLayout);
        activateAndSetDeviceLayout();

        context.setCurrentFragment(fragment);

        assertSame(fragment, context.getCurrentFragment());

        Format pane = new Pane(canvasLayout);
        pane.setName("pane");


        assertSame(pane, context.getPane("pane"));

        Format dp = new DissectingPane(canvasLayout);
        dp.setName("dissectingPane");
        canvasLayout.removeFormat(dp);
        fragment.addFormat(dp);

        context.setCurrentFragment(fragment);

        assertNull(context.getPane("pane"));

        assertSame(dp, context.getPane("dissectingPane"));

        Fragment fragment2 = new Fragment(canvasLayout);

        context.setCurrentFragment(fragment2);

        assertSame(pane, context.getPane("pane"));
        assertNull(context.getPane("dissectingPane"));
    }

    /**
     * Test updateFormFragmentationState
     */
    public void testUpdateFormFragmentationState() throws RepositoryException {
        // The layout consists of an outer fragment that contains an inner
        // fragment which contains a grid. The grid has two fragmented form
        // fields containing panes.
        Fragment outerFrag = new Fragment(canvasLayout);
        outerFrag.setName("outerFrag");
        canvasLayout.setRootFormat(outerFrag);

        Fragment innerFrag = new Fragment(canvasLayout);
        innerFrag.setName("innerFrag");
        innerFrag.setParent(outerFrag);

        FormFragment formFrag1 = new FormFragment(canvasLayout);
        formFrag1.setName("formFrag1");

        FormFragment formFrag2 = new FormFragment(canvasLayout);
        formFrag2.setName("formFrag2");

        Form form = new Form(canvasLayout);
        form.setName("form");
        form.addFormFragment(formFrag1);
        form.addFormFragment(formFrag2);
        form.setParent(innerFrag);

        Grid grid = new Grid(canvasLayout);
        grid.setRows(2);
        grid.setColumns(1);
        grid.setParent(form);
        grid.attributesHaveBeenSet();
        formFrag1.setParent(grid);
        formFrag2.setParent(grid);

        Pane pane1 = new Pane(canvasLayout);
        pane1.setName("pane1");
        pane1.setParent(formFrag1);

        Pane pane2 = new Pane(canvasLayout);
        pane2.setName("pane2");
        pane2.setParent(formFrag2);

        MarinerURL requestURL = context.getRequestURL(false);
        requestURL.setParameterValue("vfrag", "f1");

        try {
            // Set the children of the formats.
            outerFrag.setChildAt(innerFrag, 0);
            innerFrag.setChildAt(grid, 0);
            grid.setChildAt(formFrag1, 0);
            grid.setChildAt(formFrag2, 1);
            formFrag1.setChildAt(pane1, 0);
            formFrag2.setChildAt(pane2, 0);

            // Set up the FormatScopes correctly.
            DeviceLayoutReplicator replicator = new DeviceLayoutReplicator();
            replicator.replicate(canvasLayout);
        } catch (LayoutException e) {
            e.printStackTrace();
            fail("Error setting up test: " + e.getMessage());
        }
        activateAndSetDeviceLayout();

        // Perform the unit test by trying to display the contents of the
        // inner fragment
        context.setCurrentFragment(innerFrag);
        context.updateFormFragmentationState(form);

        // Check that the current form fragment has been set to formFrag1
        assertSame("Current form frag should be formFrag1", formFrag1,
                context.getCurrentFormFragment());
    }


    public void testPeekSelectState() throws Exception {
        // testPushSelectState does the non-exception testing of this method
        try {
            context.peekSelectState();

            fail("Should have had an empty stack exception thrown");
        } catch (EmptyStackException e) {
            // Expected condition
        }
    }


    public void testPushSelectState() throws Exception {
        SelectState initialState = new SelectState(Precept.MATCH_FIRST,
                BooleanValue.TRUE);
        SelectState secondState = new SelectState(Precept.MATCH_EVERY,
                BooleanValue.FALSE);

        context.pushSelectState(initialState);

        assertSame("Expected to find the initial State",
                context.peekSelectState(),
                initialState);

        context.pushSelectState(secondState);

        assertSame("Expected to find the second State",
                context.peekSelectState(),
                secondState);
    }

    public void testPopSelectState() throws Exception {
        SelectState initialState = new SelectState(Precept.MATCH_FIRST,
                BooleanValue.TRUE);
        SelectState secondState = new SelectState(Precept.MATCH_EVERY,
                BooleanValue.FALSE);

        context.pushSelectState(initialState);
        context.pushSelectState(secondState);

        assertSame("Expected to find the second State",
                context.popSelectState(),
                secondState);
        assertSame("Expected to find the initial State",
                context.popSelectState(),
                initialState);

        try {
            context.popSelectState();

            fail("Should have had an empty stack exception thrown");
        } catch (EmptyStackException e) {
            // Expected condition
        }
    }

    /**
     * Test that getAbsolutePageBaseURL works as expected.
     */
    public void testGetAbsolutePageBaseURL()
            throws Exception {

        // HACK: set the real context volantis to our overriden one because
        // the methods under test use the real one.
        // Probably we should avoid redefining the context's volantis it as it
        // will break a load of other methods as well.
        PrivateAccessor.setField(context, "volantisBean", volantis);

        // Use the usual value for base url.
        PrivateAccessor.setField(volantis, "baseURL", new MarinerURL("/"));

        // Test a "null" page base.
        PrivateAccessor.setField(volantis, "pageBase", null);
        assertEquals("", "/", context.getAbsolutePageBaseURL());

        // Test an empty string page base.
        PrivateAccessor.setField(volantis, "pageBase", "");
        assertEquals("", "/", context.getAbsolutePageBaseURL());

        // Test a whitespace string page base.
        PrivateAccessor.setField(volantis, "pageBase", " ");
        assertEquals("", "/", context.getAbsolutePageBaseURL());

        // Test the historically defined "normal" page base.
        PrivateAccessor.setField(volantis, "pageBase", "volantis");
        assertEquals("", "/volantis/", context.getAbsolutePageBaseURL());

        // Test the (presumably) proper definition of page base.
        PrivateAccessor.setField(volantis, "pageBase", "volantis/");
        assertEquals("", "/volantis/", context.getAbsolutePageBaseURL());
    }

    /**
     * This tests creating branded names from a variety of name and brand
     * strings as may be expected in a running MCS.
     */
    public void testGetBrandedName() throws Exception {
        // @todo Later This was added as an L3 to test the slash handling of
        // @todo       the getBrandedName(...) method.  However the other paths
        // @todo       through the code should also be tested.

        // Test data
        final String componentName = "name";
        final String brand = "brand";
        final String slash = "/";

        String brandedName = null;

        // Get a branded name where the brand does not end in a slash but the
        // name starts with a slash
        brandedName = MarinerPageContext.getBrandedName(slash + componentName,
                brand);
        // And check it is as expected
        assertEquals("Branded name should match that expected (1)",
                brandedName,
                brand + slash + componentName);

        // Get a branded name where the brand ends in a slash but the name
        // does not start with a slash
        brandedName = MarinerPageContext.getBrandedName(componentName,
                brand + slash);
        // And check it is as expected
        assertEquals("Branded name should match that expected (2)",
                brandedName,
                brand + slash + componentName);

        // Get a branded name where the brand ends in a slash and the name
        // starts with a slash
        brandedName = MarinerPageContext.getBrandedName(slash + componentName,
                brand + slash);
        // And check it is as expected
        assertEquals("Branded name should match that expected (3)",
                brandedName,
                brand + slash + componentName);
    }

    public OldMarinerPageContextTestCase(String name) {
        super(name);
    }

    /**
     * Set up a dummy MarinerPageContext to avoid having to call
     * initializePage().
     * <p>
     * @todo Ideally each test case in this class should use a separate inner
     * class i.e. each test method should define a MyMarinerPageContext inner
     * class inside its method declaration. Otherwise we risk test code for
     * peer tests affecting each other.
     * <p>
     * NOTE: This is the only place where it is valid to extend
     * MarinerPageContext for testing, as that is the class under test.
     * Other test cases must use {@link TestMarinerPageContext}.
     */
    private class MyMarinerPageContext extends MarinerPageContext {
        private PageGenerationCache pageGenerationCache;
        private MarinerURL requestURL;
        private MarinerRequestContext requestContext;
        private Volantis volantis;

        public MyMarinerPageContext() throws Exception {
            // Work around the fact that the selectStateStack is private and
            // that it is only initialized in the initializePage() method
            // not called in this test environment
            PrivateAccessor.setField(this, "selectStateStack", new Stack());
        }


        /*
         * Override superclass method (non-Javadoc)
         * @see com.volantis.mcs.context.MarinerPageContext#getPageGenerationCache()
         */
        public PageGenerationCache getPageGenerationCache() {
            if (pageGenerationCache == null) {
                pageGenerationCache = new PageGenerationCache();
            }
            return pageGenerationCache;
        }

        /*
         * Override superclass method (non-Javadoc)
         * @see com.volantis.mcs.context.MarinerPageContext#getRequestURL(boolean)
         */
        public MarinerURL getRequestURL(boolean clone) {
            if (requestURL == null) {
                requestURL = new MarinerURL();
            }
            if (clone) {
                return new MarinerURL(requestURL);
            } else {
                return requestURL;
            }
        }

        /**
         * Set the request URL. This method makes it easier to test
         * MarinerPageContext
         * @param marinerURL The MarinerURL that we want to set the requestURL
         * too.
         */
        public void setRequestURL(MarinerURL marinerURL) {
            requestURL = marinerURL;
        }


        /**
         * Get the request context.
         *
         * @return The request context.
         */
        public MarinerRequestContext getRequestContext() {
            return requestContext;
        }

        /**
         * Set the requestContext
         * @param context The request context.
         */
        public void setRequestContext(MarinerRequestContext context) {
            requestContext = context;
        }

        /**
         * Get the Volantis bean.
         *
         * @return The Volantis bean.
         */
        public Volantis getVolantisBean() {
            return volantis;
        }

        /**
         * Set the Volantis bean
         * @param volantis The Volantis bean
         */
        public void setVolantisBean(Volantis volantis) {
            this.volantis = volantis;
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 27-Jun-05	8878/3	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 13-Mar-05	6842/5	emma	VBM:2005020302 Make all file/resource references in config files relative to that file

 11-Mar-05	6842/2	emma	VBM:2005020302 Making file references in config files relative to those files

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 15-Feb-05	6974/1	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 11-Mar-05	7308/2	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 17-Feb-05	6957/3	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 17-Jan-05	6693/3	allan	VBM:2005011403 Remove MPS specific image url parameters

 17-Jan-05	6693/1	allan	VBM:2005011403 Remove MPS specific image url parameters

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 12-Nov-04	6188/1	claire	VBM:2004110406 mergevbm: Ensure branded name doesn't contain slashes from brand and component name

 11-Nov-04	6185/4	claire	VBM:2004110406 Ensure branded name doesn't contain slashes from brand and component name

 08-Nov-04	6027/4	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/3	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 01-Nov-04	6068/3	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 21-Oct-04	5895/1	geoff	VBM:2004101503 chartimage path rendered icnorrectly when deployed as ROOT webapp

 21-Oct-04	5884/1	geoff	VBM:2004101503 chartimage path rendered icnorrectly when deployed as ROOT webapp

 02-Jul-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 01-Jul-04	4778/2	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 13-Feb-04	2966/2	ianw	VBM:2004011923 Added mcsi:policy function

 12-Feb-04	2761/3	mat	VBM:2004011910 Removed unusual import

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 11-Aug-03	1019/1	philws	VBM:2003080807 Provide MCS core extensions for handling the select markup element's state

 ===========================================================================
*/
