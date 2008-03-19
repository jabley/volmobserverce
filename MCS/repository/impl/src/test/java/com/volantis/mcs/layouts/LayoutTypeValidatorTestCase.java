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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.synergetics.testtools.TestCaseAbstract;

public class LayoutTypeValidatorTestCase extends TestCaseAbstract {

    public void testRGBOrColorNameType() {
        // rgb
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("#"));
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("#a"));
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("#ab"));
        assertTrue("",  LayoutTypeValidator.isRGBOrColorNameType("#abc"));
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("#abca"));
        assertTrue("",  LayoutTypeValidator.isRGBOrColorNameType("#abcabc"));
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("#abcabca"));
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("#abcabcab"));
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("#abcabcabc"));

        // color name
        assertTrue("", LayoutTypeValidator.isRGBOrColorNameType("red"));
        assertFalse("", LayoutTypeValidator.isRGBOrColorNameType("moon"));
    }

    public void testThemeClassNameType() {
        assertTrue("", LayoutTypeValidator.isThemeClassNameType("name"));
        assertFalse("", LayoutTypeValidator.isThemeClassNameType("{name}"));
        assertFalse("", LayoutTypeValidator.isThemeClassNameType("1name"));
    }

    public void testThemeClassNameList() {
        assertTrue("", LayoutTypeValidator.isThemeClassNameList("a b c"));
        assertFalse("", LayoutTypeValidator.isThemeClassNameList("a b 1c"));
    }

    public void testQuotedComponentReferenceOrStyleClassType() {
        assertTrue("",
                LayoutTypeValidator.isQuotedComponentReferenceOrStyleClassType("name"));
        assertTrue("",
                LayoutTypeValidator.isQuotedComponentReferenceOrStyleClassType("{name}"));
        assertFalse("",
                LayoutTypeValidator.isQuotedComponentReferenceOrStyleClassType("1name"));
    }


    public void testFormatNameType() {
        assertTrue("", LayoutTypeValidator.isFormatNameType("a1"));
        assertFalse("", LayoutTypeValidator.isFormatNameType("1a"));
    }

    public void testClockValueTypeList() {
        assertTrue("", LayoutTypeValidator.isClockValueTypeList("25s 5s"));
        assertFalse("", LayoutTypeValidator.isClockValueTypeList("99a 5s"));
    }

    public void testUnsigned() {
        assertTrue("", LayoutTypeValidator.isClockValueTypeList("1"));
        assertFalse("", LayoutTypeValidator.isClockValueTypeList("-1"));
    }

}
