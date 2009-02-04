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

import com.volantis.styling.impl.engine.AbstractStylerContext;
import com.volantis.styling.impl.engine.listeners.MutableListeners;
import com.volantis.styling.impl.engine.matchers.MatcherContext;

/**
 * The context used by the {@link DeviceStylingEngineImpl}.
 */
public class DeviceStylerContext
        extends AbstractStylerContext {

    /**
     * The builder for the styles.
     */
    private DeviceStylesBuilder builder;

    /**
     * Initialise.
     *
     * @param matcherContext The context for the matchers.
     */
    public DeviceStylerContext(MatcherContext matcherContext,
                               MutableListeners depthChangeListeners) {
        super(matcherContext, depthChangeListeners);
    }

    /**
     * Get the styles builder.
     *
     * @return The styles builder.
     */
    public DeviceStylesBuilder getStylesBuilder() {
        return builder;
    }

    /**
     * Set the styles builds.
     *
     * @param builder The styles builder.
     */
    public void setStylesBuilder(DeviceStylesBuilder builder) {
        this.builder = builder;
    }
}

