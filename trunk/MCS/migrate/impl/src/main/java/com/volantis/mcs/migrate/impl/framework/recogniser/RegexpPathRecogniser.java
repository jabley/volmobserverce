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

import com.volantis.mcs.migrate.api.framework.PathRecogniser;
import org.apache.regexp.RE;

/**
 * A path recogniser that recognises paths using a regular expression.
 */
public class RegexpPathRecogniser implements PathRecogniser {

    /**
     * The regular expression used to recognise paths.
     */
    private RE pathExpression;

    /**
     * Initialise.
     *
     * @param pathExpressionString the regular expression used to recognise
     *      paths.
     */
    public RegexpPathRecogniser(String pathExpressionString) {

        this.pathExpression = new RE(pathExpressionString);
    }

    // Javadoc inherited.
    public boolean recognisePath(String path) {

        return pathExpression.match(path);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
