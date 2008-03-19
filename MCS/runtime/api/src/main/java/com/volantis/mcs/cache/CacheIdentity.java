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

package com.volantis.mcs.cache;

/**
 * This interface describe the identity used to look up @link CacheEntry
 * objects in the {@link CacheStore}
 *
 * @mock.generate
 */
public interface CacheIdentity extends Comparable {

    /**
     * Gets a base 64 encoded string representation of the identity suitable
     * for serialising the identity via a HTTP request.
     *
     * @return The base64 encode key of this identity.
     */
    public String getBase64KeyAsString();

    /**
     * Gets the create time of this CacheIdentity.
     * @return The create time in milliseconds since the EPOC
     */
    public long getCreateTime();

    /**
     * Returns the time after which this CacheEntity is considered as expired. 
     * Value Long.MAX_VALUE indicates, that this identity will never expire.
     * 
     * @return The expires time in milliseconds since the EPOC
     */
    public long getExpiresTime();
    
    /**
     * Get thw sequence number used in the generation of the key for this
     * identity.
     *
     * @return The sequence number
     */
    public short getSequenceNo();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
