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
package com.volantis.mcs.protocols.menu.shared.builder;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.menu.shared.model.AbstractModelElement;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenu;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuIcon;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItemGroup;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuLabel;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuText;
import com.volantis.mcs.protocols.menu.shared.model.MutableEventTarget;
import com.volantis.mcs.protocols.menu.shared.model.MutablePaneTargeted;
import com.volantis.mcs.protocols.menu.shared.model.MutableTitled;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Stack;

/**
 * This is a complete menu model builder implementation. It utilizes the
 * concrete menu model implementation.
 *
 * <p><strong>NOTE:</strong> this class is responsible for checking
 * "completeness" of menu definitions. While it seems sensible to push this
 * checking down into the menu model itself, it is not easy to do without
 * having to introduce either dependencies between the various menu model
 * interface implementations or checking methods on the menu model API.</p>
 */
public class ConcreteMenuModelBuilder implements MenuModelBuilder {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ConcreteMenuModelBuilder.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(ConcreteMenuModelBuilder.class);

    /**
     * Tracks the current entity "path" (the stack of entities that are
     * currently "open"). Note that <code>null</code> values in the path are
     * not allowed.
     *
     * <p>This stack should be managed using the {@link #push} and {@link #pop}
     * methods.</p>
     */
    private final Stack entities = new Stack();

    /**
     * Tracks the current entity.
     */
    private Object currentEntity = null;

    /**
     * Holds the menu to be returned by {@link #getCompletedMenuModel}.
     */
    private Menu menu = null;

    /**
     * Returns the current entity as an {@link ConcreteMenu} if it is one, or
     * null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private ConcreteMenu getCurrentMenu() {
        ConcreteMenu current = null;

        if (currentEntity instanceof ConcreteMenu) {
            current = (ConcreteMenu) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as an {@link ConcreteMenuItemGroup} if it is
     * one, or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private ConcreteMenuItemGroup getCurrentMenuItemGroup() {
        ConcreteMenuItemGroup current = null;

        if (currentEntity instanceof ConcreteMenuItemGroup) {
            current = (ConcreteMenuItemGroup) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as an {@link ConcreteMenuItem} if it is one,
     * or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private ConcreteMenuItem getCurrentMenuItem() {
        ConcreteMenuItem current = null;

        if (currentEntity instanceof ConcreteMenuItem) {
            current = (ConcreteMenuItem) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as an {@link ConcreteMenuIcon} if it is one,
     * or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private ConcreteMenuIcon getCurrentMenuIcon() {
        ConcreteMenuIcon current = null;

        if (currentEntity instanceof ConcreteMenuIcon) {
            current = (ConcreteMenuIcon) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as an {@link ConcreteMenuLabel} if it is one,
     * or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private ConcreteMenuLabel getCurrentMenuLabel() {
        ConcreteMenuLabel current = null;

        if (currentEntity instanceof ConcreteMenuLabel) {
            current = (ConcreteMenuLabel) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as an {@link ConcreteMenuText} if it is one,
     * or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private ConcreteMenuText getCurrentMenuText() {
        ConcreteMenuText current = null;

        if (currentEntity instanceof ConcreteMenuText) {
            current = (ConcreteMenuText) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as an {@link AbstractModelElement} if it is one, or
     * null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private AbstractModelElement getCurrentModelElement() {
        AbstractModelElement current = null;

        if (currentEntity instanceof AbstractModelElement) {
            current = (AbstractModelElement) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as a {@link MutablePaneTargeted} if it is
     * one, or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private MutablePaneTargeted getCurrentPaneTargeted() {
        MutablePaneTargeted current = null;

        if (currentEntity instanceof MutablePaneTargeted) {
            current = (MutablePaneTargeted) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as a {@link MutableEventTarget} if it is
     * one, or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private MutableEventTarget getCurrentEventTarget() {
        MutableEventTarget current = null;

        if (currentEntity instanceof MutableEventTarget) {
            current = (MutableEventTarget) currentEntity;
        }

        return current;
    }

    /**
     * Returns the current entity as a {@link MutableTitled} if it is
     * one, or null otherwise.
     *
     * @return the current entity or null if not of the required class
     */
    private MutableTitled getCurrentTitled() {
        MutableTitled current = null;

        if (currentEntity instanceof MutableTitled) {
            current = (MutableTitled) currentEntity;
        }

        return current;
    }

