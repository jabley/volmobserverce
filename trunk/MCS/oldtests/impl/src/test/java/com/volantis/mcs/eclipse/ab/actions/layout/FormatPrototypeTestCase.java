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
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.StringReader;
import java.io.StringWriter;

import org.custommonkey.xmlunit.XMLAssert;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Tests {@link FormatPrototype}.
 */
public class FormatPrototypeTestCase extends TestCaseAbstract {

    /**
     * Tests explicitly named format type prototype creations.
     */
    public void testGet() throws Exception {
        FormatType[] types = new FormatType[]{
            FormatType.COLUMN_ITERATOR_PANE,
            FormatType.DISSECTING_PANE,
            FormatType.EMPTY,
            FormatType.FORM,
            FormatType.FORM_FRAGMENT,
            FormatType.FRAGMENT,
            FormatType.GRID,
            FormatType.PANE,
            FormatType.REGION,
            FormatType.REPLICA,
            FormatType.ROW_ITERATOR_PANE,
            FormatType.SEGMENT,
            FormatType.SEGMENT_GRID,
            FormatType.SPATIAL_FORMAT_ITERATOR,
            FormatType.TEMPORAL_FORMAT_ITERATOR
        };

        for (int i = 0; i < types.length; i++) {
            FormatType type = types[i];
            Element expected = expectedDOM(type);

            compare(expected, FormatPrototype.get(type));
        }

    }

    /**
     * Tests various grid structure creations.
     */
    public void testCreateSizedGrid() throws Exception {
        doTest(FormatType.GRID, null, 0, 0);
        doTest(FormatType.GRID, "keepIt", 0, 0);
        doTest(FormatType.GRID, null, 0, 5);
        doTest(FormatType.GRID, "keepIt", 0, 5);
        doTest(FormatType.GRID, null, 3, 5);
        doTest(FormatType.GRID, "keepIt", 3, 5);
        doTest(FormatType.GRID, null, 3, 0);
        doTest(FormatType.SEGMENT_GRID, null, 0, 5);
        doTest(FormatType.SEGMENT_GRID, "keepIt", 0, 5);
        doTest(FormatType.SEGMENT_GRID, null, 4, 5);
        doTest(FormatType.SEGMENT_GRID, "keepIt", 4, 5);
        doTest(FormatType.SEGMENT_GRID, null, 4, 0);
    }

    /**
     * Simple test that the right structure is created for the given sized grid
     * format type.
     *
     * @param gridType      the grid format type
     * @param firstCellName the name of the element to go in the first cell or
     *                      null
     * @param rows          number of rows
     * @param cols          number of columns
     */
    protected void doTest(FormatType gridType,
                          String firstCellName,
                          int rows, int cols) throws Exception {
        compare(expectedDOM(gridType, firstCellName, rows, cols),
                FormatPrototype.createSizedGrid(
                        gridType,
                        (firstCellName == null) ?
                null :
                (Element) FormatPrototype.factory.element(firstCellName),
                        rows,
                        cols));
    }

    /**
     * Creates a DOM appropriate to a specifically dimensioned grid format
     * type.
     *
     * @param gridType      the format type (should be a grid structure format
     *                      type)
     * @param firstCellName name of the element to go in the first cell or
     *                      null
     * @param rows          number of required rows
     * @param cols          number of required columns
     * @return the created DOM element
     */
    protected Element expectedDOM(FormatType gridType,
                                  String firstCellName,
                                  int rows,
                                  int cols) throws Exception {
        final String name = gridType.getElementName();

        if (firstCellName != null) {
            if (rows < 1) {
                rows = 1;
            }

            if (cols < 1) {
                cols = 1;
            }
        }

        String expected =
                "<" + name + " rows='" + rows + "' columns='" + cols + "'>" +
                "<" + name + "Columns>";

        for (int i = 0; i < cols; i++) {
            expected +=
                    "<" + name + "Column/>";
        }

        expected +=
                "</" + name + "Columns>";

        for (int i = 0; i < rows; i++) {
            expected +=
                    "<" + name + "Row>";

            for (int j = 0; j < cols; j++) {
                if ((i == 0) && (j == 0) && (firstCellName != null)) {
                    expected +=
                            "<" + firstCellName + "/>";
                } else {
                    expected +=
                            "<emptyFormat/>";
                }
            }

            expected +=
                    "</" + name + "Row>";
        }

        expected +=
                "</" + name + ">";

        return createDOM(expected);
    }

    /**
     * Generates a DOM appropriate to the format type.
     *
     * @param type the format type for which an expected XML structure is to be
     *             created
     * @return the expected equivalent DOM
     */
    protected Element expectedDOM(FormatType type) throws Exception {
        String expected = null;

        final String name = type.getElementName();

        if (type.getStructure() == FormatType.Structure.LEAF) {
            expected = "<" + name + "/>";
        } else if (type.getStructure() == FormatType.Structure.SIMPLE_CONTAINER) {
            if (type == FormatType.SPATIAL_FORMAT_ITERATOR) {
                expected =
                        "<" + name + " "
                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_COLUMNS_ATTRIBUTE.getName()
                        + "=\""
                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_CELLS_VALUE_VARIABLE.getName()
                        + "\" "
                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_COLUMN_COUNT_ATTRIBUTE.getName()
                        + "=\"0\" "
                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_ROWS_ATTRIBUTE.getName()
                        + "=\""
                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_CELLS_VALUE_VARIABLE.getName()
                        + "\" "
                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_ROW_COUNT_ATTRIBUTE.getName()
                        + "=\"0\" "

                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_INDEXING_DIRECTION_ATTRIBUTE.getName()
                        + "=\""
                        + LayoutSchemaType.
                        SPATIAL_ITERATOR_INDEXING_DIRECTION_VALUE_ACROSS_DOWN.getName()
                        + "\""
                        + ">" +
                        "<emptyFormat/>" +
                        "</" + name + ">";
            } else {
                expected =
                        "<" + name + ">" +
                        "<emptyFormat/>" +
                        "</" + name + ">";
            }
        } else {
            expected =
                    "<" + name + ">" +
                    "<" + name + "Columns>" +
                    "<" + name + "Column/>" +
                    "</" + name + "Columns>" +
                    "<" + name + "Row>" +
                    "<emptyFormat/>" +
                    "</" + name + "Row>" +
                    "</" + name + ">";
        }

        return createDOM(expected);
    }

    /**
     * Supporting method to compare two JDOM structures.
     *
     * @param expected the expected structure
     * @param actual   the actual structure
     */
    protected void compare(Element expected,
                           Element actual) throws Exception {
        XMLOutputter out = new XMLOutputter();
        StringWriter expectedWriter = new StringWriter();
        StringWriter actualWriter = new StringWriter();

        out.output(expected, expectedWriter);
        out.output(actual, actualWriter);

        XMLAssert.assertXMLEqual(expectedWriter.toString(),
                actualWriter.toString());
    }

    /**
     * Supporting method to convert an XML string to an LPDM ODOM structure.
     *
     * @param xml the XML string to be parsed
     * @return the equivalent LPDM ODOM structure (namespaces automatically
     *         added)
     */
    protected Element createDOM(String xml) throws Exception {
        SAXBuilder builder = new SAXBuilder();

        builder.setFactory(FormatPrototype.factory);

        return builder.build(new StringReader(xml)).getRootElement();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 14-May-04	4390/1	adrian	VBM:2004051003 Added default value support to Spatial Iterator Format

 14-May-04	4344/1	adrian	VBM:2004051003 Added default value support to Spatial Iterator Format

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 ===========================================================================
*/
