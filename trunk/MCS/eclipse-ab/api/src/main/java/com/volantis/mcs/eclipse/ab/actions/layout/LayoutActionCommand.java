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
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionCommand;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;

import java.util.List;

import org.jdom.Element;

/**
 * Base class which all layout related ODOMActionCommands should extend. This
 * class provides some utility methods for manipulating ODOM. These methods
 * should be used in preference to direct manipulation as they ensure that only
 * one selection event is fired irrespective of how many modifications are
 * made to the ODOM.
 *
 */
public abstract class LayoutActionCommand implements ODOMActionCommand {


    /**
     * Selection manager that can be used to reset the selection to
     * a defined element.
     */
    protected ODOMSelectionManager selectionManager;

    /**
     * Constructor that takes a ODOMEditorContext. The ODOMSelectionManager
     * contained in the EditorContext is enabled/disabled to control the number
     * of ODOMSelectionEvents fired when classes derived from this one call the
     * replaceElement methods.
     *
     * @param context the editor context. Must not be null and must contain a
     * valid ODOMSelectionManager
     * @throws IllegalArgumentException if the selectionManager is null.
     */
    protected LayoutActionCommand(ODOMEditorContext context) {
        this(context.getODOMSelectionManager());
    }

    /**
     * Constructor taking an ODOMSelectionManager that will be enabled/disabled
     * as necessary to restrict the SelectionEvents fired by classes derived
     * from this class.
     *
     * @param selectionManager the selectionManager to use.
     * @throws IllegalArgumentException if the selectionManager is null.
     */
    protected LayoutActionCommand(ODOMSelectionManager selectionManager) {
        if (selectionManager == null) {
            throw new IllegalArgumentException(
                    "selectionManager must not be null");
        }
        this.selectionManager = selectionManager;
    }

    /**
     * Convienience method that allows replaceElement to fire a single event
     * when the replacement element is added or to not fire any selection event
     * at all.
     * @param element the element to replace
     * @param replacement the replacement element
     * @param fireSelectionEvent if true a single selection event will be
     * fired. If false no such event will be fired.
     */
    protected void replaceElement(Element element, Element replacement,
                                  boolean fireSelectionEvent){
        selectionManager.setEnabled(fireSelectionEvent);
        replaceElement(element, replacement);
    }

    /**
     * Replace the specified element with the replacement element.
     * <code>element</code> must have a valid parent. This method disables
     * the ODOMSelectionManager while it removes the old element. Then it
     * returns the Manager to its original state (either enabled or disabled)
     * it so that selectionEvents based on the add can/cannot be fired.
     * The manager is always enabled when this method exits.
     *
     * @param element the element to replace
     * @param replacement the replacement element
     */
    protected void replaceElement(Element element, Element replacement) {
        try {
            Element parent = element.getParent();
            if (parent == null) {
                throw new IllegalArgumentException(
                        "The element \"" + element.getName() + //$NON-NLS-1$
                        " must have a non-null parent"); //$NON-NLS-1$
            }
            List content = parent.getContent();

            // rather then use replace we remove the old element then add the
            // new element. This allows us to disable the ODOMSelectionManager
            // so it does not fire a selection event on remove.
            // @todo depends if LayoutDesigner is redesigned this may no longer
            // @todo be necessary. (may be able to use
            // @todo content.set(content.indexOf(element),replacement))
            int index = content.indexOf(element);

            boolean enabledState = selectionManager.isEnabled();
            selectionManager.setEnabled(false);
            content.remove(index);
            selectionManager.setEnabled(enabledState);
            content.add(index, replacement);
            

        } finally {
            // ensure the manger is re-enabled.
            selectionManager.setEnabled(true);
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

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 ===========================================================================
*/
