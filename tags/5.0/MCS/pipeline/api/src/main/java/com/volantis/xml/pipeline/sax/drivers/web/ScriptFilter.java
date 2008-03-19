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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Provide a java bean implementation of the ScriptFilter.
 */
public class ScriptFilter {
    /**
     * Volantis copyright mark.
     * */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ScriptFilter.class);

    /** The class object. */
    private Class scriptClass;

    /**
     * The contentType string.
     */
    private String contentType;

    /**
     * Get the script class.
     *
     * @return the script class.
     */
    public Class getScriptClass() {
        return scriptClass;
    }

    /**
     * Set the script class.
     *
     * @param scriptClass the script class.
     */
    public void setScriptClass(Class scriptClass) {
        this.scriptClass = scriptClass;
    }

    /**
     * Get the content type.
     *
     * @return set the content type.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set the content type.
     *
     * @param contentType the content type.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	310/1	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 ===========================================================================
*/
