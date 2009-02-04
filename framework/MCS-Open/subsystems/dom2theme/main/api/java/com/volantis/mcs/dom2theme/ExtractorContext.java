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

package com.volantis.mcs.dom2theme;

import com.volantis.mcs.css.version.CSSVersion;

/**
 * Provides contextual information to the extractor.
 *
 * <p>This is separated from the configuration as the configuration can be
 * shared across a number of usages of the extractor but the contextual
 * information is for one specific usage.</p>
 *
 * @mock.generate 
 */
public interface ExtractorContext {

    /**
     * Get the asset resolver.
     *
     * @return The asset resolver.
     */
    AssetResolver getAssetResolver();
    
    /**
     * Returns true, if type rules are to be generated, false otherwise.
     * 
     * @return true, if type rules are to be generated, false otherwise.
     */
    boolean generateTypeRules();

    /**
     * Get the {@link CSSVersion} associated with this extractor context.
     *
     * @return CSSVersion - will never be null.
     */
    CSSVersion getCSSVersion();
}
