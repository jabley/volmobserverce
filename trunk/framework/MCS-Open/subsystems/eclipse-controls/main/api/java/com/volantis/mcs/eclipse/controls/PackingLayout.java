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

package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

/**
 * This layout permits controls that are subject to this layout to be packed
 * (moved to the top of the display) if visible.<p>
 *
 * Invsible controls are deliberately ignored when computing the layout's size.<p>
 *
 * This layout only permits 1 or 2 columns to be displayed. If 2 columns have
 * been specified it is assumed that the first column should contain a Label
 * control.<p>
 *
 * This layout also caters for basic column spanning using the set/getLayoutData
 * methods and relying on the existing GridData object's <code>horizontalSpanning</code>
 * field. <p> If this value is set to a value > 1 then the control being
 * processed will take up all the rest of the column real-estate.
 */
public class PackingLayout extends Layout {

    /**
     * Constant to prevent controls growing horizontally in size.
     * todo: Find out how to retrieve this value dynamically. It is most likely derived from ((2xMARGIN)+(2*SPACING))*2 where MARGIN is 5 and SPACING is 2.
     */
    private static final int TRIM_FACTOR = 28;

    /**
     * An array of the sizes of each of the controls in the layout.
     */
    private Point[] sizes;

    /**
     * The maximum computed width of each of the columns.
     */
    private int columnWidth[];

    /**
     * The total combined height of the controls.
     */
    private int totalHeight;

    /**
     * The maximum combined width of the controls in a row.
     */
    private int maxWidth;

    /**
     * The default number of columns.
     */
    private int numberOfColumns = 1;

    /**
     * The default first column width.
     */
    private int firstColumnWidth = -1;

    /**
     * The vertical spacing between rows.
     */
    public int verticalSpacing = 0;

    /**
     * The margin height definition (vertical client area offset).
     */
    public int marginHeight;

    /**
     * The margin width definition (horizontal client area offset).
     */
    public int marginWidth;

    /**
     * Default constructor that creates an instance of this object with the
     * default number of columns.
     */
    public PackingLayout() {
        this(1);
    }

    /**
     * Construct an instance of this object with the specified number of
     * columns.
     *
     * @param numberOfColumns the number of columns which should be 1 or 2.
     * @throws IllegalArgumentException if the number of columns is not 1 or 2.
     */
    public PackingLayout(int numberOfColumns)
            throws IllegalArgumentException {

        if (numberOfColumns <= 0 || numberOfColumns > 2) {
            throw new IllegalArgumentException(
                    "Only 1 or 2 columns are currently supported." + numberOfColumns);
        }
        this.numberOfColumns = numberOfColumns;
    }

    // javadoc inherited.
    protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
        Control children[] = composite.getChildren();
        if (flushCache || sizes == null || sizes.length != children.length) {
            initialize(children);
        }

        int width = wHint;
        int height = hHint;
        if (wHint == SWT.DEFAULT) {
            width = maxWidth;
        }
        if (hHint == SWT.DEFAULT) {
            height = totalHeight;
        }
        width = getWidth(composite, width);

        // Set the height to 1 pixel effectively ignoring the marginHeight.
        // Note that the obvious value of 0 results in strange behaviour in
        // Eclipse.
        height = height == 0 ? 1 : height + (2 * marginHeight);

