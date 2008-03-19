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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/PageHeadTestCase.java,v 1.5 2003/02/06 11:38:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Nov-02    Adrian          VBM:2002100404 - Created this class as a
 *                              testcase for PageHead
 * 01-Dec-02    Phil W-S        VBM:2002112901 - Update after refactoring.
 * 13-Dec-02    Phil W-S        VBM:2002110516 - Changes for refactored
 *                              PageHead.
 * 19-Dec-02    Phil W-S        VBM:2002121601 - Fix tests after change to way
 *                              in which device layouts are used.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses the 
 *                              new TestMariner...Context classes rather than 
 *                              "cut & paste" inner classes which extend 
 *                              Mariner...Context.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.css.CssCandidate;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This class unit test the PageHeadclass.
 */
public class PageHeadTestCase
    extends TestCase {

    /**
     * constant to represent pane names.
     */
    private static final String PANE_NAME = "MyPane";

    /**
     * Used for format style handling.
     */
    private static final Pane PANE = new Pane(null);

    static {
        PANE.setName(PANE_NAME);
        PANE.setInstance(0);
    }

    /**
     * flag to test if URLTestCssCandidate.writeCss was called
     */
    private boolean urlWriteCss;

    /**
     * static MarinerRequestContext to return from MyMarinerPageContext
     */
    private static MarinerRequestContext requestContext
            = new TestMarinerRequestContext();

    private TestMarinerPageContext context;
    private InternalDevice internalDevice;

    /**
     * Construct a new instance of PageHeadTestCase.
     * @param name The name of the testcase.
     */
    public PageHeadTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.protocols.PageHead class.
     */
    public void testConstructors() {
        //
        // Test public PageHead ( ) constructor
        //
        // Nothing to test.
    }

    /**
     * Set up the member variables required for the tests.
     * @throws Exception if a problem occured setting up the member variables
     */
    protected void setUp() throws Exception {
        super.setUp();
        context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        context.setDeviceName("Master");
        context.pushDeviceLayoutContext(new DeviceLayoutContext());

        internalDevice = InternalDeviceTestHelper.createTestDevice();

    }

    /**
     * Reset the values of the testcase member variables.
     * @throws Exception if a problem occurred resetting the member variables.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        urlWriteCss = false;
    }

    /**
     * This method tests the method public void release ( )
     * for the com.volantis.mcs.protocols.PageHead class.
     */
    public void notestRelease()
        throws Exception {
        //
        // Test public void release ( ) method.
        //
        Assert.fail("public void release ( ) not tested.");
    }

    /**
     * This method tests the method public OutputBuffer getInitial ( )
     * for the com.volantis.mcs.protocols.PageHead class.
     */
    public void notestGetInitial()
        throws Exception {
        //
        // Test public OutputBuffer getInitial ( ) method.
        //
        Assert.fail("public OutputBuffer getInitial ( ) not tested.");
    }

    /**
     * This method tests the method public OutputBuffer getHead ( )
     * for the com.volantis.mcs.protocols.PageHead class.
     */
    public void notestGetHead()
        throws Exception {
        //
        // Test public OutputBuffer getHead ( ) method.
        //
        Assert.fail("public OutputBuffer getHead ( ) not tested.");
    }

    /**
     * This method tests the method public OutputBuffer getScript ( )
     * for the com.volantis.mcs.protocols.PageHead class.
     */
    public void notestGetScript()
        throws Exception {
        //
        // Test public OutputBuffer getScript ( ) method.
        //
        Assert.fail("public OutputBuffer getScript ( ) not tested.");
    }

    /**
     * This method tests the method public void setAttribute ( String,Object )
     * for the com.volantis.mcs.protocols.PageHead class.
     */
    public void notestSetAttribute()
        throws Exception {
        //
        // Test public void setAttribute ( String,Object ) method.
        //
        Assert.fail("public void setAttribute ( String,Object ) not tested.");
    }

    /**
     * This method tests the method public Object getAttribute ( String )
     * for the com.volantis.mcs.protocols.PageHead class.
     */
    public void notestGetAttribute()
        throws Exception {
        //
        // Test public Object getAttribute ( String ) method.
        //
        Assert.fail("public Object getAttribute ( String ) not tested.");
    }

    /**
     * This method tests the method public void writeCssCandidates ( )
     * for the com.volantis.mcs.protocols.PageHead class.
     */
    public void testWriteCssCandidates()
        throws Exception {
        //
        // Test public void writeCssCandidates ( ) method.
        //
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);

        protocol.setMarinerPageContext(context);

        PageHead head = new FakeHeadPageHead();

        CssCandidate candidate = new URLTestCssCandidate();
        head.addURLCssCandidate(candidate);

//        candidate = new DeviceLayoutTestCssCandidate(protocol);
//        head.addDeviceLayoutCssCandidate(candidate);

//        candidate = new DeviceThemeTestCssCandidate(protocol);
//        head.addDeviceThemeCssCandidate(candidate);

        head.getHead();
        head.writeCssCandidates(protocol);

        assertTrue("Did not write URLCssCandidate", urlWriteCss);
//        assertTrue("Did not write DeviceLayoutCssCandidate", layoutWriteCss);
//        assertTrue("Did not write DeviceThemeCssCandidate", themeWriteCss);
    }

    /**
     * specialisation of URLCssCandidate to add to urlCandidates
     * list in PageHead
     */
    private class URLTestCssCandidate implements CssCandidate {

        public void writeCss(VolantisProtocol protocol,
                             OutputBuffer buffer) {
            urlWriteCss = true;
        }
    }

    private class FakeHeadPageHead extends PageHead {

        public FakeHeadPageHead() {
            super();
        }

        public OutputBuffer getBuffer(String name, boolean create) {
            DOMOutputBuffer buffer = new DOMOutputBuffer();
            buffer.initialise();
            return buffer;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/3	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Jul-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
