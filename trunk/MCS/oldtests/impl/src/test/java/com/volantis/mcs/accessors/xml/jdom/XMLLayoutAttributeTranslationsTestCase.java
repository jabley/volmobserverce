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
package com.volantis.mcs.accessors.xml.jdom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Test the XMLLayoutAttributeTranslations
 */
public class XMLLayoutAttributeTranslationsTestCase extends TestCaseAbstract {

    /**
     * This was originally populated by iterating over all keys in the {@link
     * XMLLayoutAttributeTranslations#element2attribute} map and obtaining each
     * attribute name for each element. The table below was code-generated and
     * the expected attribute name updated where necessary.
     */
    private final String[][] elementAttributeNames = new String[][]{
        // { [elementName], [attributeName], [expectedAttributeName] }
        {"gridFormatRow", "Height", "height"},
        {"gridFormatRow", "StyleClass", "styleClass"},
        {"segmentFormat", "Resize", "resize"},
        {"segmentFormat", "MarginWidth", "marginWidth"},
        {"segmentFormat", "Scrolling", "scrolling"},
        {"segmentFormat", "BorderColour", "borderColor"},
        {"segmentFormat", "FrameBorder", "frameBorder"},
        {"segmentFormat", "Name", "name"},
        {"segmentFormat", "MarginHeight", "marginHeight"},
        {"replicaFormat", "Replicant", "sourceFormatName"}, // different
        {"replicaFormat", "ReplicantType", "sourceFormatType"}, // different
        {"replicaFormat", "Name", "name"},
        {"segmentGridFormat", "FrameSpacing", "frameSpacing"},
        {"segmentGridFormat", "Rows", "rows"},
        {"segmentGridFormat", "BorderColour", "borderColor"},
        {"segmentGridFormat", "FrameBorder", "frameBorder"},
        {"segmentGridFormat", "BorderWidth", "borderWidth"},
        {"segmentGridFormat", "Columns", "columns"},
        {"spatialFormatIterator", "SpatialIteratorColumns", "columns"}, // different
        {"spatialFormatIterator", "OptimizationLevel", "optimizationLevel"},
        {"spatialFormatIterator", "BackgroundColour", "backgroundColor"},
        {"spatialFormatIterator", "Height", "height"},
        {"spatialFormatIterator", "CellPadding", "cellPadding"},
        {"spatialFormatIterator", "SpatialIteratorColumnCount", "columnCount"}, // different
        {"spatialFormatIterator", "HorizontalAlignment", "horizontalAlignment"},
        {"spatialFormatIterator", "CellSpacing", "cellSpacing"},
        {"spatialFormatIterator", "WidthUnits", "widthUnits"},
        {"spatialFormatIterator", "StyleClass", "styleClass"},
        {"spatialFormatIterator", "BackgroundComponent", "backgroundComponent"},
        {"spatialFormatIterator", "RowStyleClasses", "rowStyleClasses"},
        {"spatialFormatIterator", "ColumnStyleClasses", "columnStyleClasses"},
        {"spatialFormatIterator", "SpatialIterator2DIndexingDir", "indexingDirection"}, // different
        {"spatialFormatIterator", "BackgroundComponentType", "backgroundComponentType"},
        {"spatialFormatIterator", "VerticalAlignment", "verticalAlignment"},
        {"spatialFormatIterator", "Width", "width"},
        {"spatialFormatIterator", "SpatialIteratorRowCount", "rowCount"}, // different
        {"spatialFormatIterator", "BorderWidth", "borderWidth"},
        {"spatialFormatIterator", "Name", "name"},
        {"spatialFormatIterator", "SpatialIteratorRows", "rows"}, // different
        {"spatialFormatIterator", "AlignContent", "alignContent"},
        {"deviceLayoutMontageFormat", "LayoutGroupName", "groupName"}, // different
        {"deviceLayoutMontageFormat", "DefaultSegmentName", "defaultSegment"}, // different
        {"deviceLayoutMontageFormat", "DestinationLayout", "destinationLayout"},
        {"dissectingPaneFormat", "NextShardLinkClass", "nextLinkStyleClass"}, // different
        {"dissectingPaneFormat", "NextShardLinkText", "nextLinkText"}, // different
        {"dissectingPaneFormat", "BackgroundColour", "backgroundColor"},
        {"dissectingPaneFormat", "Height", "height"},
        {"dissectingPaneFormat", "CellPadding", "cellPadding"},
        {"dissectingPaneFormat", "FilterText", "filterOnKeyboardUsability"}, // different
        {"dissectingPaneFormat", "HorizontalAlignment", "horizontalAlignment"},
        {"dissectingPaneFormat", "CellSpacing", "cellSpacing"},
        {"dissectingPaneFormat", "PreviousShardLinkText", "previousLinkText"}, // different
        {"dissectingPaneFormat", "PreviousShardShortcut", "previousLinkShortcut"}, // different
        {"dissectingPaneFormat", "WidthUnits", "widthUnits"},
        {"dissectingPaneFormat", "shardLinkOrder", "shardLinkOrder"},
        {"dissectingPaneFormat", "BackgroundComponent", "backgroundComponent"},
        {"dissectingPaneFormat", "MaximumContentSize", "maxContentSize"}, // different
        {"dissectingPaneFormat", "NextShardShortcut", "nextLinkShortcut"}, // different
        {"dissectingPaneFormat", "BackgroundComponentType", "backgroundComponentType"},
        {"dissectingPaneFormat", "VerticalAlignment", "verticalAlignment"},
        {"dissectingPaneFormat", "Width", "width"},
        {"dissectingPaneFormat", "BorderWidth", "borderWidth"},
        {"dissectingPaneFormat", "PreviousShardLinkClass", "previousLinkStyleClass"}, // different
        {"dissectingPaneFormat", "Name", "name"},
        {"fragmentFormat", "BackLinkText", "backLinkText"},
        {"fragmentFormat", "FragLinkClass", "linkStyleClass"}, // different
        {"fragmentFormat", "Name", "name"},
        {"fragmentFormat", "fragLinkOrder", "fragmentLinkOrder"}, // different
        {"fragmentFormat", "LinkText", "linkText"},
        {"fragmentFormat", "PeerLinks", "showPeerLinks"}, // different
        {"regionFormat", "Name", "name"},
        {"regionFormat", "DestinationArea", "destinationArea"},
        {"temporalFormatIterator", "BackgroundColour", "backgroundColor"},
        {"temporalFormatIterator", "Height", "height"},
        {"temporalFormatIterator", "CellPadding", "cellPadding"},
        {"temporalFormatIterator", "TemporalIteratorClockValues", "clockValues"}, // different
        {"temporalFormatIterator", "TemporalIteratorCellCount", "cellCount"}, // different
        {"temporalFormatIterator", "HorizontalAlignment", "horizontalAlignment"},
        {"temporalFormatIterator", "CellSpacing", "cellSpacing"},
        {"temporalFormatIterator", "WidthUnits", "widthUnits"},
        {"temporalFormatIterator", "BackgroundComponent", "backgroundComponent"},
        {"temporalFormatIterator", "BackgroundComponentType", "backgroundComponentType"},
        {"temporalFormatIterator", "VerticalAlignment", "verticalAlignment"},
        {"temporalFormatIterator", "Width", "width"},
        {"temporalFormatIterator", "TemporalIteratorCells", "cells"}, // different
        {"temporalFormatIterator", "BorderWidth", "borderWidth"},
        {"temporalFormatIterator", "Name", "name"},
        {"gridFormatColumn", "Width", "width"},
        {"gridFormatColumn", "StyleClass", "styleClass"},
        {"gridFormatColumn", "WidthUnits", "widthUnits"},
        {"paneFormat", "OptimizationLevel", "optimizationLevel"},
        {"paneFormat", "BackgroundColour", "backgroundColor"},
        {"paneFormat", "Height", "height"},
        {"paneFormat", "CellPadding", "cellPadding"},
        {"paneFormat", "HorizontalAlignment", "horizontalAlignment"},
        {"paneFormat", "FilterText", "filterOnKeyboardUsability"}, // different
        {"paneFormat", "CellSpacing", "cellSpacing"},
        {"paneFormat", "DestinationArea", "destinationArea"},
        {"paneFormat", "WidthUnits", "widthUnits"},
        {"paneFormat", "BackgroundComponent", "backgroundComponent"},
        {"paneFormat", "BackgroundComponentType", "backgroundComponentType"},
        {"paneFormat", "VerticalAlignment", "verticalAlignment"},
        {"paneFormat", "Width", "width"},
        {"paneFormat", "BorderWidth", "borderWidth"},
        {"paneFormat", "Name", "name"},
        {"formFormat", "Name", "name"},
        {"formFragmentFormat", "PreviousLinkPosition", "previousLinkPosition"},
        {"formFragmentFormat", "Reset", "allowReset"}, // different
        {"formFragmentFormat", "NextLinkPosition", "nextLinkPosition"},
        {"formFragmentFormat", "PreviousLinkStyle", "previousLinkStyleClass"}, // different
        {"formFragmentFormat", "Name", "name"},
        {"formFragmentFormat", "NextLinkText", "nextLinkText"},
        {"formFragmentFormat", "NextLinkStyle", "nextLinkStyleClass"}, // different
        {"formFragmentFormat", "PreviousLinkText", "previousLinkText"},
        {"gridFormat", "OptimizationLevel", "optimizationLevel"},
        {"gridFormat", "BackgroundColour", "backgroundColor"},
        {"gridFormat", "Height", "height"},
        {"gridFormat", "CellPadding", "cellPadding"},
        {"gridFormat", "HorizontalAlignment", "horizontalAlignment"},
        {"gridFormat", "CellSpacing", "cellSpacing"},
        {"gridFormat", "StyleClass", "styleClass"},
        {"gridFormat", "WidthUnits", "widthUnits"},
        {"gridFormat", "Columns", "columns"},
        {"gridFormat", "BackgroundComponent", "backgroundComponent"},
        {"gridFormat", "Rows", "rows"},
        {"gridFormat", "BackgroundComponentType", "backgroundComponentType"},
        {"gridFormat", "VerticalAlignment", "verticalAlignment"},
        {"gridFormat", "Width", "width"},
        {"gridFormat", "BorderWidth", "borderWidth"},
        {"gridFormat", "Name", "name"},
        {"deviceLayoutCanvasFormat", "LayoutGroupName", "groupName"}, // different
        {"deviceLayoutCanvasFormat", "DestinationLayout", "destinationLayout"},
        {"deviceLayoutCanvasFormat", "DefaultFragmentName", "defaultFragment"}, // different
        {"segmentGridFormatRow", "Height", "height"},
        {"columnIteratorPaneFormat", "OptimizationLevel", "optimizationLevel"},
        {"columnIteratorPaneFormat", "BackgroundColour", "backgroundColor"},
        {"columnIteratorPaneFormat", "Height", "height"},
        {"columnIteratorPaneFormat", "CellPadding", "cellPadding"},
        {"columnIteratorPaneFormat", "HorizontalAlignment", "horizontalAlignment"},
        {"columnIteratorPaneFormat", "FilterText", "filterOnKeyboardUsability"}, // different
        {"columnIteratorPaneFormat", "CellSpacing", "cellSpacing"},
        {"columnIteratorPaneFormat", "DestinationArea", "destinationArea"},
        {"columnIteratorPaneFormat", "WidthUnits", "widthUnits"},
        {"columnIteratorPaneFormat", "BackgroundComponent", "backgroundComponent"},
        {"columnIteratorPaneFormat", "BackgroundComponentType", "backgroundComponentType"},
        {"columnIteratorPaneFormat", "VerticalAlignment", "verticalAlignment"},
        {"columnIteratorPaneFormat", "Width", "width"},
        {"columnIteratorPaneFormat", "BorderWidth", "borderWidth"},
        {"columnIteratorPaneFormat", "Name", "name"},
        {"rowIteratorPaneFormat", "OptimizationLevel", "optimizationLevel"},
        {"rowIteratorPaneFormat", "BackgroundColour", "backgroundColor"},
        {"rowIteratorPaneFormat", "Height", "height"},
        {"rowIteratorPaneFormat", "CellPadding", "cellPadding"},
        {"rowIteratorPaneFormat", "HorizontalAlignment", "horizontalAlignment"},
        {"rowIteratorPaneFormat", "FilterText", "filterOnKeyboardUsability"}, // different
        {"rowIteratorPaneFormat", "CellSpacing", "cellSpacing"},
        {"rowIteratorPaneFormat", "DestinationArea", "destinationArea"},
        {"rowIteratorPaneFormat", "WidthUnits", "widthUnits"},
        {"rowIteratorPaneFormat", "BackgroundComponent", "backgroundComponent"},
        {"rowIteratorPaneFormat", "BackgroundComponentType", "backgroundComponentType"},
        {"rowIteratorPaneFormat", "VerticalAlignment", "verticalAlignment"},
        {"rowIteratorPaneFormat", "Width", "width"},
        {"rowIteratorPaneFormat", "BorderWidth", "borderWidth"},
        {"rowIteratorPaneFormat", "Name", "name"},
        {"segmentGridFormatColumn", "Width", "width"},
        {"segmentGridFormatColumn", "WidthUnits", "widthUnits"}
    };

