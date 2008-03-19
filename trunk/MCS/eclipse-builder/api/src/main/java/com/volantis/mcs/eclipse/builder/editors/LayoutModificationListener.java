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
package com.volantis.mcs.eclipse.builder.editors;

import com.volantis.mcs.layouts.Layout;

/**
 * Interface allowing objects to listen for changes in the content of layouts.
 *
 * <p>Because layout modification does not take place within the interaction
 * layer a separate listener is required for changes to the layout.</p>
 */
public interface LayoutModificationListener {
    /**
     * Called to indicate that the specified layout may have been modified.
     */
    public void layoutModified(Layout layout);
}
