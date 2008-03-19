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

package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;

import java.util.HashMap;
import java.util.Map;

/**
 * Labels are associated with menu items and (optionally) menus themselves.
 */
public final class ConcreteMenuLabel extends AbstractModelElement
    implements MenuLabel, MutablePaneTargeted, MutableEventTarget,
        MutableTitled {

    /**
     * A label always has an associated text.
     */
    private MenuText text;

    /**
     * A label may have an associated icon.
     */
    private MenuIcon icon;

    /**
     * The pane that this label is targeted at.
     */
    private FormatReference pane;

    /**
     * A mapping for all the events that this class can handle along with the
     * associated handling code.
     */
    private final Map eventHandlers;

    /**
     * The optionally defined help text for this label.
     */
    private String title;

    /**
     * Initialises a new instance of the menu label and sets the text.
     *
     * @param elementDetails information about the PAPI element that is
     *                       associated with this menu entity. May be null.
     * @param text      The text to use for this label. Cannot be null
     */
    public ConcreteMenuLabel(ElementDetails elementDetails,
                             MenuText text) {
        this(elementDetails, text, null);
    }

    /**
     * Create a new instance of the menu label and sets the text and icon.
     *
     * @param elementDetails information about the PAPI element that is
     *                       associated with this menu entity. May be null.
     * @param text      The text to use for this label.  Cannot be null
     * @param icon      The icon to use for this label.  May be null
     */
    public ConcreteMenuLabel(ElementDetails elementDetails,
                             MenuText text,
                             MenuIcon icon) {
        super(elementDetails);
        setText(text);
        setIcon(icon);
        eventHandlers = new HashMap();
    }

    // JavaDoc inherited
    public FormatReference getPane() {
        return pane;
    }

    // JavaDoc inherited
    public void setPane(FormatReference pane) {
        this.pane = pane;
    }

    // JavaDoc inherited
    public ScriptAssetReference getEventHandler(EventType type) {
        if (type == null) {
            throw new IllegalArgumentException("Cannot have a null type");
        }
        
        return (ScriptAssetReference) eventHandlers.get(type);
    }

    // JavaDoc inherited
    public void setEventHandler(EventType type, ScriptAssetReference handler) {
        if (type == null) {
            throw new IllegalArgumentException("Cannot have a null type");
        }

        eventHandlers.put(type, handler);
    }

    // JavaDoc inherited
    public String getTitle() {
        return title;
    }

    // JavaDoc inherited
    public void setTitle(String title) {
        this.title = title;
    }

    // JavaDoc inherited
    public MenuIcon getIcon() {
        return icon;
    }

    /**
     * Sets the optional icon for the label.
     *
     * @param icon The icon to use for this label
     */
    public void setIcon(MenuIcon icon) {
        this.icon = icon;
    }

    // JavaDoc inherited
    public MenuText getText() {
        return text;
    }

    /**
     * Sets the text for the label.  This method will overwrite any existing
     * text value.  If it is necessary to know the previous value then a call
     * to this method should be coupled with a call to the
     * {@ link #getText() getText()} method.  The text to be set cannot
     * be null.
     *
     * @param text The text to use for this label.
     */
    public void setText(MenuText text) {
        if (text == null) {
            throw new IllegalArgumentException("MenuText cannot be null");
        }
        this.text = text;
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This class extends one that provides its own equals implementation,
        // so call it
        if (!super.equals(o)) {
            return false;
        }

        final ConcreteMenuLabel concreteMenuLabel = (ConcreteMenuLabel) o;

        // Check text for equality
        if ((text != null && !text.equals(concreteMenuLabel.text)) ||
                (text == null && concreteMenuLabel.text != null)) {
            return false;
        }

        // Text was equal so check icon for equality
        if ((icon != null && !icon.equals(concreteMenuLabel.icon)) ||
                (icon == null && concreteMenuLabel.icon != null)) {
            return false;
        }

        // Previous fields were equal so check the pane for equality
        if ((pane != null && !pane.equals(concreteMenuLabel.pane)) ||
                (pane == null && concreteMenuLabel.pane != null)) {
            return false;
        }

        // Previous fields were equal so check event handlers for equality
        if ((eventHandlers != null &&
                !eventHandlers.equals(concreteMenuLabel.eventHandlers)) ||
                (eventHandlers == null &&
                concreteMenuLabel.eventHandlers != null)) {
            return false;
        }

        // Check title for equality
        if ((title != null && !title.equals(concreteMenuLabel.title)) ||
                (title == null && concreteMenuLabel.title != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (text != null ? text.hashCode() : 0);
        result = 29 * result + (icon != null ? icon.hashCode() : 0);
        result = 29 * result + (pane != null ? pane.hashCode() : 0);
        result = 29 * result + (eventHandlers != null ?
                eventHandlers.hashCode() : 0);
        result = 29 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3491/5	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/3	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
