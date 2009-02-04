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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Class is responsible for holding and operating on script library graph stucture
 */
public class ScriptLibraryDependencyGraph {

    /*
     * Stores references to the registered modules using asset name as a key.
     * This map in fact is to hold directed acyclic graph with nodes/modules
     * holding references to their parents   
     */
    private Map<String, ScriptModule> registeredModules = new HashMap<String, ScriptModule>();

    /*
     * Stores modules reusability ie number of nodes referencing directly or indirectly this node
     * (plus one for the node itself) 
     */
    private Map<ScriptModule, Integer> reusabilityMap = new HashMap<ScriptModule, Integer>(); 

    /**
     * Adds module to dependecy graph     
     *
     * @param module
     * @throws UnregisteredScriptModuleDependencyException is thrown in case any parent/dependency
     * module hasn't been registered
     * @throws AlreadyRegisteredScriptModuleException is thrown in case given module has already been
     * registered 
     * @throws CyclicDependencyScriptModuleException
     */
    public void addModule(ScriptModule module)
            throws UnregisteredScriptModuleDependencyException,
                AlreadyRegisteredScriptModuleException,
                CyclicDependencyScriptModuleException {

        if (isRegistered(module)) {
            throw new AlreadyRegisteredScriptModuleException(
                        "Module " + module + " has already been registered.");
        }

        for (ScriptModule dependencyModule : module.getDependencies()) {
            // since ScriptModule (holding dependency collection) is immutable it is
            // not possible to create cyclic dependency (ie module dependency is reference to
            // its defining module) so actually this check is never violated
            if (dependencyModule == module) {
                throw new CyclicDependencyScriptModuleException(
                        "Module " + module + " is referenced by its dependency module.");
            }
            if (!isRegistered(dependencyModule)) {
                throw new UnregisteredScriptModuleDependencyException(
                        "Depency module " + dependencyModule + " hasn't been registered.");
            }
            
        }

        registeredModules.put(module.getAssetName(), module);
        incrementReusability(module);

    }

    /**
     * Increment reusability stored in reusabilityMap for the given
     * module and all his ancestors
     */
    private void incrementReusability(ScriptModule module) {

        //increment reusability for this module
        Integer reusability =  reusabilityMap.get(module);
        if (reusability == null) {
            reusabilityMap.put(module, 1);            
        } else {
            reusabilityMap.put(module, reusability + 1);
        }

        //and for its ancestors
        for (ScriptModule dependencyModule : module.getDependencies()) {
            incrementReusability(dependencyModule);
        }
  
    }

    /**
     * Gets ordered dependency list for a given module. Slighty modified
     * breath breadth-first search algorith is used.
     *
     * @param 
     * @return ordered list of script module dependencies for a given module
     * @throws UnregisteredScriptModuleDependencyException is thrown in case required
     * module hasn't been registered 
     */
    public List<ScriptModule> getDependencyList(Set<ScriptModule> requiredModules, Set<String> effectStyles)
            throws UnregisteredScriptModuleDependencyException {

        List<ScriptModule> visitedList = new ArrayList<ScriptModule>();
        Queue<ScriptModule> toVisitQueue = new LinkedList<ScriptModule>();
        
        for (ScriptModule sm : requiredModules) {

            if (!isRegistered(sm)) {
                throw new UnregisteredScriptModuleDependencyException(
                            "Requested module hasn't been registered: " + sm);
            }
            toVisitQueue.offer(sm);
        }

        while(!toVisitQueue.isEmpty()) {
            
            ScriptModule currentModule = toVisitQueue.poll();

            boolean useThisModule = true;

            if (currentModule instanceof ConditionalScriptModule) {
                ConditionalScriptModule currentConditionalModule =
                        (ConditionalScriptModule) currentModule;
                useThisModule = currentConditionalModule.isNeeded(effectStyles , null);
            }

            if (useThisModule) {
                if (visitedList.contains(currentModule)) {
                    // move this module to the list head
                    visitedList.remove(currentModule);
                    visitedList.add(currentModule);
                } else {
                    visitedList.add(currentModule);
                }

                for (ScriptModule sm : currentModule.getDependencies()) {
                    toVisitQueue.offer(sm);
                }
            }

        }

        Collections.reverse(visitedList);
        return visitedList; 

    }

    public boolean isRegistered(ScriptModule module) {
        return registeredModules.containsKey(module.getAssetName());
    }

    public int getReusability(ScriptModule module) {
        return reusabilityMap.get(module);
    }

}
