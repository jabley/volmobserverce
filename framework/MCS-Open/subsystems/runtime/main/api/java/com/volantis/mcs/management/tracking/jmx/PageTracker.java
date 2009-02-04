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
import com.volantis.mcs.management.tracking.PageDetailsManager;

import java.util.LinkedList;
import java.util.List;

/**
 * A thread safe implementation of the <code>PageDetailsManager</code>.
 * This class allows <code>PageDetails</code> to be added to an internal cache.
 */
public class PageTracker implements PageDetailsManager, PageTrackerMBean {

    /**
     * Restrict the constructor to package access.
     */
    PageTracker() {
    }


    /**
     * internal object to use for synchronization.
     */
    private Object mutex = new Object();

    /**
     * List of PageDetails objects.
     */
    private List pageDetailsList = new LinkedList();


    /**
     * Thread safe implementation.
     * rest of javadoc inherited.
     */
    public void flushPageDetails() {
        synchronized (mutex) {
            pageDetailsList.clear();
        }
    }

    /**
     * Thread safe implementation.
     * rest of javaDoc inherited
     */
    public PageDetails[] retrievePageDetails() {
        synchronized (mutex) {
            return (PageDetails[]) pageDetailsList.toArray(
                    new PageDetails[pageDetailsList.size()]);
        }
    }

    /**
     * Thread safe implementation.
     * Rest of javaDoc inherited.
     * @param pageDetails
     */
    public void addPageDetails(PageDetails pageDetails) {
        synchronized (mutex) {
            pageDetailsList.add(pageDetails);
        }
    }


}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
