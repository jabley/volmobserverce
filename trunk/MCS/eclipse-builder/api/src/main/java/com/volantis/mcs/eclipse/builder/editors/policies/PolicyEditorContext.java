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
package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.DiagnosticEvent;
import com.volantis.mcs.interaction.diagnostic.DiagnosticListener;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.synergetics.ObjectHelper;
import org.eclipse.core.resources.IFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Context class representing shared data between editors working on the same
 * policy.
 */
public class PolicyEditorContext extends EditorContext {
    /**
     * The currently selected policy variant.
     */
    private BeanProxy selectedVariant;

    /**
     * The variable policy type being edited.
     */
    private PolicyType policyType;

    /**
     * The default variant type for the variable policy type being edited.
     */
    private VariantType defaultVariantType;

    /**
     * A list of listeners for changes in the selected variant.
     */
    private List variantSelectionListeners = new ArrayList();

    /**
     * The possible category references for the policy.
     */
    private List categoryValues;

    // Javadoc inherited
    protected Class getModelType() {
        return ThemeFactory.getDefaultInstance().getRuleClass();
    }

    // Javadoc not required
    public void setSelectedVariant(BeanProxy newVariant) {
        BeanProxy oldVariant = selectedVariant;
        selectedVariant = newVariant;
        if (!ObjectHelper.equals(oldVariant, newVariant)) {
            fireSelectedVariantChanged(oldVariant, newVariant);
        }
    }

    // Javadoc not required
    public BeanProxy getSelectedVariant() {
        return selectedVariant;
    }

    // Javadoc not required
    public void addSelectedVariantListener(BuilderSelectionListener listener) {
        if (variantSelectionListeners.contains(listener)) {
            throw new IllegalStateException("Can't add same listener multiple times");
        }
        variantSelectionListeners.add(listener);
    }

    // Javadoc not required
    public void removeSelectedVariantListener(BuilderSelectionListener listener) {
        variantSelectionListeners.remove(listener);
    }

    // Javadoc not required
    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    // Javadoc not required
    public PolicyType getPolicyType() {
        return policyType;
    }

    // Javadoc not required
    public void setDefaultVariantType(VariantType defaultVariantType) {
        this.defaultVariantType = defaultVariantType;
    }

    // Javadoc not required
    public VariantType getDefaultVariantType() {
        return defaultVariantType;
    }

    /**
     * Notify listeners that the selected variant has changed.
     *
     * @param oldVariant The previously selected variant
     * @param newVariant The new selected variant
     */
    private void fireSelectedVariantChanged(BeanProxy oldVariant, BeanProxy newVariant) {
        if (!variantSelectionListeners.isEmpty()) {
            BuilderSelectionEvent event = new BuilderSelectionEvent(this, oldVariant, newVariant);
            Iterator it = variantSelectionListeners.iterator();
            while (it.hasNext()) {
                BuilderSelectionListener listener = (BuilderSelectionListener) it.next();
                listener.selectionMade(event);
            }
        }
    }

    // Javadoc inherited
    public synchronized void loadResource(IFile file) throws PolicyFileAccessException {
        // Only load if there is no model associated with the context, to
        // prevent multiple tabs of the same editor forcing multiple loads.
        if (getInteractionModel() == null) {
            setProject(file.getProject());
            PolicyFileAccessor policyFileAccessor = getPolicyFileAccessor();

            // Load the policy and grow the proxy
            PolicyBuilder policy = policyFileAccessor.loadPolicy(file);
            final Proxy proxy = policyFileAccessor.wrapPolicy(policy,
                    getModelDescriptor(), getProject());

            // Revalidate the model on all changes
            proxy.validate();
            proxy.addListener(
                new InteractionEventListenerAdapter() {
                    protected void interactionEvent(InteractionEvent event) {
                        if (event.isOriginator ()) {
                            proxy.validate();
                        }
                    }

                    // The read-only state changing does not require any
                    // re-validation.
                    public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                    }
                }, true
            );

            proxy.addDiagnosticListener(new DiagnosticListener() {
                public void diagnosticsChanged(DiagnosticEvent event) {
                    reportErrors();
                }
            });

            reportErrors();

            setResource(file);
            setInteractionModel(proxy);
        }
    }

    // Javadoc not required
    public void setCategoryValues(List categoryValues) {
        this.categoryValues = categoryValues;
    }

    // Javadoc not required
    public List getCategoryValues() {
        return categoryValues;
    }
}
