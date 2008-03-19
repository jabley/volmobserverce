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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.themes.StylePropertyDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * <p>There is effectively a State Transition Matrix for this part of the system,
 * which is defined in the orginating VBM 2007011003, and also in a clearer
 * form below:</p>
 *
 * <table border="1" cellpadding="2" cellspacing="0">
 *   <tr>
 *     <th>test index</th>
 *     <th>Parent?</th>
 *     <th>Parent Color Style</th>
 *     <th>Context Color Style</th>
 *     <th>Children?</th>
 *     <th>Context Element has Visually Important Property?</th>
 *   </tr>
 *   <tr>
 *     <td>1</td><td>N</td><td>N/a</td><td>null</td><td>N</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>2</td><td>N</td><td>N/a</td><td>null</td><td>Y</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>3</td><td>N</td><td>N/a</td><td>red</td><td>N</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>4</td><td>N</td><td>N/a</td><td>red</td><td>Y</td><td><b>Y</b></td>
 *   </tr>
 *   <tr>
 *     <td>5</td><td>Y</td><td>null</td><td>null</td><td>N</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>6</td><td>Y</td><td>null</td><td>null</td><td>Y</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>7</td><td>Y</td><td>null</td><td>red</td><td>N</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>8</td><td>Y</td><td>null</td><td>red</td><td>Y</td><td><b>Y</b></td>
 *   </tr>
 *   <tr>
 *     <td>9</td><td>Y</td><td>blue</td><td>null</td><td>N</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>10</td><td>Y</td><td>blue</td><td>null</td><td>Y</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>11</td><td>Y</td><td>blue</td><td>red</td><td>N</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>12</td><td>Y</td><td>blue</td><td>red</td><td>Y</td><td><b>Y</b></td>
 *   </tr>
 *   <tr>
 *     <td>13</td><td>Y</td><td>blue</td><td>blue</td><td>N</td><td>N</td>
 *   </tr>
 *   <tr>
 *     <td>14</td><td>Y</td><td>blue</td><td>blue</td><td>Y</td><td>N</td>
 *   </tr>
 * </table>
 *
 * <p>The attempts exercise this matrix. An alternative, Parameterised TestCase
 * was written that provided a static TestSuite suite() method, but that didn't
 * seem to add much to the readability of the tests.</p>
 */
public class ColorStylePropertyAnalyserTestCase
        extends StylePropertyAnalyserTestCaseAbstract {

    private Collection properties;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        properties = Collections.singletonList(StylePropertyDetails.COLOR);
    }

    private static Element getChild(Element parent) {
        return (Element) parent.getHead();
    }

    public void testNoParentNoContextStyleNoChildrenIsNotVisuallImportant() {
        Element div = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(
                properties, div));
    }

    public void testNoParentNoContextStyleWithChildrenIsNotVisuallyImportant() {
        Element div = getRootElement("<div>Hello world</div>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(
                properties, div));
    }

    public void testNoParentRedContextStyleNoChildrenIsNotVisuallyImportant() {
        Element div = getRootElement("<div style=\"color: #f00;\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(
                properties, div));
    }

    public void testNoParentRedContextStyleWithChildrenIsVisuallyImportant() {
        Element div = getRootElement(
                "<div style=\"color: #f00;\">Hello world</div>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(
                properties, div));
    }

    public void testParentNoStyleNoContextStyleNoChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement("<div><div/></div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentNoStyleNoContextStyleWithChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement("<div><div>Hello world</div></div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentNoStyleRedContextStyleNoChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement("<div><div style=\"color: #f00;\" /></div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentNoStyleRedContextStyleWithChildrenIsVisuallyImportant() {
        Element parent = getRootElement(
                "<div>" +
                    "<div style=\"color: #f00;\">Hello world</div>" +
                "</div>");
        Element contextElement = getChild(parent);
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentBlueStyleNoContextStyleNoChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement("<div style=\"color: #00f;\"><div/></div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentBlueStyleNoContextStyleWithChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement(
                "<div style=\"color: #00f;\">" +
                    "<div>Hello world</div>" +
                "</div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentBlueStyleRedContextStyleNoChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement(
                "<div style=\"color: #00f;\">" +
                    "<div style=\"color: #f00;\"/>" +
                "</div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentBlueStyleRedContextStyleWithChildrenIsVisuallyImportant() {
        Element parent = getRootElement(
                "<div style=\"color: #00f;\">" +
                    "<div style=\"color: #f00;\">Hello world</div>" +
                "</div>");
        Element contextElement = getChild(parent);
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentBlueStyleBlueContextStyleNoChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement(
                "<div style=\"color: #00f;\">" +
                    "<div style=\"color: #00f;\"/>" +
                "</div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }

    public void testParentBlueStyleBlueContextStyleWithChildrenIsNotVisuallyImportant() {
        Element parent = getRootElement(
                "<div style=\"color: #00f;\">" +
                    "<div style=\"color: #00f;\">Hello world</div>" +
                "</div>");
        Element contextElement = getChild(parent);
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties,
                contextElement));
    }
}
