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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.management.tracking.jmx;

import com.volantis.mcs.management.tracking.PageDetails;


/**
 * Interface that provides the Management bean interface for PageTracking.
 * @volantis-api-include-in PublicAPI
 */
public interface PageTrackerMBean {

    /**
     * Management operation that allows the queue of <code>PageDetails</code>
     * objects, which are being tracked, to be flushed.
     */
    public void flushPageDetails();

    /**
     * Returns an array of the <code>PageDetails</code> obejcts that have been
     * tracked.
     */
    public PageDetails[] retrievePageDetails();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Oct-04	5921/1	tom	VBM:2004101102 added public API documentation for canvas tracking

 22-Oct-04	5910/1	tom	VBM:2004101102 Added Public API documentation for canvas tracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
