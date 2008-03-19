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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.common.ClassVersionProperties;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * Creates actions for use by various GUI components.
 */
public abstract class ActionFactory {
    private static final ActionFactory DEFAULT_INSTANCE = (ActionFactory)
            ClassVersionProperties.getInstance("ActionFactory.class");

    /**
     * @return the default instance of the factory.
     */
    public static ActionFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Returns a select all action for the specified table viewer.
     *
     * @param viewer The table viewer whose contents the action should select
     * @return a select all action for the specified table viewer
     */
    public abstract Action createSelectAllAction(TableViewer viewer);

    /**
     * Returns a lock action for the variant currently selected within the
     * specified editor context.
     *
     * <p>If the action factory does not support locking of variants then null
     * should be returned, and the relevant UI components will not be created.</p>
     *
     * @param context The editor context in which the variant will be locked
     * @param parentShell The parent shell for the editor
     * @return a lock action for the currently selected variant, or null if
     *         locking is not supported
     */
    public abstract Action createLockVariantAction(EditorContext context, final Shell parentShell);

    /**
     * Returns an unlock action for the variant currently selected within the
     * specified editor context.
     *
     * <p>If the action factory does not support locking of variants then null
     * should be returned, and the relevant UI components will not be created.</p>
     *
     * @param context The editor context in which the variant will be unlocked
     * @param parentShell The parent shell for the editor
     * @return an unlock action for the currently selected variant, or null if
     *         locking is not supported
     */
    public abstract Action createUnlockVariantAction(EditorContext context, Shell parentShell);

    /**
     * Returns a delete action for the variant currently selected within the
     * specified editor context.
     *
     * @param context The editor context in which the variant will be deleted
     * @param parentShell The parent shell for the editor
     * @return a delete action for the currently selected variant
     */
    public abstract Action createDeleteVariantAction(EditorContext context, Viewer viewer, Shell parentShell);

    /**
     * Returns a copy action for the variant currently selected within the
     * specified editor context.
     *
     * @param context The editor context in which the variant will be copied
     * @param parentShell The parent shell for the editor
     * @return a copy action for the currently selected variant
     */
    public abstract Action createCopyVariantAction(EditorContext context, Widget widget, Viewer viewer);

    /**
     * Returns a cut action for the variant currently selected within the
     * specified editor context.
     *
     * @param context The editor context in which the variant will be cut
     * @param parentShell The parent shell for the editor
     * @return a cut action for the currently selected variant
     */
    public abstract Action createCutVariantAction(EditorContext context, Action copyAction, Action deleteAction);

    /**
     * Returns a paste action for the specified editor context.
     *
     * @param context The editor context in which the variant will be pasted
     * @param parentShell The parent shell for the editor
     * @return a paste action
     */
    public abstract Action createPasteVariantAction(EditorContext context, Widget widget);

    /**
     * Returns an add variant action for the specified editor context.
     *
     * <p>The add action provides a wizard for user-friendly addition of new
     * variants.</p>
     *
     * @param context The editor context in which the variant will be added
     * @param parentShell The parent shell for the editor
     * @return an add action
     */
    public abstract Action createAddVariantAction(EditorContext context);

    /**
     * Returns a new variant action for the specified editor context.
     *
     * <p>The new action quickly creates a new variant with default values.</p>
     *
     * @param context The editor context in which the variant will be added
     * @param parentShell The parent shell for the editor
     * @return a new action for the currently selected variant
     */
    public abstract Action createNewVariantAction(EditorContext context, Control control);

    /**
     * Returns a lock action for the policy represented by the specified
     * editor context.
     *
     * <p>If the action factory does not support locking of policies then null
     * should be returned, and the relevant UI components will not be created.</p>
     *
     * @param context The editor context in which the policy will be locked
     * @param parentShell The parent shell for the editor
     * @return a lock action for the specified policy, or null if locking is not
     *         supported
     */
    public abstract Action createLockPolicyAction(EditorContext context, Shell parentShell);

    /**
     * Returns an unlock action for the policy represented by the specified 
     * editor context.
     *
     * <p>If the action factory does not support locking of policies then null
     * should be returned, and the relevant UI components will not be created.</p>
     *
     * @param context The editor context in which the policy will be locked
     * @param parentShell The parent shell for the editor
     * @return an unlock action for the specified policy, or null if locking is
     *         not supported
     */
    public abstract Action createUnlockPolicyAction(EditorContext context, Shell parentShell);
}
