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

package com.volantis.mcs.runtime.policies.theme;

import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * Provides all the thematic information needed at runtime.
 *
 * @mock.generate
 */
public interface RuntimeDeviceTheme
        extends RepositoryObject {

    /**
     * Get the compiled style sheet.
     *
     * @return The compiled style sheet.
     */
    CompiledStyleSheet getCompiledStyleSheet();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 ===========================================================================
*/
