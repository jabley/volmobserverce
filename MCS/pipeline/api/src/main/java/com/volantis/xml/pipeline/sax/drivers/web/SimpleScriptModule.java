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
import org.xml.sax.XMLFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the module element in the configuration. A module
 * is a sub-element of the script element and contains
 */
public class SimpleScriptModule implements ScriptModule {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SimpleScriptModule.class);

    /**
     * A map of filter classes keyed on the content type that they handle.
     */
    private Map scriptFilters = new HashMap();

    /**
     * The id of this ScriptModule
     */
    private Object id;

    // javadoc inherited
    public XMLFilter selectScriptFilter(String contentType) {
        XMLFilter filter = null;
        ScriptFilter scriptFilter = (ScriptFilter)scriptFilters.get(contentType);
        if (scriptFilter != null) {
            Class sfClass = scriptFilter.getScriptClass();
            if (sfClass != null) {
                try {
                    filter = (XMLFilter)sfClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    logger.error("unexpected-instantiation-exception", e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.error("unexpected-illegal-access-exception", e);
                }
            }
        }
        return filter;
    }

    // javadoc inherited
    public Object getId() {
        return id;
    }

    /**
     * Set the id of this ScriptModule.
     * @param id The id.
     */
    public void setId(Object id) {
        this.id = id;
    }

    /**
     * Put a new script filter class to this ScriptModule. This class must by
     * an XMLFilter. If there is already a filter for the given content type in
     * this ScriptModule then it will be replaced and the previous filter
     * returned.
     *
     * @param  filter                   the <code>ScriptFilter</code> object.
     * @return                          The replaced ScriptFilter or null if none
     *                                  was replaced.
     * @throws IllegalArgumentException If filter or contentType is null or
     *                                  scriptClass is not a kind of XMLFilter.
     */
    public ScriptFilter putScriptFilter(ScriptFilter filter) {
        if ((filter == null) || (filter.getContentType() == null)) {
            throw new IllegalArgumentException(
                    "Filter or contentType must not be null");
        }
        if (!XMLFilter.class.isAssignableFrom(filter.getScriptClass())) {
            throw new IllegalArgumentException(
                    "Script Filter class must be a kind of XMLFilter");
        }
        return (ScriptFilter)scriptFilters.put(filter.getContentType(),
                                               filter);
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

 06-Aug-03	310/5	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 31-Jul-03	217/5	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/3	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 ===========================================================================
*/
