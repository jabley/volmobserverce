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
package com.volantis.mcs.eclipse.builder.editors.impl;


import com.volantis.mcs.eclipse.builder.common.VariantTransfer;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.ActionFactory;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.wizards.variants.NewVariantWizard;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.theme.ThemeContentBuilder;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * Default implementation of action factory.
 *
 * <p>Creates most actions differently based on whether the project is
 * collaborative.</p>
 */
public class DefaultActionFactory extends ActionFactory {
    private static final String RESOURCE_PREFIX = "ActionFactory.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(DefaultActionFactory.class);

    // Javadoc inherited
    public Action createLockVariantAction(final EditorContext context,
                                          final Shell parentShell) {
        return null;
    }

    // Javadoc inherited
    public Action createUnlockVariantAction(final EditorContext context,
                                            final Shell parentShell) {
        return null;
    }

    // Javadoc inherited
    public Action createDeleteVariantAction(final EditorContext context,
                                            final Viewer viewer,
                                            Shell parentShell) {
        Action deleteAction = new Action() {
            public void run() {
                IStructuredSelection selection =
                        (IStructuredSelection) viewer.getSelection();

                Object[] selectedObjects = selection.toArray();
                BeanProxy theme = (BeanProxy) context.getInteractionModel();
                ListProxy variants = (ListProxy)
                        theme.getPropertyProxy(PolicyModel.VARIANTS);
                for (int i = 0; i < selectedObjects.length; i++) {
                    variants.removeItemProxy((Proxy) selectedObjects[i]);
                }
            }
        };
        deleteAction.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "delete.action"));

        return deleteAction;
    }

    // Javadoc inherited
    public Action createLockPolicyAction(final EditorContext context,
                                         final Shell parentShell) {
        return null;
    }

    // Javadoc inherited
    public Action createUnlockPolicyAction(final EditorContext context, final Shell parentShell) {
        return null;
    }

    private void updateReadWriteState(EditorContext context) {
        try {
            context.getPolicyFileAccessor().updatePolicyProxyState(context.getInteractionModel(), context.getProject());
        } catch (PolicyFileAccessException pfae) {

        }
    }
}
