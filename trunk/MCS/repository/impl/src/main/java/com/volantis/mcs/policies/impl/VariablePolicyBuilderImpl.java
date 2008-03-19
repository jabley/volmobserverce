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
import com.volantis.mcs.model.path.IndexedStep;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PropertyStep;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.InternalVariablePolicyBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilderVisitor;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.InternalVariantBuilder;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VariablePolicyBuilderImpl
        extends AbstractConcretePolicyBuilder
        implements VariablePolicyBuilder, InternalVariablePolicyBuilder {

    /**
     * A map from {@link VariablePolicyType} to a {@link Set} of allowable
     * {@link VariantType}s.
     */
    private static final Map variablePolicyType2AllowableVariants;

    /**
     * A map from {@link VariablePolicyType} to a {@link Set} of allowable
     * {@link PolicyType}s.
     */
    private static final Map variablePolicyType2AllowableAlternates;

    static {
        Map map = new HashMap();
        Set set;

        // Audio.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.AUDIO);
        map.put(PolicyType.AUDIO, set);

        // Chart.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.CHART);
        map.put(PolicyType.CHART, set);

        // Image.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.IMAGE);
        map.put(PolicyType.IMAGE, set);

        // Layout.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.LAYOUT);
        map.put(PolicyType.LAYOUT, set);

        // Link.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.LINK);
        map.put(PolicyType.LINK, set);

        // Resource.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.AUDIO);
        set.add(VariantType.IMAGE);
        set.add(VariantType.LINK);
        set.add(VariantType.TEXT);
        set.add(VariantType.VIDEO);
        map.put(PolicyType.RESOURCE, set);

        // Script.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.SCRIPT);
        map.put(PolicyType.SCRIPT, set);

        // Text.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.TEXT);
        map.put(PolicyType.TEXT, set);

        // Theme.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.THEME);
        map.put(PolicyType.THEME, set);

        // Video.
        set = new HashSet();
        set.add(VariantType.NULL);
        set.add(VariantType.VIDEO);
        map.put(PolicyType.VIDEO, set);

        variablePolicyType2AllowableVariants = map;

        // Setup the allowable Alternates

        map = new HashMap();

        // Audio.
        set = new HashSet();
        set.add(PolicyType.AUDIO);
        set.add(PolicyType.TEXT);
        map.put(PolicyType.AUDIO, set);

        // Chart.
        set = new HashSet();
        set.add(PolicyType.CHART);
        set.add(PolicyType.IMAGE);
        set.add(PolicyType.TEXT);
        map.put(PolicyType.CHART, set);

        // Image.
        set = new HashSet();
        set.add(PolicyType.IMAGE);
        set.add(PolicyType.TEXT);
        map.put(PolicyType.IMAGE, set);

        // Link.
        set = new HashSet();
        set.add(PolicyType.LINK);
        set.add(PolicyType.TEXT);
        map.put(PolicyType.LINK, set);


        // Script.
        set = new HashSet();
        set.add(PolicyType.SCRIPT);
        map.put(PolicyType.SCRIPT, set);

        // Text.
        set = new HashSet();
        set.add(PolicyType.TEXT);
        map.put(PolicyType.TEXT, set);

        // Video (Dynamic Visual).
        set = new HashSet();
        set.add(PolicyType.VIDEO);
        set.add(PolicyType.IMAGE);
        set.add(PolicyType.TEXT);
        map.put(PolicyType.VIDEO, set);

        variablePolicyType2AllowableAlternates = map;
    }

    private VariablePolicy variablePolicy;

    private VariablePolicyType variablePolicyType;

    /**
     * 
     */
    private String categorizationScheme;

    private List variantBuilders;

    private transient List externalVariantBuilders;

    public VariablePolicyBuilderImpl() {
        this((VariablePolicyType) null);
    }

    public VariablePolicyBuilderImpl(VariablePolicyType type) {
        super(null);

        variablePolicyType = type;
        variantBuilders = new ArrayList();
        externalVariantBuilders = createExternalList(variantBuilders);
    }

    public VariablePolicyBuilderImpl(VariablePolicy variablePolicy) {
        super(variablePolicy);

        if (variablePolicy != null) {
            this.variablePolicy = variablePolicy;
            variablePolicyType = variablePolicy.getVariablePolicyType();
            categorizationScheme = variablePolicy.getCategorizationScheme();
            List variants = variablePolicy.getVariants();
            variantBuilders = new ArrayList();
            for (Iterator i = variants.iterator(); i.hasNext();) {
                Variant variant = (Variant) i.next();
                variantBuilders.add(variant.getVariantBuilder());
            }
            externalVariantBuilders = createExternalList(variantBuilders);
        }
    }

    public Policy getPolicy() {
        return getVariablePolicy();
    }

    public VariablePolicy getVariablePolicy() {
        if (variablePolicy == null) {
            // Make sure only valid instances are built.
            validate();
            variablePolicy = new VariablePolicyImpl(this);
        }

        return variablePolicy;
    }

    protected void clearBuiltObject() {
        variablePolicy = null;
    }

    // Javadoc inherited.
    public void accept(PolicyBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public VariablePolicyType getVariablePolicyType() {
        return variablePolicyType;
    }

    public void setVariablePolicyType(VariablePolicyType variablePolicyType) {
        if (variablePolicyType == null) {
            throw new IllegalArgumentException("variablePolicyType cannot be null");
        }

        if (!equals(this.variablePolicy, variablePolicy)) {
            stateChanged();
        }

        this.variablePolicyType = variablePolicyType;
    }

    // Javadoc inherited.
    public void addVariantBuilder(VariantBuilder variantBuilder) {
        externalVariantBuilders.add(variantBuilder);
    }

    public List getVariantBuilders() {
        return externalVariantBuilders;
    }

    public Iterator variantBuilderIterator() {
        return externalVariantBuilders.iterator();
    }

    public String getCategorizationScheme() {
        return categorizationScheme;
    }

    public void setCategorizationScheme(String categorizationScheme) {
        if (!equals(this.categorizationScheme, categorizationScheme)) {
            stateChanged();
        }

        this.categorizationScheme = categorizationScheme;
    }

    public void validate(ValidationContext context) {
        validateAlternates(context);
        validateVariants(context);
    }

    private void validateVariants(ValidationContext context) {
        if (variantBuilders == null || variantBuilders.isEmpty()) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.WARNING,
                    context.createMessage(PolicyMessages.NO_VARIANTS));
        } else {
            Set allowableVariants = (Set) variablePolicyType2AllowableVariants
                    .get(getVariablePolicyType());

            context.beginDefinitionScope(PolicyModel.DEVICE_TARGETED);
            context.beginDefinitionScope(PolicyModel.CATEGORY_TARGETED);
            context.beginDefinitionScope(PolicyModel.DEFAULT_SELECTION);

            Step variantsStep = context.pushPropertyStep(PolicyModel.VARIANTS);

            // Iterate over the variants.
            for (int i = 0; i < variantBuilders.size(); i++) {
                InternalVariantBuilder variantBuilder =
                        (InternalVariantBuilder) variantBuilders.get(i);

                Step step = context.pushIndexedStep(i);

                // Make sure that the variant is allowed within this policy
                // type.
                VariantType variantType = variantBuilder.getVariantType();
                if (variantType == null) {
                    context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                            context.createMessage(PolicyMessages.UNSPECIFIED,
                                    PolicyModel.VARIANT_TYPE.getDescription()));
                } else if (allowableVariants.contains(variantType)) {
                    variantBuilder.validate(context);
                } else {
                    throw new IllegalStateException(
                            "Todo, implement validation, or allowable " +
                            "variant types and alternates");
                }

                context.popStep(step);

            }

            context.popStep(variantsStep);

            context.endDefinitionScope(PolicyModel.DEFAULT_SELECTION);
            context.endDefinitionScope(PolicyModel.CATEGORY_TARGETED);
            context.endDefinitionScope(PolicyModel.DEVICE_TARGETED);
        }
    }

    // Javadoc inherited.
    public void validateAndPrune() {
        StrictValidator validator = ModelFactory.getDefaultInstance().
                createStrictPruningValidator();
        validator.validate(this);
    }

    // Javadoc inherited.
    public void prune(ValidationContext context, List diagnostics) {

        // Reconstruct the set of variants which had errors from the path.
        // Yuck.
        Set variantIndexSet = new HashSet();
        for (int i = 0; i < diagnostics.size(); i++) {
            Diagnostic diagnostic = (Diagnostic) diagnostics.get(i);

            // Search for paths which start with a variant (i.e.
            // "/variants/n/...") and collect the set of unique variant ids
            // within them.
            Path path = diagnostic.getPath();
            if (path.getStepCount() >= 2) {
                Step step0 = path.getStep(0);
                if (step0 instanceof PropertyStep) {
                    PropertyStep pstep = (PropertyStep) step0;
                    if (pstep.getProperty().equals(
                            PolicyModel.VARIANTS.getName())) {
                        Step step1 = path.getStep(1);
                        if (step1 instanceof IndexedStep) {
                            // We have found a path of the form /variant/n/...

                            // So save the variant's index in the set.
                            IndexedStep istep = (IndexedStep) step1;
                            variantIndexSet.add(new Integer(istep.getIndex()));

                            // And mark it as pruned to prevent it causing the
                            // validation to fail.
                            diagnostic.markAsPruned();
                        }
                    }
                }
            }
        }

        Step variantsStep = context.pushPropertyStep(PolicyModel.VARIANTS);

        // Iterate though the set of unique variant id's which had errors,
        // creating an warning for each and removing the variant.
        for (Iterator iterator = variantIndexSet.iterator();
                iterator.hasNext();) {
            Integer variantIndex = (Integer) iterator.next();

            Step step = context.pushIndexedStep(variantIndex.intValue());

            // report we are removing this variant
            context.addDiagnostic(sourceLocation,
                    DiagnosticLevel.WARNING,
                    context.createMessage(
                            PolicyMessages.VARIANT_INVALID_IGNORED,
                            new Object[] {
                                }));

            // remove the variant
            variantBuilders.remove(variantIndex.intValue());

            context.popStep(step);

        }

        context.popStep(variantsStep);

    }

    public PolicyType getPolicyType() {
        return getVariablePolicyType();
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof VariablePolicyBuilderImpl) ?
                equalsSpecific((VariablePolicyBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(VariablePolicyBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(variablePolicyType, other.variablePolicyType) &&
                equals(categorizationScheme, other.categorizationScheme) &&
                equals(externalVariantBuilders, other.externalVariantBuilders);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, variablePolicyType);
        result = hashCode(result, categorizationScheme);
        result = hashCode(result, externalVariantBuilders);
        return result;
    }

    public boolean equalsExcludingVariants(Object obj) {

        if (obj instanceof VariablePolicyBuilderImpl) {
            if (obj == this) {
                return true;
            }

            VariablePolicyBuilderImpl other = (VariablePolicyBuilderImpl) obj;
            return super.equalsSpecific(other) &&
                    equals(variablePolicyType, other.variablePolicyType) &&
                   equals(categorizationScheme, other.categorizationScheme);
        } else {
            return false;
        }
    }

    // Javadoc inherited.
    protected Set getAllowableAlternatives() {
        return (Set)variablePolicyType2AllowableAlternates.get(getPolicyType());
    }
}
