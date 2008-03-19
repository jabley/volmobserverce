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

import org.xml.sax.XMLFilter;

/**
 * An interface that provides the XMLFilters for a given content type.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface ScriptModule {
    /**
     * Select the XMLFilter that will handle the scripting for a given
     * content type.
     * @param contentType The content type that the selected XMLFilter should
     * handle.
     * @return An XMLFilter that will handle scripts for the given content
     * type.
     */
    public XMLFilter selectScriptFilter(String contentType);

    /**
     * Get the identity of this ScriptModule.
     * @return The identity of this ScriptModule.
     * @volantis-api-exclude-from PublicAPI
     */
    public Object getId();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 ===========================================================================
*/
