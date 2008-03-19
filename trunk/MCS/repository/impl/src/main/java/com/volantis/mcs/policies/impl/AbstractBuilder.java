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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.policies.impl;

import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.jibx.JiBXSourceLocation;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.InternalBuilder;
import com.volantis.mcs.policies.InternalBuilderContainer;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.shared.collection.ListNotificationListener;
import com.volantis.shared.collection.NotifyingList;

import java.util.List;

/**
 * Base class for all the builders.
 */
public abstract class AbstractBuilder
        extends EqualsHashCodeBase
        implements Validatable, ListNotificationListener,
        InternalBuilderContainer {

    /**
     * The location of this object within the source document.
     */
    protected final SourceLocation sourceLocation =
            new JiBXSourceLocation(this);

    private InternalBuilderContainer container;

    protected abstract Object getBuiltObject();

    protected abstract void clearBuiltObject();

    protected void stateChanged() {
        clearBuiltObject();

        // Propogate the state change event to any container.
        if (container != null) {
            container.nestedStateChanged();
        }
    }

    public void nestedStateChanged() {
        stateChanged();
    }

    protected void changedNestedBuilder(InternalBuilder oldBuilder,
                                        InternalBuilder newBuilder) {

        if (oldBuilder != null) {
            oldBuilder.setContainer(null);
        }

        if (newBuilder != null) {
            newBuilder.setContainer(this);
        }

        stateChanged();
    }

    public void setContainer(InternalBuilderContainer container) {
        this.container = container;
    }


    protected void validate() {
        StrictValidator validator = ModelFactory.getDefaultInstance()
                .createStrictValidator();
        validator.validate(this);
    }

    protected ListNotificationListener getListNotificationListener() {
        return this;
    }

    public void addingObject(NotifyingList list, Object object) {

        attachPotentialBuilder(object);

        stateChanged();
    }

    private void attachPotentialBuilder(Object object) {
        // If the object being added is an abstract builder then make sure
        // that any changes in it are propagated up to this builder.
        if (object instanceof InternalBuilder) {
            InternalBuilder nested = (InternalBuilder) object;
            nested.setContainer(this);
        }
    }

    public void removingObject(NotifyingList list, Object object) {

        detachPotentialBuilder(object);

        stateChanged();
    }

    private void detachPotentialBuilder(Object object) {
        // If the object being removed is an abstract builder then stop it
        // from propagating any changes up to this builder.
        if (object instanceof AbstractBuilder) {
            InternalBuilder nested = (InternalBuilder) object;
            nested.setContainer(null);
        }
    }

    public void replacingObject(
            NotifyingList list, Object oldObject, Object newObject) {

        detachPotentialBuilder(oldObject);
        attachPotentialBuilder(newObject);

        stateChanged();
    }

    protected NotifyingList createExternalList(final List backing) {
        return new NotifyingList(getListNotificationListener(), backing);
    }

    protected void checkReference(
            ValidationContext context, final PolicyReference reference,
            final PolicyType requiredPolicyType,
            boolean required) {

        PolicyType expectedPolicyType;
        if (reference == null) {
            if (required) {
                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.POLICY_REFERENCE_UNSPECIFIED));
            }
        } else {
            expectedPolicyType = reference.getExpectedPolicyType();
            if (expectedPolicyType != requiredPolicyType) {
                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.POLICY_REFERENCE_TYPE_MISMATCH,
                                new Object[]{
                                    requiredPolicyType,
                                    expectedPolicyType
                                }));
            }
        }
    }
}
