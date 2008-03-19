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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.MCSMenuHorizontalSeparatorKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base test for {@link MenuSeparatorRendererFactory} implementations
 */
public abstract class MenuSeparatorRendererFactoryTestAbstract
        extends TestCaseAbstract {
    /**
     * The horizontal separator type enumeration values in an iterable form.
     */
    protected static final StyleKeyword[] types =
            {MCSMenuHorizontalSeparatorKeywords.NONE,
             MCSMenuHorizontalSeparatorKeywords.SPACE,
             MCSMenuHorizontalSeparatorKeywords.NON_BREAKING_SPACE};

    /**
     * Tests that the correct type of separator renderer is created for the
     * tested factory method. Relies on the appropriate
     * <code>getExpected...Class</code> method in this test case.
     */
    public void testCreateHorizontalMenuSeparator() throws Exception {
        for (int index = 0; index < types.length; index++) {
            checkClass("createHorizontalMenuSeparator",
                       getExpectedHorizontalMenuSeparatorRendererClass(
                               types[index]),
                       createFactory().createHorizontalMenuSeparator(
                               types[index]));
        }
    }

    /**
     * Tests that the correct type of separator renderer is created for the
     * tested factory method. Relies on the appropriate
     * <code>getExpected...Class</code> method in this test case.
     */
    public void testCreateVerticalMenuSeparator() throws Exception {
        checkClass("createVerticalMenuSeparator",
                   getExpectedVerticalMenuSeparatorRendererClass(),
                   createFactory().createVerticalMenuSeparator());
    }

    /**
     * Tests that the correct type of separator renderer is created for the
     * tested factory method. Relies on the appropriate
     * <code>getExpected...Class</code> method in this test case.
     */
    public void testCreateHorizontalMenuItemSeparator() throws Exception {
        for (int index = 0; index < types.length; index++) {
            checkClass("createHorizontalMenuItemSeparator",
                       getExpectedHorizontalMenuItemSeparatorRendererClass(
                               types[index]),
                       createFactory().createHorizontalMenuItemSeparator(
                               types[index]));
        }
    }

    /**
     * Tests that the correct type of separator renderer is created for the
     * tested factory method. Relies on the appropriate
     * <code>getExpected...Class</code> method in this test case.
     */
    public void testCreateVerticalMenuItemSeparator() throws Exception {
        checkClass("createVerticalMenuItemSeparator",
                   getExpectedVerticalMenuItemSeparatorRendererClass(),
                   createFactory().createVerticalMenuItemSeparator());
    }

    /**
     * Tests that the correct type of separator renderer is created for the
     * tested factory method. Relies on the appropriate
     * <code>getExpected...Class</code> method in this test case.
     */
    public void testCreateCharacterMenuItemGroupSeparator() throws Exception {
        checkClass("createCharacterMenuItemGroupSeparator",
                   getExpectedCharacterMenuItemGroupSeparatorRendererClass(),
                   createFactory().createCharacterMenuItemGroupSeparator("-",
                                                                         2));
    }

    /**
     * Tests that the correct type of separator renderer is created for the
     * tested factory method. Relies on the appropriate
     * <code>getExpected...Class</code> method in this test case.
     */
    public void testCreateImageMenuItemGroupSeparator() throws Exception {
        checkClass("createImageMenuItemGroupSeparator",
                   getExpectedImageMenuItemGroupSeparatorRendererClass(),
                   createFactory().createImageMenuItemGroupSeparator(
                           new LiteralImageAssetReference("url")));
    }

    /**
     * Supporting method used to perform the verification of returned type.
     *
     * @param method   the name of the method under test
     * @param expected the expected class of result, null if the result should
     *                 be null
     * @param actual   the result, may be null
     */
    protected void checkClass(String method,
                              Class expected,
                              Object actual) throws Exception {
        if (expected == null) {
            assertNull(method + " should return null", actual);
        } else {
            assertSame(method + " should return " + expected.getName(),
                       expected,
                       actual.getClass());
        }
    }

    /**
     * Returns an instance of the factory class to be tested.
     *
     * @return an instance of the factory class to be tested
     */
    protected abstract MenuSeparatorRendererFactory createFactory();

    /**
     * Returns the class of the specified separator renderer or null if no
     * renderer should have been created.
     *
     * @param type the type of horizontal separator needed
     * @return the separator renderer's class or null
     */
    protected abstract Class getExpectedHorizontalMenuSeparatorRendererClass(
            StyleKeyword type);

    /**
     * Returns the class of the specified separator renderer or null if no
     * renderer should have been created.
     *
     * @return the separator renderer's class or null
     */
    protected abstract Class getExpectedVerticalMenuSeparatorRendererClass();

    /**
     * Returns the class of the specified separator renderer or null if no
     * renderer should have been created.
     *
     * @return the separator renderer's class or null
     */
    protected abstract Class getExpectedVerticalMenuItemSeparatorRendererClass();

    /**
     * Returns the class of the specified separator renderer or null if no
     * renderer should have been created.
     *
     * @param type the type of horizontal separator needed
     * @return the separator renderer's class or null
     */
    protected abstract Class getExpectedHorizontalMenuItemSeparatorRendererClass(
            StyleKeyword type);

    /**
     * Returns the class of the specified separator renderer or null if no
     * renderer should have been created.
     *
     * @return the separator renderer's class or null
     */
    protected abstract Class getExpectedCharacterMenuItemGroupSeparatorRendererClass();

    /**
     * Returns the class of the specified separator renderer or null if no
     * renderer should have been created.
     *
     * @return the separator renderer's class or null
     */
    protected abstract Class getExpectedImageMenuItemGroupSeparatorRendererClass();
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
