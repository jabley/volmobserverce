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

import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataPixelsDimension;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.metadata.MetaDataVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;

public class ImageMetaDataImpl
        extends AbstractMetaDataPixelsDimension
        implements ImageMetaData {

    private final ImageEncoding imageEncoding;
    private final int pixelDepth;
    private final ImageRendering rendering;
    private final ImageConversionMode conversionMode;
    private final int preserveLeft;
    private final int preserveRight;

    public ImageMetaDataImpl(ImageMetaDataBuilder builder) {
        super(builder);

        imageEncoding = builder.getImageEncoding();
        conversionMode = builder.getConversionMode();
        rendering = builder.getRendering();
        pixelDepth = builder.getPixelDepth();
        preserveLeft = builder.getPreserveLeft();
        preserveRight = builder.getPreserveRight();
    }

    public MetaDataBuilder getMetaDataBuilder() {
        return getImageMetaDataBuilder();
    }

    public ImageMetaDataBuilder getImageMetaDataBuilder() {
        return new ImageMetaDataBuilderImpl(this);
    }

    public void accept(MetaDataVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getImageEncoding();
    }

    public ImageEncoding getImageEncoding() {
        return imageEncoding;
    }

    // Javadoc inherited.
    public int getPixelDepth() {
        return pixelDepth;
    }

    // Javadoc inherited.
    public ImageRendering getRendering() {
        return rendering;
    }

    // Javadoc inherited.
    public ImageConversionMode getConversionMode() {
        return conversionMode;
    }

    public int getPreserveLeft() {
        return preserveLeft;
    }

    public int getPreserveRight() {
        return preserveRight;
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.IMAGE;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ImageMetaDataImpl) ?
                equalsSpecific((ImageMetaDataImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ImageMetaDataImpl other) {
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
}
