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

import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.graphics.Image;


/**
 * Provides an abstraction of a menu item for the ImageDropDown widget. The
 * ImageDropDownItem allows
 * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownItemChangeListener}s
 * to be added to listen for
 * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}s.
 * These events are fired to registered listeners when the ImageDropDownItem
 * becomes enabled or disabed, and when its tooltip text changes.
 */
public final class ImageDropDownItem {

    /**
     * The image to use in the menu item. Cannot be null.
     */
    private Image menuItemImage;

    /**
     * The text for the menu item. May be null.
     */
    private String menuItemText;

    /**
     * The tooltip text to display for the ImageDropDown's button.
     */
    private String tooltipText;

    /**
     * A flag indicating whether the ImageDropDownItem is enabled.
     */
    private boolean enabled = true;

    /**
     * A list of listeners which respond to
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}s.
     */
    private ListenerList listeners;


    /**
     * Create an ImageDropDownItem with the specified data.
     * @param menuItemImage the image for the menu item
     * @param menuItemText the text for the menu item
     * @param tooltipText the tooltip text for a widget
     * interested in the menu choice made
     */
    public ImageDropDownItem(Image menuItemImage, String menuItemText, String tooltipText) {
        if (menuItemImage == null) {
            throw new IllegalArgumentException("Cannot be null: menuItemImage."); //$NON-NLS-1$
        }
        this.menuItemImage = menuItemImage;
        this.menuItemText = menuItemText == null ? "" : menuItemText; //$NON-NLS-1$
        this.tooltipText = tooltipText == null ? "" : tooltipText; //$NON-NLS-1$

        listeners = new ListenerList();
    }

    /**
     * Gets the text of the menu item
     * @return the menu item's text
     */
    public String getText() {
        return menuItemText;
    }

    /**
     * Gets the tooltip text
     * @return the tooltip text
     */
    public String getToolTipText() {
        return tooltipText;
    }

    /**
     * Sets the tooltip text for the ImageDropDownMenuItem and fires an
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}
     * of type
     * {com.volantis.mcs.eclipse.controls.events.ImageDropDownItemChangeQualifier.TOOLTIP}
     * to all registered listeners.
     * @param tooltip the new tooltip text
     */
    public void setToolTipText(String tooltip) {
        this.tooltipText = tooltip == null ? "" : tooltip;
        ImageDropDownChangeEvent event =
                new ImageDropDownChangeEvent(this,
                        ImageDropDownChangeEvent.TOOLTIP);
        fireChangeEvent(event);
    }

    /**
     * Gets the image of the menu item
     * @return the menu item's image
     */
    public Image getImage() {
        return menuItemImage;
    }

    /** Fires an
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}
     * of an appropriate type to all registered listeners.
     * @param event the change event
     */
    private void fireChangeEvent(ImageDropDownChangeEvent event) {
        Object[] interested = listeners.getListeners();
        for (int i = 0; i < interested.length; i++) {
            if (interested[i] != null) {
                ((ImageDropDownItemChangeListener) interested[i]).
                        imageDropDownItemChanged(event);
            }
        }
    }

    /**
     * Gets the enablement status of the ImageDropDownItem.
     * @return true if enabled; false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enablement status for the ImageDropDownMenuItem and fires an
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownChangeEvent}
     * of type
     * {com.volantis.mcs.eclipse.controls.events.ImageDropDownItemChangeQualifier.ENABLEMENT}
     * to all registered listeners.
     * @param enabled the new enablement status
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ImageDropDownChangeEvent event =
                new ImageDropDownChangeEvent(this,
                        ImageDropDownChangeEvent.ENABLEMENT);
        fireChangeEvent(event);
    }

    /**
     * Adds a
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownItemChangeListener}.
     * @param listener the listener to add
     */
    public void addImageDropDownItemChangeListener(
            ImageDropDownItemChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a
     * {@link com.volantis.mcs.eclipse.controls.events.ImageDropDownItemChangeListener}.
     * @param listener the listener to remove
     */
    public void removeImageDropDownItemChangeListener(
            ImageDropDownItemChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Apr-04	4081/1	pcameron	VBM:2004031007 Added PoliciesSection

 01-Mar-04	3197/8	pcameron	VBM:2004021904 Rework issues

 01-Mar-04	3197/4	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 04-Dec-03	2088/3	pcameron	VBM:2003112402 Some tweaks to ImageDropDown control

 03-Dec-03	2088/1	pcameron	VBM:2003112402 Added ImageDropDown control

 ===========================================================================
*/
