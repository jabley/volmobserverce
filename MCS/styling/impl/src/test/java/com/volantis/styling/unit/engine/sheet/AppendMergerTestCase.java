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

package com.volantis.styling.unit.engine.sheet;

import com.volantis.mcs.themes.Priority;
import com.volantis.styling.compiler.Source;
import com.volantis.styling.compiler.StyleSheetSource;
import com.volantis.styling.impl.engine.sheet.AppendMerger;
import com.volantis.styling.impl.engine.sheet.MutableStylerListMock;
import com.volantis.styling.impl.engine.sheet.StylerListMerger;
import com.volantis.styling.impl.engine.sheet.StylerListMock;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerMock;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test cases for {@link AppendMerger}.
 */
public class AppendMergerTestCase
        extends TestCaseAbstract {

    protected MutableStylerListMock listMock;
    protected StylerListMock deltaMock;
    protected StylerMock listFirstNormalStylerMock;
    protected StylerMock listSecondNormalStylerMock;
    protected StylerMock listFirstImportantStyleMock;
    private StylerMock deltaFirstNormalStylerMock;
    private StylerMock deltaSecondNormalStylerMock;
    private StylerMock deltaFirstImportantStylerMock;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        listMock = new MutableStylerListMock("listMock", expectations);

        deltaMock = new StylerListMock("deltaMock", expectations);

        Source firstSource = StyleSheetSource.LAYOUT;
        Source secondSource = StyleSheetSource.THEME;

        listFirstNormalStylerMock = createStylerMock(
                "listFirstNormalStylerMock", firstSource, Priority.NORMAL);

        listSecondNormalStylerMock = createStylerMock(
                "listSecondNormalStylerMock", secondSource, Priority.NORMAL);

        listFirstImportantStyleMock = createStylerMock(
                "listFirstImportantStyleMock", firstSource, Priority.IMPORTANT);

        deltaFirstNormalStylerMock = createStylerMock(
                "deltaFirstNormalStylerMock", firstSource, Priority.NORMAL);

        deltaSecondNormalStylerMock = createStylerMock(
                "deltaSecondNormalStylerMock", secondSource, Priority.NORMAL);

        deltaFirstImportantStylerMock = createStylerMock(
                "deltaFirstImportantStylerMock", firstSource, Priority.IMPORTANT);

    }

    /**
     * Create a styler mock with the specified priority.
     *
     * @param identifier The identifier of the mock.
     * @param source
     * @param priority The priority of the styler.
     *
     * @return A new styler mock.
     */
    private StylerMock createStylerMock(final String identifier,
            Source source, final Priority priority) {

        StylerMock stylerMock = new StylerMock(identifier, expectations);
        stylerMock.expects.getSource().returns(source).any();
        stylerMock.expects.getPriority().returns(priority).any();
        return stylerMock;
    }

    /**
     * Test that it will merge an empty list into an empty list.
     */
    public void testMergeEmptyIntoEmpty() {

        Styler[] listStylers = new Styler[]{
        };
        Styler[] deltaStylers = new Styler[]{
        };
        Styler[] expectedStylers = new Styler[]{
        };

        doTestMerge(listStylers, deltaStylers, expectedStylers);
    }

    /**
     * Test that it will merge both normal and important into an empty list.
     */
    public void testMergeIntoEmpty() {

        Styler[] listStylers = new Styler[]{
        };
        Styler[] deltaStylers = new Styler[]{
            deltaFirstNormalStylerMock,
            deltaFirstImportantStylerMock,
        };
        Styler[] expectedStylers = new Styler[]{
            deltaFirstNormalStylerMock,
            deltaFirstImportantStylerMock,
        };

        doTestMerge(listStylers, deltaStylers, expectedStylers);
    }

    /**
     * Test that it will merge important into an list containing both.
     */
    public void testMergeImportantIntoBoth() {

        Styler[] listStylers = new Styler[]{
            listFirstNormalStylerMock,
            listFirstImportantStyleMock,
        };
        Styler[] deltaStylers = new Styler[]{
            deltaFirstImportantStylerMock,
        };
        Styler[] expectedStylers = new Styler[]{
            listFirstNormalStylerMock,
            listFirstImportantStyleMock,
            deltaFirstImportantStylerMock,
        };

        doTestMerge(listStylers, deltaStylers, expectedStylers);
    }

    /**
     * Test that it will merge when there are both normal and important in both
     * lists.
     */
    public void testMerge() {

        Styler[] listStylers = new Styler[]{
            listFirstNormalStylerMock,
            listFirstImportantStyleMock,
        };
        Styler[] deltaStylers = new Styler[]{
            deltaFirstNormalStylerMock,
            deltaFirstImportantStylerMock,
        };
        Styler[] expectedStylers = new Styler[]{
            listFirstNormalStylerMock,
            deltaFirstNormalStylerMock,
            listFirstImportantStyleMock,
            deltaFirstImportantStylerMock,
        };

        doTestMerge(listStylers, deltaStylers, expectedStylers);
    }

    /**
     * Test that it will merge when there are both normal and important in both
     * lists.
     */
    public void testMergeWithSource() {

        Styler[] listStylers = new Styler[]{
            listFirstNormalStylerMock,
            listFirstImportantStyleMock,
            listSecondNormalStylerMock,
        };
        Styler[] deltaStylers = new Styler[]{
            deltaFirstNormalStylerMock,
            deltaFirstImportantStylerMock,
            deltaSecondNormalStylerMock,
        };
        Styler[] expectedStylers = new Styler[]{
            listFirstNormalStylerMock,
            deltaFirstNormalStylerMock,
            listFirstImportantStyleMock,
            deltaFirstImportantStylerMock,
            listSecondNormalStylerMock,
            deltaSecondNormalStylerMock,
        };

        doTestMerge(listStylers, deltaStylers, expectedStylers);
    }

    private void doTestMerge(
            final Styler[] listStylers, final Styler[] deltaStylers,
            final Styler[] expectedStylers) {

        final List listStylerList = new ArrayList(Arrays.asList(listStylers));
        List deltaStylerList = new ArrayList(Arrays.asList(deltaStylers));

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        deltaMock.expects.iterator().returns(deltaStylerList.iterator());
        listMock.expects.listIterator().returns(listStylerList.listIterator());
        // dodgy merge adds them back in again.
        listMock.fuzzy.append(mockFactory.expectsInstanceOf(Styler.class))
                .does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        listStylerList.add(event.getArgument(Styler.class));
                        return null;
                    }
                }).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        List expectedStylerList = Arrays.asList(expectedStylers);

        StylerListMerger merger = new AppendMerger();
        merger.merge(listMock, deltaMock);

        assertEquals("Stylers", expectedStylerList, listStylerList);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
