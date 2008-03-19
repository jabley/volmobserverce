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
/*----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 *----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import junit.framework.TestCase;
import junit.framework.Assert;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.testtools.mocks.MockFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IMarker;
import org.jdom.Element;


/**
 * Basic testcase. This does NOT fully test the MarkerGeneratingErrorReporter.
 * It only tests getXPath(XPath)
 */
public class MarkerGeneratingErrorReporterTestCase extends TestCase {

    /**
     * The location of the file used by the MockFile IResource
     */
    private static final String POLICY_FILE =
             "com/volantis/mcs/eclipse/common/imageComponent.NOHIST.vic";

    /*
     * The MGER on which to run tests
     */
    private MarkerGeneratingErrorReporter mger;

    /**
     * An attribute key for identifying the encoded namespaces attribute
     * stored on a marker. This is a duplicate of the NAMESPACES_ATTRIBUTE that
     * exists in MarkerGeneratingErrorReporter
     */
    private static final String NAMESPACES_ATTRIBUTE =
            XPath.class.getName() + "/NAMESPACES"; //$NON-NLS-1$


    /**
     * The resource used to create the MGER
     */
    private IResource resource;

    /**
     * The DeviceRepositoryAccessorManager to use for the tests.
     */
    private static DeviceRepositoryAccessorManager deviceRAM;

    /**
     * Test case constructor
     * @param name the name of the test.
     */
    public MarkerGeneratingErrorReporterTestCase(String name){
        super(name);
    }

    /**
     * Test the behviour of the getXPath(XPath) method.
      * @throws Exception
     */
    public void testGetXPath() throws Exception {

        resource = new MockFile(POLICY_FILE);


        // set up a number of paths for the test
        XPath root = new XPath("/");
        XPath aPath1 = new XPath("/aone");
        XPath aPath2 = new XPath("/aone/atwo");

        XPath bPath1 = new XPath("/bone");
        XPath bPath2 = new XPath("/bone/btwo");

        XPath bPath3 = new XPath("/bone/bthree");
        XPath cPath1 = new XPath("/cone");


        mger = new MarkerGeneratingErrorReporter(resource,
                new Element("aone"), null);

        // create PROBLEM markers from the paths
        IMarker marker1 = resource.createMarker(IMarker.PROBLEM);
        marker1.setAttribute(XPath.class.getName(), aPath2.getExternalForm());
        marker1.setAttribute(NAMESPACES_ATTRIBUTE, ":aPath2Test");

        IMarker marker2 = resource.createMarker(IMarker.PROBLEM);
        marker2.setAttribute(XPath.class.getName(), aPath1.getExternalForm());
        marker2.setAttribute(NAMESPACES_ATTRIBUTE, ":aPath1Test");

        IMarker marker3 = resource.createMarker(IMarker.PROBLEM);
        marker3.setAttribute(XPath.class.getName(), bPath2.getExternalForm());
        marker3.setAttribute(NAMESPACES_ATTRIBUTE, ":bPath2Test");

        // create some MESSAGE markers
        IMarker marker4 = resource.createMarker(IMarker.MESSAGE);
        marker4.setAttribute(XPath.class.getName(), bPath1.getExternalForm());
        marker4.setAttribute(NAMESPACES_ATTRIBUTE, ":bPath2Test");

        IMarker marker5= resource.createMarker(IMarker.MESSAGE);
        marker5.setAttribute(XPath.class.getName(), bPath3.getExternalForm());
        marker5.setAttribute(NAMESPACES_ATTRIBUTE, ":cPath1Test");


        IMarker markerc = resource.createMarker(IMarker.MESSAGE);
        markerc.setAttribute(XPath.class.getName(), cPath1.getExternalForm());
        markerc.setAttribute(NAMESPACES_ATTRIBUTE, ":cPathTest");

        // test to ensure that XPath is NOT returned for the correct path string but incorrect namespace
        Assert.assertEquals("try to obtain xPath using correct namespace",
                bPath2.getExternalForm(),
                mger.getXPath(marker3).getExternalForm());

        // test marker with no XPath string or NAMESPACES_ATTRIBUTE (return null)
        IMarker marker6 = resource.createMarker(IMarker.PROBLEM);
        Assert.assertNull("Test for invalid marker (marker does not contain an XPath or"+
                "a NAMESPACE_ATTRIBUTE)", mger.getXPath(marker6));

        // test marker with no XPath string.
        marker6.setAttribute(NAMESPACES_ATTRIBUTE, "cPath1Test");
        Assert.assertNull("IMarker does not have a XPath.class.getName() attribute but does have a " +
                "NAMESPACES_ATTRIBUTE", mger.getXPath(marker6));

        // bPath3 is a MESSAGE (/bone/bthree) but marker3 is a PROBLEM  marker and is in the
        // same path
        IMarker marker7 = resource.createMarker(IMarker.PROBLEM);
        marker7.setAttribute(XPath.class.getName(),bPath3.getExternalForm());
        Assert.assertEquals("IMarker does not have a valid NAMESPACES_ATTRIBUTE so a match should be found",
            bPath3.getExternalForm(), mger.getXPath(marker7).getExternalForm());

        try{ // should throw an exception
            mger.getXPath(markerc);
            Assert.fail("An exception should have been thrown"); // force failure if exception not thrown
        }catch(IllegalStateException ise){
            // success. An exception should have been thrown
        }


    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/3	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 27-Apr-04	3983/3	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 27-Apr-04	3983/1	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 ===========================================================================
*/
