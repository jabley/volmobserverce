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

import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Listener;

/**
 * A text specific implementation of the actionable interface.<p>
 *
 * Most of the implemented methods are delegated to the wrapped text control.
 */
public class TextActionable implements Actionable {

    /**
     * The wrapped text control to delegate the actionable method invocations to.
     */
    protected Text text = null;

    /**
     * Construct this object with a text control which may not be null.
     * @param text the text control which may not be null.
     * @throws IllegalArgumentException if the text argument is null.
     */
    public TextActionable(Text text) {
        if (text == null) {
            throw new IllegalArgumentException("Text argument may not be null");
        }
        this.text = text;
        text.setData(ACTIONABLE_DATA_KEY, this);
    }

    // javadoc inherited
    public void clearSelection() {
        text.clearSelection();
    }

    // javadoc inherited
    public void copy() {
        text.copy();
    }

    // javadoc inherited
    public void cut() {
        text.cut();
    }

    // javadoc inherited
    public void paste() {
        text.paste();
    }

    // javadoc inherited
    public void delete() {
        clearSelection();
    }

    // javadoc inherited
    public void selectAll() {
        text.selectAll();
    }

    // javadoc inherited
    public boolean isEnabled(String key) {
        boolean result = false;
        if (IWorkbenchActionConstants.DELETE.equals(key)) {
            result = text.getSelectionCount() > 0 ||
                    text.getCaretPosition() < text.getCharCount();
        } else if (IWorkbenchActionConstants.COPY.equals(key) ||
                IWorkbenchActionConstants.CUT.equals(key)) {
            result = text.getSelectionCount() > 0;
        }
        return result;
    }

    // javadoc inherited
    public boolean isDisposed() {
        return text.isDisposed();
    }

    // javadoc inherited
    public void addKeyListener(KeyListener listener) {
        text.addKeyListener(listener);
    }

    // javadoc inherited
    public void removeKeyListener(KeyAdapter keyAdapter) {
        text.removeKeyListener(keyAdapter);
    }

    // javadoc inherited
    public void addListener(int eventType, Listener handler) {
        text.addListener(eventType, handler);
    }

    // javadoc inherited
    public void removeListener(int eventType, Listener listener) {
        text.removeListener(eventType, listener);
    }

    // javadoc inherited
    public void addMouseListener(MouseListener listener) {
        text.addMouseListener(listener);
    }

    // javadoc inherited
    public void removeMouseListener(MouseAdapter mouseAdapter) {
        text.removeMouseListener(mouseAdapter);
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
