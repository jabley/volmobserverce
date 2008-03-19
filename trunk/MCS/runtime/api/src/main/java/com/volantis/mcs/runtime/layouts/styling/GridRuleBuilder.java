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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.Column;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Row;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.NthChildSelector;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Subject;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates style rules that when used by a {@link FormatStylingEngine} will
 * cause the grid's styles to be applied to the resulting markup.
 *
 * <p>The following rules are created:</p>
 *
 * <ul>
 *
 * <li>One for the grid that has the common styles associated with it.</li>
 *
 * <li>One for each column that specifies the width of the column.</li>
 *
 * <li>One for each row that specifies the height of the row.</li>
 *
 * <li>One for each cell that specifies the horizontal and vertical alignment
 * of the contained format, if any.</li>
 *
 * </ul>
 */
public class GridRuleBuilder
        extends AbstractFormatRuleBuilder {

    public GridRuleBuilder(
            FormatAttributeToStyleValueConverter converter,
            StyleSheetFactory factory) {
        super(converter, factory);
    }

    public void addRules(StyleSheet styleSheet, Format format) {
        Rule rule = createRuleWithoutProperties(format);

        MutableStyleProperties styleProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        // A grid is equivalent to a CSS table.
        initialiseCommonProperties(styleProperties, format,
                DisplayKeywords.TABLE);

        rule.setProperties(styleProperties);
        styleSheet.addRule(rule);

        Grid grid = (Grid) format;

        // Handle the columns.
        int columns = grid.getColumns();
        for (int c = 0; c < columns; c += 1) {
            Column column = grid.getColumn(c);
            addColumnRules(styleSheet, column);
        }

        // Handle the rows.
        int rows = grid.getRows();
        for (int r = 0; r < rows; r += 1) {
            Row row = grid.getRow(r);
            addRowRules(styleSheet, row);
        }

        // Handle the cells.
        for (int r = 0; r < rows; r += 1) {
            Row row = grid.getRow(r);
            for (int c = 0; c < columns; c += 1) {
                Format child = grid.getChildAt(r, c);
                addCellRules(styleSheet, grid, row, c, child);
            }
        }
    }

    private void addCellRules(
            StyleSheet styleSheet, Grid grid, Row row, int column,
            Format child) {

        Rule rule = ThemeFactory.getDefaultInstance().createRule();

        SelectorSequence context = createContextualSelectorSequence(row);
        Subject subject = createNthChildSelector(column);
        CombinedSelector selector = factory.createCombinedSelector();
        selector.setCombinator(CombinatorEnum.CHILD);
        selector.setContext(context);
        selector.setSubject(subject);

        List selectors = new ArrayList();
        selectors.add(selector);
        rule.setSelectors(selectors);

        MutableStyleProperties styleProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        // A grid cell is equivalent to a CSS table cell.
        styleProperties.setStyleValue(StylePropertyDetails.DISPLAY,
                DisplayKeywords.TABLE_CELL);

        StyleValue value;
        if (child != null) {
            value = converter.getHorizontalAlign(child.getHorizontalAlignment());
            if (value != null) {
                styleProperties.setStyleValue(StylePropertyDetails.TEXT_ALIGN,
                                              value);
            }

            value = converter.getVerticalAlign(child.getVerticalAlignment());
            if (value != null) {
                styleProperties.setStyleValue(StylePropertyDetails.VERTICAL_ALIGN,
                                              value);
            }
        }

        // Apply the cell padding from the grid as padding on the cells.
        setPadding(styleProperties, grid);

        rule.setProperties(styleProperties);

        styleSheet.addRule(rule);
    }

    private Subject createNthChildSelector(int column) {
        SelectorSequence sequence = factory.createSelectorSequence();
        NthChildSelector nthChild = factory.createNthChildSelector(
                0, column + 1);
        sequence.addSelector(nthChild);
        return sequence;
    }

    private void addRowRules(StyleSheet styleSheet, Row row) {
        Rule rule = createRuleWithoutProperties(row);

        MutableStyleProperties styleProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        // A grid row is equivalent to a CSS table row.
        styleProperties.setStyleValue(StylePropertyDetails.DISPLAY,
                DisplayKeywords.TABLE_ROW);

        StyleValue value = converter.getDimensionValue(row.getHeight(),
                row.getHeightUnits());
        if (value != null) {
            styleProperties.setStyleValue(StylePropertyDetails.HEIGHT, value);
        }

        rule.setProperties(styleProperties);
        styleSheet.addRule(rule);
    }

    private void addColumnRules(StyleSheet styleSheet, Column column) {
        Rule rule = createRuleWithoutProperties(column);

        MutableStyleProperties styleProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        StyleValue value = converter.getDimensionValue(column.getWidth(),
                column.getWidthUnits());
        if (value != null) {
            styleProperties.setStyleValue(StylePropertyDetails.WIDTH, value);
        }

        rule.setProperties(styleProperties);
        styleSheet.addRule(rule);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/4	emma	VBM:2005111705 Interim commit

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 02-Dec-05	10567/1	emma	VBM:2005112901 Forward port of bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10544/1	emma	VBM:2005112901 Bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/2	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/3	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
