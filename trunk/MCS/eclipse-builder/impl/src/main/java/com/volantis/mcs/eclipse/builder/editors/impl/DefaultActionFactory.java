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
    public Action createSelectAllAction(final TableViewer viewer) {
        Action selectAllAction = new Action() {
            public void run() {
                viewer.getTable().selectAll();
            }
        };
        selectAllAction.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "selectAll.action"));
        return selectAllAction;
    }

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
    public Action createCopyVariantAction(final EditorContext context,
                                          final Widget widget,
                                          final Viewer viewer) {
        Action copyAction = new Action() {
            public void run() {
                Clipboard clipboard = new Clipboard(widget.getDisplay());
                VariantTransfer transfer = VariantTransfer.getInstance();
                IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                Object[] selectionArray = selection.toArray();
                VariantBuilder[] modelArray = new VariantBuilder[selectionArray.length];
                for (int i = 0; i < selectionArray.length; i++) {
                    modelArray[i] = (VariantBuilder) ((Proxy) selectionArray[i]).getModelObject();
                }
                clipboard.setContents(new Object[]{modelArray},
                        new Transfer[]{transfer});
                clipboard.dispose();
            }
        };
        copyAction.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "copy.action"));

        return copyAction;
    }

    // Javadoc inherited
    public Action createCutVariantAction(final EditorContext context,
                                         final Action copyAction,
                                         final Action deleteAction) {
        if (copyAction == null) {
            throw new IllegalArgumentException("Copy action must be non-null");
        }

        if (deleteAction == null) {
            throw new IllegalArgumentException("Delete action must be non-null");
        }

        Action cutAction = new Action() {
            /**
             * Cut is essentially the same as a copy followed by a delete, so
             * we reuse the copy/delete functionality here.
             */
            public void run() {
                copyAction.run();
                deleteAction.run();
            }
        };
        cutAction.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "cut.action"));
        return cutAction;
    }

    // Javadoc inherited
    public Action createPasteVariantAction(final EditorContext context,
                                           final Widget widget) {
        Action pasteAction = new Action() {
            public void run() {
                VariantTransfer transfer = VariantTransfer.getInstance();
                Clipboard clipboard = new Clipboard(widget.getDisplay());

                try {
                    Object[] contents = (Object[]) clipboard.getContents(transfer);

                    if (contents != null && contents.length > 0) {
                        for (int i = 0; i < contents.length; i++) {
                            Object newAsset = contents[i];
                            if (newAsset instanceof VariantBuilder) {
                                BeanProxy theme = (BeanProxy) context.getInteractionModel();
                                ListProxy variants = (ListProxy)
                                        theme.getPropertyProxy(PolicyModel.VARIANTS);
                                // TODO better We should probably check that we have the right sort of variant (or at least a compatible one)
                                Operation addOperation = variants.prepareAddModelItemOperation(newAsset);
                                context.executeOperation(addOperation);
                            }
                        }
                    }
                } finally {
                    clipboard.dispose();
                }
            }
        };
        pasteAction.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "paste.action"));

        return pasteAction;
    }

    // Javadoc inherited
    public Action createAddVariantAction(final EditorContext context) {
        Action addAction = new Action() {
            public void run() {
                BeanProxy theme = (BeanProxy) context.getInteractionModel();
                ListProxy variants = (ListProxy)
                        theme.getPropertyProxy(PolicyModel.VARIANTS);

                final VariantType variantType =
                    ((PolicyEditorContext) context).getDefaultVariantType();
                VariantBuilder newVariant = PolicyFactory.getDefaultInstance().
                        createVariantBuilder(variantType);
                if (variantType == VariantType.THEME) {
                    final ThemeContentBuilder contentBuilder =
                        InternalPolicyFactory.getInternalInstance().
                            createThemeContentBuilder();
                    newVariant.setContentBuilder(contentBuilder);
                }
                Operation addOperation =
                        variants.prepareAddModelItemOperation(newVariant);
                context.executeOperation(addOperation);
            }
        };
        addAction.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "add.action"));

        return addAction;
    }

    // Javadoc inherited
    public Action createNewVariantAction(final EditorContext context, final Control control) {
        Action newAction = new Action() {
            public void run() {
                BeanProxy interactionModel = (BeanProxy) context.getInteractionModel();
                ListProxy variantsModel = (ListProxy) interactionModel.getPropertyProxy(PolicyModel.VARIANTS);
                VariantType type = ((PolicyEditorContext) context).getDefaultVariantType();
                NewVariantWizard wizard = NewVariantWizard.createNewVariantWizard(type, variantsModel, context);
                wizard.init(null, null);
                // Instantiates the wizard container with the wizard and opens it
                WizardDialog dialog = new WizardDialog(control.getShell(), wizard);
                dialog.create();
                dialog.open();
            }
        };
        newAction.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "new.action"));

        return newAction;
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
