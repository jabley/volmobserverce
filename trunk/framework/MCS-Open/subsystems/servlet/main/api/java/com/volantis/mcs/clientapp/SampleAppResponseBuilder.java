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

package com.volantis.mcs.clientapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.volantis.mcs.widgets.services.ResponseBuilder;
 
/**
 * Base class for builders of responses used by sample application
 */
abstract class SampleAppResponseBuilder extends ResponseBuilder {
    
    protected void writeHeadContents(Map params, PrintWriter out) throws IOException {
        out.println("<response:link rel=\"mcs:theme\" href=\"/themes/main.mthm\"/>");
    }
}
