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
 * $Header: /src/voyager/com/volantis/mcs/objects/CacheableRepositoryObject.java,v 1.2 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Dec-02    Steve           VBM:2002090210 - Interface that any repository
 *                              object with caching information must implement
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.objects;

/**
 * Interface that any repository object with caching information must implement
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public interface CacheableRepositoryObject
        extends RepositoryObject {

    /**
     * Set the policy caching attribute
     *
     * @param flag boolean flag enabling or disabling caching
     */
    public void setCacheThisPolicy(boolean flag);

    /**
     * Get the policy caching attribute
     *
     * @return boolean flag denoting whether this policy should be cached
     */
    public boolean getCacheThisPolicy();


    /**
     * Set whether or not this component is to be retained when a retry is
     * in progress.
     *
     * @param flag boolean flag enabling or disabling retry caching
     */
    public void setRetainDuringRetry(boolean flag);

    /**
     * Get the retained during retry attribute
     *
     * @return boolean flag sepcifying whether or not the component is retained.
     */
    public boolean getRetainDuringRetry();

    /**
     * Set whether or not component retrieval is retried on failure.
     *
     * @param flag boolean flag specifying whether retries should be performed.
     */
    public void setRetryFailedRetrieval(boolean flag);

    /**
     * Get the retained during retry attribute
     *
     * @return boolean flag sepcifying whether or not the component is retained.
     */
    public boolean getRetryFailedRetrieval();

    /**
     * Set the length of time for this component to live in a cache.
     *
     * @param secs int amount of time in seconds
     */
    public void setTimeToLive(int secs);

    /**
     * Return the number of seconds that this component has to live in the cache
     *
     * @return int the number of seconds.
     */
    public int getTimeToLive();

    /**
     * Set the number of seconds between retrieval retry attempts
     *
     * @param secs int the number of seconds between attempts
     */
    public void setRetryInterval(int secs);

    /**
     * Get the number of seconds between retry attempts
     *
     * @return int number of seconds
     */
    public int getRetryInterval();

    /**
     * Set the maximum number of retries before the component is abandoned.
     *
     * @param count int the maximum number of retries
     */
    public void setRetryMaxCount(int count);

    /**
     * Get the maximum number of retries before the component is abandoned.
     *
     * @return int the maximum number of retries
     */
    public int getRetryMaxCount();

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
