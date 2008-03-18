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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.cache.group;

import com.volantis.cache.CacheEntry;
import com.volantis.cache.Cache;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.system.SystemClock;

public class TestCacheableObjectProvider
        implements CacheableObjectProvider {
    
    public ProviderResult retrieve(
            SystemClock clock, Object key, CacheEntry entry) {
        String value = "value for (" + entry.getKey() + ")";
        return new ProviderResult(value, selectGroup(entry), true, null);
    }

    private Group selectGroup(CacheEntry entry) {
        Cache cache = entry.getCache();
        String key = (String) entry.getKey();
        int index = key.indexOf('.');
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Invalid key '" + key + "'");
        }

        String group = key.substring(0, index);
        Group root = cache.getRootGroup();
        return root.getGroup(group);
    }
}
