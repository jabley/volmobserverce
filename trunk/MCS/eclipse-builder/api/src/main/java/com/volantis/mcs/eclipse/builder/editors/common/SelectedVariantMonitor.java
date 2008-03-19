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

import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.BeanPropertyChangedEvent;
import com.volantis.mcs.interaction.event.ProxyModelChangedEvent;
import com.volantis.mcs.interaction.event.InsertedIntoListEvent;
import com.volantis.mcs.interaction.event.RemovedFromListEvent;
import com.volantis.mcs.interaction.event.CompositeEvent;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;

/**
 */
public abstract class SelectedVariantMonitor implements InteractionEventListener, BuilderSelectionListener {
    private BeanProxy selectedVariant = null;

    private BuilderSelectionListener variantChangedListener =
            new BuilderSelectionListener() {
                public void selectionMade(BuilderSelectionEvent event) {
                    registerSelectedVariant((BeanProxy) event.getSelection());
                    SelectedVariantMonitor.this.selectionMade(event);
                }
            };

    public SelectedVariantMonitor(PolicyEditorContext context) {
        context.addSelectedVariantListener(variantChangedListener);
        registerSelectedVariant(context.getSelectedVariant());
    }

    /**
     * Default method called on all changes to the variant. The standard
     * implementation does nothing, but can be overridden.
     */
    public void variantOrSelectionChanged() {
    }

    // Javadoc inherited
    public void propertySet(BeanPropertyChangedEvent event) {
        variantOrSelectionChanged();
    }

    // Javadoc inherited
    public void proxyModelChanged(ProxyModelChangedEvent event) {
        variantOrSelectionChanged();
    }

    // Javadoc inherited
    public void addedToList(InsertedIntoListEvent event) {
        variantOrSelectionChanged();
    }

    // Javadoc inherited
    public void removedFromList(RemovedFromListEvent event) {
        variantOrSelectionChanged();
    }

    // Javadoc inherited
    public void compositeEvent(CompositeEvent event) {
        variantOrSelectionChanged();
    }

    // Javadoc inherited
    public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
        variantOrSelectionChanged();
    }

    // Javadoc inherited
    public void selectionMade(BuilderSelectionEvent event) {
        variantOrSelectionChanged();
    }

    /**
     * Registers the relevant event listeners for a newly selected variant.
     *
     * @param newSelection
     */
    private synchronized void registerSelectedVariant(BeanProxy newSelection) {
        if (selectedVariant != null) {
            selectedVariant.removeListener(this);
        }

        if (newSelection != null) {
            newSelection.addListener(this, true);
        }

        selectedVariant = newSelection;
    }
}
