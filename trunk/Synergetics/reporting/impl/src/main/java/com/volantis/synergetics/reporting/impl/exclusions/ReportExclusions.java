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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl.exclusions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Exclusions for particular report
 *
 */
public class ReportExclusions {

    /**
     * Exclusion list
     */
    private ArrayList exclusions;

    /**
     * Constructor
     *
     */
    public ReportExclusions() {
        exclusions = new ArrayList();
    }

    /**
     * Add unique report excluder to list
     * @param excluder EventExcluder
     */
    public void addUniqueExclusion(EventExcluder excluder) {
        boolean exists = false;
        for(Iterator i = exclusions.iterator(); i.hasNext() && !exists;) {
            EventExcluder item = (EventExcluder) i.next();
            if(item.isEqual(excluder)) {
                exists = true;
            }
        }
        if(!exists) {
            exclusions.add(excluder);
        }
    }

    /**
     * Is event excluded from being reported
     * @param metrics Map report event metrics
     */
    public boolean isExcluded(Map metrics) {
        boolean excluded = false;
        for(Iterator i = exclusions.iterator(); i.hasNext() && !excluded;) {
            EventExcluder item = (EventExcluder) i.next();
            excluded = item.isExcluded(metrics);
        }
        return excluded;
    }

}
