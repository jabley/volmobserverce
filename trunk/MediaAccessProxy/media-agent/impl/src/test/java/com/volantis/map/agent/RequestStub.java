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
package com.volantis.map.agent;

import com.volantis.map.common.param.Parameters;
import com.volantis.map.agent.impl.DefaultAgentParameters;

import java.util.HashSet;
import java.util.Set;

/**
 */
public class RequestStub implements Request {

    private final DefaultAgentParameters inputParams;

    private final Set outputParamNames;

    public RequestStub() {
        inputParams = new DefaultAgentParameters();
        outputParamNames = new HashSet();
    }

    public void addInputParameter(final String name, final String value) {
        inputParams.setParameterValue(name, value);
    }

    public void addOutputParameterName(final String name) {
        outputParamNames.add(name);
    }

    public String getResourceType() {
        return "IMAGE";
    }

    public String getSourceURL() {
        return "http://hello.world.com/image.jpg";
    }

    public Parameters getInputParams() {
        return inputParams;
    }

    public Set getOutputParams() {
        return outputParamNames;
    }
}
