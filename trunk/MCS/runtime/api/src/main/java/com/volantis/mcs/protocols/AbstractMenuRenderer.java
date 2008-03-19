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
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.wml.MenuRendererContext;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract implementation of the {@link MenuRenderer} interface. This
 * implementation manges the iteration over the menu items and groups.
 * It is up to subclasses of this class to implement the methods
 * that actually write the markup to the output buffer.
 */
public abstract class AbstractMenuRenderer
        implements MenuRenderer, MenuChildRendererVisitor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractMenuRenderer.class);

    /**
     * The {@link MenuRendererContext} associated with the menu being rendered
     */
    protected final MenuRendererContext menuRendererContext;

    /**
     * The {@link MarinerPageContext} associated with the page being rendered
     */
    protected final MarinerPageContext pageContext;

    /**
     * Creates a new <code>AbstractMenuRenderer</code>
     * @param pageContext the <code>MarinerPageContext</code> associated with
     * the page being rendered
     */
    protected AbstractMenuRenderer(MenuRendererContext menuRendererContext,
                                MarinerPageContext pageContext) {
        this.menuRendererContext = menuRendererContext;
        this.pageContext = pageContext;
    }

    // javadoc inherited
    public void renderMenu(MenuAttributes attributes)
            throws ProtocolException {
        // obtain the buffer that the menu will be rendered to.
        DOMOutputBuffer outputBuffer = getOutputBuffer(attributes);
        // open the menu
        openMenu(outputBuffer, attributes);
        // render the menu items
        Collection menuItems = attributes.getItems();
        renderMenuItems(attributes, menuItems, outputBuffer);
        // close the menu
        closeMenu(outputBuffer, attributes);
    }

    /**
     * Render a <code>Collection</code> of menu items and menu groups.
     * @param attributes the MenuAttributes
     * @param menuItems the collection of menu items and menu groups
     * @param outputBuffer the buffer to render the items into
     * @throws ProtocolException if an error occurs
     */
    private void renderMenuItems(MenuAttributes attributes,
                                   Collection menuItems,
                                   DOMOutputBuffer outputBuffer)
            throws ProtocolException {

        MenuChildVisitable menuItem;
        for (Iterator i = menuItems.iterator(); i.hasNext();) {
            menuItem = (MenuChildVisitable) i.next();
            menuItem.visit(this,
                           outputBuffer,
                           attributes,
                           i.hasNext(),
                           false,
                           MenuOrientation.VERTICAL);
        }
    }

    // javadoc inherited
    public void renderMenuChild(DOMOutputBuffer dom,
                                MenuAttributes attributes,
                                MenuItem child,
                                boolean notLast,
                                boolean iteratorPane,
                                MenuOrientation orientation)
            throws ProtocolException {
        if (logger.isDebugEnabled()) {
            logger.debug("visiting menu item " + child);
        }
        // write out the menu item
        writeMenuItem(dom, child);
    }

    // javadoc inherited
    public void renderMenuChild(DOMOutputBuffer dom,
                                MenuAttributes attributes,
                                MenuItemGroupAttributes groupAttributes,
                                boolean notLast,
                                boolean iteratorPane,
                                MenuOrientation orientation)
            throws ProtocolException {
        if (logger.isDebugEnabled()) {
            logger.debug("visiting menu group item " + groupAttributes);
        }
        // open the menu grouping
        openMenuGroup(dom, groupAttributes);
        // renderer any menu items/groups that this group encloses.
        renderMenuItems(attributes, groupAttributes.getItems(), dom);
        // close the menu grouping
        openMenuGroup(dom, groupAttributes);
    }

    /**
     * Returns that DOMOutputBuffer that the menu is rendered into.
     * @return a DOMOutputBuffer instance
     */
    protected abstract DOMOutputBuffer getOutputBuffer(
            MenuAttributes attributes) throws ProtocolException;

    /**
     * Write out the markup that opens the menu
     * @param outputBuffer the DOMOutputBuffer to write to.
     * @param attributes the attributes for the menu that is being opened
     */
    protected abstract void openMenu(DOMOutputBuffer outputBuffer,
                                     MenuAttributes attributes)
            throws ProtocolException;

    /**
     * Write out the markup that closes the menu
     * @param outputBuffer the DOMOutputBuffer to write to.
     * @param attributes the attributes for the menu that is being closed
     */
    protected abstract void closeMenu(DOMOutputBuffer outputBuffer,
                                      MenuAttributes attributes)
            throws ProtocolException;

    /**
     * Write out the markup that opens a menu group
     * @param outputBuffer the DOMOutputBuffer to write to.
     * @param groupAtts the attributes for the menu group that is being opened
     */
    protected abstract void openMenuGroup(DOMOutputBuffer outputBuffer,
                                          MenuItemGroupAttributes groupAtts)
            throws ProtocolException;

    /**
     * Write out the markup that closes a menu group
     * @param outputBuffer the DOMOutputBuffer to write to.
     * @param groupAtts the attributes for the menu group that is being closed
     */
    protected abstract void closeMenuGroup(DOMOutputBuffer outputBuffer,
                                           MenuItemGroupAttributes groupAtts)
            throws ProtocolException;

    /**
     * Write out the markup for a menu item
     * @param outputBuffer the DOMOutputBuffer to write to.
     * @param menuItem the menu items attributes
     */
    protected abstract void writeMenuItem(DOMOutputBuffer outputBuffer,
                                         MenuItem menuItem)
            throws ProtocolException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 18-Sep-03	1394/3	doug	VBM:2003090902 Added support for Openwave numeric shortcut menus

 17-Sep-03	1394/1	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 ===========================================================================
*/
