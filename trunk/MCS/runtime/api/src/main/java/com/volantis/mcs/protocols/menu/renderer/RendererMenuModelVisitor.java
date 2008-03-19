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
package com.volantis.mcs.protocols.menu.renderer;

import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitor;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitorException;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A visitor which adapts MenuModelVisitor for use during rendering.
 * <p>
 * In particular, it adapts the exception types used to allow the user to 
 * provide code which uses RendererException rather than having to deal with
 * MenuModelVisitorException.
 * <p>
 * It does not do iteration (or come with steak knives).
 */ 
public abstract class RendererMenuModelVisitor 
        implements MenuModelVisitor {

    // Javadoc inherited.
    public void visit(MenuItem item) throws MenuModelVisitorException {
        // Adapt the exception type.
        try {
            rendererVisit(item);
        } catch (RendererException e) {
            throw new MenuModelVisitorException(e);
        }
    }

    // Javadoc inherited.
    public void visit(MenuItemGroup group) throws MenuModelVisitorException {
        // Adapt the exception type.
        try {
            rendererVisit(group);
        } catch (RendererException e) {
            throw new MenuModelVisitorException(e);
        }
    }

    // Javadoc inherited.
    public void visit(Menu menu) throws MenuModelVisitorException {
        // Adapt the exception type.
        try {
            rendererVisit(menu);
        } catch (RendererException e) {
            throw new MenuModelVisitorException(e);
        }
    }

    /**
     * Accepts this visitor into the menu entry provided.
     * <p>
     * This method is provided to adapt the accept methods on the entries 
     * themselves to throw RendererException rather than 
     * MenuModelVisitorException.
     * 
     * @param entry the menu entry to accept into the visitor
     * @throws RendererException
     */ 
    public void accept(MenuEntry entry) throws RendererException {
        try {
            entry.accept(this);
        } catch (MenuModelVisitorException e) {
            // Extract any contained renderer exception and rethrow it
            // to avoid nesting too many levels of exception.
            if (e.getCause() instanceof RendererException &&
                    e.getMessage() == null) {
                throw (RendererException) e.getCause();
            } else {
                throw new RendererException(e);
            }
        }
    }

    /**
     * Called when a menu item is found during a menu model traversal for
     * rendering.
     * <p>
     * This method is provided to adapt the related visit method on the 
     * visitor to throw RendererException rather than 
     * MenuModelVisitorException.
     *
     * @param item the menu item that has been found.
     * @throws RendererException if there is a problem during the visitation.
     */
    public abstract void rendererVisit(MenuItem item) 
            throws RendererException;

    /**
     * Called when a menu item group is found during a menu model traversal 
     * for rendering.
     * <p>
     * This method is provided to adapt the related visit method on the 
     * visitor to throw RendererException rather than 
     * MenuModelVisitorException.
     *
     * @param group the menu item group that has been found.
     * @throws RendererException if there is a problem during the visitation.
     */
    public abstract void rendererVisit(MenuItemGroup group) 
            throws RendererException;

    /**
     * Called when a menu is found during a menu model traversal for 
     * rendering.
     * <p>
     * This method is provided to adapt the related visit method on the 
     * visitor to throw RendererException rather than 
     * MenuModelVisitorException.
     *
     * @param menu the menu that has been found.
     * @throws RendererException if there is a problem during the visitation.
     */
    public abstract void rendererVisit(Menu menu) 
            throws RendererException;


    /**
     * Render the children of the menu.
     *
     * @param menu The menu whose children are to be rendered.
     *
     * @throws RendererException If a problem was encountered while
     * visting the children.
     */
    public void renderChildren(Menu menu) throws RendererException {
        int numChildren = menu.getSize();
        for (int i = 0; i < numChildren; i++) {
            MenuEntry menuItem = menu.get(i);
            accept(menuItem);
        }
    }

    /**
     * Render the children of the menu item group.
     *
     * @param group The menu item group whose children are to be rendered.
     *
     * @throws RendererException If a problem was encountered while
     * visting the children.
     */
    protected void renderChildren(MenuItemGroup group) throws RendererException {
        int numChildren = group.getSize();
        for (int i = 0; i < numChildren; i++) {
            MenuItem menuItem = group.get(i);
            accept(menuItem);
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

 17-May-04	4424/1	geoff	VBM:2004051414 Enhanced Menus: extra divs being written for labels.

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 16-Apr-04	3645/3	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/
