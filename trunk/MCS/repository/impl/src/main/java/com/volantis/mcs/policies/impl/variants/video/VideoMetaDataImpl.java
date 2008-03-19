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

import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataPixelsDimension;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.metadata.MetaDataVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.video.VideoMetaData;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.policies.variants.video.VideoEncoding;

public class VideoMetaDataImpl
        extends AbstractMetaDataPixelsDimension
        implements VideoMetaData {

    private final VideoEncoding videoEncoding;

    public VideoMetaDataImpl(VideoMetaDataBuilderImpl builder) {
        super(builder);

        videoEncoding = builder.getVideoEncoding();
    }

    public MetaDataBuilder getMetaDataBuilder() {
        return getVideoMetaDataBuilder();
    }

    public VideoMetaDataBuilder getVideoMetaDataBuilder() {
        return new VideoMetaDataBuilderImpl(this);
    }

    public void accept(MetaDataVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getVideoEncoding();
    }

    public VideoEncoding getVideoEncoding() {
        return videoEncoding;
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.VIDEO;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof VideoMetaDataImpl) ?
                equalsSpecific((VideoMetaDataImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(VideoMetaDataImpl other) {
        return super.equalsSpecific(other);
    }

    // Javadoc inherited.
    public int hashCode() {
        return super.hashCode();
    }
}
