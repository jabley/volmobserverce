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

package com.volantis.mcs.protocols.menu.builder;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.styling.Styles;

/**
 * An implementation of this interface is used to build the menu model, based
 * on "build events" and "build data" corresponding to the methods contained on
 * this interface.
 *
 * <p>An implementation of this interface should be obtained from a {@link
 * MenuModelBuilderFactory} (possibly using the {@link
 * MenuModelBuilderFactory#getDefaultInstance default instance}). It is assumed
 * that the obtained instance would then be held by a "build director".</p>
 * @mock.generate 
 */
public interface MenuModelBuilder {
    /**
     * All the builder event and data methods throw BuilderExceptions if the
     * method invocation occurs when the build is in a state that is not
     * supported or expected by the method.
     *
     * @link dependency
     */
    /*# BuilderException lnkBuilderException; */

    /**
     * Returns the top-level menu for a completed menu model. This will return
     * null at any time before completion of the top-level menu.
     *
     * @return the top-level menu for a completed menu model, or null if the
     *         model is not complete
     */
    Menu getCompletedMenuModel();

    /**
     * Called to indicate that a new menu should be opened. Menus may be
     * started as the top-level entry in the menu model or nested within other
     * menus. A menu must have details of the associated PAPI element defined,
     * as designated by a call to {@link #setElementDetails}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void startMenu() throws BuilderException;

    /**
     * Called to indicate that the current menu should be closed. This returns
     * null unless the menu being closed is the outer-most one, in which case
     * the menu that has been built is returned. This must balance a call to
     * {@link #startMenu}.
     *
     * @return null except when the outer-most menu is closed in which case the
     *         menu that has just been completed is returned
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    Menu endMenu() throws BuilderException;

    /**
     * Called to indicate that a new menu group should be opened. Menu groups
     * may only appear as direct children of menus. A menu group must have
     * details of the associated PAPI element defined, as designated by a call
     * to {@link #setElementDetails}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void startMenuGroup() throws BuilderException;

    /**
     * Called to indicate that the current menu group should be closed. A menu
     * group must contain at least one menu item, as designated via calls to
     * {@link #startMenuItem} and {@link #endMenuItem}. This must balance a
     * call to {@link #startMenuGroup}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void endMenuGroup() throws BuilderException;

    /**
     * Called to indicate that a new menu item should be opened. Menu items may
     * appear as children of menus or menu groups. To maintain a valid build
     * state {@link #startLabel} must be called before calling {@link
     * #endMenuItem}. In addition, a menu item must have details of the
     * associated PAPI element defined, as designated by a call to
     * {@link #setElementDetails}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void startMenuItem() throws BuilderException;

    /**
     * Called to indicate that the current menu item should be closed. This
     * must balance a call to {@link #startMenuItem}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void endMenuItem() throws BuilderException;

    /**
     * Called to indicate that a menu label should be opened. Labels may appear
     * with menus or menu items. To maintain a valid build state {@link
     * #startText} must be called before calling {@link #endLabel}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void startLabel() throws BuilderException;

    /**
     * Called to indicate that the current menu label should be closed. This
     * must balance a call to {@link #startLabel}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void endLabel() throws BuilderException;

    /**
     * Called to indicate that a new menu icon should be opened. Menu icons may
     * appear within menu labels only. In order to maintain a valid build state
     * the {@link #setNormalImageURL} method must be called before calling
     * {@link #endIcon}.
     *
     * <p><strong>NOTE:</strong> Menu icons may currently not be used in the
     * label for a menu.</p>
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void startIcon() throws BuilderException;

    /**
     * Called to indicate that the current menu icon should be closed. This
     * must balance a call to {@link #startIcon}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void endIcon() throws BuilderException;

    /**
     * Called to indicate that a new menu text should be opened. Menu text may
     * appear within menu labels only. In order to maintain a valid build state
     * the {@link #setText} method must be called before calling {@link
     * #endText}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void startText() throws BuilderException;

    /**
     * Called to indicate that the current menu text should be closed. This
     * must balance a call to {@link #startText}.
     *
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void endText() throws BuilderException;

    /**
     * Called to set the pane reference for a currently open pane targeted
     * entity.
     *
     * @param pane the pane format reference. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setPane(FormatReference pane) throws BuilderException;

    /**
     * Called to set the prompt for the currently open menu or menu item.
     *
     * @param prompt the prompt component ID or string. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setPrompt(TextAssetReference prompt) throws BuilderException;

    /**
     * Called to set the error message for the currently open menu. This can be
     * a component ID or literal text.
     *
     * @param message the error message component ID or string. May not be
     *                null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setErrorMessage(TextAssetReference message) throws BuilderException;

    /**
     * Called to set the help message for the currently open menu. This can be
     * a component ID or literal text.
     *
     * @param help the help message component ID or string. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setHelp(TextAssetReference help) throws BuilderException;

    /**
     * Called to set the event handler for the specified event type on the
     * currently open event target. The handler can be a component ID or
     * literal text.
     *
     * @param eventType the type of event for which a handler is to be
     *                  registered. May not be null
     * @param handler   the handler for the specified type of event. May not be
     *                  null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setEventHandler(EventType eventType,
                         ScriptAssetReference handler) throws BuilderException;

    /**
     * Called to set the href on the currently open menu item.
     *
     * @param href the menu item's href. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setHref(LinkAssetReference href) throws BuilderException;

    /**
     * Called to set the name of the segment to which the currently open menu
     * item is to be targetted.
     *
     * @param segment the name of the segment to be targetted. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setSegment(String segment) throws BuilderException;

    /**
     * Sets the WAPTV destination area for the currently open menu item.
     *
     * @param target the name of the destination area
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setTarget(String target) throws BuilderException;

    /**
     * Called to set the title for the currently open titled entity.
     *
     * @param title the "help text" for a titled entity. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setTitle(String title) throws BuilderException;

    /**
     * Called to set the "normal" (or only) image URL for the currently open
     * icon.
     *
     * @param url the "normal" image URL. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setNormalImageURL(ImageAssetReference url) throws BuilderException;

    /**
     * Called to set the "over" image URL for the currently open icon.
     *
     * @param url the "over" image URL. May not be null
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setOverImageURL(ImageAssetReference url) throws BuilderException;

    /**
     * Called to set the details of the associated PAPI element (other than
     * deprecated menu type) for the currently open entity.
     *
     * @param elementName
     *                   the name of the element. May not be null
     * @param id         the ID. May be null
     * @param styles     the Styles, already populated as required based on the
     *                   other parameters and anything else necessary to
     *                   initialize them as required by the build director
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setElementDetails(
            String elementName,
            String id,
            Styles styles) throws BuilderException;

    /**
     * Called to set the shortcut for the currently open menu item. This can be
     * a component ID or literal text.
     *
     * <p><strong>NOTE:</strong> Shortcut is also aliased by
     * <code>accessKey</code></p>
     *
     * @param shortcut the handler defined using the shortcut (or alias)
     * @throws BuilderException if the handler is not appropriate to the
     *                          current state of the builder
     */
    void setShortcut(TextAssetReference shortcut) throws BuilderException;