    /**
     * Test the translating of old to new attribute names.
     */
    public void testTranslateOldToNewAttributeName() throws Exception {

        XMLLayoutAttributeTranslations translations =
                XMLLayoutAttributeTranslations.getInstance();

        HashMap map = (HashMap) PrivateAccessor.getField(translations,
                                                         "element2attribute");

        // Verify that all the elements that have been manually set-up
        // in XMLLayoutAttributesTranslations are contained in the 2D array
        // above (elementAttributeNames). Each element/attribute must exist
        // in the array above and the total number of elements/attributes
        // defined in XMLLayoutAttributesTranslations should match the total
        // number defined in the 2D array above.
        int actualElementAttributeCount = 0;
        Iterator elementIterator = map.keySet().iterator();
        while (elementIterator.hasNext()) {
            String elementName = (String) elementIterator.next();
            XMLLayoutAttributeTranslations.TranslationElementInfo info =
                    (XMLLayoutAttributeTranslations.TranslationElementInfo)
                    map.get(elementName);
            Iterator translator = info.getWriteNameTranslator().getMap().
                    keySet().iterator();
            while (translator.hasNext()) {
                String s = (String) translator.next();
                verifyElementAttributeIsChecked(elementName, s);
                ++actualElementAttributeCount;
            }
        }

        assertEquals("Total number of attribute/element combinations should " +
                     "match:",
                     elementAttributeNames.length,
                     actualElementAttributeCount);


        String result;
        // Ensure that the translated name is what we expect.
        for (int i = 0; i < elementAttributeNames.length; i++) {
            result = translations.translateOldToNewAttributeName(
                    elementAttributeNames[i][0], elementAttributeNames[i][1]);
            assertEquals("Testing: " + elementAttributeNames[i][0] + ":" +
                         elementAttributeNames[i][1] + ": ",
                         elementAttributeNames[i][2],
                         result);
        }
    }

    /**
     * Helper method for verifying that the element and attribute combination
     * occurs in the array of element/attribute/expected values.
     *
     * @param elementName
     *                  the element name.
     * @param attribute the attribute name.
     */
    private void verifyElementAttributeIsChecked(String elementName,
                                                 String attribute) {
        boolean matchFound = false;
        for (int i = 0; i < elementAttributeNames.length; i++) {
            String[] attributeName = elementAttributeNames[i];
            if (elementAttributeNames[i][0].equals(elementName) &&
                    elementAttributeNames[i][1].equals(attribute)) {
                matchFound = true;
            }
        }
        if (!matchFound) {
            fail("Element/attribute combination '" + elementName + "/" +
                 attribute + "' not found.");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Dec-04	6387/1	adrianj	VBM:2004120205 Added content align attribute to Spatial Iterator format

 12-Oct-04	5769/3	byron	VBM:2004100805 Support style classes on grids and spatial format iterators: Accessors - rework issues

 11-Oct-04	5769/1	byron	VBM:2004100805 Support style classes on grids and spatial format iterators: Accessors

 ===========================================================================
*/
