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

package com.volantis.mcs.policies.impl.variants.metadata;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaData;

public abstract class AbstractMetaDataPixelsDimensionBuilder
        extends AbstractMetaDataSingleEncodingBuilder
        implements PixelDimensionsMetaDataBuilder {

    private int width;
    private int height;

    protected AbstractMetaDataPixelsDimensionBuilder(
            PixelDimensionsMetaData pixelsDimension) {

        if (pixelsDimension != null) {
            width = pixelsDimension.getWidth();
            height = pixelsDimension.getHeight();
        }
    }

    // Javadoc inherited.
    public int getWidth() {
        return width;
    }

    // Javadoc inherited.
    public void setWidth(int width) {
        if (!equals(this.width, width)) {
            stateChanged();
        }

        this.width = width;
    }

    // Javadoc inherited.
    public int getHeight() {
        return height;
    }

    // Javadoc inherited.
    public void setHeight(int height) {
        if (!equals(this.height, height)) {
            stateChanged();
        }

        this.height = height;
    }

    protected final void validateSingleEncodingImpl(ValidationContext context) {

        // Make sure that the width is greater than 0.
        validateDimension(context, width, PolicyModel.WIDTH);

        // Make sure that the height is greater than 0.
        validateDimension(context, height, PolicyModel.HEIGHT);

        validatePixelsDimensionImpl(context);
    }

    protected abstract void validatePixelsDimensionImpl(
            ValidationContext context);

    /**
     * Make sure that the dimension is valid.
     *
     * @param context   The validation context.
     * @param dimension The dimension to check.
     * @param property  The dimension property for reporting errors.
     */
    private void validateDimension(
            ValidationContext context, int dimension,
            PropertyIdentifier property) {
        if (dimension <= 0) {
            Step step = context.pushPropertyStep(property);
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.MINIMUM_EXCLUSIVE,
                            new Object[]{
                                property.getDescription(),
                                new Integer(0),
                                new Integer(dimension)
                            }));
            context.popStep(step);
        }
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AbstractMetaDataPixelsDimensionBuilder) ?
                equalsSpecific((AbstractMetaDataPixelsDimensionBuilder) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AbstractMetaDataPixelsDimensionBuilder other) {
        return super.equalsSpecific(other) &&
                equals(width, other.width) &&
                equals(height, other.height);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, width);
        result = hashCode(result, height);
        return result;
    }
}
