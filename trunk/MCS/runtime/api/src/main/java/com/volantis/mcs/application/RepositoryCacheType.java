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

package com.volantis.mcs.application;

/**
 * This class is a Type-Safe Enumeration for addressing remote repository caches.
 * @volantis-api-include-in PublicAPI
 *
 * @deprecated This was deprecated in version 3.5.1.
 */
public final class RepositoryCacheType {

    /**
     * Identifies the cache for remote asset group components.
     */
    public static final RepositoryCacheType REMOTE_ASSET_GROUP_CACHE =
            new RepositoryCacheType("REMOTE_ASSET_GROUP_CACHE");

    /**
     * Identifies the cache for remote audio components.
     */
    public static final RepositoryCacheType REMOTE_AUDIO_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_AUDIO_POLICY_CACHE");

    /**
     * Identifies the cache for remote button image components.
     */
    public static final RepositoryCacheType REMOTE_BUTTON_IMAGE_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_BUTTON_IMAGE_POLICY_CACHE");

    /**
     * Identifies the cache for remote chart components.
     */
    public static final RepositoryCacheType REMOTE_CHART_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_CHART_POLICY_CACHE");

    /**
     * Identifies the cache for remote dynamic visual components.
     */
    public static final RepositoryCacheType REMOTE_DYNAMIC_VISUAL_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_DYNAMIC_VISUAL_POLICY_CACHE");

    /**
     * Identifies the cache for remote image components.
     */
    public static final RepositoryCacheType REMOTE_IMAGE_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_IMAGE_POLICY_CACHE");

    /**
     * Identifies the cache for remote layouts.
     */
    public static final RepositoryCacheType REMOTE_LAYOUT_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_LAYOUT_POLICY_CACHE");

    /**
     * Identifies the cache for remote link components.
     */
    public static final RepositoryCacheType REMOTE_LINK_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_LINK_POLICY_CACHE");

    /**
     * Identifies the cache for remote rollover image components.
     */
    public static final RepositoryCacheType REMOTE_ROLLOVER_IMAGE_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_ROLLOVER_IMAGE_POLICY_CACHE");

    /**
     * Identifies the cache for remote script components.
     */
    public static final RepositoryCacheType REMOTE_SCRIPT_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_SCRIPT_POLICY_CACHE");

    /**
     * Identifies the cache for remote text components.
     */
    public static final RepositoryCacheType REMOTE_TEXT_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_TEXT_POLICY_CACHE");

    /**
     * Identifies the cache for remote themes.
     */
    public static final RepositoryCacheType REMOTE_THEME_POLICY_CACHE =
            new RepositoryCacheType("REMOTE_THEME_POLICY_CACHE");

    /**
     * Constructor
     * @param name The name of this cache type
     */
    private RepositoryCacheType(String name) {

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Dec-04	6518/3	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 ===========================================================================
*/
