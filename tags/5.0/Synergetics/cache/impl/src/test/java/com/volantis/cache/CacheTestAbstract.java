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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.cache.impl.InternalCache;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.io.IndentingWriter;

import java.io.StringWriter;
import java.io.PrintWriter;

public abstract class CacheTestAbstract
        extends TestCaseAbstract {

    protected void ensureCacheIntegrity(final InternalCache cache) {
        TestIntegrityCheckingReporter reporter =
                new TestIntegrityCheckingReporter();
        cache.performIntegrityCheck(reporter);
        StringBuffer issues = reporter.getIssues();
        if (issues != null) {
            IndentingWriter printer = new IndentingWriter(System.out);
            cache.debugStructure(printer);
            fail(issues.toString());
        }
    }
}
