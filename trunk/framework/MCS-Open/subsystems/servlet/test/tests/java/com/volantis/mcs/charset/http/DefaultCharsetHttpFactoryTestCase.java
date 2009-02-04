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
package com.volantis.mcs.charset.http;

import com.volantis.synergetics.testtools.TestCaseAbstract;

public class DefaultCharsetHttpFactoryTestCase extends TestCaseAbstract {

    /**
     * Default test case constructor
     * @param name
     */
    public DefaultCharsetHttpFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Simple tests to ensure the factory produces results.
     * @throws Exception
     */
    public void testGetDefaultInstance() throws Exception {
        CharsetHttpFactory factory = CharsetHttpFactory.getDefaultInstance();
        assertNotNull("Factory should not be null ", factory);

        CharsetHttpSelector selector = factory.getDefaultCharsetHttpSelector();
        assertNotNull("Selector should not be null", selector);

    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 02-Aug-04	5017/3	matthew	VBM:2004073003 Add CharsetHttpSelector and corresponding factories/default implementations

 ===========================================================================
*/
