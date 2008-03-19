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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;

public abstract class StylePropertyAnalyserTestCaseAbstract extends TestCaseAbstract {

    private StrictStyledDOMHelper helper;
    private StylePropertyAnalyser analyser;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        helper = new StrictStyledDOMHelper(null);
        analyser = StylePropertyAnalyser.getInstance();
    }

    /**
     * Return the <code>StrictStyledDOMHelper</code>.
     *
     * @return a StrictStyledDOMHelper
     */
    protected StrictStyledDOMHelper getTester() {
        return this.helper;
    }

    /**
     * Return a <code>StylePropertyAnalyser</code>.
     *
     * @return a StylePropertyAnalyser
     */
    protected StylePropertyAnalyser getAnalyser() {
        return this.analyser;
    }

    /**
     * Return an Element creating by parsing the specified fragment into a
     * <cod>Document</code> and getting the Content Root Head.
     *
     * @param fragment
     * @return an Element
     */
    protected Element getRootElement(final String fragment) {
        final Document document = helper.parse(fragment);
        return document.getRootElement();
    }
}
