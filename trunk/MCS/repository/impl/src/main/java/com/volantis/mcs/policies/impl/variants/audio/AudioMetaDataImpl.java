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

package com.volantis.mcs.policies.impl.variants.audio;

import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataSingleEncoding;
import com.volantis.mcs.policies.variants.audio.AudioMetaData;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.metadata.MetaDataVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;

public class AudioMetaDataImpl
        extends AbstractMetaDataSingleEncoding
        implements AudioMetaData {

    private final AudioEncoding audioEncoding;

    public AudioMetaDataImpl(AudioMetaDataBuilder builder) {
        
        audioEncoding = builder.getAudioEncoding();
    }

    public MetaDataBuilder getMetaDataBuilder() {
        return getAudioMetaDataBuilder();
    }

    public AudioMetaDataBuilder getAudioMetaDataBuilder() {
        return new AudioMetaDataBuilderImpl(this);
    }

    public void accept(MetaDataVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getAudioEncoding();
    }

    public AudioEncoding getAudioEncoding() {
        return audioEncoding;
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.AUDIO;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AudioMetaDataImpl) ?
                equalsSpecific((AudioMetaDataImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AudioMetaDataImpl other) {
        return super.equalsSpecific(other);
    }

    // Javadoc inherited.
    public int hashCode() {
        return super.hashCode();
    }
}