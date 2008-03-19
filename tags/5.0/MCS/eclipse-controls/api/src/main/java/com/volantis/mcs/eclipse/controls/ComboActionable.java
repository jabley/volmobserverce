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

package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * A combo specific implementation of the actionable interface.<p>
 *
 * This implementation needs to handle editable and non-editable combo box
 * actionable actions.<p>
 *
 * Most invocations are delegated to the wrapped combo control.
 */
public class ComboActionable implements Actionable {

    /**
     * The wrapped combo control to delegate the actionable method invocations to.
     */
    private Combo combo;

    /**
     * Reduce the amount of garbage by caching the selection point.
     */
    private Point selection;

    /**
     * Construct this object with a combo control which may not be null.
     * @param combo the combo control which may not be null.
     * @throws IllegalArgumentException if the combo argument is null.
     */
    public ComboActionable(Combo combo) {
        if (combo == null) {
            throw new IllegalArgumentException("Combo argument may not be null.");
        }
        this.combo = combo;
        this.combo.setData(ACTIONABLE_DATA_KEY, this);
    }

    // javadoc inherited
    public void addKeyListener(KeyListener listener) {
        combo.addKeyListener(listener);
    }

    // javadoc inherited
    public void addListener(int eventType, Listener listener) {
        combo.addListener(eventType, listener);
    }

    // javadoc inherited
    public void addMouseListener(MouseListener listener) {
        combo.addMouseListener(listener);
    }

    // javadoc inherited
    public void clearSelection() {
        combo.clearSelection();
    }

    // javadoc inherited
    public void copy() {
        combo.copy();
    }

    // javadoc inherited
    public void cut() {
        combo.cut();
    }
    // javadoc inherited

    public void delete() {
        combo.clearSelection();
    }

    // javadoc inherited
    public void paste() {
        // For read only combos, paste may be clever enough to select the item
        // in the list that matches the clipboard's text. However, we assume
        // the eclipse's paste() implementation will handle this case.
        combo.paste();
    }

    // javadoc inherited
    public boolean isDisposed() {
        return combo.isDisposed();
    }

    // javadoc inherited
    public boolean isEnabled(String key) {
        // The combo box delete, paste, select all and cut is not really
        // meaningful if the combo is read only.
        boolean result = false;
        if (IWorkbenchActionConstants.DELETE.equals(key) ||
              IWorkbenchActionConstants.PASTE.equals(key)) {
            // If the combo is not read only then permit above actions.
            result = (combo.getStyle() & SWT.READ_ONLY) != 0;
        } else if (IWorkbenchActionConstants.COPY.equals(key) ||
                IWorkbenchActionConstants.CUT.equals(key) ||
                IWorkbenchActionConstants.SELECT_ALL.equals(key)) {
            // It is possible to copy the text a read only and editable combo.
            result = combo.getSelection().x != combo.getSelection().y;
        }
        return result;
    }

    // javadoc inherited
    public void removeKeyListener(KeyAdapter listener) {
        combo.removeKeyListener(listener);
    }

    // javadoc inherited
    public void removeListener(int eventType, Listener listener) {
        combo.removeListener(eventType, listener);
    }

    // javadoc inherited
    public void removeMouseListener(MouseAdapter mouseAdapter) {
        combo.removeMouseListener(mouseAdapter);
    }

    // javadoc inherited
    public void selectAll() {
        int length = combo.getText().length();
        // If the current text is different to the cached version then we have
        // to create a new point with the new length.
        if ((selection == null) || (length != selection.y)) {
            selection = new Point(0, length);
        }
        combo.setSelection(selection);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 ===========================================================================
*/
