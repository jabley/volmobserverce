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

package com.volantis.mcs.policies.impl.variants.selection;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.integrity.DefinitionScope;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.Selection;
import com.volantis.mcs.policies.variants.selection.SelectionType;
import com.volantis.mcs.policies.variants.selection.TargetedSelection;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.InternalSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilderVisitor;

import java.util.ArrayList;
import java.util.List;

public class TargetedSelectionBuilderImpl
        extends AbstractBuilder
        implements TargetedSelectionBuilder,
        InternalSelectionBuilder {

    private TargetedSelection targetedSelection;

    private List deviceReferences;

    private List categoryReferences;

    private transient List externalDeviceReferences;

    private transient List externalCategoryReferences;

    public TargetedSelectionBuilderImpl() {
        this(null);
    }

    public TargetedSelectionBuilderImpl(TargetedSelection targetedSelection) {
        if (targetedSelection == null) {
            deviceReferences = new ArrayList();
            categoryReferences = new ArrayList();
        } else {
            this.targetedSelection = targetedSelection;
            deviceReferences =
                new ArrayList(targetedSelection.getDeviceReferences());
            categoryReferences =
                new ArrayList(targetedSelection.getCategoryReferences());
        }
        externalDeviceReferences = createExternalList(deviceReferences);
        externalCategoryReferences = createExternalList(categoryReferences);
    }

    // javadoc inherited
    public Selection getSelection() {
        return getTargetedSelection();
    }

    // javadoc inherited
    public TargetedSelection getTargetedSelection() {
        if (targetedSelection == null) {
            // Make sure only valid instances are built.
            validate();
            targetedSelection = new TargetedSelectionImpl(this);
        }

        return targetedSelection;
    }

    // javadoc inherited
    protected Object getBuiltObject() {
        return getTargetedSelection();
    }

    // javadoc inherited
    protected void clearBuiltObject() {
        targetedSelection = null;
    }

    boolean jibxHasDevices() {
        return !nullOrEmpty(externalDeviceReferences);
    }

    boolean jibxHasCategories() {
        return !nullOrEmpty(externalCategoryReferences);
    }

    // javadoc inherited
    public void addDevice(String deviceName) {
        checkDeviceReferences();
        externalDeviceReferences.add(new DeviceReferenceImpl(deviceName));
    }

    // javadoc inherited
    public List getModifiableDeviceReferences() {
        checkDeviceReferences();
        return externalDeviceReferences;
    }

    /**
     * Checks if the deviceReferences field is null and if so, initializes it
     * with an empty list and sets the externalDeviceReferences list to use
     * this new list.
     *
     * @throws IllegalStateException if the externalDeviceReferences list
     * contains device names
     */
    private void checkDeviceReferences() {
        if (deviceReferences == null) {
            if (!nullOrEmpty(externalDeviceReferences)) {
                throw new IllegalStateException(
                    "External device references expected to be empty");
            }
            deviceReferences = new ArrayList();
            externalDeviceReferences = createExternalList(deviceReferences);
        }
    }

    // javadoc inherited
    public void addCategory(String categoryName) {
        checkCategoryReferences();
        externalCategoryReferences.add(new CategoryReferenceImpl(categoryName));
    }

    // javadoc inherited
    public List getModifiableCategoryReferences() {
        checkCategoryReferences();
        return externalCategoryReferences;
    }

    /**
     * Checks if the categoryReferences field is null and if so, initializes it
     * with an empty list and sets the externalCategoryReferences list to use
     * this new list.
     *
     * @throws IllegalStateException if the externalCategoryReferences list
     * contains category names
     */
    private void checkCategoryReferences() {
        if (categoryReferences == null) {
            if (!nullOrEmpty(externalCategoryReferences)) {
                throw new IllegalStateException(
                    "External category references expected to be empty");
            }
            categoryReferences = new ArrayList();
            externalCategoryReferences = createExternalList(categoryReferences);
        }
    }

    // javadoc inherited
    public void accept(SelectionBuilderVisitor visitor) {
        visitor.visit(this);
    }

    // javadoc inherited
    public void validate(ValidationContext context) {

        // At least one device or category must be targeted.
        if (nullOrEmpty(externalDeviceReferences) &&
                nullOrEmpty(externalCategoryReferences)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.WARNING,
                    context.createMessage(PolicyMessages.NO_TARGETS));
        }

        // TODO: add path for individual devices/categories here for better
        // error reporting.
        if (!nullOrEmpty(externalDeviceReferences)) {
            // Get the scope for the definition of device targets.
            final DefinitionScope scope =
                context.getDefinitionScope(PolicyModel.DEVICE_TARGETED);
            for (int i = 0; i < externalDeviceReferences.size(); i++) {
                DeviceReference reference = (DeviceReference)
                        externalDeviceReferences.get(i);
                String deviceName = reference.getDeviceName();
                if (scope != null) {
                    scope.define(context, sourceLocation, deviceName);
                }
            }
        }

        if (!nullOrEmpty(externalCategoryReferences)) {
            // Get the scope for the definition of category targets.
            final DefinitionScope scope =
                context.getDefinitionScope(PolicyModel.CATEGORY_TARGETED);
            for (int i = 0; i < externalCategoryReferences.size(); i++) {
                CategoryReference reference = (CategoryReference)
                        externalCategoryReferences.get(i);
                String categoryName = reference.getCategoryName();
                if (scope != null) {
                    scope.define(context, sourceLocation, categoryName);
                }
            }
        }
    }

    // javadoc inherited
    public SelectionType getSelectionType() {
        return SelectionType.TARGETED;
    }

    private boolean nullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof TargetedSelectionBuilderImpl) &&
            equalsSpecific((TargetedSelectionBuilderImpl) obj);
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(TargetedSelectionBuilderImpl other) {
        return super.equalsSpecific(other) &&
            equals(externalDeviceReferences, other.externalDeviceReferences) &&
            equals(externalCategoryReferences, other.externalCategoryReferences);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, externalDeviceReferences);
        result = hashCode(result, externalCategoryReferences);
        return result;
    }
}
