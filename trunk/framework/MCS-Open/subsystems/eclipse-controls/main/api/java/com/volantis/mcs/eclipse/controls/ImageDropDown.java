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

import com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent;
import com.volantis.mcs.eclipse.controls.events.ImageDropDownItemChangeListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * A button which displays a drop-down menu of items and which
 * displays the image of the selected item.
 */
public class ImageDropDown extends Composite {

    /**
     * Resource prefix for the ImageDropDown.
     */
    private final static String RESOURCE_PREFIX = "ImageDropDown."; //$NON-NLS-1$

    /**
     * The horizontal gap between the menu item image and the down arrow image.
     */
    private static final int IMAGE_SEPARATION =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "images.horizontalSpacing").intValue();

    /**
     * The key used for passing the tooltip text via the MenuItem widget's
     * data field.
     */
    private static final String TOOLTIP_TEXT_DATA = "tooltipText";

    /**
     * The button of the control.
     */
    private Button button;

    /**
     * The button's background color. Needed when drawing
     * the icon images.
     */
    private Color buttonBackground;

    /**
     * The menu for the control.
     */
    private Menu menu;

    /**
     * The index of the currently selected menu item
     * in the menu. This is communicated to listeners
     * as an Integer in the SelectionEvent object, and
     * also as the usual int in the getItem() accessor.
     */
    private int currentSelectedItemIndex;

    /**
     * The list of menu items for the menu.
     */
    private ImageDropDownItem[] items;

    /**
     * The image cache.
     */
    private HashMap imageCache;

    /**
     * The down arrow image.
     */
    private Image downArrowImage;

    /**
     * The ImageDropDown's list of registered listeners.
     */
    private ListenerList listeners;

    /**
     * A map of ImageDropDownItems/MenuItem key/value pairs. This is used
     * when processing enablement changes from the source of a
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}
     * (i.e. the ImageDropDownItem) to determine which MenuItem widget should be
     * enabled or disabled.
     */
    private final Map dropDownItemHash;

    /**
     * Creates an ImageDropDown widget.
     * @param parent the parent Composite of the widget
     * @param style the style of the widget
     * @param items the ImageDropDownItems to use in the
     * ImageDropDown menu
     */
    public ImageDropDown(Composite parent, int style, ImageDropDownItem[] items) {
        super(parent, style);
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty: items.");
        }
        this.items = items;
        imageCache = new HashMap(items.length, 1);
        listeners = new ListenerList();
        // A load factor of 1 is specified to allow the HashMap to be filled to
        // its specified capacity, without having to resize. The default load
        // factor of 0.75 means that when the HashMap is 3/4 full, it will be
        // resized by rehashing to roughly double its size.
        dropDownItemHash = new HashMap(items.length, 1);

        addButton();
        createMenu();
        //You must have a DisposeListener to dispose of
        //any additional resources (typically those created
        //without a parent in the constructor).
        this.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if (!ImageDropDown.this.isDisposed()) {
                    disposeResources();
                }
            }
        });
        initAccessible();
    }


    /**
     * Creates and adds the button for ImageDropDown
     */
    private void addButton() {
        button = new Button(this, SWT.PUSH);
        //Save the background so button can be "cleared"
        //when creating the images for it.
        buttonBackground = button.getBackground();
        button.setImage(getButtonImage(items[0].getImage()));
        button.setToolTipText(items[0].getToolTipText());
        button.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                Rectangle buttonBounds = button.getBounds();
                Point point = button.toDisplay(new Point(buttonBounds.x, buttonBounds.y));
                menu.setLocation(point.x, point.y + buttonBounds.height);
                menu.setVisible(!menu.isVisible());
            }

            //Without this method you have to click twice on the
            //button after you've selected from the menu for the
            //menu to pop up again.
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        //Button must be packed to become visible
        //It appears not to be given a size in this
        //case until it is packed.
        button.pack();
    }

    /**
     * Creates the menu used by ImageDropDown from the ImageDropDownItems
     */
    private void createMenu() {
        // Creates the ImageDropDownItemChangeListener which listens for
        // tooltip text changes and ImageDropDownItem enablement changes.
        final ImageDropDownItemChangeListener changeListener =
                new ImageDropDownItemChangeListener() {
                    public void imageDropDownItemChanged(
                            ImageDropDownChangeEvent event) {
                        if (event.getType() ==
                                ImageDropDownChangeEvent.ENABLEMENT) {
                            handleEnablementChange(event);
                        } else if (event.getType() ==
                                ImageDropDownChangeEvent.TOOLTIP) {
                            handleTooltipChange(event);
                        }
                    }
                };

        //The menu must be added to the parent because the ImageDropDown
        //display only consists of the button. The menu is displayed
        //outside of the button and ImageDropDown.
        menu = new Menu(this.getParent());
        for (int i = 0; i < items.length; i++) {

            ImageDropDownItem iddItem = items[i];

            // ImageDropDown is interested in tooltip text changes and
            // enablement changes on its ImageDropDownItems.
            iddItem.addImageDropDownItemChangeListener(changeListener);

            String text = iddItem.getText();
            // Text can be null, but image cannot
            MenuItem menuItem = new MenuItem(menu, SWT.NONE);
            menuItem.setImage(iddItem.getImage());
            menuItem.setData(TOOLTIP_TEXT_DATA, iddItem.getToolTipText());
            menuItem.setText(text == null ? "" : text);

            // Store the ImageDropDownItem and MenuItem in a hash.
            dropDownItemHash.put(iddItem, menuItem);

            //Add a listener for menu selection events. When a
            //selection is made, update the button and inform
            //registered listeners.
            menuItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent selectionEvent) {
                    MenuItem menuItem = (MenuItem) selectionEvent.widget;
                    menu.setVisible(false);
                    button.setToolTipText((String)
                            menuItem.getData(TOOLTIP_TEXT_DATA));
                    button.setImage(getButtonImage(menuItem.getImage()));
                    currentSelectedItemIndex = menu.indexOf(menuItem);
                    Event e = new Event();
                    e.widget = selectionEvent.widget;
                    // ImageDropDown communicates the change
                    // with the selected index as an Integer.
                    e.data = new Integer(currentSelectedItemIndex);
                    SelectionEvent se = new SelectionEvent(e);
                    fireSelectionEvent(se);
                }
            });
        }
    }

    /**
     * The handler for
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}s
     * resulting from tooltip text changes.
     * @param event the change event
     */
    private void handleTooltipChange(ImageDropDownChangeEvent event) {
        ImageDropDownItem item = event.getImageDropDownItem();
        String updatedTooltipText = item.getToolTipText();
        button.setToolTipText(updatedTooltipText);
    }

    /**
     * The handler for
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}s
     * resulting from enablement changes.
     * @param event the change event
     */
    private void handleEnablementChange(ImageDropDownChangeEvent event) {
        ImageDropDownItem item = event.getImageDropDownItem();
        MenuItem menuItem = (MenuItem) dropDownItemHash.get(item);
        menuItem.setEnabled(item.isEnabled());
    }

    /**
     * This seems to be the best/only way of disposing of additional resources.
     * You must also call this method from an attached DisposeListener, otherwise
     * the method will not be called.
     */
    private void disposeResources() {
        for (Iterator it = imageCache.values().iterator(); it.hasNext();) {
            Image image = (Image) it.next();
            if (image != null && !image.isDisposed()) {
                image.dispose();
                image = null;
            }
        }
        imageCache.clear();
        if (downArrowImage != null && !downArrowImage.isDisposed()) {
            downArrowImage.dispose();
            downArrowImage = null;
        }
    }


    /**
     * Gets the image for the button.
     * @param origImage the key for the imageCache
     * @return the image to display on the button.
     */
    private Image getButtonImage(Image origImage) {
        Image image = (Image) imageCache.get(origImage);
        if (image == null) {
            image = combineImageWithArrow(origImage);
            if (image != null) {
                imageCache.put(origImage, image);
            }
        }
        return image;
    }


    /**
     * Creates and returns a new image by combining the supplied image
     * with the down arrow image. The returned image is displayed by
     * the button.
     * @param image the image from the menu to display
     * @return the combined image for the button
     */
    private Image combineImageWithArrow(Image image) {
        if (downArrowImage == null) {
            downArrowImage = ControlsMessages.getImage(RESOURCE_PREFIX + "images.downArrow");
        }
        //The combinedImage for the button has a width twice the width
        //of the image passed in. This means the down arrow image needs
        //to be centered within the remaining width.
        int width = image.getImageData().width;
        int height = image.getImageData().height;
        int newWidth = 2 * width + IMAGE_SEPARATION;
        int arrowPosX = width + (width - downArrowImage.getImageData().width) / 2;
        int arrowPosY = (height - downArrowImage.getImageData().height) / 2;
        Image combinedImage = new Image(button.getDisplay(), newWidth, height);
        GC newGC = new GC(combinedImage);
        //Clear button to background color
        newGC.setBackground(buttonBackground);
        newGC.fillRectangle(0, 0, newWidth, height);
        //Draw supplied image on the left
        newGC.drawImage(image, 0, 0);
        //Draw arrow image on the right
        newGC.drawImage(downArrowImage, arrowPosX + IMAGE_SEPARATION, arrowPosY);
        newGC.dispose();
        return combinedImage;
    }


    /**
     * Fires a SelectionEvent event to all registered listeners.
     * @param selectionEvent the SelectionEvent. selectionEvent.data
     * contains the currently selected item as an Integer.
     */
    private void fireSelectionEvent(SelectionEvent selectionEvent) {
        Object[] interested = listeners.getListeners();
        for (int i = 0; i < interested.length; i++) {
            if (interested[i] != null) {
                ((SelectionListener) interested[i]).widgetSelected(selectionEvent);
            }
        }
    }


    /**
     * Gets the current selection of the button.
     * @return the index of the currently selected item on the button.
     */
    public int getItem() {
        return currentSelectedItemIndex;
    }

    /**
     * Provide a mechanism for setting the selected item to that passed in as a
     * parameter. If the item isn't in the list, the selection is not changed.
     *
     * Calling this method does not result in a SelectionEvent being fired.
     *
     * @param selectedItem the selected item
     */
    public void setItem(ImageDropDownItem selectedItem) {
        if (menu != null) {
            MenuItem item = null;
            final MenuItem[] items = menu.getItems();
            int i = 0;
            for (i = 0; (item == null) && (i < items.length); i++) {
                if (items[i].getText().equals(selectedItem.getText())) {
                    item = items[i];
                }
            }
            if (item != null) {
                menu.setDefaultItem(item);
                // Update the ImageDrpDown's Button widget with the correct
                // image and tooltip text.
                item.setSelection(true);
                currentSelectedItemIndex = i - 1;
                button.setToolTipText((String) item.getData(TOOLTIP_TEXT_DATA));
                button.setImage(getButtonImage(item.getImage()));
            }
        }
    }

    /**
     * Add a SelectionListener.
     * @param listener the listener to add
     */
    public void addSelectionListener(SelectionListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a SelectionListener.
     * @param listener the listener to remove
     */
    public void removeSelectionListener(SelectionListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Gets the size of the ImageDropDown button.
     * @return a Point describing the size
     */
    public Point getSize() {
        return button.getSize();
    }

    /**
     * Sets the size of the ImageDropDown button
     * @param size a Point describing the size
     */
    public void setSize(Point size) {
        button.setSize(size);
    }

    /**
     * Sets the size of the ImageDropDown button
     * @param width the width of the button
     * @param height the height of the button
     */
    public void setSize(int width, int height) {
        button.setSize(width, height);
    }

    /**
     * Overriden to provide focus to the ImageDropDown's button.
     * @return the focus status.
     */
    public boolean setFocus() {
        return button.setFocus();
    }

    /**
     * Overridden to enable/disable all controls in this composite in order to
     * ensure the 'greyed' look is propagated to each control.
     *
     * @param enabled the enabled state.
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        menu.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        SingleComponentACL acl = new SingleComponentACL() {
            public void getValue(AccessibleControlEvent ae) {
                ae.result = items[currentSelectedItemIndex].getText();
            }
        };
        acl.setControl(this);
        acl.setRole(ACC.ROLE_COMBOBOX);
        getAccessible().addAccessibleControlListener(acl);

        StandardAccessibleListener al = new StandardAccessibleListener(this);
        getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/2	adrianj	VBM:2004102602 Accessibility support for custom controls

 30-Apr-04	4081/1	pcameron	VBM:2004031007 Added PoliciesSection

 22-Apr-04	3964/5	matthew	VBM:2004032601 reworked code to set the browseButton enabled state. When setItem is called on an ImageDropDown no SelectionEvent results.

 22-Apr-04	3964/3	matthew	VBM:2004032601 removed unused and missleading jdoc and inline comments. Added DEFAULT_SELECTION.

 21-Apr-04	3964/1	matthew	VBM:2004032601 2004032601 problem setText corrected. Minor refactoring to remove secondary storage of state and to move initialisation of browseButton enabled state to a more appropriate location

 02-Mar-04	3197/17	pcameron	VBM:2004021904 Further tweaks to PolicyValueOriginSelector

 01-Mar-04	3197/13	pcameron	VBM:2004021904 Rework issues

 01-Mar-04	3197/8	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/4	byron	VBM:2004012602 Addressed xxxlinkText enablement for dissecting panes and other bug fixes

 28-Jan-04	2752/2	byron	VBM:2004012602 Address issues from review

 05-Dec-03	2128/3	pcameron	VBM:2003112105 Tweaks to TextDefinition

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 04-Dec-03	2088/3	pcameron	VBM:2003112402 Some tweaks to ImageDropDown control

 03-Dec-03	2088/1	pcameron	VBM:2003112402 Added ImageDropDown control

 ===========================================================================
*/
