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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/dissection/ElementAnnotationTestCase.java,v 1.4 2003/02/24 13:27:45 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Jan-03    Adrian          VBM:2003011605 - Added this testcase to test 
 *                              the method isKeepTogetherElement. 
 * 06-Feb-03    Byron           VBM:2003020610 - Added testGenerateTags method.
 * 17-Feb-03    Byron           VBM:2003020610 - testGenerateTags was broken
 *                              due to changes in ProtocolConfiguration class.
 * 21-Feb-03    Phil W-S        VBM:2003022006 - Revised the test for
 *                              generateTags and added new tests.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.html.XHTMLBasic;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.mime.MultipartApplicationContext;
import com.volantis.mcs.runtime.packagers.mime.MultipartPackageHandler;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import junit.framework.TestCase;

import java.util.List;

/**
 * This class unit test the ElementAnnotationclass.
 */
public class ElementAnnotationTestCase 
    extends TestCase implements DissectionConstants {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Handy interface allowing command pattern objects to be created that
     * execute in an environment configured by the doElementAnnotationTest
     * method.
     */
    protected static interface ElementAnnotationTestSteps {
        /**
         * Implement this method to perform an annotation test on the supplied
         * annotation object within the given context.
         *
         * @param context
         * @param annotation
         * @throws Exception
         */
        void execute(MarinerRequestContext context,
                     ElementAnnotation annotation) throws Exception;
    }

    /**
     * This method tests the method protected boolean isKeepTogetherElement ( )
     * for the com.volantis.mcs.protocols.dissection.ElementAnnotation class.
     */
    public void testIsKeepTogetherElement()
            throws Exception {
        //
        // Test public boolean getAlwaysEmpty ( ) method.
        //
        Element element = domFactory.createElement();
        element.setName(KEEPTOGETHER_ELEMENT); 
        
        ElementAnnotation ea = new ElementAnnotation();        
        ea.setElement(element);
        
        assertTrue("isKeepTogetherElement should return true as the element" +
                "is a keepTogether and this is the first call to the method.",
                ea.isKeepTogetherElement());
        
        assertTrue("isKeepTogetherElement should return false as the element" +
                "is a keepTogether and this is the second call to the method.",
                !ea.isKeepTogetherElement());
        
        
        // Check the method returns false when the element is not a
        // keepTogether element.
        element.setName("myElement");
        ea = new ElementAnnotation();
        ea.setElement(element);
        
        assertTrue("isKeepTogetherElement should return false as the element" +
                "is not a keepTogether", !ea.isKeepTogetherElement());
    }

    protected void doElementAnnotationTest(ElementAnnotationTestSteps steps)
        throws Exception {
        ElementAnnotation annotation = new ElementAnnotation();

        // Create an element with an arbitrary name
        Element element = domFactory.createElement("href");

        // Initialize all the contexts (page,request,application).
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        pageContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ApplicationContext applicationContext =
            new MultipartApplicationContext(requestContext);

        applicationContext.setPackager(new MultipartPackageHandler());
        ContextInternals.setApplicationContext(requestContext,
                                               applicationContext);

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        // In order to perform this testing we will need to use a protocol that
        // has a url mappings defined. I have chosen XHTMLBasic and will test
        // against the object/src attribute and reference.
        ProtocolBuilder builder = new ProtocolBuilder();
        XHTMLBasic protocol = (XHTMLBasic) builder.build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                internalDevice);
        protocol.setMarinerPageContext(pageContext);
        annotation.setProtocol(protocol);

        annotation.setElement(element);

        // Now that the environment is fully set up, try running the actual
        // test steps required
        steps.execute(requestContext, annotation);
    }

    /**
     * Test the generation dissected content.
     * @throws Exception
     */
    public void testGenerateDissectedContents() throws Exception {
        doElementAnnotationTest(
            new ElementAnnotationTestSteps() {
                public void execute(MarinerRequestContext context,
                                    ElementAnnotation annotation)
                    throws Exception {
                    // Set up the second part of the test where the element has
                    // a name in the list of asset URL locations. The element
                    // should have an attribute which is also defined in the
                    // map. Also, there should be a map between the url and an
                    // asset.
                    Element element = annotation.element;
                    ApplicationContext ac =
                        ContextInternals.getApplicationContext(context);
                    PackageResources packageResources =
                        ac.getPackageResources();
                    final String elementName = "object";
                    final String url = "/fudged/candidate/asset/url";

                    element.setName(elementName);
                    element.setAttribute("src", url);

                    PackageResources.Asset asset = new PackageResources.Asset(
                        elementName, false);

                    packageResources.addAssetURLMapping(url, asset);

                    ReusableStringBuffer rsb = new ReusableStringBuffer();

                    packageResources.initializeEncodedURLs();

                    annotation.generateDissectedContents(rsb);

                    List encodedURLs = packageResources.getEncodedURLs();
                    assertNotNull("There should be encoded URLs",
                                  encodedURLs);
                    assertTrue("Encoded url list should contain " + url,
                               encodedURLs.contains(url));
                    assertEquals("Generated markup was not as",
                                 "<object src=\"" + url + "\"></object>",
                                 rsb.toString());
                }
            }
        );
    }

    public void testGenerateTags() throws Exception {
        doElementAnnotationTest(
            new ElementAnnotationTestSteps() {
                public void execute(MarinerRequestContext context,
                                    ElementAnnotation annotation)
                    throws Exception {
                    assertTrue("Tags should not be generated yet",
                               !annotation.generatedTags);

                    annotation.generateTags();

                    assertTrue("Tags should now be generated",
                               annotation.generatedTags);
                }
            }
        );
    }

    public void testGenerateShardContentsImpl() throws Exception {
        doElementAnnotationTest(
            new ElementAnnotationTestSteps() {
                public void execute(MarinerRequestContext context,
                                    ElementAnnotation annotation)
                    throws Exception {
                    // Set up the second part of the test where the element has
                    // a name in the list of asset URL locations. The element
                    // should have an attribute which is also defined in the
                    // map. Also, there should be a map between the url and an
                    // asset.
                    Element element = annotation.element;
                    ApplicationContext ac =
                        ContextInternals.getApplicationContext(context);
                    PackageResources packageResources =
                        ac.getPackageResources();
                    final String elementName = "object";
                    final String url = "/fudged/candidate/asset/url";

                    element.setName(elementName);
                    element.setAttribute("src", url);

                    PackageResources.Asset asset = new PackageResources.Asset(
                        elementName, false);

                    packageResources.addAssetURLMapping(url, asset);

                    ReusableStringBuffer rsb = new ReusableStringBuffer();

                    packageResources.initializeEncodedURLs();

                    annotation.generateShardContentsImpl(rsb, 1, true);

                    List encodedURLs = packageResources.getEncodedURLs();
                    assertNotNull("There should be encoded URLs",
                                  encodedURLs);
                    assertTrue("Encoded url list should contain " + url,
                               encodedURLs.contains(url));
                    assertEquals("Generated markup was not as",
                                 "<object src=\"" + url + "\"></object>",
                                 rsb.toString());
                }
            }
        );
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
