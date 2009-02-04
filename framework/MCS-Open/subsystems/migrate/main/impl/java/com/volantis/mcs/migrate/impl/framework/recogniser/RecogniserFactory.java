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
package com.volantis.mcs.migrate.impl.framework.recogniser;

import com.volantis.mcs.migrate.api.framework.ContentRecogniser;
import com.volantis.mcs.migrate.api.framework.PathRecogniser;

/**
 * A factory for creating standard/built-in recogniser objects.
 *
 * @mock.generate
 */
public interface RecogniserFactory {

    /**
     * Create a path recogniser that recognises paths using a regular
     * expression.
     *
     * @param re the regular expression used to do path matching.
     * @return the path recogniser created.
     */
    PathRecogniser createRegexpPathRecogniser(String re);

    /**
     * Create a content recogniser that recognises content using a regular
     * expression.
     *
     * @param re the regular expression used to do content matching.
     * @return the content recogniser created.
     */
    ContentRecogniser createRegexpContentRecogniser(String re);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8036/9	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
