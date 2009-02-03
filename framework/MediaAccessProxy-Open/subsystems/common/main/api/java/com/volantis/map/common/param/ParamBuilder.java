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

package com.volantis.map.common.param;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;
import java.io.File;

/**
 * General implementation of the ParamBuilder, containing logic which is common
 * for all operations.
 *
 * Additionally defines useful staff for other param builders.
 */
public class ParamBuilder {

    /**
      * Derive missing parameters from the existing ones
      * and from additional info about the local environment
      */
     public void overlayEnvironmentParameters(MutableParameters target, Parameters source)
             throws ParameterBuilderException {

        completeSourceURL(target, source);
     }

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
     * @throws ParameterBuilderException if it is impossible to set parameter.
     */
    protected void setStringParam(String paramName,
                                  String paramValue,
                                  MutableParameters params)
        throws ParameterBuilderException {

        if (paramValue != null && paramValue.length() > 0) {
            params.setParameterValue(paramName, paramValue);
        }        
    }

    private void completeSourceURL(MutableParameters params, Parameters source)
            throws ParameterBuilderException {

        if (params.containsName(ParameterNames.SOURCE_URL)) {
            return;
        }

        URI sourceURI = null;
        String scheme = null;
        String userInfo = null;
        String host = null;
        int port = 80;
        String path = null;
        String query = null;
        String fragment = null;

        try {
            if(params.containsName(ParameterNames.SOURCE_PROTOCOL)) {
                scheme = params.getParameterValue(ParameterNames.SOURCE_PROTOCOL);
            }
            if (params.containsName(ParameterNames.SOURCE_USER_INFO)) {
                userInfo = params.getParameterValue(ParameterNames.SOURCE_USER_INFO);
            }
            if (params.containsName(ParameterNames.SOURCE_HOST)) {
                host = params.getParameterValue(ParameterNames.SOURCE_HOST);
            }
            if (params.containsName(ParameterNames.SOURCE_PORT)) {
                port = params.getInteger(ParameterNames.SOURCE_PORT);
            }
            path = params.getParameterValue(ParameterNames.SOURCE_PATH);
            if (params.containsName(ParameterNames.SOURCE_QUERY)) {
                query = params.getParameterValue(ParameterNames.SOURCE_QUERY);
            }
            if (params.containsName(ParameterNames.SOURCE_FRAGMENT)) {
                fragment = params.getParameterValue(ParameterNames.SOURCE_FRAGMENT);
            }
        } catch (MissingParameterException x) {
            throw new ParameterBuilderException(x);
        }

        if (null != host && !"".equals(host)) {

            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            // The URI constructor takes care of encoding illegal chars
            try {
                sourceURI = new URI(scheme,
                        userInfo, host, port,
                        path, query, fragment);
            } catch (URISyntaxException x) {
                throw new ParameterBuilderException(x);
            }

        } else if (source.containsName(ParameterNames.ENV_LOCAL_ROOT)) {

            String localResourceRoot;
            try {
                localResourceRoot = source.getParameterValue(ParameterNames.ENV_LOCAL_ROOT);
            } catch (MissingParameterException x) {
                throw new ParameterBuilderException(x);
            }

            File file = new File(localResourceRoot);
            sourceURI = file.toURI().resolve(path);
        }

        if (null != sourceURI) {
            URL sourceURL = null;
            try {
                sourceURL = sourceURI.toURL();
            } catch (MalformedURLException x) {
                throw new ParameterBuilderException(x);
            }

            params.setParameterValue(ParameterNames.SOURCE_URL, sourceURL.toExternalForm());
        }
    }
}
