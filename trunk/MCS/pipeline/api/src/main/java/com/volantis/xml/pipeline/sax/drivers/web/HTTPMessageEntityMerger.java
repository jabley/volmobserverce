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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntity;

/**
 * This interface is for use in a command pattern that allows two
 * HTTPMessageEntity objects of the same class to be merged.
 */
public interface HTTPMessageEntityMerger {

    /**
     * Merge two HTTPMessageEntity objects. Properties in the master
     * take precendence over properties in the alternative where the same
     * properties are set on both.
     * @param master The master.
     * @param alternative The alternative source of property values.
     * @return A HTTPMessageEntity that is a merge of master and
     * alternative.
     */
    public HTTPMessageEntity
            mergeHTTPMessageEntities(HTTPMessageEntity master,
                                     HTTPMessageEntity alternative)
            throws HTTPException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Feb-05	6976/1	matthew	VBM:2005020308 Add HTTP Caching mechanism

 10-Feb-05	1005/1	matthew	VBM:2005020308 Added http cache and refactored AbtractPluggableHTTPManager

 ===========================================================================
*/
