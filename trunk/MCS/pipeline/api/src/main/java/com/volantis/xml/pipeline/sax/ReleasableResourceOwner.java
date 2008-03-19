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
package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.ResourceOwner;

/**
 * This class allows us to release only those ResourceOwners which
 * we requested be released when they were originally provided to a data
 * structure.
 */
public class ReleasableResourceOwner {

    /**
     * The ResourceOwner we are wrapping
     */
    private ResourceOwner resourceOwner;

    /**
     * Create a new instance of ReleasableResourceOwner
     * @param resourceOwner The ResourceOwner that we have requested will
     * be released later.
     */
    public ReleasableResourceOwner(ResourceOwner resourceOwner) {
        this.resourceOwner = resourceOwner;
    }

    /**
     * Get the releasable ResourceOwner that this object wraps.
     * @return the releasable ResourceOwner that this object wraps.
     */
    public ResourceOwner getResourceOwner() {
        return resourceOwner;
    }

    /**
     * Release the ResourceOwner that the object wraps and return a
     * reference to it.
     */
    public ResourceOwner release() {
        resourceOwner.release();
        return resourceOwner;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
