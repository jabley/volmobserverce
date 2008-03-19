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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-May-03    Adrian          VBM:2003051901 - Added this as the common 
 *                              interface for those classes that provide a 
 *                              pruning strategy for caches. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

import java.util.Comparator;

/**
 * A CacheStrategy for ReadThroughFutureResult objects.  Caches that set a
 * limit on the maximum number of elements allowed in that cache need a
 * strategy for removing objects when the cache reaches this limit. One
 * approach to implement this strategy is to maintain a sorted cache where the
 * sort key is defined by a Comparator. Implementors of CacheStrategy provide
 * this facility.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public interface CacheStrategy extends Comparator {

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