        return new Point(width, height);
    }

    /**
     * Helper method that computes the width of the client area from the
     * parent's client area. For some reason the client area itself returns a
     * zero width size.
     *
     * @param composite the composite to use to obtain the client area's width.
     * @param widthHint the current width hint.
     * @return the width of the client area that the controls may occupy.
     */
    private int getWidth(Composite composite, int widthHint) {
        int width = widthHint;
        Rectangle rect = composite.getParent().getClientArea();
        int spacing = 0;
        if (!(composite.getParent().getLayout() instanceof PackingLayout)) {
            rect = composite.getParent().getClientArea();
            spacing += TRIM_FACTOR;
        }

        width = Math.max(width + 2 * marginWidth,
                rect.width - (marginWidth * 2) - spacing);
        return width;
    }

    // javadoc inherited.
    protected void layout(Composite composite, boolean flushCache) {
        Control[] children = composite.getChildren();

        if (flushCache || sizes == null || sizes.length != children.length) {
            initialize(children);
        }
        int x = marginWidth;
        int y = marginHeight;

        // Get the maximum width of the composite's client area (used to allow
        // the control in the last column to fill the rest of the space).
        final int width = getWidth(composite, SWT.DEFAULT);

        for (int i = 0; i < children.length; i++) {
            final Control child = children[i];
            if (child.getVisible()) {
                int labelVOffset = 0;
                boolean span = getColumnSpan(child) > 1;

                int col = getColumnIndex(child, span);
                if (child instanceof Label) {
                    // Non-label control should be next in the sizes array.
                    int index = i >= sizes.length ? i : i + 1;
                    labelVOffset = (sizes[index].y - sizes[i].y) / 2;
                }

                // Determine if we need to move to the next row, handle spanning
                // and fill in space for the last column.
                if (col == (numberOfColumns - 1)) {
                    int fillWidth = width - getOtherColumnsWidth(numberOfColumns - 1);
                    fillWidth = fillWidth < 0 ? columnWidth[col] : fillWidth;
                    child.setBounds(x, y + labelVOffset, fillWidth, sizes[i].y);

                    // Bump up the row offset and reset the column offset.
                    y += getControlHeight(child, sizes[i].y);
                    x = marginWidth;
                } else {
                    // If we span then this control takes up all the rest of the
                    // columns horizontal real estate.
                    int otherColumnsWidth = getOtherColumnsWidth(col - 1);
                    int colWidth = span ? width - otherColumnsWidth :
                            columnWidth[col];

                    child.setBounds(x, y + labelVOffset, colWidth, sizes[i].y);

                    if (span) {
                        y += getControlHeight(child, sizes[i].y);
                    } else {
                        x += colWidth;
                    }
                }
            }
        }
    }

    /**
     * Initialize the various member variables related to how much real-estate
     * the controls actually use.
     * @param children the array of children controls.
     */
    void initialize(Control children[]) {
        // Total height is the combined vertical space taken up by all VISIBLE
        // controls.
        totalHeight = 0;

        // Store the maximum width for each column
        columnWidth = new int[numberOfColumns];
        sizes = new Point[children.length];

        for (int i = 0; i < children.length; i++) {

            sizes[i] = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

            boolean span = getColumnSpan(children[i]) > 1;
            int col = getColumnIndex(children[i], span);

            if (col == (numberOfColumns - 1) || span) {
                if (children[i].getVisible()) {
                    totalHeight += getControlHeight(children[i], sizes[i].y);
                }
            }
            if (!span) {
                columnWidth[col] = Math.max(columnWidth[col], sizes[i].x);
                columnWidth[col] = Math.max(columnWidth[col], firstColumnWidth);
            }
        }

        // Determine the maximum width of all the columns.
        maxWidth = 0;
        for (int index = 0; index < columnWidth.length; index++) {
            maxWidth += columnWidth[index];
        }
    }

    /**
     * Compute the width of all the columns up to the column identified by the index.
     * @param index the index of the column
     * @return the width of all other columns up to the column identified by the index.
     */
    private int getOtherColumnsWidth(final int index) {
        int otherColumnsWidth = 0;
        for (int i = 0; i < index; i++) {
            otherColumnsWidth += columnWidth[i];
        }
        return otherColumnsWidth;
    }

    /**
     * Determine the height of a control using the vertical margin and cached
     * height of the control.
     *
     * @param control the control itself .
     * @param height  the cached height value.
     * @return the controls height with vertical spacing and borderwidth taken
     *         into account.
     */
    private int getControlHeight(Control control, int height) {
        return 1 + height + (verticalSpacing * 2) +
                (control.getBorderWidth() * 2);
    }

    /**
     * Get the column index from the child based on the spane and index in the
     * sizes array. If the span has been set the column is assumed to be 0. Also
     * if the control is a label the column index is 0.
     *
     * @param control the control.
     * @param span  true if this control is to be spanned, false otherwise.
     * @return zero-based column index (may be 0 or 1 only).
     */
    private int getColumnIndex(Control control, boolean span) {
        // We can safely assume that if the control is a Label or we are spanning
        // or the number of columns is 1 then we must be in the first column,
        // otherwise we must be the second column.
        int result = 1;
        if (control instanceof Label || span || numberOfColumns == 1) {
            result = 0;
        }
        return result;
    }

    /**
     * Get the column span for a particular child. This is obtained from the
     * layout data's GridData object.
     *
     * @param child the child control.
     * @return -1 if nothing was found or the value of the horizontal span.
     */
    private int getColumnSpan(Control child) {
        int horizontalSpan = -1;
        final GridData gridData = ((GridData)child.getLayoutData());
        if (gridData != null) {
            horizontalSpan = gridData.horizontalSpan;
        }
        return horizontalSpan;
    }

    /**
     * Set the vertical alignmargin (vertical spacing between controls).
     * @param verticalSpacing the vertical margin/spacing between controls.
     */
    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }


    /**
     * Get the first columns width.
     * @return the first columns width.
     */
    public int getFirstColumnWidth() {
        int width = -1;
        if (columnWidth != null && columnWidth.length > 0) {
            width = columnWidth[0];
        }
        return width;
    }

    /**
     * Set the first column's width variable. T
     * @param width the first column's width variable. T
     */
    public void setFirstColumnWidth(int width) {
        this.firstColumnWidth = width;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Mar-04	3304/1	byron	VBM:2004021910 Layout: Format Attributes doesn't always display selection's details on Windows

 17-Feb-04	3066/1	byron	VBM:2004021707 PackingLayout is creating invisible controls in AttributesComposite

 16-Feb-04	2891/5	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - scrollbar fixed

 13-Feb-04	2891/3	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - take 2

 13-Feb-04	2891/1	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management

 ===========================================================================
*/
