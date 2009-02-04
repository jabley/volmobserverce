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
package com.volantis.mcs.protocols.html;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.debug.DebugStyledDocument;
import com.volantis.mcs.devices.InternalDeviceMock;
import com.volantis.mcs.protocols.trans.TransformationConfigurationMock;
import com.volantis.mcs.protocols.DOMProtocolMock;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;

/**
 * Test suite for table width transformations.
 */
public class XHTMLMobile1_0TableWidthTransformerTestCase
        extends TestCaseAbstract {

    /**
     * Make sure that width attributes given as percentages are correcly
     * converted to pixels.
     *
     * @throws Throwable if any error occurs
     */
    public void testEmulateWidthAttributePercentages()
            throws Throwable {

        checkTransformation(
                "<html>" +
                    "<body>" +
                        "<table width=\"100%\">" +
                            "<tr>" +
                                "<td width=\"20%\">Blah</td>" +
                                "<td width=\"80%\">Blah</td>" +
                            "</tr>" +
                        "</table>" +
                    "</body>" +
                "</html>",
                "<html>" +
                    "<body>" +
                        "<table width=\"120\">" +
                            "<tr>" +
                                "<td width=\"24\">Blah</td>" +
                                "<td width=\"96\">Blah</td>" +
                            "</tr>" +
                        "</table>" +
                    "</body>" +
                "</html>",
                120, true);
    }

    /**
     * Make sure that width styles given as percentages are correcly
     * converted to pixels.
     *
     * @throws Throwable if any error occurs
     */
    public void testEmulateWidthStylePercentages()
            throws Throwable {

        checkTransformation(
                "<html>" +
                    "<body>" +
                        "<table style=\"width: 100%\">" +
                            "<tr>" +
                                "<td style=\"width: 20%\">Blah</td>" +
                                "<td style=\"width: 80%\">Blah</td>" +
                            "</tr>" +
                        "</table>" +
                    "</body>" +
                "</html>",
                "<html>" +
                    "<body>" +
                        "<table STYLE=\"width: 120px\">" +
                            "<tr>" +
                                "<td STYLE=\"width: 24px\">Blah</td>" +
                                "<td STYLE=\"width: 96px\">Blah</td>" +
                            "</tr>" +
                        "</table>" +
                    "</body>" +
                "</html>",
                120, true);
    }

    /**
     * Make sure that cell widths in percents are retained, if parent cell width
     * is not set, but widths are not converted to pixels.
     *
     * @throws Throwable if any error occurs
     */
    public void testWidthIfNoParentTableWidthSetAndPercentagesNotConverted()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table>" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table>" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, false);
    }

    /**
     * Make sure that cell widths in percents are removed, if parent cell width
     * is not set and widths are converted to pixels.
     *
     * @throws Throwable if any error occurs
     */
    public void testWidthIfNoParentTableWidthSetAndPercentagesConverted()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table>" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table>" +
                        "<tr>" +
                            "<td>col1</td>" +
                            "<td>col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, true);
    }

    /**
     * Make sure that cell widths in percents are retained, if parent cell width
     * is not set and widths are converted to pixels.
     *
     * @throws Throwable if any error occurs
     */
    public void testWidthIfNoParentTableWidthSetAndCellWidthInPixels()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table>" +
                        "<tr>" +
                            "<td width=\"20\">col1</td>" +
                            "<td width=\"80\">col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table>" +
                        "<tr>" +
                            "<td width=\"20\">col1</td>" +
                            "<td width=\"80\">col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, false);
    }

    /**
     * Make sure that cell widths are properly converted, if percent
     * and pixel widths are mixed.
     *
     * @throws Throwable if any error occurs
     */
    public void testCalculationOfMixedWidths()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80\">col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"120\">" +
                        "<tr>" +
                            "<td width=\"24\">col1</td>" +
                            "<td width=\"80\">col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, true);
    }

    /**
     * Make sure that width attribute is ignored, if table width
     * is not set while executing table flattening algorithm.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningIfNoWidthSpecified()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"60\">col1</td>" +
                            "<td>" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td width=\"100%\">col2</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    // correct value, though table may display incorrectly
                    "<table width=\"120\">" +
                        "<tr>" +
                            "<td width=\"60\">col1</td>" +
                            "<td>col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, true);
    }

    /**
     * Make sure that table and column widths are unchanged, when they are
     * specified in pixels, but their total width exceeds the total width
     * of the device screen.
     *
     * @throws Throwable if any error occurs
     */
    public void testTooWideColumns()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"150\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td>col1</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                            "<td>" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td width=\"100%\">col2</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    // correct value, though table may display incorrectly
                    "<table width=\"120\">" +
                        "<tr>" +
                            "<td width=\"150\">col1</td>" +
                            "<td>col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, true);
    }

    /**
     * Make sure that table and column widths are unchanged, when they are
     * specified in pixels, but their total width exceeds the total width
     * of the device screen. This test works with styles instead of attributes.
     *
     * @throws Throwable if any error occurs
     */
    public void testTooWideColumnsOnStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 150px\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td>col1</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                            "<td style=\"width: auto\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td style=\"width: 100%\">col2</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    // correct value, though table may display incorrectly
                    "<table STYLE=\"width: 120px\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 150px\">col1</td>" +
                            "<td>col2</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, true);
    }

    /**
     * Check basic table flattening.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithAttributes()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td width=\"60%\">col2</td>" +
                                        "<td width=\"40%\">col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"48%\">col2</td>" +
                            "<td width=\"32%\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            120, false);
    }

    /**
     * Check basic table flattening (with percent -> pixels conversion).
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithConversionAndAttributes()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td width=\"60%\">col2</td>" +
                                        "<td width=\"40%\">col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"200\">" +
                        "<tr>" +
                            "<td width=\"40\">col1</td>" +
                            "<td width=\"96\">col2</td>" +
                            "<td width=\"64\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            200, true);
    }

    /**
     * Check table flattening with columns specified as pixels.
     * In this case just copy the widths from the cells being promoted.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithWidthInPixelsAndAttributes()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20\">col1</td>" +
                            "<td width=\"80\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td width=\"50\">col2</td>" +
                                        "<td width=\"30\">col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20\">col1</td>" +
                            "<td width=\"50\">col2</td>" +
                            "<td width=\"30\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, false);
    }

    /**
     * Check table flattening without specifying width of columns to be
     * promoted. In this case divide the widths evenly.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithNoWidthsAndAttributes()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td>col2</td>" +
                                        "<td>col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"40%\">col2</td>" +
                            "<td width=\"40%\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, false);
    }

    /**
     * Check table flattening without specifying width of columns to be
     * promoted. In this case divide the widths evenly.
     * The width of the outer table is specified in pixels.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithPixelsNoWidthsAndAttributes()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20\">col1</td>" +
                            "<td width=\"80\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td>col2</td>" +
                                        "<td>col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"100\">" +
                        "<tr>" +
                            "<td width=\"20\">col1</td>" +
                            "<td width=\"40\">col2</td>" +
                            "<td width=\"40\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, true);
    }

    /**
     * Checks table flattening on two nested rows
     * As a result two rows should be produces, with the first column
     * spanning the two rows.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningOfTwoNestedRowsAndAttributes()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td width=\"60%\">col2</td>" +
                                        "<td width=\"40%\">col3</td>" +
                                    "</tr>" +
                                    "<tr>" +
                                        "<td>col4</td>" +
                                        "<td>col5</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\" rowspan=\"2\">col1</td>" +
                            "<td width=\"48%\">col2</td>" +
                            "<td width=\"32%\">col3</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>col4</td>" +
                            "<td>col5</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, false);
    }

    /**
     * Checks table flattening on double-nested tables
     * (note that now the usable width is set to 125px).
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningOfDoubleNestedTablesAndAttributes()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table width=\"100%\">" +
                        "<tr>" +
                            "<td width=\"20%\">col1</td>" +
                            "<td width=\"80%\">" +
                                "<table width=\"100%\">" +
                                    "<tr>" +
                                        "<td width=\"20%\">col2</td>" +
                                        "<td width=\"80%\">" +
                                            "<table width=\"100%\">" +
                                                "<tr>" +
                                                    "<td width=\"20%\">col3</td>" +
                                                    "<td width=\"80%\">col4</td>" +
                                                "</tr>" +
                                            "</table>" +
                                        "</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table width=\"125\">" +
                        "<tr>" +
                            "<td width=\"25\">col1</td>" +
                            "<td width=\"20\">col2</td>" +
                            "<td width=\"16\">col3</td>" +
                            "<td width=\"64\">col4</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            125, true);
    }

    /**
     * Check basic table flattening.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 20%\">col1</td>" +
                            "<td style=\"width: 80%\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td style=\"width: 60%\">col2</td>" +
                                        "<td style=\"width: 40%\">col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table STYLE=\"width: 100%\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 20%\">col1</td>" +
                            "<td STYLE=\"width: 48%\">col2</td>" +
                            "<td STYLE=\"width: 32%\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, false);
    }

    /**
     * Check basic table flattening (with percent -> pixels conversion).
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithConversionAndStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 20%\">col1</td>" +
                            "<td style=\"width: 80%\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td style=\"width: 60%\">col2</td>" +
                                        "<td style=\"width: 40%\">col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table STYLE=\"width: 200px\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 40px\">col1</td>" +
                            "<td STYLE=\"width: 96px\">col2</td>" +
                            "<td STYLE=\"width: 64px\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            200, true);
    }

    /**
     * Check table flattening with columns specified as pixels.
     * In this case just copy the widths from the cells being promoted.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithWidthInPixelsAndStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 20px\">col1</td>" +
                            "<td style=\"width: 80px\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td style=\"width: 50px\">col2</td>" +
                                        "<td style=\"width: 30px\">col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table STYLE=\"width: 100%\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 20px\">col1</td>" +
                            "<td STYLE=\"width: 50px\">col2</td>" +
                            "<td STYLE=\"width: 30px\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, false);
    }

    /**
     * Check table flattening without specifying width of columns to be
     * promoted. In this case divide the widths evenly.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithNoWidthsAndStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 20%\">col1</td>" +
                            "<td style=\"width: 80%\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td>col2</td>" +
                                        "<td>col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table STYLE=\"width: 100%\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 20%\">col1</td>" +
                            "<td STYLE=\"width: 40%\">col2</td>" +
                            "<td STYLE=\"width: 40%\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, false);
    }

    /**
     * Check table flattening without specifying width of columns to be
     * promoted. In this case divide the widths evenly.
     * The width of the outer table is specified in pixels.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningWithPixelsNoWidthsAndStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 20px\">col1</td>" +
                            "<td style=\"width: 80px\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td>col2</td>" +
                                        "<td>col3</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table STYLE=\"width: 100px\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 20px\">col1</td>" +
                            "<td STYLE=\"width: 40px\">col2</td>" +
                            "<td STYLE=\"width: 40px\">col3</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, true);
    }

    /**
     * Checks table flattening on two nested rows
     * As a result two rows should be produces, with the first column
     * spanning the two rows.
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningOfTwoNestedRowsAndStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 20%\">col1</td>" +
                            "<td style=\"width: 80%\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td style=\"width: 60%\">col2</td>" +
                                        "<td style=\"width: 40%\">col3</td>" +
                                    "</tr>" +
                                    "<tr>" +
                                        "<td>col4</td>" +
                                        "<td>col5</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table STYLE=\"width: 100%\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 20%\" rowspan=\"2\">col1</td>" +
                            "<td STYLE=\"width: 48%\">col2</td>" +
                            "<td STYLE=\"width: 32%\">col3</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>col4</td>" +
                            "<td>col5</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            100, false);
    }

    /**
     * Checks table flattening on double-nested tables
     * (note that now the usable width is set to 125px).
     *
     * @throws Throwable if any error occurs
     */
    public void testTableFlatteningOfDoubleNestedTablesAndStyles()
            throws Throwable {
        checkTransformation(
            "<html>" +
                "<body>" +
                    "<table style=\"width: 100%\">" +
                        "<tr>" +
                            "<td style=\"width: 20%\">col1</td>" +
                            "<td style=\"width: 80%\">" +
                                "<table style=\"width: 100%\">" +
                                    "<tr>" +
                                        "<td style=\"width: 20%\">col2</td>" +
                                        "<td style=\"width: 80%\">" +
                                            "<table style=\"width: 100%\">" +
                                                "<tr>" +
                                                    "<td style=\"width: 20%\">col3</td>" +
                                                    "<td style=\"width: 80%\">col4</td>" +
                                                "</tr>" +
                                            "</table>" +
                                        "</td>" +
                                    "</tr>" +
                                "</table>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            "<html>" +
                "<body>" +
                    "<table STYLE=\"width: 125px\">" +
                        "<tr>" +
                            "<td STYLE=\"width: 25px\">col1</td>" +
                            "<td STYLE=\"width: 20px\">col2</td>" +
                            "<td STYLE=\"width: 16px\">col3</td>" +
                            "<td STYLE=\"width: 64px\">col4</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>" +
            "</html>",
            125, true);
    }

    private void checkTransformation(String input, String expected,
                                     int deviceWidth,
                                     boolean convertPercentagesToPixels)
            throws Throwable {

        ProtocolSupportFactory factory = new DefaultProtocolSupportFactory();

        DOMProtocolMock protocolMock = new DOMProtocolMock(
                "protocolMock", expectations, factory, null);

        TransformationConfigurationMock transformationConfigurationMock =
                new TransformationConfigurationMock(
                        "transformationConfigurationMock", expectations);

        MarinerPageContextMock pageContextMock = new MarinerPageContextMock(
                "pageContextMock", expectations);

        InternalDeviceMock deviceMock = new InternalDeviceMock(
                "deviceMock", expectations);

        ProtocolConfigurationMock protocolConfigMock =
                new ProtocolConfigurationMock("protocolConfigMock",
                        expectations);

        // XHTML Mobile 1.0 doesn't support nested tables.
        protocolMock.expects.supportsNestedTables().returns(false).any();
        protocolMock.expects.getMarinerPageContext().returns(pageContextMock).any();
        protocolMock.expects.getDOMFactory().returns(factory.getDOMFactory()).any();
        protocolMock.expects.getProtocolConfiguration().returns(protocolConfigMock).any();

        pageContextMock.expects.getDevice().returns(deviceMock).any();

        deviceMock.expects.getPolicyValue(DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE)
                .returns(convertPercentagesToPixels ? DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE__DEVICE_WIDTH :
                        DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE__NONE)
                .any();

        deviceMock.expects.getPolicyValue(DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_TABLE)
                .returns(DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_TABLE__NONE)
                .any();

        deviceMock.expects.getPixelsX().returns(deviceWidth).any();

        StrictStyledDOMHelper helper = new StrictStyledDOMHelper(null, "STYLE");

        Document document = helper.parse(/*getClass().getResourceAsStream(
                "width-percentages-attributes1-input.xml")*/
                input);

        XHTMLMobile1_0_UnabridgedTransformer transformer =
                new XHTMLMobile1_0_UnabridgedTransformer(transformationConfigurationMock);

        // Don't render the document out before transforming as it is
        // destructive and loses the styles.
//        String before = helper.render(document);
//        System.out.println("before: " + before);
//
        MutableStylePropertySet interesting =
                         new MutableStylePropertySetImpl();
        interesting.add(StylePropertyDetails.WIDTH);
        DebugStyledDocument debugStyledDocument;

        debugStyledDocument = new DebugStyledDocument(interesting);
        String before = debugStyledDocument.debug(document);

        document = transformer.transform(protocolMock, document);

        debugStyledDocument = new DebugStyledDocument(interesting);
        String after = debugStyledDocument.debug(document);

        String result = helper.render(document);

        boolean failed = true;
        try {
            assertXMLEquals("Transformed document does not match", expected, result);
            failed = false;
        } finally {
            if (failed) {
                System.out.println("Test Failed: " + getName());
                System.out.println("before:   " + before);
                System.out.println("after:    " + after);
                System.out.println("expected: " + expected);
                System.out.println("result:   " + result);
                System.out.println();
            }
        }
    }
}
