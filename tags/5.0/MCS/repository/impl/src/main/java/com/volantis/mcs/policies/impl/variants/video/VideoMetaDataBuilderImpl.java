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

package com.volantis.mcs.policies.impl.variants.video;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataPixelsDimensionBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.mcs.policies.variants.video.VideoMetaData;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;

public class VideoMetaDataBuilderImpl
        extends AbstractMetaDataPixelsDimensionBuilder
        implements VideoMetaDataBuilder {

    private VideoMetaData videoMetaData;
    private VideoEncoding videoEncoding;

    public VideoMetaDataBuilderImpl(VideoMetaDataImpl videoMetaData) {
        super(videoMetaData);

        if (videoMetaData != null) {
            this.videoMetaData = videoMetaData;
            videoEncoding = videoMetaData.getVideoEncoding();
        }
    }

    public VideoMetaDataBuilderImpl() {
        this(null);
    }

    public MetaData getMetaData() {
        return getVideoMetaData();
    }

    public VideoMetaData getVideoMetaData() {
        if (videoMetaData == null) {
            // Make sure only valid instances are built.
            validate();
            videoMetaData = new VideoMetaDataImpl(this);
        }

        return videoMetaData;
    }

    protected void clearBuiltObject() {
        videoMetaData = null;
    }

    public void accept(MetaDataBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getVideoEncoding();
    }

    public VideoEncoding getVideoEncoding() {
        return videoEncoding;
    }

    public void setVideoEncoding(VideoEncoding videoEncoding) {
        if (!equals(this.videoEncoding, videoEncoding)) {
            stateChanged();
        }
        
        this.videoEncoding = videoEncoding;
    }

    protected void validatePixelsDimensionImpl(ValidationContext context) {
        // Nothing else to validate.
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.VIDEO;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof VideoMetaDataBuilderImpl) ?
                equalsSpecific((VideoMetaDataBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(VideoMetaDataBuilderImpl other) {
        return super.equalsSpecific(other);
    }

    // Javadoc inherited.
    public int hashCode() {
        return super.hashCode();
    }
}
