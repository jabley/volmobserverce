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
package com.volantis.mcs.eclipse.common;

import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.testtools.mocks.MockFile;
import junit.framework.TestCase;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 * This class tests PolicyUtils
 */
public class PolicyUtilsTestCase extends TestCase {
    private static final String POLICY_FILE =
            "com/volantis/mcs/eclipse/common/imageComponent.NOHIST.vic";

    /**
     * Test findProblemMarkers.
     */
    public void testFindProblemMarkers() throws Exception {
        IResource resource = new MockFile(POLICY_FILE);

        XPath root = new XPath("/");
        XPath aPath1 = new XPath("/aone");
        XPath aPath2 = new XPath("/aone/atwo");

        XPath bPath1 = new XPath("/bone");
        XPath bPath2 = new XPath("/bone/btwo");

        IMarker marker1 = resource.createMarker(IMarker.PROBLEM);
        marker1.setAttribute(XPath.class.getName(), aPath2.getExternalForm());
        IMarker marker2 = resource.createMarker(IMarker.PROBLEM);
        marker2.setAttribute(XPath.class.getName(), aPath1.getExternalForm());
        IMarker marker3 = resource.createMarker(IMarker.PROBLEM);
        marker3.setAttribute(XPath.class.getName(), bPath2.getExternalForm());
        IMarker marker4 = resource.createMarker(IMarker.MESSAGE);
        marker4.setAttribute(XPath.class.getName(), bPath1.getExternalForm());

        IMarker markers [];

        markers = PolicyUtils.findProblemMarkers(resource, root);

        List markerList = Arrays.asList(markers);

        assertEquals("There should be 3 markers.", 3, markers.length);
        assertTrue("Expected to find /aone marker",
                markerList.contains(marker1));
        assertTrue("Expected to find /aone/atwo marker",
                markerList.contains(marker2));
        assertTrue("Expected to find /bone/btwo marker",
                markerList.contains(marker3));

        markers = PolicyUtils.findProblemMarkers(resource, aPath2);
        markerList = Arrays.asList(markers);

        assertEquals("There can be only 1 ... marker", 1, markers.length);
        assertTrue("Expected to find /aone/atwo marker",
                markerList.contains(marker1));

        markers = PolicyUtils.findProblemMarkers(resource, aPath1);
        markerList = Arrays.asList(markers);

        assertEquals("There should be 2 markers.", 2, markers.length);
        assertTrue("Expected to find /aone marker",
                markerList.contains(marker1));
        assertTrue("Expected to find /aone/atwo marker",
                markerList.contains(marker2));
    }

