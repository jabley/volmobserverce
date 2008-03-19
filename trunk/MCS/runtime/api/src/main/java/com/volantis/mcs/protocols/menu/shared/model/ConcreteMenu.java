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

import com.volantis.mcs.protocols.ShortcutProperties;
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
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuItemShortcutActiveKeywords;
import com.volantis.styling.values.PropertyValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A menu may contain any combination of menu entries within it (including
 * nested menus). It has an associated label.
 *
 * <p><strong>NOTE:</strong> the deprecated menu type is accessible from the
 * style properties associated with this menu.</p>
 */
public class ConcreteMenu extends AbstractMenuEntry
        implements Menu, MutableEventTarget, MutableTitled {

    /**
     * A menu may have an associated label.
     */
    private MenuLabel label;

    /**
     * A menu can contain an arbitrary number of menu entries, clearly
     * including whole nested menus as well as other menu entry
     * implementations.
     */
    private final List menuEntries;

    /**
     * The help value associated with this menu. This can be a Component ID or
     * literal text.
     */
    private TextAssetReference help;

    /**
     * The error message associated with this menu. This can be a Component ID
     * or literal text.
     */
    private TextAssetReference errorMessage;

    /**
     * The prompt value associated with this menu. This can be a Component ID
     * or literal text.
     */
    private TextAssetReference prompt;

    /**
     * The title associated with this menu.
     */
    private String title;

    /**
     * A mapping for all the events that this class can handle along with the
     * associated handling code.
     */
    private final Map eventHandlers;

    /**
     * The menu item associated with this menu.
     */
    private MenuItem menuItem;

    /**
     * The shortcut properties, if any, associated with this Menu.
     */
    private ShortcutProperties shortcutProperties;

    /**
     * Initialises a new instance of a Menu with the associated element
     * details.
     *
     * @param elementDetails details of the PAPI element which is associated
     * with this menu
     */
    public ConcreteMenu(ElementDetails elementDetails) {
        super(elementDetails);
        menuEntries = new ArrayList();
        eventHandlers = new HashMap();
    }

    // JavaDoc inherited
    public MenuItem getAsMenuItem() {
        // Only process as a menu item if this is a sub-menu that is not pane
        // targeted. The latter is the case because menu label "menu items"
        // are placeholders that are only required when sub-menus are rendered
        // in-line with their containing menus.
        if (isSubMenu() && (pane == null)) {
            if (menuItem == null) {
                // NB: The label is guaranteed to have a non-null style info in
                //     this case (see {@link MenuLabel}'s class javadoc for
                //     details)
                // @todo later check what additional values should be set up on the fake item
                ConcreteMenuItem item = new ConcreteMenuItem(
                        label.getElementDetails(),
                        label);
                item.setContainer(getContainer());
                item.setTitle(getTitle());
                menuItem = item;
            }
        }
        return menuItem;
    }

    /**
     * A utility method that checks to see whether the current menu is a
     * sub menu or a top level menu.
     *
     * @return true if the menu is a sub menu (i.e. it has a parent), false
     *         otherwise.
     */
    private boolean isSubMenu() {
        return (getContainer() != null);
    }

    // JavaDoc inherited
    public void accept(MenuModelVisitor visitor)
            throws MenuModelVisitorException {
        visitor.visit(this);
    }

    // JavaDoc inherited
    public MenuEntry get(int index) throws IndexOutOfBoundsException {
        return (MenuEntry) menuEntries.get(index);
    }

    /**
     * Add another entry to the collection of objects held by this menu.  This
     * will set the parent of the entry being added to the instance of this
     * class that will hold it.
     *
     * @param newEntry The entry to add
     */
    public void add(MenuEntry newEntry) {
        menuEntries.add(newEntry);
        ((AbstractMenuEntry) newEntry).setContainer(this);
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
        } else if (type.equals(EventType.ON_BLUR)) {
            throw new IllegalArgumentException("Blur events not applicable");
        } else if (type.equals(EventType.ON_FOCUS)) {
            throw new IllegalArgumentException("Focus events not applicable");
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
    public MenuLabel getLabel() {
        return label;
    }

    /**
     * Sets the presentational label for this menu
     *
     * @param label The label to use
     */
    public void setLabel(MenuLabel label) {
        this.label = label;
    }

    // JavaDoc inherited
    public int getSize() {
        return menuEntries.size();
    }

    // JavaDoc inherited
    public TextAssetReference getHelp() {
        return help;
    }

    /**
     * Sets the help object associated with this menu.   This can be a
     * Component ID or literal text.
     *
     * @param help The help to use
     */
    public void setHelp(TextAssetReference help) {
        this.help = help;
    }

    // JavaDoc inherited
    public TextAssetReference getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message associated with this menu.   This can be a
     * Component ID or literal text.

     * @param errorMessage The error message to use
     */
    public void setErrorMessage(TextAssetReference errorMessage) {
        this.errorMessage = errorMessage;
    }

    // JavaDoc inherited
    public TextAssetReference getPrompt() {
        return prompt;
    }

    /**
     * Sets the prompt value associated with this menu. This can be a
     * Component ID or literal text.
     *
     * @param prompt The prompt to use
     */
    public void setPrompt(TextAssetReference prompt) {
        this.prompt = prompt;
    }

    // JavaDoc inherited
    public String getTitle() {
        return title;
    }

    // JavaDoc inherited
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the shortcut properties object associated with this menu.
     * @param shortcutProps
     */
    public void setShortcutProperties(ShortcutProperties shortcutProps) {
        this.shortcutProperties = shortcutProps;
    }

    /**
     * Get the shortcut properties object associated with this menu. This method
     * actually completes the configuration of the returned ShortcutProperties
     * object and should not be called until after the menu is fully
     * constructed.
     * @return the shortcut Properties object associated witht his menu or null
     *  if there is not one.
     */
    public ShortcutProperties getShortcutProperties() {


        PropertyValues propertyValues =
                getElementDetails().getStyles().getPropertyValues();

        // shortcut properties should never be null but if it is then
        // create a default one.
        if (shortcutProperties == null) {
            shortcutProperties = new ShortcutProperties();


            if (propertyValues != null) {
                StyleValue activeStyle = propertyValues.
                        getComputedValue(StylePropertyDetails.
                                      MCS_MENU_ITEM_SHORTCUT_ACTIVE);

                boolean active =
                        (activeStyle == MCSMenuItemShortcutActiveKeywords.ACTIVE);
                shortcutProperties.setActive(active);
            }
        }
        return shortcutProperties;
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This class extends one that provides its own equals implementation,
        // so call it
        if (!super.equals(o)) {
            return false;
        }

        final ConcreteMenu concreteMenu = (ConcreteMenu) o;

        // Test label for equality
        if ((label != null && !label.equals(concreteMenu.label)) ||
                (label == null && concreteMenu.label != null)) {
            return false;
        }

        // Labels were equal so test menu entries for equality
        if ((menuEntries != null &&
                !menuEntries.equals(concreteMenu.menuEntries)) ||
                (menuEntries == null && concreteMenu.menuEntries != null)) {
            return false;
        }

        // Previous fields were equal so check title for equality
        if ((title != null && !title.equals(concreteMenu.title)) ||
                (title == null && concreteMenu.title != null)) {
            return false;
        }

        // Previous fields were equal so check prompt for equality
        if ((prompt != null && !prompt.equals(concreteMenu.prompt)) ||
                (prompt == null && concreteMenu.prompt != null)) {
            return false;
        }

        // Previous fields were equal so check error message for equality
        if ((errorMessage != null &&
                !errorMessage.equals(concreteMenu.errorMessage)) ||
                (errorMessage == null && concreteMenu.errorMessage != null)) {
            return false;
        }

        // Previous fields were equal so check help for equality
        if ((help != null && !help.equals(concreteMenu.help)) ||
                (help == null && concreteMenu.help != null)) {
            return false;
        }

        // Previous fields were equal so check event handlers for equality
        if ((eventHandlers != null &&
                !eventHandlers.equals(concreteMenu.eventHandlers)) ||
                (eventHandlers == null &&
                concreteMenu.eventHandlers != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (label != null ? label.hashCode() : 0);
        result = 29 * result + (menuEntries != null ?
                                menuEntries.hashCode() : 0);
        result = 29 * result + (help != null ? help.hashCode() : 0);
        result = 29 * result + (errorMessage != null ?
                                errorMessage.hashCode() : 0);
        result = 29 * result + (prompt != null ? prompt.hashCode() : 0);
        result = 29 * result + (title != null ? title.hashCode() : 0);
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

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 16-Feb-05	6129/7	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/3	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-May-04	4220/1	claire	VBM:2004050603 Enhance Menu Support: Builder: Validate nested pane names

 06-May-04	3999/3	philws	VBM:2004042202 Review updates

 06-May-04	3999/1	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 20-Apr-04	3945/1	claire	VBM:2004042006 Providing menu as menu item functionality

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 26-Mar-04	3491/4	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/2	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
