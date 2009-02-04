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
package com.volantis.mcs.eclipse.ab.search;

import com.volantis.mcs.eclipse.ab.search.devices.DeviceSearchQuery;
import com.volantis.mcs.eclipse.ab.search.devices.DeviceSearchQueryOptions;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;
import org.eclipse.core.resources.IFile;

/**
 * Test case for DeviceSearchQuery.
 */
public class DeviceSearchQueryTestCase extends TestCaseAbstract {

    /**
     * Test createRegExpString.
     */
    public void testCreateRegExpString() throws Throwable {
        SearchScope scope = new SearchScope() {
            public String getLabel() {
                return null;
            }

            public IFile[] getFiles() {
                return new IFile[0];
            }
        };
        DeviceSearchQueryOptions options = new DeviceSearchQueryOptions();

        options.setCaseSensitive(true);

        String searchString = "*PC*?";

        DeviceSearchQuery query = new DeviceSearchQuery(scope, searchString,
                options);

        String regExpStr = (String) PrivateAccessor.invoke(query,
                "createRegExpString",
                null, null);

        assertEquals("^.*PC.*.?$", regExpStr);

        options.setCaseSensitive(false);

        regExpStr = (String) PrivateAccessor.invoke(query,
                "createRegExpString",
                null, null);

        assertEquals("^.*[Pp][Cc].*.?$", regExpStr);

        options.setRegularExpression(true);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Oct-04	5557/1	allan	VBM:2004070608 Unit tests and rework issues

 ===========================================================================
*/
