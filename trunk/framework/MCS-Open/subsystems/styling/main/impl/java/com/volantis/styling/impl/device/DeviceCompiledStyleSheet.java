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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.device;

import com.volantis.styling.impl.engine.listeners.Listeners;
import com.volantis.styling.impl.engine.sheet.CompiledStyleSheetImpl;
import com.volantis.styling.impl.engine.sheet.ImmutableStylerList;
import com.volantis.styling.impl.state.ImmutableStateRegistry;

/**
 * A device specific extension of a {@link CompiledStyleSheetImpl}, this
 * contains the defaults that should be used when the device is styled.
 */
public class DeviceCompiledStyleSheet
        extends CompiledStyleSheetImpl {

    /**
     * The defaults.
     */
    private final Defaults defaults;

    /**
     * Initialise.
     *
     * @param stylerList    The list of stylers.
     * @param stateRegistry The registry for state needed by the compiled style sheet.
     * @param listeners     The listeners that need invoking during compilation.
     * @param defaults      The defaults to use.
     */
    public DeviceCompiledStyleSheet(
            ImmutableStylerList stylerList,
            ImmutableStateRegistry stateRegistry,
            Listeners listeners,
            Defaults defaults) {
        super(stylerList, stateRegistry, listeners);
        
        this.defaults = defaults;
    }

    /**
     * Get the defaults.
     *
     * @return The defaults.
     */
    public Defaults getDefaults() {
        return defaults;
    }
}
