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
package com.volantis.synergetics.cornerstone.utilities.xml.jaxp;

import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.MCSTransformerMetaFactory;

import javax.xml.transform.TransformerFactory;

/**
 * A transformer meta factory implementation that is suitable for use in
 * test cases.
 * <p>
 * This has to create transformer factories using an implementation which is
 * guaranteed to be available at test time.
 */
public class TestTransformerMetaFactory implements TransformerMetaFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The real meta factory that we delegate to to create factories.
     * <p>
     * This factory must work during a testsuite run. For this reason, the
     * normal JAXP version is not good, as we do not ship a normal JAXP
     * implementation. This means that it would not work when run under JDK 1.3,
     * although it would probably work under JDK 1.4 which does provide this.
     */
    private static final TransformerMetaFactory testCompatibleMetaFactory =
            new MCSTransformerMetaFactory();

    /**
     * Creates a transformer factory that it suitable for use in a test case.
     */
    public TransformerFactory createTransformerFactory() {

        return testCompatibleMetaFactory.createTransformerFactory();
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 ===========================================================================
*/
