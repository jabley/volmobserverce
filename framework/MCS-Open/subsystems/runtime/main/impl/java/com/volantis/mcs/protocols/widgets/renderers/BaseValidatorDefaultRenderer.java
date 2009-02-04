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

package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;

import java.util.Set;
import java.util.HashSet;

/**
 * Base class for all renderers related to validation elements that require
 * widget validator module to be reqistered. It performs single validator module
 * registration for all those renderers.
 */
abstract public class BaseValidatorDefaultRenderer extends WidgetDefaultRenderer {

    public static final ScriptModule WIDGET_VALIDATOR = createAndRegisterWidgetValidator();

    private static ScriptModule createAndRegisterWidgetValidator() {
        Set dependencies = new HashSet();
        dependencies.add(WidgetScriptModules.BASE_BB);
        dependencies.add(WidgetScriptModules.VALIDATE);
        ScriptModule module = new ScriptModule("/vfc-validator.mscr", dependencies,
                41300, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

}
