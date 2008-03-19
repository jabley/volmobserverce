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

package com.volantis.map.ics.imageprocessor.parameters;

import com.volantis.map.common.param.MutableParameters;

/**
 * General implementation of the ParamBuilder, this param builder defines
 * useful staff for other param builders.
 */
public abstract class AbstractParamBuilder {

    /**
     * Sets int parameter. The value -1 treated as invalid value and isn't
     * set.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setIntParam(String paramName, int paramValue,
                               MutableParameters params)
        throws ParameterBuilderException {

        if (paramValue != -1) {
            params.setParameterValue(paramName, Integer.toString(paramValue));
        }
    }

    /**
     * Sets long parameter. The value -1 treated as invalid value and isn't
     * set.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setIntParam(String paramName, long paramValue,
                               MutableParameters params)
        throws ParameterBuilderException {

        if (paramValue != -1) {
            params.setParameterValue(paramName, Long.toString(paramValue));
        }
    }

    /**
     * Sets boolean parameter.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setBooleanParam(String paramName,
                                   boolean paramValue,
                                   MutableParameters params)
        throws ParameterBuilderException {

        params.setParameterValue(paramName, Boolean.toString(paramValue));
    }

    /**
     * Sets string parameter. null and empty string treated as invalid value
     * and isn't set.
     *
     * @param paramName  - parameter name.
     * @param paramValue - parameter value.
     * @param params     - parameter container to set parameter to.
     * @throws com.volantis.map.ics.imageprocessor.parameters.ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setStringParam(String paramName,
                                  String paramValue,
                                  MutableParameters params)
        throws ParameterBuilderException {

        if (paramValue != null && paramValue != "") {
            params.setParameterValue(paramName, paramValue);
        }
    }
}
