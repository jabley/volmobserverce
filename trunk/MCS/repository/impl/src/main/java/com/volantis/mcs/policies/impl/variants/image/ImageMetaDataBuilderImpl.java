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

package com.volantis.mcs.policies.impl.variants.image;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataPixelsDimensionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;

public class ImageMetaDataBuilderImpl
        extends AbstractMetaDataPixelsDimensionBuilder
        implements ImageMetaDataBuilder, Validatable {

    private ImageMetaData imageMetaData;

    private ImageEncoding imageEncoding;
    private int pixelDepth;
    private ImageRendering rendering;
    private ImageConversionMode conversionMode;
    private int preserveLeft = -1;
    private int preserveRight = -1;

    public ImageMetaDataBuilderImpl(ImageMetaDataImpl imageMetaData) {
        super(imageMetaData);

        if (imageMetaData == null) {
            rendering = ImageRendering.COLOR;
            conversionMode = ImageConversionMode.NEVER_CONVERT;
        } else {
            this.imageMetaData = imageMetaData;
            imageEncoding = imageMetaData.getImageEncoding();
            pixelDepth = imageMetaData.getPixelDepth();
            rendering = imageMetaData.getRendering();
            conversionMode = imageMetaData.getConversionMode();
        }
        this.imageMetaData = imageMetaData;
    }

    public ImageMetaDataBuilderImpl() {
        this(null);
    }

    public MetaData getMetaData() {
        return getImageMetaData();
    }

    public ImageMetaData getImageMetaData() {
        if (imageMetaData == null) {
            // Make sure only valid instances are built.
            validate();
            imageMetaData = new ImageMetaDataImpl(this);
        }

        return imageMetaData;
    }

    public void accept(MetaDataBuilderVisitor visitor) {
        visitor.visit(this);
    }

    protected void clearBuiltObject() {
        imageMetaData = null;
    }

    public Encoding getEncoding() {
        return getImageEncoding();
    }

    public ImageEncoding getImageEncoding() {
        return imageEncoding;
    }

    public void setImageEncoding(ImageEncoding imageEncoding) {
        if (!equals(this.imageEncoding, imageEncoding)) {
            stateChanged();
        }

        this.imageEncoding = imageEncoding;
    }

    // Javadoc inherited.
    public int getPixelDepth() {
        return pixelDepth;
    }

    // Javadoc inherited.
    public void setPixelDepth(int pixelDepth) {
        if (!equals(this.pixelDepth, pixelDepth)) {
            stateChanged();
        }

        this.pixelDepth = pixelDepth;
    }

    // Javadoc inherited.
    public ImageRendering getRendering() {
        return rendering;
    }

    // Javadoc inherited.
    public void setRendering(ImageRendering rendering) {
        if (rendering == null) {
            throw new IllegalArgumentException("rendering cannot be null");
        }

        if (!equals(this.rendering, rendering)) {
            stateChanged();
        }

        this.rendering = rendering;
    }

    // Javadoc inherited.
    public ImageConversionMode getConversionMode() {
        return conversionMode;
    }

    // Javadoc inherited.
    public void setConversionMode(ImageConversionMode conversionMode) {
        if (conversionMode == null) {
            throw new IllegalArgumentException("conversionMode cannot be null");
        }

        if (!equals(this.conversionMode, conversionMode)) {
            stateChanged();
        }

        this.conversionMode = conversionMode;
    }

    public int getPreserveLeft() {
        return preserveLeft;
    }

    public void setPreserveLeft(int preserveLeft) {
        if (!equals(this.preserveLeft, preserveLeft)) {
            stateChanged();
        }

        this.preserveLeft = preserveLeft;
    }

    public int getPreserveRight() {
        return preserveRight;
    }

    public void setPreserveRight(int preserveRight) {
        if (!equals(this.preserveRight, preserveRight)) {
            stateChanged();
        }

        this.preserveRight = preserveRight;
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.IMAGE;
    }

    protected void validatePixelsDimensionImpl(ValidationContext context) {

        // Make sure that the pixel depth is within the range 1 ... 32
        if (pixelDepth < 1 || pixelDepth > 32) {
            Step step = context.pushPropertyStep(PolicyModel.PIXEL_DEPTH);
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(
                            PolicyMessages.RANGE_INCLUSIVE_INCLUSIVE,
                            new Object[]{
                                PolicyModel.PIXEL_DEPTH.getDescription(),
                                new Integer(1),
                                new Integer(32),
                                new Integer(pixelDepth)
                            }));
            context.popStep(step);
        }
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ImageMetaDataBuilderImpl) ?
                equalsSpecific((ImageMetaDataBuilderImpl) obj) : false;
    }


    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ImageMetaDataBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(pixelDepth, other.pixelDepth) &&
                equals(rendering, other.rendering) &&
                equals(conversionMode, other.conversionMode) &&
                equals(preserveLeft, other.preserveLeft) &&
                equals(preserveRight, other.preserveRight);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, pixelDepth);
        result = hashCode(result, rendering);
        result = hashCode(result, conversionMode);
        result = hashCode(result, preserveLeft);
        result = hashCode(result, preserveRight);
        return result;
    }

    void jibxPostSet() {
        if (conversionMode == null) {
            conversionMode = ImageConversionMode.NEVER_CONVERT;
        }
        if (rendering == null) {
            rendering = ImageRendering.COLOR;
        }
    }

    boolean jibxHasConversionMode() {
        return conversionMode != null &&
                conversionMode != ImageConversionMode.NEVER_CONVERT;
    }
}
