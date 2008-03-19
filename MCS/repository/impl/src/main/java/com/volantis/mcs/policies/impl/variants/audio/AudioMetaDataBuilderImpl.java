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

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataSingleEncodingBuilder;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.audio.AudioMetaData;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.metadata.Encoding;

public class AudioMetaDataBuilderImpl
        extends AbstractMetaDataSingleEncodingBuilder
        implements AudioMetaDataBuilder {

    private AudioMetaData audioMetaData;

    private AudioEncoding audioEncoding;

    public AudioMetaDataBuilderImpl(AudioMetaData audioMetaData) {

        if (audioMetaData != null) {
            this.audioMetaData = audioMetaData;
            audioEncoding = audioMetaData.getAudioEncoding();
        }
    }

    public AudioMetaDataBuilderImpl() {
        this(null);
    }

    public MetaData getMetaData() {
        return getAudioMetaData();
    }

    public AudioMetaData getAudioMetaData() {
        if (audioMetaData == null) {
            // Make sure only valid instances are built.
            validate();
            audioMetaData = new AudioMetaDataImpl(this);
        }

        return audioMetaData;
    }

    public Encoding getEncoding() {
        return getAudioEncoding();
    }

    public AudioEncoding getAudioEncoding() {
        return audioEncoding;
    }

    public void setAudioEncoding(AudioEncoding audioEncoding) {
        if (!equals(this.audioEncoding, audioEncoding)) {
            stateChanged();
        }
        
        this.audioEncoding = audioEncoding;
    }

    protected void clearBuiltObject() {
        audioMetaData = null;
    }

    protected void validateSingleEncodingImpl(ValidationContext context) {
        // Nothing else to validate.
    }

    public void accept(MetaDataBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.AUDIO;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AudioMetaDataBuilderImpl) ?
                equalsSpecific((AudioMetaDataBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AudioMetaDataBuilderImpl other) {
        return super.equalsSpecific(other);
    }

    // Javadoc inherited.
    public int hashCode() {
        return super.hashCode();
    }
}
