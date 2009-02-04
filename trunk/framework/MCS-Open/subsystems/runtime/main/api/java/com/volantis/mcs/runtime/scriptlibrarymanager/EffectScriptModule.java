/*
This file is part of Volantis Mobility Server.

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server. If not, see <http://www.gnu.org/licenses/>.
*/
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.scriptlibrarymanager;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.renderers.StylesExtractor;
import com.volantis.styling.Styles;
import com.volantis.styling.StatefulPseudoClasses;

import java.util.Set;

/**
 * Script module which depends on CSS style value
 * vfc-ce-common.js depends on mcs-effect-style - included if value differ from 'none'
 * vfc-ce-slide.js depends on mcs-effect-style: slide
 */
public class EffectScriptModule extends ConditionalScriptModule {

    private static String RANDOM = "random";
    private static String ALL = "all";

    private String[] effectNames;

    public EffectScriptModule(String assetName, String[] effectNames, Set dependencies,
                              int size, boolean cacheable) {
        super(assetName, dependencies, size, cacheable);
        this.effectNames = effectNames;
    }

    /**
     * Check if mcs-effect-style and :mcs-concealed {mcs-effect-style} in given attributes has set effectName or not
     * @param effectStyles
     * @param protocol
     * @return
     */
    public boolean isNeeded(Set<String> effectStyles, VolantisProtocol protocol) {

        for (String effectName : effectNames) {
            if ((effectStyles.contains(effectName) || effectStyles.contains(RANDOM) || effectStyles.contains(ALL))) {
                return true;
            }
        }

        return false;
    }
}
