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

package com.volantis.mcs.protocols.gallery.renderers;

import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;

import java.util.Set;
import java.util.HashSet;

/**
 * Base class for all renderers related to gallery elements that require
 * widget gallery module to be reqistered. It performs single gallery module
 * registration for all those renderers. 
 */
public abstract class BaseGalleryDefaultRenderer extends ElementDefaultRenderer {

    public static final ScriptModule WIDGET_GALLERY = createAndRegisterWidgetGallery();

    private static ScriptModule createAndRegisterWidgetGallery() {
        Set dependencies = new HashSet();
        // always needed directly dependend module
        dependencies.add(WidgetScriptModules.BASE_BB);
        dependencies.add(WidgetScriptModules.EFFECT_BASE);

        // conditionals effects modules
        dependencies.add(WidgetScriptModules.CE_APPEAR);
        dependencies.add(WidgetScriptModules.CE_BLINDDOWN);
        dependencies.add(WidgetScriptModules.CE_BLINDUP);
        dependencies.add(WidgetScriptModules.CE_DROPOUT);
        dependencies.add(WidgetScriptModules.CE_FADE);
        dependencies.add(WidgetScriptModules.CE_FOLD);
        dependencies.add(WidgetScriptModules.CE_GROW);
        dependencies.add(WidgetScriptModules.CE_PUFF);
        dependencies.add(WidgetScriptModules.CE_SHAKE);
        dependencies.add(WidgetScriptModules.CE_SHRINK);
        dependencies.add(WidgetScriptModules.CE_SLIDE);
        dependencies.add(WidgetScriptModules.CE_PULSATE);

        ScriptModule module = new ScriptModule("/vfc-gallery.mscr", dependencies,
                37600, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

}
