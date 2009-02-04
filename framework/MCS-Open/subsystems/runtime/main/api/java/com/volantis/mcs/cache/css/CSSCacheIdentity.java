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


package com.volantis.mcs.cache.css;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.cache.impl.AbstractCacheIdentity;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This class represents a specialisation of @link CSSIdentity for use with
 * {@link CSSCacheEntry} objects.
 */
public class CSSCacheIdentity extends AbstractCacheIdentity {

    /**
     * Create a new CSSCacheIdentity from a creation time, expires time and a 
     * sequence number.
     * 
     * @param createTime The creation time of this identity supplied by the
     * managing cache.
     * @param expiresTime The time after which the entry identified by this 
     * identity expires, supplied by managing cache.
     * @param sequenceNo The sequence number of this identity supplied by the
     * managing cache. 
     */
    public CSSCacheIdentity(long createTime, long expiresTime, short sequenceNo) {
        super(createTime, expiresTime, sequenceNo);
    }

    //Javadoc inherited
    public CSSCacheIdentity(String base64Key) {
        super(base64Key);
    }


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
