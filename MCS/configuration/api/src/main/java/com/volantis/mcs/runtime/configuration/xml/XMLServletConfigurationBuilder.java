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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/XMLServletConfigurationBuilder.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a
 *                              ConfigurationBuilder for building the config
 *                              objects from XML and DTD files in a servlet
 *                              WEB-INF directory.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;

/**
 * A ConfigurationBuilder for building the config objects from XML and XSD
 * files in a servlet WEB-INF directory.
 *
 * @todo this needs testing in the real app server environments we use,
 * rather than just Tomcat 4 :-).
 */
public class XMLServletConfigurationBuilder extends XMLConfigurationBuilder {

    private final ConfigContext configContext;

    public XMLServletConfigurationBuilder(ConfigContext configContext)
            throws ConfigurationException {
        super();

        this.configContext = configContext;
    }

    public MarinerConfiguration buildConfiguration()
            throws ConfigurationException {

        // Validation.
        if (configContext == null) {
            throw new IllegalStateException("set config context first");
        }
        setInputSource(configContext.getMainConfigInputSource());

        // OK, we have found the files, lets parse the xml.
        return super.buildConfiguration();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Mar-05	6842/3	emma	VBM:2005020302 Making file references in config files relative to those files

 21-Feb-05	6986/1	emma	VBM:2005021411 Changes merged from MCS3.3

 18-Feb-05	6974/1	emma	VBM:2005021411 Making the device repository and xml policies locations relative to mcs-config.xml

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 28-Jun-04	4726/2	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 06-Jan-04	2271/4	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 19-Dec-03	2246/3	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements (add export jars)

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
