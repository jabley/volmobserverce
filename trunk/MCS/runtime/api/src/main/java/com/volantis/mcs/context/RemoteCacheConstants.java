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
 * $Header: /src/voyager/com/volantis/mcs/context/RemoteCacheConstants.java,v 1.2 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-May-02    Steve           VBM:2002040817 Constants for remote cache indexes
 * 12-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

/**
 * Constants for addressing the remote repository caches.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Remote repository caches are now no longer separately
 * identifiable.
 */
public interface RemoteCacheConstants
{
    /** Identifies the cache for remote asset group components */
    public static final int REMOTEASSETGROUPCACHE = 0;
    /** Identifies the cache for remote audio components */
    public static final int REMOTEAUDIOPOLICYCACHE = 1;
    /** Identifies the cache for remote button image components */
    public static final int REMOTEBUTTONIMAGEPOLICYCACHE = 2;
    /** Identifies the cache for remote chart components */
    public static final int REMOTECHARTPOLICYCACHE = 3;
    /** Identifies the cache for remote dynamic visual components */
    public static final int REMOTEDYNAMICVISUALPOLICYCACHE = 4;
    /** Identifies the cache for remote image components */
    public static final int REMOTEIMAGEPOLICYCACHE = 5;
    /** Identifies the cache for remote layouts */
    public static final int REMOTELAYOUTPOLICYCACHE = 6;
    /** Identifies the cache for remote link components */
    public static final int REMOTELINKPOLICYCACHE = 7;
    /** Identifies the cache for remote rollover image components */    
    public static final int REMOTEROLLOVERIMAGEPOLICYCACHE = 8;
    /** Identifies the cache for remote script components */
    public static final int REMOTESCRIPTPOLICYCACHE = 9;
    /** Identifies the cache for remote text components */
    public static final int REMOTETEXTPOLICYCACHE = 10;
    /** Identifies the cache for remote themes */
    public static final int REMOTETHEMEPOLICYCACHE = 11;    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Dec-04	6518/3	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
