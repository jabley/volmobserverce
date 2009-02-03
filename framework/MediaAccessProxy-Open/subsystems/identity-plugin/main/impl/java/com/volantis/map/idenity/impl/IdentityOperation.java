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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.idenity.impl;

import com.volantis.map.operation.Operation;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.operation.Result;
import com.volantis.map.common.param.Parameters;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Identity plugin returns the Resource Descriptor parameters
 */
public class IdentityOperation implements Operation {


    /**
     * This operation just writes out some of the information in the resource
     * descriptor.
     *
     * @param descriptor
     * @param request
     * @param response
     * @throws IOException
     */
    public Result execute(ResourceDescriptor descriptor,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        Result result = Result.UNSUPPORTED;
        if (descriptor.getResourceType().equals("identity")) {

            response.setContentType("text/html");
            Writer w = response.getWriter();
            w.write("<html><head>");
            w.write(descriptor.getExternalID()+"\n");
            w.write("</head><body>");
            w.write("<p>" + descriptor.getResourceType() +"</p>");
            w.write("---- parameters ----");
            Parameters p = descriptor.getInputParameters();
            Iterator it = p.getParameterNames();
            while (it.hasNext()) {
                String name = (String) it.next();
                w.write("<p>");
                w.write(name);
                w.write("=");
                w.write(p.getParameterValue(name));
                w.write("\n</p>");
            }
            w.write("</body></html>");
            result = Result.SUCCESS;
        }
        return result;
    }
}