    /**
     * Helper method used to push an item on to the {@link #entities} stack.
     *
     * @param entity the item to push onto the stack. May not be null
     */
    private void push(Object entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "pushed entity may not be null");
        }

        entities.push(entity);

        currentEntity = entity;
    }

    /**
     * Helper method used to pop an item off the {@link #entities} stack. The
     * new top-of-stack object is returned.
     *
     * @return the new top-of-stack object or null if the stack is empty after
     *         popping the top item
     */
    private Object pop() {
        entities.pop();

        if (entities.isEmpty()) {
            currentEntity = null;
        } else {
            currentEntity = entities.peek();
        }

        return currentEntity;
    }

    /**
     * Helper method used to throw a builder exception with an appropriate
     * message when the given class of item cannot have the {@link
     * #currentEntity} as its parent.
     *
     * @param clazz the class of item that cannot be a child of the current
     *              entity
     * @throws BuilderException the exception required
     */
    private void reportBadParent(Class clazz) throws BuilderException {
        throw new BuilderException(exceptionLocalizer.format(
                    "menu-entity-child-invalid",
                    new Object[] {
                        clazz.getName(),
                        (currentEntity != null ?
                            currentEntity.getClass().getName() :
                            null)}));
    }

    /**
     * Helper method used to throw a builder exception with an appropriate
     * message when the given attribute cannot be set on the {@link
     * #currentEntity}.
     *
     * @param name the name of attribute that cannot be set on the current
     *             entity
     * @throws BuilderException the exception required
     */
    private void reportBadParent(String name) throws BuilderException {
        throw new BuilderException(exceptionLocalizer.format(
                    "attribute-not-settable",
                    new Object[] {
                        name,
                        (currentEntity != null ?
                            currentEntity.getClass().getName() :
                            null)}));
    }

    /**
     * Helper method used to throw a builder exception with an appropriate
     * message when unbalanced calls to start/end methods are detected.
     *
     * @throws BuilderException the exception required
     */
    private void reportUnbalancedStartEnd() throws BuilderException {
        throw new BuilderException(
                    exceptionLocalizer.format("menu-model-builder-unbalanced"));
    }

    /**
     * Helper method used to throw a builder exception with an appropriate
     * message when the given type of entity is not allowed in the current
     * context.
     *
     * @param clazz the type of entity that is not allowed
     * @throws BuilderException the exception required
     */
    private void reportTypeNotAllowed(Class clazz) throws BuilderException {
        throw new BuilderException(
                    exceptionLocalizer.format("menu-model-entity-invalid",
                                              clazz.getName()));
    }

    /**
     * Helper method used to throw a builder exception with an appropriate
     * message when the given named value has not been set correctly in the
     * current context.
     *
     * @param name  the name of attribute that has a bad value
     * @param cause an optional causitive exception
     * @throws BuilderException the exception required
     */
    private void reportBadValue(String name,
                                Throwable cause) throws BuilderException {
        throw new BuilderException(exceptionLocalizer.format(
                    "menu-model-missing-attribute",
                    new Object[] {
                        name,
                        (currentEntity != null ?
                            currentEntity.getClass().getName() :null)}),
                                   cause);
    }

    /**
     * Helper method used to throw a builder exception with an appropriate
     * message when the given class of item is missing from a specific
     * context.
     *
     * @param clazz the class of item missing
     * @throws BuilderException the exception required
     */
    private void reportMissing(Class clazz) throws BuilderException {
        throw new BuilderException(
                    exceptionLocalizer.format("menu-model-error",
                                              clazz.getName()));
    }

    // javadoc inherited
    public Menu getCompletedMenuModel() {
        return menu;
    }

    // javadoc inherited
    public void startMenu() throws BuilderException {
        ConcreteMenu parent = getCurrentMenu();

        if (currentEntity == null) {
            push(new ConcreteMenu(new ConcreteElementDetails()));
        } else if (parent != null) {
            ConcreteMenu menu = new ConcreteMenu(new ConcreteElementDetails());
            parent.add(menu);
            push(menu);
        } else {
            reportBadParent(Menu.class);
        }
    }

    // javadoc inherited
    public Menu endMenu() throws BuilderException {
        if ((menu = getCurrentMenu()) != null) {
            checkMenu(menu);

            if (pop() != null) {
                // The menu is a nested menu, so don't return it (this tells
                // the build director that the build is not complete)
                menu = null;
            }
        } else {
            reportUnbalancedStartEnd();
        }

        return menu;
    }

    // javadoc inherited
    public void startMenuGroup() throws BuilderException {
        ConcreteMenu parentMenu = getCurrentMenu();

        if (parentMenu != null) {
            ConcreteMenuItemGroup group = new ConcreteMenuItemGroup(
                    new ConcreteElementDetails());
            parentMenu.add(group);
            push(group);
        } else {
            reportBadParent(MenuItemGroup.class);
        }
    }

    // javadoc inherited
    public void endMenuGroup() throws BuilderException {
        ConcreteMenuItemGroup group = getCurrentMenuItemGroup();

        if (group != null) {
            checkGroup(group);

            pop();
        } else {
            reportUnbalancedStartEnd();
        }
    }

    // javadoc inherited
    public void startMenuItem() throws BuilderException {
        ConcreteMenu parentMenu = getCurrentMenu();

        // May appear in menus or menu groups.
        // @todo later could use an interface common to the two target classes
        if (parentMenu != null) {
            ConcreteMenuItem item = new ConcreteMenuItem(
                    new ConcreteElementDetails(),
                    new ConcreteMenuLabel(
                            null,
                            new ConcreteMenuText()));
            parentMenu.add(item);
            push(item);
        } else {
            ConcreteMenuItemGroup parentGroup = getCurrentMenuItemGroup();

            if (parentGroup != null) {
                ConcreteMenuItem item = new ConcreteMenuItem(
                        new ConcreteElementDetails(),
                        new ConcreteMenuLabel(
                                null,
                                new ConcreteMenuText()));
                parentGroup.add(item);
                push(item);
            } else {
                reportBadParent(MenuItem.class);
            }
        }
    }

    // javadoc inherited
    public void endMenuItem() throws BuilderException {
        ConcreteMenuItem item = getCurrentMenuItem();
        if (item != null) {
            checkItem(item);

            pop();
        } else {
            reportUnbalancedStartEnd();
        }
    }

    // javadoc inherited
    public void startLabel() throws BuilderException {
        ConcreteMenu parentMenu = getCurrentMenu();

        // May appear in menus or menu items (the latter always has one)
        // @todo later could use an interface common to the two target classes
        if (parentMenu != null) {
            ConcreteMenuLabel label = new ConcreteMenuLabel(
                    new ConcreteElementDetails(), new ConcreteMenuText());

            parentMenu.setLabel(label);
            push(label);
        } else {
            ConcreteMenuItem item = getCurrentMenuItem();

            if (item != null) {
                push(item.getLabel());
            } else {
                reportBadParent(MenuLabel.class);
            }
        }
    }

    // javadoc inherited
    public void endLabel() throws BuilderException {
        ConcreteMenuLabel label = getCurrentMenuLabel();

        if (label != null) {
            // Note that the label is not checked here since we don't know the
            // context of the label's use
            pop();
        } else {
            reportUnbalancedStartEnd();
        }
    }

    // javadoc inherited
    public void startIcon() throws BuilderException {
        ConcreteMenuLabel parent = getCurrentMenuLabel();

        if (parent != null) {
            MenuIcon icon = parent.getIcon();

            if (icon == null) {
                icon = new ConcreteMenuIcon();

                parent.setIcon(icon);
            }

            push(icon);
        } else {
            reportBadParent(MenuIcon.class);
        }
    }

    // javadoc inherited
    public void endIcon() throws BuilderException {
        ConcreteMenuIcon icon = getCurrentMenuIcon();

        if (icon != null) {
            // Note that the icon is not checked here since we don't know the
            // context of the icon's use
            pop();
        } else {
            reportUnbalancedStartEnd();
        }
    }

    // javadoc inherited
    public void startText() throws BuilderException {
        // It is currently the case that the text, if allowed, will already
        // exist within the label
        ConcreteMenuLabel parent = getCurrentMenuLabel();

        if (parent != null) {
            if (parent.getText() != null) {
                push(parent.getText());
            } else {
                reportTypeNotAllowed(MenuText.class);
            }
        } else {
            reportBadParent(MenuText.class);
        }
    }

    // javadoc inherited
    public void endText() throws BuilderException {
        ConcreteMenuText text = getCurrentMenuText();

        if (text != null) {
            checkText(text);

            pop();
        } else {
            reportUnbalancedStartEnd();
        }
    }

    /**
     * Helper method that verifies that the menu is correctly configured.
     *
     * @param menu the menu to be checked. May be null
     * @throws BuilderException if the menu is not correctly configured
     * @todo later check the style info for the menu
     */
    private void checkMenu(Menu menu) throws BuilderException {
        if (menu != null) {
            checkLabel(menu.getLabel(), false);
        }
    }

    /**
     * Helper method that verifies that the group is correctly configured.
     *
     * @param group the menu group to be checked. May be null
     * @throws BuilderException if the menu group is not correctly configured
     * @todo later check the style info for the group
     */
    private void checkGroup(MenuItemGroup group) throws BuilderException {
        if (group != null) {
            if (group.getSize() == 0) {
                // Must have at least one menu item
                throw new BuilderException(
                            exceptionLocalizer.format("menu-item-required"));
            }
        }
    }

    /**
     * Helper method that verifies that the menu item is correctly configured.
     *
     * @param item the menu item to be checked. May be null
     * @throws BuilderException if the menu item is not correctly configured
     * @todo later check the style info for the item
     */
    private void checkItem(MenuItem item) throws BuilderException {
        if (item != null) {
            try {
                if (item.getHref() == null) {
                    reportBadValue("href", null);
                }
            } catch (BuilderException e) {
                throw e;
            } catch (Exception e) {
                throw new BuilderException(e);
            }

            checkLabel(item.getLabel(), true);
        }
    }

    /**
     * Helper method that verifies that the label is correctly configured.
     *
     * @param label the menu label to be checked. May be null
     * @param belongsToItem indicates that the label belongs to a menu item
     * @throws BuilderException if the menu label is not correctly configured
     */
    private void checkLabel(MenuLabel label,
                            boolean belongsToItem) throws BuilderException {
        if (label != null) {
            final ElementDetails elementDetails = label.getElementDetails();

            if (belongsToItem) {
                if (elementDetails != null) {
                    reportTypeNotAllowed(ElementDetails.class);
                }
            } else if (elementDetails == null) {
                reportMissing(ElementDetails.class);
            } else {
                checkElementDetails(elementDetails);
            }

            checkText(label.getText());
            checkIcon(label.getIcon(), belongsToItem);
        }
    }

    /**
     * Helper method that verifies that the element details are correctly
     * configured (i.e. has non null element name and styles).
     *
     * @param elementDetails the details to be checked. May be null
     * @throws BuilderException if the element details are not correctly configured
     */
    private void checkElementDetails(ElementDetails elementDetails)
            throws BuilderException {
        if (elementDetails != null) {
            String value = "elementName";

            try {
                if (elementDetails.getElementName() == null) {
                    reportBadValue(value, null);
                }

                value = "styles";

                if (elementDetails.getStyles() == null) {
                    reportBadValue(value, null);
                }
            } catch (BuilderException e) {
                // just propagate
                throw e;
            } catch (Exception e) {
                // wrap in a builder exception
                reportBadValue(value, e);
            }
        }
    }

    /**
     * Helper method that verifies that the text is correctly configured.
     *
     * @param text the menu text to be checked. May be null
     * @throws BuilderException if the menu text is not correctly configured
     */
    private void checkText(MenuText text) throws BuilderException {
        if (text != null) {
            // Make sure that the text has been set on the menu text
            try {
                if (text.getText() == null) {
                    reportBadValue("text", null);
                }
            } catch (BuilderException e) {
                // just propagate
                throw e;
            } catch (Exception e) {
                // wrap in a builder exception
                reportBadValue("text", e);
            }
        } else {
            reportMissing(MenuText.class);
        }
    }

    /**
     * Helper method that verifies that the icon is correctly configured.
     *
     * @param icon the menu icon to be checked. May be null
     * @param allowed indicates whether the icon is permitted at this point
     * @throws BuilderException if the menu icon is not correctly configured
     */
    private void checkIcon(MenuIcon icon,
                           boolean allowed) throws BuilderException {
        if (icon != null) {
            if (!allowed) {
                reportTypeNotAllowed(MenuIcon.class);
            } else {
                // Make sure that the normal URL has been set on the menu icon
                try {
                    if (icon.getNormalURL() == null) {
                        reportBadValue("normal URL", null);
                    }
                } catch (BuilderException e) {
                    // just propagate
                    throw e;
                } catch (Exception e) {
                    // wrap in a builder exception
                    reportBadValue("normal URL", e);
                }
            }
        }
    }

    // javadoc inherited
    public void setPane(FormatReference pane) throws BuilderException {
        MutablePaneTargeted entry = getCurrentPaneTargeted();

        if (entry != null) {
            try {
                checkNestedPanes(pane);
                entry.setPane(pane);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("pane");
        }
    }

    /**
     * Checks the parents of the current menu (if any) for any pane references
     * (again if any) that have the same stem as the pane passsed as a parameter
     * to this method.  If so a warning is logged and the return value will be
     * false (the check failed).
     *
     * @param pane The pane to look for in any parent menu entries
     * @return     True if the check succeeded and the pane was not found in
     *             any parents, false otherwise.
     */
    private boolean checkNestedPanes(FormatReference pane) {
        boolean success = true;

        if (menu != null) {
            MenuEntry currentMenu = menu.getContainer();
            while (currentMenu != null) {
                FormatReference ref = currentMenu.getPane();
                if (ref != null && ref.getStem().equals(pane.getStem())) {
                    // fail the check
                    success = false;
                    logger.warn("menu-target-warning", new Object[]{pane});
                }
                currentMenu = currentMenu.getContainer();
            }
        }

        // No real need to return a value at the moment as a warning is just
        // logged but in future there may be a need to check whether anything
        // was logged and it failed the check in the calling method.
        return success;
    }

    // javadoc inherited
    public void setPrompt(TextAssetReference prompt) throws BuilderException {
        ConcreteMenuItem item = getCurrentMenuItem();

        // Applicable to menus and menu items
        if (item != null) {
            try {
                item.setPrompt(prompt);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            ConcreteMenu menu = getCurrentMenu();

            if (menu != null) {
                try {
                    menu.setPrompt(prompt);
                } catch (Exception e) {
                    throw new BuilderException(e);
                }
            } else {
                reportBadParent("prompt");
            }
        }
    }

    // javadoc inherited
    public void setErrorMessage(TextAssetReference message) throws BuilderException {
        ConcreteMenu menu = getCurrentMenu();

        if (menu != null) {
            try {
                menu.setErrorMessage(message);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("error message");
        }
    }

    // javadoc inherited
    public void setHelp(TextAssetReference help) throws BuilderException {
        ConcreteMenu menu = getCurrentMenu();

        if (menu != null) {
            try {
                menu.setHelp(help);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("help");
        }
    }

    // javadoc inherited
    public void setEventHandler(EventType eventType,
                                ScriptAssetReference handler) throws BuilderException {
        MutableEventTarget item = getCurrentEventTarget();

        if (item != null) {
            try {
                item.setEventHandler(eventType, handler);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent((eventType == null) ? "event handler" :
                            eventType.toString());
        }
    }

    // javadoc inherited
    public void setHref(LinkAssetReference href) throws BuilderException {
        ConcreteMenuItem item = getCurrentMenuItem();

        if (item != null) {
            try {
                item.setHref(href);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("href");
        }
    }

    // javadoc inherited
    public void setSegment(String segment) throws BuilderException {
        ConcreteMenuItem item = getCurrentMenuItem();

        if (item != null) {
            try {
                item.setSegment(segment);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("segment");
        }
    }

    // javadoc inherited
    public void setTarget(String target) throws BuilderException {
        ConcreteMenuItem item = getCurrentMenuItem();

        if (item != null) {
            try {
                item.setTarget(target);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("target");
        }
    }

    // javadoc inherited
    public void setTitle(String title) throws BuilderException {
        MutableTitled item = getCurrentTitled();

        if (item != null) {
            try {
                item.setTitle(title);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("title");
        }
    }

    // javadoc inherited
    public void setNormalImageURL(ImageAssetReference url)
            throws BuilderException {
        ConcreteMenuIcon icon = getCurrentMenuIcon();

        if (icon != null) {
            try {
                icon.setNormalURL(url);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("normal URL");
        }
    }

    // javadoc inherited
    public void setOverImageURL(ImageAssetReference url)
            throws BuilderException {
        ConcreteMenuIcon icon = getCurrentMenuIcon();

        if (icon != null) {
            try {
                icon.setOverURL(url);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("over URL");
        }
    }

    // javadoc inherited
    public void setElementDetails(
            String elementName, String id,
            Styles styles)
            throws BuilderException {

        AbstractModelElement modelElement = getCurrentModelElement();

        if (modelElement != null) {
            ConcreteElementDetails elementDetails =
                    (ConcreteElementDetails) modelElement.getElementDetails();

            if (elementDetails == null) {
                // Handle those cases where the element detas are optional
                elementDetails = new ConcreteElementDetails();

                modelElement.setElementDetails(elementDetails);
            }

            try {
                elementDetails.setElementName(elementName);
                elementDetails.setId(id);
                elementDetails.setStyles(styles);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("style");
        }
    }

    // javadoc inherited
    public void setShortcut(TextAssetReference shortcut) throws BuilderException {
        ConcreteMenuItem item = getCurrentMenuItem();

        if (item != null) {
            try {
                item.setShortcut(shortcut);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("shortcut");
        }
    }

    // javadoc inherited
    public void setText(OutputBuffer text) throws BuilderException {
        ConcreteMenuText menuText = getCurrentMenuText();

        if (menuText != null) {
            try {
                menuText.setText(text);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            reportBadParent("text");
        }
    }

    /**
     * Set the shortcut properties on this menu builder.
     * @param shortcutProperties
     */
    public void setShortcutProperties(ShortcutProperties shortcutProperties)
            throws BuilderException {

        ConcreteMenu menu = getCurrentMenu();
        if (menu != null) {
            try {
                menu.setShortcutProperties(shortcutProperties);
            } catch (Exception e) {
                throw new BuilderException(e);
            }
        } else {
            throw new BuilderException(
                    exceptionLocalizer.
                    format("menu-building-shortcut-error"));
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9609/2	ibush	VBM:2005082215 Move on/off color values for menu items

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Mar-05	7240/1	emma	VBM:2005022812 mergevbm from MCS 3.3

 02-Mar-05	7214/1	emma	VBM:2005022812 Fixing leftover localization logging problems

 18-Feb-05	7028/3	matthew	VBM:2005021714 Fix some documentation and error messages

 18-Feb-05	7028/1	matthew	VBM:2005021714 Fix some documentation and error messages

 16-Feb-05	6129/9	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/7	matthew	VBM:2004102019 yet another supermerge

 27-Jan-05	6129/5	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/2	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 13-May-04	4325/1	geoff	VBM:2004051208 Enhance Menu Support: WML Dissection: Menu Module Component Factory

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/2	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-May-04	4220/1	claire	VBM:2004050603 Enhance Menu Support: Builder: Validate nested pane names

 06-Apr-04	3429/7	philws	VBM:2004031502 MenuLabelElement implementation

 06-Apr-04	3641/6	claire	VBM:2004032602 Enhancements and updating testcase coverage

 06-Apr-04	3641/4	claire	VBM:2004032602 Corrected icon and label validation

 30-Mar-04	3641/1	claire	VBM:2004032602 Using menu types and styles in PAPI

 29-Mar-04	3500/6	claire	VBM:2004031806 Fixed supermerge issues

 26-Mar-04	3500/3	claire	VBM:2004031806 Initial implementation of abstract component image references

 26-Mar-04	3491/3	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 15-Mar-04	3342/3	philws	VBM:2004022707 Review comment updates and changing MenuItem href to Object

 15-Mar-04	3342/1	philws	VBM:2004022707 Implement the Menu Model Builder

 ===========================================================================
*/
