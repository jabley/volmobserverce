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
package com.volantis.xml.pipeline.sax.drivers.web;

/**
 * Interface that allows accesses to cached HTTP responses
 */
public interface HTTPCache {
    /**
     * Allows the cache to be flushed
     * @param filterPattern the URL prefix that identifies those entries in
     * the cache that should be flushed. If null then the entire cache is
     * flushed.
     * @return the status of the operation.
     */
    public int flushCache(String filterPattern);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 12-Jul-04	751/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
