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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import com.volantis.xml.pipeline.sax.ResourceOwner;

import java.util.Stack;

/**
 * This helper class provides common methods for processing
 * {@link ResourceOwner}s and {@link ReleasableResourceOwner}s
 */
public class ResourceOwnerHelper {

    /**
     * The Volantis copyright statement.
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Check if the specified Objects are, or contain, the same instance of a
     * ResourceOwner object
     * @param object1 the Object to compare to object2
     * @param object2 the Object to compare to object1
     * @return true if the Objects are, or contain, the same instance of a
     * ResourceOwner.
     */
    public static boolean sameResourceOwner(Object object1, Object object2) {
        boolean result = false;
        ResourceOwner addedResourceOwner = getAsResourceOwner(object1);

        if (addedResourceOwner != null) {
            ResourceOwner removedResourceOwner = getAsResourceOwner(object2);
            if (removedResourceOwner != null) {
                result = (addedResourceOwner == removedResourceOwner);
            }
        }
        return result;
    }

    /**
     * Get the specified Object as a ResourceOwner, if it is/contains a
     * ResourceOwner.
     * @param object The Object to return as a ResourceOwner
     * @return A ResourceOwner if the Object was either a ResourceOwner or a
     * ReleasableResourceOwner containing a ResourceOwner.
     */
    public static ResourceOwner getAsResourceOwner(Object object) {
        ResourceOwner result = null;
        if (object instanceof ResourceOwner) {
            result = (ResourceOwner)object;
        } else if (object instanceof ReleasableResourceOwner) {
            result = ((ReleasableResourceOwner)object).getResourceOwner();
        }
        return result;
    }

    /**
     * Release the object if it is a ReleasableResourceOwner.
     * @param object the Object to release.
     */
    public static void releaseIfReleasableResourceOwner(Object object) {
        if (object instanceof ReleasableResourceOwner) {
            ReleasableResourceOwner rro =
                    (ReleasableResourceOwner)object;
            ResourceOwner toRelease = rro.getResourceOwner();
            if (toRelease != null) {
                toRelease.release();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
