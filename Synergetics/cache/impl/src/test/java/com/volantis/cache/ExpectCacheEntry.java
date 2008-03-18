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

package com.volantis.cache;

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.cache.impl.InternalCacheEntry;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class ExpectCacheEntry implements MethodAction {
    private final Object key;
    private final Object value;

    public ExpectCacheEntry(final Object key, final Object value) {
        this.key = key;
        this.value = value;
    }

    public Object perform(MethodActionEvent event)
            throws Throwable {

        InternalCacheEntry entry = (InternalCacheEntry)
                event.getArgument(CacheEntry.class);
        TestCaseAbstract.assertEquals(key, entry.getKey());
        TestCaseAbstract.assertEquals(value, entry.getValue());
        return null;
    }
}
