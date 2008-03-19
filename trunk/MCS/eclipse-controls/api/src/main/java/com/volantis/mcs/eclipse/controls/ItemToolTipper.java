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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Provide a mechanism for adding toolips to an Item in an item container
 * such as a Table, Tree or List.
 */
public class ItemToolTipper {
    /**
     * The data item key for the tooltip text.
     */
    private static final String TOOLTIP_KEY = "tt_tooltip";

    /**
     * The data item key identifying the toolip listener attached to a widget.
     */
    private static final String TOOLTIP_LISTENER_KEY = "tt_tooltip_listener";

    /**
     * The ItemContainer associated with this ItemToolTipper.
     */
    private final ItemContainer container;

    /**
     * A Listener for handling the tool tips.
     */
    private Listener toolTipListener;

    /**
     * Construct a new ItemToolTipper for items in the specified
     * ItemContainer.
     * @param container The ItemContainer. Must not be null.
     * @throws IllegalArgumentException If container is null.
     */
    public ItemToolTipper(ItemContainer container) {
        if(container==null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "container");
        }
        this.container = container;
            addToolTipListener();
    }

    /**
     * Set the toolTip text on a item.
     * @param item The Item.
     * @param toolTip The toolTip text.
     */
    public void setToolTipText(Item item, String toolTip) {
        item.setData(TOOLTIP_KEY, toolTip);
    }

    /**
     * Add a listener that handles tooltips to a widget if no such listener
     * already exists on the widget.
     */
    private void addToolTipListener() {
        toolTipListener = new Listener() {
            Shell tip = null;
            Label label = null;

            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.Dispose:
                        removeListeners();
                    case SWT.KeyDown:
                    case SWT.MouseDown:
                    case SWT.MouseMove:
                    case SWT.MouseExit:
                        if (tip != null) {
                            tip.dispose();
                            tip = null;
                            label = null;
                        }
                        break;
                    case SWT.MouseHover:
                        if (tip != null && !tip.isDisposed()) {
                            tip.dispose();
                        }
                        Display display = container.getDisplay();
                        Point pt = new Point(event.x, event.y);
                        Item item = container.getItem(pt);
                        if (item != null) {
                            String tooltipText = getToolTipText(item);
                            if (tooltipText != null) {
                                tip = new Shell(display, SWT.ON_TOP);
                                tip.setLayout(new FillLayout());
                                label = new Label(tip, SWT.CENTER);
                                label.setText(tooltipText);
                                label.setForeground(display.
                                        getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                                label.setBackground(display.
                                        getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                                Point size = tip.computeSize(SWT.DEFAULT,
                                        SWT.DEFAULT);
                                pt = display.getCursorLocation();

                                Rectangle displayBounds = display.getBounds();
                                int left = pt.x;
                                int width = size.x + 2;
                                if ((left + width) > displayBounds.width) {
                                    left = (width  > displayBounds.width) ?
                                            0 : displayBounds.width - width;
                                }

                                tip.setBounds(left, pt.y + size.y,
                                        width, size.y);
                                tip.setVisible(true);
                            }
                        }
                }
            }
        };
        container.setData(TOOLTIP_LISTENER_KEY, toolTipListener);
        container.addListener(SWT.Dispose, toolTipListener);
        container.addListener(SWT.KeyDown, toolTipListener);
        container.addListener(SWT.MouseDown, toolTipListener);
        container.addListener(SWT.MouseExit, toolTipListener);
        container.addListener(SWT.MouseHover, toolTipListener);
        container.addListener(SWT.MouseMove, toolTipListener);
    }

    /**
     * Remove the listeners that were added to the container.
     */
    private void removeListeners() {
        container.removeListener(SWT.Dispose, toolTipListener);
        container.removeListener(SWT.KeyDown, toolTipListener);
        container.removeListener(SWT.MouseDown, toolTipListener);
        container.removeListener(SWT.MouseExit, toolTipListener);
        container.removeListener(SWT.MouseHover, toolTipListener);
        container.removeListener(SWT.MouseMove, toolTipListener);
    }

    /**
     * Get the TooltipText for a given Item. Subclasses should override
     * this method if their tooltip text is not made available by a call to
     * setToolTipText.
     * @param item The Item used to retrieve the tool tip text.
     * @return The tool tip text associated with the given item or null if
     * no tool tip was available.
     */
    protected String getToolTipText(Item item) {
         return (String) item.getData(TOOLTIP_KEY);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 18-Jan-04	2562/1	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 23-Dec-03	2299/1	richardc	VBM:2003121719 Use pt.x not pt.x-size.x as x parameter of setBounds

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
