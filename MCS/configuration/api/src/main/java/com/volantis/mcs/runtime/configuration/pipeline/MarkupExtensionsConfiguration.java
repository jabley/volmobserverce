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
package com.volantis.mcs.runtime.configuration.pipeline;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Configuration data holder for the
 * <pipeline-configuration>/<markup-extensions> element
 */
public class MarkupExtensionsConfiguration {

    /**
     * Will contain any {@link MarkupExtensionConfiguration} objects
     */
    private List markupExtensions;

    /**
     * Initializes a <code>MarkupExtensionsConfiguration</code> instance
     */
    public MarkupExtensionsConfiguration() {
        markupExtensions = new ArrayList();
    }

    /**
     * Adds a {@link MarkupExtensionConfiguration} object to this configuration
     * @param markupExtension a MarkupExtensionConfiguration instance
     */
    public void addMarkupExtensionConfiguration(
                MarkupExtensionConfiguration markupExtension) {
        markupExtensions.add(markupExtension);
    }

    /**
     * Allows iteration of this configurations contained
     * {@link MarkupExtensionConfiguration} instances
     * @return and {@link Iterator}
     */
    public Iterator getMarkupExtensionConfgurations() {
        return markupExtensions.iterator();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	7418/1	doug	VBM:2005021505 Simplified pipeline initialization

 ===========================================================================
*/
