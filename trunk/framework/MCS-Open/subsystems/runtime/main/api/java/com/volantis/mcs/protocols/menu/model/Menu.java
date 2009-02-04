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

package com.volantis.mcs.protocols.menu.model;

import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * A menu may contain any combination of menu entries within it (including
 * nested menus). It has an associated label too.
 * 
 * <p><strong>NOTE:</strong> the deprecated menu type is accessible from the
 * style properties associated with this menu.</p>
 *
 * @mock.generate 
 */
public interface Menu extends MenuEntry, EventTarget, Titled {

    /**
     * Returns the (optional) associated label for this menu.
     *
     * @return the menu's label or null if it has none
     */
    MenuLabel getLabel();

    /**
     * Returns a representation of the menu as a menu item.  Repeated calls to
     * this method will result in the same object being returned as once an
     * instance has been created it will not be recreated.
     *
     * @return the menu item instance that can be used to represent this menu
     *         if the menu is a sub-menu, null otherwise.
     */
    MenuItem getAsMenuItem();

    /**
     * Returns the number of immediate child menu entries contained in this
     * menu.
     *
     * @return the number of immediate children of this menu (may be zero)
     */
    int getSize();

    /**
     * Returns the specified child of this menu.
     *
     * @param index the index of the child to return, in the range
     *              [0 .. size() - 1]
     * @return the requested child of this menu
     * @throws IndexOutOfBoundsException if the specified index is not within
     *                                   range
     */
    MenuEntry get(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the help value associated with this menu. This can be a
     * Component ID or literal text.
     *
     * @return the help for this menu
     */
    TextAssetReference getHelp();

    /**
     * Returns the error message associated with this menu. This can be a
     * Component ID or literal text.
     *
     * @return the error message for this menu
     */
    TextAssetReference getErrorMessage();

    /**
     * Returns the prompt value associated with this menu. This can be a
     * Component ID or literal text.
     *
     * @return the prompt for this menu
     */
    TextAssetReference getPrompt();

    /**
     * Returns the handler registered on the menu for the given type of event,
     * or null if a handler has not been registered. Each event handler can be
     * a component ID or a piece of literal text.
     *
     * <p><strong>NOTE:</strong> does not allow {@link EventType#ON_BLUR
     * ON_BLUR} or {@link EventType#ON_FOCUS ON_FOCUS} to be retrieved as these
     * are not applicable to menus.</p>
     */
    ScriptAssetReference getEventHandler(EventType type);

    /**
     * Get the shortcut properties object associated with this menu.
     * @return the shortcut Properties object associated witht his menu or null
     *  if there is not one.
     */
    public ShortcutProperties getShortcutProperties();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Apr-04	3945/1	claire	VBM:2004042006 Providing menu as menu item functionality

 26-Mar-04	3491/4	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/2	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 03-Mar-04	3288/1	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/
