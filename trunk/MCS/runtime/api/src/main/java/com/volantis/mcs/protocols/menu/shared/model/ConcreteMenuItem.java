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

import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitor;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitorException;

import java.util.HashMap;
import java.util.Map;

/**
 * Menu items represent actionable entries in menus (directly or within menu
 * groups). These items have associated stylistic properties that can be used
 * to control behaviour and presentation.
 */
public class ConcreteMenuItem extends AbstractMenuEntry
    implements MenuItem, MutableEventTarget, MutableTitled {

    /**
     * A menu item's presentational information is stored in the associated
     * label.
     */
    private MenuLabel label;

    /**
     * The URL that is the target of this menu item.
     */
    private LinkAssetReference href;

    /**
     * The segment (by name) at which this menu item is (optionally)
     * targeted. For use in a Montage context.
     */
    private String segment;

    /**
     * The optionally defined destination area for use in a WAPTV context.
     */
    private String target;

    /**
     * The optionally defined help text for this menu item.
     */
    private String title;

    /**
     * The prompt value associated with this menu. This can be a Component ID
     * or literal text.
     */
    private TextAssetReference prompt;

    /**
     * The shortcut associated with this menu item. This can be a Component ID
     * or literal text.
     */
    private TextAssetReference shortcut;

    /**
     * The menu provides a link from a given menu item to its containing menu
     * (this may be the item's immediate parent or could be arbitrarilly deeply
     * separated from the menu item by interceeding menu groups).
     */
    private Menu menu;

    /**
     * A mapping for all the events that this class can handle along with the
     * associated handling code.
     */
    private final Map eventHandlers;


    /**
     * Initialises a new instance of a menu item and initialises it with the
     * presentation label and information about the associated PAPI element
     * that was provided.  The label cannot be null.
     *
     * @param elementDetails information about the PAPI element which is
     *                       associated with this menu entity. Used when
     *                       displaying the menu
     * @param label     The presentation label used with this menu item
     */
    public ConcreteMenuItem(ElementDetails elementDetails, MenuLabel label) {
        super(elementDetails);
        setLabel(label);
        eventHandlers = new HashMap();
    }

    // JavaDoc inherited
    public boolean isSubMenuItem() {
        // The menu builder for usual menu items enforces the existence of a
        // non-null href, but for sub menus this is not set.
        return (href == null);
    }

    /**
     * A utility method that recurses through the parents of this menu item
     * (if any) to discover the containing menu item.  If there is null parent
     * this will throw an IllegalStateException because this situation should
     * never occur.
     */
    private void findMenu() {
        MenuEntry parent = getContainer();
        while (parent != null && !(parent instanceof Menu)) {
            parent = parent.getContainer();
        }
        if (parent == null) {
            throw new IllegalStateException(
                "Menu items must appear directly or indirectly within a menu");
        }
        menu = (Menu) parent;
    }

    // JavaDoc inherited
    public void accept(MenuModelVisitor visitor)
            throws MenuModelVisitorException {
        visitor.visit(this);
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

    /**
     * Removes the event handling for the given type.  Removed values are just
     * discarded.   If the old value is important then this method call should
     * be coupled with a call to the
     * {@link #getEventHandler(EventType) getEventHandler} method.
     * <p>
     * This method is intentionally package protected to prevent any
     * specializations outside of this package using it either directly
     * or through overriding.
     *
     * @param type The event type which whould have its handlers removed
     */
    void removeEventHandler(EventType type) {
        eventHandlers.remove(type);
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
    public String getTarget() {
        return target;
    }

    /**
     * Sets the destination area for use in a WAPTV context.
     *
     * @param target The destination area which should be targeted
     */
    public void setTarget(String target) {
        this.target = target;
    }

    // JavaDoc inherited
    public TextAssetReference getShortcut() {
        return shortcut;
    }

    /**
     * Sets the shortcut associated with this menu item. This can be a
     * component ID or literal text.
     *
     * <p>This is also aliased from accessKey.</p>
     *
     * @param shortcut The shortcut to use for the menu item
     */
    public void setShortcut(TextAssetReference shortcut) {
        this.shortcut = shortcut;
    }

    // JavaDoc inherited
    public String getSegment() {
        return segment;
    }

    /**
     * Sets the segment (by name) at which this menu item is targetted.
     * For use in a Montage context.
     *
     * @param segment The name of the segment to target
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    // JavaDoc inheited
    void setContainer(MenuEntry container) {
        super.setContainer(container);
        // Update the menu field
        findMenu();
    }

    // JavaDoc inherited
    public Menu getMenu() {
        return menu;
    }

    // JavaDoc inherited
    public MenuLabel getLabel() {
        return label;
    }

    /**
     * Sets the menu label associated with this menu item.
     *
     * @param label  The new label.  This cannot be null.
     */
    public void setLabel(MenuLabel label) {
        if (label == null) {
            throw new IllegalArgumentException("The label cannot be null");
        }
        this.label = label;
    }

    // JavaDoc inherited
    public LinkAssetReference getHref() {
        return href;
    }

    /**
     * Sets the URL that is the target of this menu item.
     *
     * @param href The new target URL string
     */
    public void setHref(LinkAssetReference href) {
        this.href = href;
    }

    // JavaDoc inherited
    public TextAssetReference getPrompt() {
        return prompt;
    }

    /**
     * Sets the prompt value associated with this menu item. This can be a
     * Component ID or literal text.
     *
     * @param prompt The new prompt value
     */
    public void setPrompt(TextAssetReference prompt) {
        this.prompt = prompt;
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This class extends one that provides its own equals implementation,
        // so call it
        if (!super.equals(o)) {
            return false;
        }

        final ConcreteMenuItem concreteMenuItem = (ConcreteMenuItem) o;

        // Check label for equality
        if ((label != null && !label.equals(concreteMenuItem.label)) ||
                (label == null && concreteMenuItem.label != null)) {
            return false;
        }

        // Labels were equal so check href for equality
        if ((href != null && !href.equals(concreteMenuItem.href)) ||
                (href == null && concreteMenuItem.href != null)) {
            return false;
        }

        // Previous fields were equal so check title for equality
        if ((title != null && !title.equals(concreteMenuItem.title)) ||
                (title == null && concreteMenuItem.title != null)) {
            return false;
        }

        // Previous fields were equal so check prompt for equality
        if ((prompt != null && !prompt.equals(concreteMenuItem.prompt)) ||
                (prompt == null && concreteMenuItem.prompt != null)) {
            return false;
        }

        // Previous fields were equal so check segment for equality
        if ((segment != null && !segment.equals(concreteMenuItem.segment)) ||
                (segment == null && concreteMenuItem.segment != null)) {
            return false;
        }

        // Previous fields were equal so check shortcut for equality
        if ((shortcut != null && !shortcut.equals(concreteMenuItem.shortcut)) ||
                (shortcut == null && concreteMenuItem.shortcut != null)) {
            return false;
        }

        // Previous fields were equal so check target for equality
        if ((target != null && !target.equals(concreteMenuItem.target)) ||
                (target == null && concreteMenuItem.target != null)) {
            return false;
        }

        // Previous fields were equal so check event handlers for equality
        if ((eventHandlers != null &&
                !eventHandlers.equals(concreteMenuItem.eventHandlers)) ||
                (eventHandlers == null &&
                concreteMenuItem.eventHandlers != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (label != null ? label.hashCode() : 0);
        result = 29 * result + (href != null ? href.hashCode() : 0);
        result = 29 * result + (segment != null ? segment.hashCode() : 0);
        result = 29 * result + (target != null ? target.hashCode() : 0);
        result = 29 * result + (title != null ? title.hashCode() : 0);
        result = 29 * result + (prompt != null ? prompt.hashCode() : 0);
        result = 29 * result + (shortcut != null ? shortcut.hashCode() : 0);
        result = 29 * result + (eventHandlers != null ?
                eventHandlers.hashCode() : 0);
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

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 20-Apr-04	3945/1	claire	VBM:2004042006 Providing menu as menu item functionality

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 26-Mar-04	3491/4	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/2	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 15-Mar-04	3342/1	philws	VBM:2004022707 Review comment updates and changing MenuItem href to Object

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
