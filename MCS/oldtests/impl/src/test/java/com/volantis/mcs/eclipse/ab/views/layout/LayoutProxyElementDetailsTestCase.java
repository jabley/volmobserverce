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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.repository.xml.XMLRepositoryConstants;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Test the LayoutProxyElementDetails class.
 */
public class LayoutProxyElementDetailsTestCase extends TestCaseAbstract {
    private String fieldSelectedElementNames = "selectedElementNames";

    /**
     * Test the computation of the intersection for various attributes.
     *
     * The following shows a list of each format and their attributes (with
     * their first letter converted to lower case.
     *
     * columnIteratorPaneFormat
     * ------------------------
     * name,backgroundColour,backgroundComponent, backgroundComponentType,
     * borderWidth,cellPadding,cellSpacing,destinationArea, verticalAlignment,
     * horizontalAlignment,height,width,widthUnits, optimizationLevel,filterText,
     *
     * dissectingPaneFormat
     * ------------------------
     * name,backgroundColour,backgroundComponent,backgroundComponentType,
     * borderWidth,cellPadding,cellSpacing,verticalAlignment,horizontalAlignment,
     * maximumContentSize,width,widthUnits,filterText,nextShardLinkText,
     * nextShardLinkClass,nextShardShortcut,previousShardLinkText,
     * previousShardLinkClass,previousShardShortcut,shardLinkOrder,
     *
     * formFormat:
     * ------------------------
     * name,
     *
     * formFragmentFormat:
     * ------------------------
     * name,parentFormName,nextLinkText,nextLinkPosition,nextLinkStyle,
     * previousLinkText,previousLinkPosition,previousLinkStyle,reset,
     *
     * fragmentFormat:
     * ------------------------
     * name,parentFragmentName,linkText,backLinkText,fragLinkClass,peerLinks,
     * fragLinkOrder,defaultFragmentName,
     *
     * gridFormat:
     * ------------------------
     * backgroundColour,backgroundComponent,backgroundComponentType,borderWidth,
     * cellPadding,cellSpacing,verticalAlignment,horizontalAlignment,width,
     * widthUnits,height,optimizationLevel,name,
     *
     * paneFormat:
     * ------------------------
     * name,backgroundColour,backgroundComponent,backgroundComponentType,
     * borderWidth,cellPadding,cellSpacing,destinationArea,verticalAlignment,
     * horizontalAlignment,height,width,widthUnits,optimizationLevel,filterText,
     *
     * regionFormat:
     * ------------------------
     * name,destinationArea,
     *
     * replicaFormat:
     * ------------------------
     * name,replicant,replicantType,
     *
     * rowIteratorPaneFormat:
     * ------------------------
     * name,backgroundColour,backgroundComponent,backgroundComponentType,
     * borderWidth,cellPadding,cellSpacing,destinationArea,verticalAlignment,
     * horizontalAlignment,height,width,widthUnits,optimizationLevel,filterText,
     *
     * segmentFormat:
     * ------------------------
     * name,frameBorder,borderColour,scrolling,marginHeight,marginWidth,resize,
     * defaultSegmentName,
     *
     * segmentGridFormat:
     * ------------------------
     * frameBorder,frameSpacing,borderColour,borderWidth,
     *
     * spatialFormatIterator:
     * ------------------------
     * name,backgroundColour,backgroundComponent,backgroundComponentType,
     * borderWidth,cellPadding,cellSpacing,verticalAlignment,horizontalAlignment,
     * width,widthUnits,height,iteratorSeparator,spatialIterator2DIndexingDir,
     * spatialIteratorRows,spatialIteratorRowCount,spatialIteratorColumns,
     * spatialIteratorColumnCount,
     *
     * temporalFormatIterator
     * ------------------------
     * name,backgroundColour,backgroundComponent,backgroundComponentType,
     * borderWidth,cellPadding,cellSpacing,verticalAlignment,horizontalAlignment,
     * width,widthUnits,height,iteratorSeparator,temporalIteratorClockValues,
     * temporalIteratorCells,temporalIteratorCellCount,
     *
     * @throws Exception
     */
    public void testComputeIntersection() throws Exception {

        List elementNameList = null;
        doTestComputeIntersection(elementNameList, getEmptyList());

        elementNameList = new ArrayList();
        doTestComputeIntersection(elementNameList, getEmptyList());

        elementNameList.add("UnknownElement");
        doTestComputeIntersection(elementNameList, getEmptyList());

        LayoutProxyElementDetails details = new LayoutProxyElementDetails();
        List formats = new ArrayList(
                (List) PrivateAccessor.getField(details, "formats"));

        // Test all the elements and their attributes.
        Iterator iterator = formats.iterator();
        while (iterator.hasNext()) {
            Format format = (Format) iterator.next();

            final String elementName = format.getFormatType().getElementName();
            elementNameList.clear();
            elementNameList.add(elementName);
            final String[] list = getAttributes(details, elementName, formats);
            //System.out.println(elementName + ": " + dumpArray(list));
            doTestComputeIntersection(elementNameList, list);
        }

        elementNameList.clear();
        elementNameList.add("formFormat");
        elementNameList.add("paneFormat");
        String[] expected = {"name"};
        doTestComputeIntersection(elementNameList, expected);

        elementNameList.clear();
        elementNameList.add("formFragmentFormat");
        elementNameList.add("fragmentFormat");
        expected = new String[]{"name"};
        doTestComputeIntersection(elementNameList, expected);

        elementNameList.clear();
        elementNameList.add("");
        elementNameList.add(null);
        doTestComputeIntersection(elementNameList, getEmptyList());

        elementNameList.clear();
        elementNameList.add("paneFormat");
        elementNameList.add("regionFormat");
        elementNameList.add("replicaFormat");
        expected = new String[]{"name"};
        doTestComputeIntersection(elementNameList, expected);

    }