    /**
     * Tests PolicyUtils.findProblemMarkers(IResource, Element, XPath).
     */
    public void testFindProblemMarkersWithElement() throws Throwable {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
//                DeviceRepositoryAccessorManager deviceRAM =
//                        new DeviceRepositoryAccessorManager(
//                                repository.getPath(),
//                                new TestTransformerMetaFactory(),
//                                new DefaultJDOMFactory(), false);
//                IResource resource = new MockFile(
//                        deviceRAM.getDeviceRepositoryName());
                IResource resource = new MockFile(repository.getPath());

                // Create a tree for the dialog.
                LPDMODOMFactory factory = new LPDMODOMFactory();


                // Create first subtree
                Element parent1 = factory.element("parent1");
                Element child1a = factory.element("child1a");
                child1a.setAttribute("attr1", "value1");
                child1a.setAttribute("attr2", "value2");
                Element child1b = factory.element("child1b");
                parent1.addContent(child1a);
                parent1.addContent(child1b);

                // Create second subtree
                Element parent2 = factory.element("parent2");
                parent2.setAttribute("attr1", "value1");
                parent2.setAttribute("attr2", "value2");
                parent2.setAttribute("attr3", "value3");
                parent2.setAttribute("attr4", "value4");
                Element child2a = factory.element("child2a");
                Element child2b = factory.element("child2b");

                parent2.addContent(child2a);
                parent2.addContent(child2b);

                // Create the root element and add content
                Element rootNode = factory.element("root");
                rootNode.addContent(parent1);
                rootNode.addContent(parent2);

                XPath rootPath = new XPath(rootNode);
                XPath parent1Path = new XPath(parent1);
                XPath child1aPath = new XPath(child1a);
                XPath child1bPath = new XPath(child1b);

                XPath parent2Path = new XPath(parent2);
                XPath child2aPath = new XPath(child2a);
                XPath child2bPath = new XPath(child2b);


                IMarker marker0 = resource.createMarker(IMarker.PROBLEM);
                marker0.setAttribute(XPath.class.getName(),
                        rootPath.getExternalForm());
                marker0.setAttribute(Element.class.getName(), rootNode.getName());

                IMarker marker1 = resource.createMarker(IMarker.PROBLEM);
                marker1.setAttribute(XPath.class.getName(),
                        parent1Path.getExternalForm());
                marker1.setAttribute(Element.class.getName(), rootNode.getName());

                IMarker marker2 = resource.createMarker(IMarker.PROBLEM);
                marker2.setAttribute(XPath.class.getName(),
                        child1aPath.getExternalForm());
                marker2.setAttribute(Element.class.getName(), child1a.getName());

                IMarker marker3 = resource.createMarker(IMarker.PROBLEM);
                marker3.setAttribute(XPath.class.getName(),
                        child1bPath.getExternalForm());
                marker3.setAttribute(Element.class.getName(), child1b.getName());

                IMarker marker4 = resource.createMarker(IMarker.PROBLEM);
                marker4.setAttribute(XPath.class.getName(),
                        parent2Path.getExternalForm());
                marker4.setAttribute(Element.class.getName(), parent2.getName());

                IMarker marker5 = resource.createMarker(IMarker.PROBLEM);
                marker5.setAttribute(XPath.class.getName(),
                        child2aPath.getExternalForm());
                marker5.setAttribute(Element.class.getName(), parent2.getName());

                IMarker marker6 = resource.createMarker(IMarker.PROBLEM);
                marker6.setAttribute(XPath.class.getName(),
                        child2bPath.getExternalForm());
                marker6.setAttribute(Element.class.getName(), parent2.getName());

                // Find markers associated with parent2
                IMarker[] markers = PolicyUtils.findProblemMarkers(resource,
                        parent2.getName(), parent2Path);
                List markerList = Arrays.asList(markers);

                // There should be markers for parent2, child2a and child2b.
                assertEquals(3, markerList.size());

                // Find markers associated with root.
                markers = PolicyUtils.findProblemMarkers(resource,
                        rootNode.getName(), rootPath);
                markerList = Arrays.asList(markers);

                // There should be markers for root and parent1
                assertEquals(2, markerList.size());

                // Find markers associated with parent1
                markers = PolicyUtils.findProblemMarkers(resource,
                        parent1.getName(), parent1Path);
                markerList = Arrays.asList(markers);

                // There are no markers associated with parent1
                assertEquals(0, markerList.size());
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 05-Apr-04	3706/1	pcameron	VBM:2004040108 Fixes to PolicyUtils and MarkerGeneratingErrorReporter

 31-Mar-04	3664/3	pcameron	VBM:2004032202 A few tweaks to PolicyUtils, PolicyUtilsTestCase and MarkerGeneratingErrorReporter

 31-Mar-04	3664/1	pcameron	VBM:2004032202 Added a new findProblemMarkers and test

 30-Dec-03	2258/7	allan	VBM:2003121725 Undefuncted PolicyUtilsTestCase.

 30-Dec-03	2258/5	allan	VBM:2003121725 Undefuncted PolicyUtilsTestCase.

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 03-Nov-03	1698/9	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 29-Oct-03	1698/4	pcameron	VBM:2003102411 Added classname from element name, and PolicyUtils

 ===========================================================================
*/
