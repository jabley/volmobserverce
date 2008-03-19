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
package com.volantis.mcs.eclipse.builder.views.navigator;

import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Shell;

/**
 * A selection listener action that delegates to another underlying selection
 * listener action. By default, passes all relevant methods on to the
 * underlying action, but individual methods can be overridden where relevant.
 */
public abstract class DelegatingSelectionListenerAction
        extends SelectionListenerAction {
    /**
     * The underlying selection listener action.
     */
    private SelectionListenerAction underlyingAction;

    /**
     * Creates a new action with the given text.
     *
     * @param text the string used as the text for the action,
     *             or <code>null</code> if there is no text
     */
    public DelegatingSelectionListenerAction(Shell shell, String text) {
        super(text);
        underlyingAction = createUnderlyingAction(shell);
    }

    // Javadoc not required
    protected SelectionListenerAction getUnderlyingAction() {
        return underlyingAction;
    }

    /**
     * Creates (or retrieves) the underlying action for this delegating action.
     */
    protected abstract SelectionListenerAction createUnderlyingAction(Shell shell);

    // Javadoc inherited
    public int getAccelerator() {
        return (underlyingAction == null) ? 0 : underlyingAction.getAccelerator();
    }

    // Javadoc inherited
    public String getActionDefinitionId() {
        return (underlyingAction == null) ? null : underlyingAction.getActionDefinitionId();
    }

    // Javadoc inherited
    public String getDescription() {
        return (underlyingAction == null) ? null : underlyingAction.getDescription();
    }

    // Javadoc inherited
    public ImageDescriptor getDisabledImageDescriptor() {
        return (underlyingAction == null) ? null : underlyingAction.getDisabledImageDescriptor();
    }

    // Javadoc inherited
    public HelpListener getHelpListener() {
        return (underlyingAction == null) ? null : underlyingAction.getHelpListener();
    }

    // Javadoc inherited
    public ImageDescriptor getHoverImageDescriptor() {
        return (underlyingAction == null) ? null : underlyingAction.getHoverImageDescriptor();
    }

    // Javadoc inherited
    public String getId() {
        return (underlyingAction == null) ? null : underlyingAction.getId();
    }

    // Javadoc inherited
    public ImageDescriptor getImageDescriptor() {
        return (underlyingAction == null) ? null : underlyingAction.getImageDescriptor();
    }

    // Javadoc inherited
    public IMenuCreator getMenuCreator() {
        return (underlyingAction == null) ? null : underlyingAction.getMenuCreator();
    }

    // Javadoc inherited
    public int getStyle() {
        return (underlyingAction == null) ? 0 : underlyingAction.getStyle();
    }

    // Javadoc inherited
    public String getText() {
        return (underlyingAction == null) ? null : underlyingAction.getText();
    }

    // Javadoc inherited
    public String getToolTipText() {
        return (underlyingAction == null) ? null : underlyingAction.getToolTipText();
    }

    // Javadoc inherited
    public boolean isChecked() {
        return (underlyingAction == null) ? false : underlyingAction.isChecked();
    }

    // Javadoc inherited
    public void setActionDefinitionId(String id) {
        if (underlyingAction != null) {
            underlyingAction.setActionDefinitionId(id);
        }
    }

    // Javadoc inherited
    public void setChecked(boolean checked) {
        if (underlyingAction != null) {
            underlyingAction.setChecked(checked);
        }
    }

    // Javadoc inherited
    public void setDescription(String text) {
        if (underlyingAction != null) {
            underlyingAction.setDescription(text);
        }
    }

    // Javadoc inherited
    public void setDisabledImageDescriptor(ImageDescriptor newImage) {
        if (underlyingAction != null) {
            underlyingAction.setDisabledImageDescriptor(newImage);
        }
    }

    // Javadoc inherited
    public void setHelpListener(HelpListener listener) {
        if (underlyingAction != null) {
            underlyingAction.setHelpListener(listener);
        }
    }

    // Javadoc inherited
    public void setHoverImageDescriptor(ImageDescriptor newImage) {
        if (underlyingAction != null) {
            underlyingAction.setHoverImageDescriptor(newImage);
        }
    }

    // Javadoc inherited
    public void setId(String id) {
        if (underlyingAction != null) {
            underlyingAction.setId(id);
        }
    }

    // Javadoc inherited
    public void setImageDescriptor(ImageDescriptor newImage) {
        if (underlyingAction != null) {
            underlyingAction.setImageDescriptor(newImage);
        }
    }

    // Javadoc inherited
    public void setMenuCreator(IMenuCreator creator) {
        if (underlyingAction != null) {
            underlyingAction.setMenuCreator(creator);
        }
    }

    // Javadoc inherited
    public void setText(String text) {
        if (underlyingAction != null) {
            underlyingAction.setText(text);
        }
    }

    // Javadoc inherited
    public void setToolTipText(String toolTipText) {
        if (underlyingAction != null) {
            underlyingAction.setToolTipText(toolTipText);
        }
    }

    // Javadoc inherited
    public void setAccelerator(int keycode) {
        if (underlyingAction != null) {
            underlyingAction.setAccelerator(keycode);
        }
    }

    // Javadoc inherited
    public void run() {
        // By default, delegate to the underlying action, using the current
        // selection
        SelectionListenerAction underlyingAction = getUnderlyingAction();
        underlyingAction.selectionChanged(getStructuredSelection());
        underlyingAction.run();
    }
}