    /**
     * Helper method to get an array from the element name and list of formats.
     */
    private String[] getAttributes(LayoutProxyElementDetails details,
                                   String elementName,
                                   List formats)
            throws NoSuchFieldException {

        Map attributeMap = (Map)PrivateAccessor.getField(
                details, "attributeMap");

        for (int i = 0; i < formats.size(); i++) {

            Format format = (Format) formats.get(i);
            if (elementName.equals(format.getFormatType().getElementName())) {

                String[] list = format.getUserAttributes();
                String[] listClone = new String[list.length];
                for (int j = 0; j < list.length; j++) {
                    String attributeOriginal = list[j];
                    String attribute = (String) attributeMap.get(attributeOriginal);
                    if (attribute == null) {
                        attribute = Character.toLowerCase(attributeOriginal.charAt(0)) +
                                attributeOriginal.substring(1, attributeOriginal.length());
                    }
                    //System.out.println(attributeOriginal + " --> "  + attribute);

                    listClone[j] = attribute;
                }
                return listClone;
            }
        }
        return new String[0];
    }

    /**
     * Helper method to get the empty elementNameList.
     */
    private String[] getEmptyList() throws Exception {
        return (String[]) PrivateAccessor.getField(
                LayoutProxyElementDetails.class, "EMPTY_LIST");
    }

    /**
     * Test the intersection computation.
     */
    private void doTestComputeIntersection(List elementNames,
                                           String[] expected)
            throws Exception {
        LayoutProxyElementDetails details = new LayoutProxyElementDetails();
        String[] result = details.computeIntersection(elementNames);

        assertNotNull("Result should not be null", result);
        assertEquals("Array size should match: " + dumpArray(result),
                expected.length, result.length);


        for (int i = 0; i < expected.length; i++) {
            assertEquals("Each item should match:\n" + dumpArray(expected)  +
                    "\n"  + dumpArray(result),
                    expected[i], result[i]);
        }
    }

    /**
     * Helper method to dump the contents of an array to a String Buffer.
     */
    private String dumpArray(String[] array) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i]).append(",");
        }
        return buffer.toString();
    }

    /**
     * Test that getElementNamespace() returns the expected value.
     */
    public void testGetElementNamespace() {
        LayoutProxyElementDetails details = new LayoutProxyElementDetails();

        Namespace expected = Namespace.getNamespace("lpdm",
                PolicySchemas.MARLIN_LPDM_2006_02.getNamespaceURL());
        assertEquals(expected, details.getElementNamespace());
    }

    /**
     * Test that getAttributeNames() works as expected.
     */
    public void testGetAttributeNames() throws Exception {
        FormatAttributesViewDetails attrDetails = new FormatAttributesViewDetails();
        attrDetails.addAttributes("name", "type1", "attributeType1", "supplementary1");

        LayoutProxyElementDetails elementDetails = new LayoutProxyElementDetails();

        // Provide the elementDetails with default values for elementName and
        // intersection so that the getAttributes returns a non-null value.
        PrivateAccessor.setField(elementDetails, "elementName", "paneFormat");
        List elementNames = new ArrayList(1);
        elementNames.add("paneFormat");
        PrivateAccessor.setField(elementDetails, fieldSelectedElementNames, elementNames);


        String[] addedAttributes = attrDetails.getAttributes();
        String[] allAttributes = elementDetails.getAttributeNames();

        assertNotNull("Expected array should not be null", addedAttributes);
        assertNotNull("Acutal array should not be null", allAttributes);

        assertEquals("Wrong number of attributes.", 1,
                addedAttributes.length);

        assertEquals("Wrong number of attributes.", 17,
                allAttributes.length);

        // The attributes do not have to be in the same order.
        for (int i = 0; i < addedAttributes.length; i++) {
            boolean found = false;
            String addedAttribute = addedAttributes[i];
            for (int j = 0; j < allAttributes.length && !found; j++) {
                found = addedAttribute.equals(allAttributes[j]);
            }
            assertTrue("Attribute \"" + addedAttribute + "\" not found.", found);
        }
    }

    /**
     * Test that IsAttributeName() works as expected.
     */
    public void testIsAttributeName() throws Exception {
        FormatAttributesViewDetails attrDetails = new FormatAttributesViewDetails();
        attrDetails.addAttributes("name", "type1", "attributeType1", "supplementary1");
        attrDetails.addAttributes("width", "type2", "attributeType2", "supplementary2");

        LayoutProxyElementDetails elementDetails = new LayoutProxyElementDetails();

        // Provide the elementDetails with default values for elementName and
        // intersection so that the getAttributes returns a non-null value.
        PrivateAccessor.setField(elementDetails, "elementName", "paneFormat");
        List elementNames = new ArrayList(2);
        elementNames.add("segmentFormat");
        elementNames.add("segmentGridFormat");
        PrivateAccessor.setField(elementDetails, fieldSelectedElementNames, elementNames);

        assertTrue("Value should be in attributes array: ",
                elementDetails.isAttributeName("frameBorder"));

        assertFalse("Value should NOT be in attributes array: ",
                elementDetails.isAttributeName("frameSpacing"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Jan-04	2728/3	byron	VBM:2004012301 Multiple selections now works better

 23-Jan-04	2728/1	byron	VBM:2004012301 Addressed issues: correct items to display null pointer and other minor issues

 22-Jan-04	2540/3	byron	VBM:2003121505 Added main formats attribute page

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 ===========================================================================
*/
