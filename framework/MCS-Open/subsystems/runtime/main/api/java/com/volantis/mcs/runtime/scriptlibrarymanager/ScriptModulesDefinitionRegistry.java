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

import com.volantis.mcs.runtime.scriptlibrarymanager.exceptions.UnregisteredScriptModuleDependencyException;
import com.volantis.mcs.runtime.scriptlibrarymanager.exceptions.AlreadyRegisteredScriptModuleException;
import com.volantis.mcs.runtime.scriptlibrarymanager.exceptions.CyclicDependencyScriptModuleException;

import java.util.List;
import java.util.Set;

/**
 * Class providing interface for registration of script modules
 * and is intended to be used in page application manner
 */
public class ScriptModulesDefinitionRegistry {

    private static ScriptLibraryDependencyGraph dependecyGraph =
            new ScriptLibraryDependencyGraph();

    private ScriptModulesDefinitionRegistry() {
    }

    public static void register(ScriptModule module)
            throws AlreadyRegisteredScriptModuleException,
                CyclicDependencyScriptModuleException,
                UnregisteredScriptModuleDependencyException {
        dependecyGraph.addModule(module);
    }

    public static List<ScriptModule> getDependencyList(Set<ScriptModule> modules, Set<String> effectStyles)
            throws UnregisteredScriptModuleDependencyException {
        return dependecyGraph.getDependencyList(modules, effectStyles);
    }

    public static boolean isRegistered(ScriptModule module) {
        return dependecyGraph.isRegistered(module);
    }

    public static int getReusability(ScriptModule module) {
        return dependecyGraph.getReusability(module);
    }

}