    /**
     * Called to set the text for the currently open text.
     *
     * @param text the text value
     * @throws BuilderException if the request is not valid given the builder's
     *                          state
     */
    void setText(OutputBuffer text) throws BuilderException;

    /**
     * Call to set the poperties used when rendering menu item shortcuts.
     *
     *  @param shortcutProperties the properties to use when rendering the
     * shortcuts.
     *
     * @throws BuilderException if the method cannot be performed due to the
     * state the builder is in.
     */
    void setShortcutProperties(ShortcutProperties shortcutProperties)
            throws BuilderException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9609/1	ibush	VBM:2005082215 Move on/off color values for menu items

 30-Aug-05	9353/4	pduffin	VBM:2005081912 Removed style class from MCS Attributes, also removed getColor() from Style

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 02-Apr-04	3429/4	philws	VBM:2004031502 MenuLabelElement implementation

 29-Mar-04	3500/5	claire	VBM:2004031806 Fixed supermerge issues

 26-Mar-04	3500/2	claire	VBM:2004031806 Initial implementation of abstract component image references

 26-Mar-04	3491/3	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 15-Mar-04	3342/4	philws	VBM:2004022707 Review comment updates and changing MenuItem href to Object

 15-Mar-04	3342/2	philws	VBM:2004022707 Implement the Menu Model Builder

 05-Mar-04	3292/3	philws	VBM:2004022703 Added Menu Model Builder API

 ===========================================================================
*/
