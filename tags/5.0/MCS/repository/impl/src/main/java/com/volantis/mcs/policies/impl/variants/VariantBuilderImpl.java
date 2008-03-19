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

package com.volantis.mcs.policies.impl.variants;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.InternalBuilder;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.impl.variants.validation.VariantValidatorSelector;
import com.volantis.mcs.policies.variants.InternalVariantBuilder;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.Selection;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;

public class VariantBuilderImpl
        extends AbstractBuilder
        implements InternalVariantBuilder {

    private final transient VariantValidatorSelector selector;

    private Variant variant;

    private VariantType variantType;

    private SelectionBuilder selectionBuilder;
    private MetaDataBuilder metaDataBuilder;
    private ContentBuilder contentBuilder;
    private String variantIdentifier;

    public VariantBuilderImpl() {
        this.selector = VariantValidatorSelector.SELECTOR;
    }

    public VariantBuilderImpl(Variant variant) {
        this();
        if (variant != null) {
            this.variant = variant;
            this.variantType = variant.getVariantType();
            Selection selection = variant.getSelection();
            if (selection == null) {
                selectionBuilder = null;
            } else {
                selectionBuilder = selection.getSelectionBuilder();
            }
            MetaData metaData = variant.getMetaData();
            if (metaData == null) {
                metaDataBuilder = null;
            } else {
                metaDataBuilder = metaData.getMetaDataBuilder();
            }
            Content content = variant.getContent();
            if (content == null) {
                contentBuilder = null;
            } else {
                contentBuilder = content.getContentBuilder();
            }
        }
    }

    public VariantBuilderImpl(VariantType variantType) {
        this();
        this.variantType = variantType;
    }

    public Variant getVariant() {
        if (variant == null) {
            // Make sure only valid instances are built.
            validate();
            variant = new VariantImpl(this);
        }

        return variant;
    }

    protected Object getBuiltObject() {
        return getVariant();
    }

    protected void clearBuiltObject() {
        variant = null;
    }

    public void setVariantType(VariantType variantType) {
        if (!equals(this.variantType, variantType)) {
            stateChanged();
        }

        this.variantType = variantType;
    }

    public VariantType getVariantType() {
        return variantType;
    }

    public SelectionBuilder getSelectionBuilder() {
        return selectionBuilder;
    }

    public void setSelectionBuilder(SelectionBuilder selectionBuilder) {
        if (!equals(this.selectionBuilder, selectionBuilder)) {
            changedNestedBuilder((InternalBuilder) this.selectionBuilder,
                    (InternalBuilder) selectionBuilder);
        }

        this.selectionBuilder = selectionBuilder;
    }

    public MetaDataBuilder getMetaDataBuilder() {
        return metaDataBuilder;
    }

    public void setMetaDataBuilder(MetaDataBuilder metaDataBuilder) {
        if (!equals(this.metaDataBuilder, metaDataBuilder)) {
            changedNestedBuilder((InternalBuilder) this.metaDataBuilder,
                    (InternalBuilder) metaDataBuilder);
        }

        this.metaDataBuilder = metaDataBuilder;
    }

    public ContentBuilder getContentBuilder() {
        return contentBuilder;
    }

    public void setContentBuilder(ContentBuilder contentBuilder) {
        if (!equals(this.contentBuilder, contentBuilder)) {
            changedNestedBuilder((InternalBuilder) this.contentBuilder,
                    (InternalBuilder) contentBuilder);
        }

        this.contentBuilder = contentBuilder;
    }

    public void validate(ValidationContext context) {

        // Cannot validate the specific parts unless a variants type has been
        // specified.
        if (variantType == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.UNSPECIFIED,
                            PolicyModel.VARIANT_TYPE.getDescription()));
        } else {
            VariantValidator validator = selector.selectValidator(variantType);

            validator.validate(context, sourceLocation, this);
        }
    }

    /**
     * Used by JiBX to determine if the selection is specified on this object.
     *
     * <p>It is required to support an optional 'selection' element around the
     * element representing the actual selection.</p>
     *
     * @return True if the selection is specified and false otherwise.
     */
    public boolean jibxHasSelection() {
        return selectionBuilder != null;
    }

    /**
     * Used by JiBX to determine if the meta data is specified on this object.
     *
     * <p>It is required to support an optional 'metaData' element around the
     * element representing the actual meta data.</p>
     *
     * @return True if the meta data is specified and false otherwise.
     */
    public boolean jibxHasMetaData() {
        return metaDataBuilder != null;
    }

    /**
     * Used by JiBX to determine if the content is specified on this object.
     *
     * <p>It is required to support an optional 'content' element around the
     * element representing the actual content.</p>
     *
     * @return True if the content is specified and false otherwise.
     */
    public boolean jibxHasContent() {
        return contentBuilder != null;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof VariantBuilderImpl) ?
                equalsSpecific((VariantBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(VariantBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(variantType, other.variantType) &&
                equals(selectionBuilder, other.selectionBuilder) &&
                equals(metaDataBuilder, other.metaDataBuilder) &&
                equals(contentBuilder, other.contentBuilder);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, variantType);
        result = hashCode(result, selectionBuilder);
        result = hashCode(result, metaDataBuilder);
        result = hashCode(result, contentBuilder);
        return result;
    }

    public void setVariantIdentifier(String variantIdentifier) {
        // TODO: how does UUID affect equals and hashcode?
        this.variantIdentifier = variantIdentifier;
    }

    public String getVariantIdentifier() {
        return variantIdentifier;
    }
}
