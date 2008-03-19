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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.mcs.context.CacheScopeConstant;

/**
 * A simple wrapper for a rendered page.
 *
 * <p>This is required to allow integration with the new caching
 * mechanism. The cache is expected to be able to generate the cache content
 * from the key, but in this case the key is created through a lossy process
 * (not normally the case in a cache) and can not be used to retrieve the
 * cache contents.</p>
 *
 * <p>Instead, the cache will generate an empty RenderedPageWrapper instance,
 * which can then be populated by the code reading from the cache if
 * necessary.</p>
 *
 * <p>It is possible that a RenderedPageWrapper will be created and placed in
 * the cache which is then found to not be eligable for caching (this could
 * be because the page does not use inline stylesheets). In this situation this
 * wrapper will be marked as invalid for caching. </p>
 */
public class RenderedPageWrapper {
    /**
     * The page stored in the wrapper.
     */
    private CachedRenderedPage page;

    /**
     * marker to show whether the page is valid for caching.
     */
    private CacheScopeConstant pageValidForCaching;

    /**
     * Get the page stored in the wrapper.
     *
     * @return The page stored in the wrapper
     */
    public CachedRenderedPage getPage() {
        return page;
    }

    /**
     * Set the page stored in the wrapper.
     *
     * @param page The new page to store in the wrapper
     */
    public void setPage(CachedRenderedPage page) {
        this.page = page;
    }

    /**
     * set the marker for the page to show whether it is valid to cache the
     * page
     * @param pageValidForCaching
     */
    public void setPageValidForCaching(CacheScopeConstant pageValidForCaching) {
        this.pageValidForCaching = pageValidForCaching;
    }

    /**
     * get the marker for the page to show whether it is valid to cache the
     * page
     * @return pageValidForCaching
     */
    public CacheScopeConstant getPageValidForCaching() {
        return pageValidForCaching;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Feb-05	6786/1	adrianj	VBM:2005012506 Added rendered page cache

 ===========================================================================
*/
