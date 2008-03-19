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

import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseListener;

/**
 * Provide and interface definition for an Actionable control. <p>
 *
 * An actionable control is one that has global actions associated with it (such
 * as cut,copy and paste).
 */
public interface Actionable {
    /**
     * The actionable key used to obtain the Actionable control from the widget's
     * data object. Every actionable control must call: <pre>
     *  <code>control.setData(ACTIONABL_DATA_KEY, this)</code></pre>
     */
    public static final String ACTIONABLE_DATA_KEY = "actionable.object";

    /**
     * The cut clipboard action.
     */
    public void cut();

    /**
     * The copy clipboard action.
     */
    public void copy();

    /**
     * The paste clipboard action.
     */
    public void paste();

    /**
     * The select all clipboard action.
     */
    public void selectAll();

    /**
     * The delete all clipboard action.
     */
    public void delete();

    /**
     * @see org.eclipse.swt.widgets.Control#isDisposed
     */
    public boolean isDisposed();

    /**
     * Clear the current selection of the actionable control.
     */
    public void clearSelection();

    /**
     * Return true if the action identified by the specified key is allowed,
     * false otherwise.
     *
     * @param key the {@link org.eclipse.ui.internal.IWorkbenchConstants} key.
     * @return true if the action identified by the specified key is allowed,
     *         false otherwise.
     */
    public boolean isEnabled(String key);

    /**
     * Add a listener to the the actionable control.
     *
     * @param eventType the type of event to listen for
     * @param listener  the listener which should be notified when the event
     *                  occurs
     * @see org.eclipse.swt.widgets.Widget#addListener
     */
    public void addListener(int eventType, Listener listener);

    /**
     * Remove a listener to the the actionable control.
     *
     * @param eventType the type of event to remove.
     * @param listener  the listener which should be removed.
     * @see org.eclipse.swt.widgets.Control#removeListener
     */
    public void removeListener(int eventType, Listener listener);

    /**
     * Add a mouse listener to the the actionable control.
     *
     * @param listener the {@link MouseListener} which should be notified.
     * @see org.eclipse.swt.widgets.Control#addMouseListener
     */
    public void addMouseListener(MouseListener listener);

    /**
     * Remove a mouse listener to the the actionable control.
     *
     * @param mouseAdapter the {@link MouseAdapter} which should be notified.
     * @see org.eclipse.swt.widgets.Control#removeMouseListener
     */
    public void removeMouseListener(MouseAdapter mouseAdapter);

    /**
     * Add a key listener to the actionable control.
     *
     * @param listener the <code>KeyListener</code> to add.
     * @see org.eclipse.swt.widgets.Control#addKeyListener
     */
    public void addKeyListener(KeyListener listener);

    /**
     * Remove a key listener from the actionable control.
     *
     * @param keyAdapter the <code>KeyAdapter</code> to remove.
     * @see org.eclipse.swt.widgets.Control#removeKeyListener
     */
    public void removeKeyListener(KeyAdapter keyAdapter);
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
