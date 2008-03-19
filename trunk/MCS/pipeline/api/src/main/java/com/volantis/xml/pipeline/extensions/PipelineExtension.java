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
 * Copyright Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.extensions;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.mcs.utilities.extensions.Extension;

/**
 * Implement this interface to allow custom modifications to the Pipeline Rules
 */
public interface PipelineExtension extends Extension {

    /**
     * Configure pipeline rules.
     *
     * @param configuration the configuration object
     */
    public void extendRules(final DynamicProcessConfiguration configuration);
}
