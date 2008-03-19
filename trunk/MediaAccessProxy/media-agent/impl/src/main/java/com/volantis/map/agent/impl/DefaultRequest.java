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
package com.volantis.map.agent.impl;

import com.volantis.map.agent.Request;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.Set;
import java.util.HashSet;

/**
 * Default implementation of the {@link Request} interface.
 */
public class DefaultRequest implements Request {
     /**
    * Used for logging
    */
   private static final LogDispatcher LOGGER =
       LocalizationFactory.createLogger(DefaultRequest.class);

     /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(DefaultRequest.class);

    /**
     * Resource type associated with this reques
     */
    private String resourceType;

    /**
     * Key to the parameter that provides the source URL
     */
    private String sourceURLParamKey;

    /**
     * The input parameters.
     */
    private Parameters inputParameters;

    /**
     * Output Parameters
     */
    private Set outputParameters;

    public DefaultRequest(String resourceType,
                          String sourceURLParamKey,
                          String sourceURL) {
        if (resourceType == null) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("resource-type-null"));

        }
        if (sourceURLParamKey == null) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("source-url-key"));
        }
        this.resourceType = resourceType;
        this.sourceURLParamKey = sourceURLParamKey;
        DefaultAgentParameters dap = new DefaultAgentParameters();
        if (sourceURL != null) {
            dap.setParameterValue(sourceURLParamKey, sourceURL);
        }
        this.inputParameters = dap;
        this.outputParameters = new HashSet();
    }

    /**
     * Create a Request object that does not have a source url set
     * @param resourceType
     * @param sourceURLParamKey
     */
    public DefaultRequest(String resourceType, String sourceURLParamKey)  {
        this(resourceType, sourceURLParamKey, null);
    }

    // javadoc inherited
    public String getResourceType() {
        return resourceType;
    }

    // javadoc inherited
    public String getSourceURL() {
        String url;
        try {
            url = inputParameters.getParameterValue(sourceURLParamKey);
        } catch (MissingParameterException e) {
            LOGGER.warn("map-source-url-missing", sourceURLParamKey);        
            url = null;
        }
        return url;
    }

    // javadoc inherited
    public Parameters getInputParams() {
        return inputParameters;
    }

    // javadoc inherited
    public Set getOutputParams() {
        return outputParameters;
    }
}
