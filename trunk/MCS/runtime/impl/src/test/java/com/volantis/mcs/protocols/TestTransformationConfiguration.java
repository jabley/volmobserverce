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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.styling.properties.StyleProperty;

/**
 * This class provides access to the ProtocolConfiguration implementation of
 * #hasImportantStyleProperties and allows the important style properties to be
 * configured for use in testing.
 * @todo this should be replaced with a mock when they're accessible from integration tests
 */
public class TestTransformationConfiguration extends ProtocolConfigurationImpl {

    /**
     * Add a {@link StyleProperty} to the set of style properties which have
     * been deemed 'important' for this class.
     *
     * @param property to be added to the set of 'important' style properties
     */
    public void addImportantProperties(StyleProperty property) {
        importantProperties.add(property);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 ===========================================================================
*/
