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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.scriptlibrarymanager.exceptions.AlreadyRegisteredScriptModuleException;
import com.volantis.mcs.runtime.scriptlibrarymanager.exceptions.UnregisteredScriptModuleDependencyException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests ScriptModulesDefinitionRegistry class   
 */
public class ScriptModulesDefinitionRegistryTestCase extends TestCaseAbstract {

    private Map<String, ScriptModule> modulesReferencieMap = new HashMap<String, ScriptModule>();

    /**
     * Tests dependencies list obtained from test dependency graph
     */
    public void testDependenciesList() {
        ScriptLibraryDependencyGraph depGraph = createDependencyGraph();

        // dependency list for module4
        ScriptModule sm = modulesReferencieMap.get("module_4");
        Set<ScriptModule> reqestedModules = new HashSet();
        reqestedModules.add(sm);
        List<ScriptModule> list = depGraph.getDependencyList(reqestedModules, Collections.EMPTY_SET);

        assertEquals(4, list.size());

        assertEquals(0, list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_4")) >
                list.indexOf(modulesReferencieMap.get("module_2")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_4")) >
                        list.indexOf(modulesReferencieMap.get("module_3")));

        // dependency list for module9
        sm = modulesReferencieMap.get("module_9");
        reqestedModules = new HashSet();
        reqestedModules.add(sm);
        list = depGraph.getDependencyList(reqestedModules, Collections.EMPTY_SET);

        assertEquals(4, list.size());

        assertEquals(0, list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_5")) >
                        list.indexOf(modulesReferencieMap.get("module_3")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_9")) >
                                list.indexOf(modulesReferencieMap.get("module_5")));

        // dependency list for module15
        sm = modulesReferencieMap.get("module_15");
        reqestedModules = new HashSet();
        reqestedModules.add(sm);
        list = depGraph.getDependencyList(reqestedModules, Collections.EMPTY_SET);

        assertEquals(12, list.size());

        assertEquals(0, list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_2")) >
            list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_3")) >
            list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_4")) >
            list.indexOf(modulesReferencieMap.get("module_2")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_4")) >
            list.indexOf(modulesReferencieMap.get("module_3")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_7")) >
            list.indexOf(modulesReferencieMap.get("module_4")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_8")) >
            list.indexOf(modulesReferencieMap.get("module_4")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_6")) >
            list.indexOf(modulesReferencieMap.get("module_4")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_11")) >
            list.indexOf(modulesReferencieMap.get("module_6")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_12")) >
            list.indexOf(modulesReferencieMap.get("module_6")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_13")) >
            list.indexOf(modulesReferencieMap.get("module_11")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_14")) >
            list.indexOf(modulesReferencieMap.get("module_13")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_14")) >
            list.indexOf(modulesReferencieMap.get("module_12")));

        assertEquals(11, list.indexOf(modulesReferencieMap.get("module_15")));

        // dependency list for module4 and module9
        sm = modulesReferencieMap.get("module_4");
        reqestedModules = new HashSet();
        reqestedModules.add(sm);
        sm = modulesReferencieMap.get("module_9");
        reqestedModules.add(sm);
        list = depGraph.getDependencyList(reqestedModules, Collections.EMPTY_SET);

        assertEquals(6, list.size());

        assertEquals(0, list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_4")) >
                    list.indexOf(modulesReferencieMap.get("module_2")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_4")) >
                    list.indexOf(modulesReferencieMap.get("module_3")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_5")) >
                    list.indexOf(modulesReferencieMap.get("module_3")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_9")) >
                    list.indexOf(modulesReferencieMap.get("module_5")));

        // dependency list for module3 and module9
        sm = modulesReferencieMap.get("module_3");
        reqestedModules = new HashSet();
        reqestedModules.add(sm);
        sm = modulesReferencieMap.get("module_9");
        reqestedModules.add(sm);
        list = depGraph.getDependencyList(reqestedModules, Collections.EMPTY_SET);

        assertEquals(4, list.size());

        assertEquals(0, list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_3")) >
            list.indexOf(modulesReferencieMap.get("module_1")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_5")) >
            list.indexOf(modulesReferencieMap.get("module_3")));

        assertTrue(list.indexOf(modulesReferencieMap.get("module_9")) >
            list.indexOf(modulesReferencieMap.get("module_5")));

        // dependency list for notRegisteredModule
        sm = modulesReferencieMap.get("notRegisteredModule");
        reqestedModules = new HashSet();
        reqestedModules.add(sm);
        try {
            list = depGraph.getDependencyList(reqestedModules, Collections.EMPTY_SET);
            fail();
        } catch (UnregisteredScriptModuleDependencyException e) {
            // exception is expected to be thrown here
        }

    }

    /**
     * Tests throwing UnregisteredScriptModuleException upon making an atempt
     * to register module with unregistered dependency
     */
    public void testNotRegisteredDependency() {

        ScriptLibraryDependencyGraph depGraph =
                        new ScriptLibraryDependencyGraph();

        Set<ScriptModule> moduleDependencies = new HashSet();
        ScriptModule nonRegisteredModule = new ScriptModule("/nonRegisteredModule.mscr",
                Collections.EMPTY_SET, 1, true);
        moduleDependencies.add(nonRegisteredModule);

        ScriptModule module = new ScriptModule("/module.mscr", moduleDependencies, 1, true);

        try {
            depGraph.addModule(module);
            fail();
        } catch (UnregisteredScriptModuleDependencyException e) {
            // exception is expected to be thrown here        
        }

    }

    /**
     * Tests throwing AlreadyRegisteredScriptModuleException upon making an atempt
     * to register the same module again
     */
    public void testAlreadyRegisteredModule() {
        ScriptLibraryDependencyGraph depGraph =
                        new ScriptLibraryDependencyGraph();

        ScriptModule module = new ScriptModule("/module.mscr", Collections.EMPTY_SET, 1, true);
        depGraph.addModule(module);

        try {
            depGraph.addModule(module);
            fail();
        } catch (AlreadyRegisteredScriptModuleException e) {
            // exception is expected to be thrown here
        }

        Set<ScriptModule> moduleDependencies = new HashSet();
        ScriptModule dependencyModule = new ScriptModule("/dependencyModule.mscr",
                Collections.EMPTY_SET, 1, true);
        moduleDependencies.add(dependencyModule);
        module = new ScriptModule("/module.mscr", moduleDependencies, 1, true);

        try {
            depGraph.addModule(module);
            fail();
        } catch (AlreadyRegisteredScriptModuleException e) {
            // exception is expected to be thrown here
        }

    }

    /**
     * Tests throwing CyclicDependencyScriptModuleException upon making an atempt
     * to register the same module again
     */
    public void testCyclicDependency() {
        ScriptLibraryDependencyGraph depGraph =
                        new ScriptLibraryDependencyGraph();

        Set<ScriptModule> moduleDependencies = new HashSet();
        ScriptModule module = new ScriptModule("/module.mscr", moduleDependencies, 1, true);
        module.getDependencies().add(module);

        depGraph.addModule(module);

        // not possible create cylic dependency to any more
//        try {
//            fail();
//        } catch (CyclicDependencyScriptModuleException e) {
//            // exception is expected to be thrown here
//        }
    }

    /**
     * Helper method for creating dependency grap. Its structure is as follows:
     *
     *                         module1
     *                         /     \
     *                    module2   module3
     *                         \     /     \____________
     *                          \   /                   \
     *                          module4                 module5
     *                   ______/   |   \______            |   \_____
     *                  /          |          \           |         \
     *             module6      module7     module8    module9    module10
     *             /    \         |           |
     *            /      \        |           |
     *      module11  module12    |          /
     *          |        |        |         /
     *          |        |        |        /
     *      module13     |        |       /
     *            \      |        |      /
     *             \     |        |     /
     *              module14      |    /
     *                    \____   |   /
     *                         \  |  /
     *                         module15
     *
     *
     * @return ScriptLibraryDependencyGraph
     */
    private ScriptLibraryDependencyGraph createDependencyGraph() {
        ScriptLibraryDependencyGraph depGraph =
                        new ScriptLibraryDependencyGraph();

        ScriptModule module1 = new ScriptModule("module_1", Collections.EMPTY_SET, 1, true);
        depGraph.addModule(module1);
        modulesReferencieMap.put(module1.getAssetName(), module1);

        Set<ScriptModule> module2_deps = new HashSet();
        module2_deps.add(module1);
        ScriptModule module2 = new ScriptModule("module_2", module2_deps, 1, true);
        depGraph.addModule(module2);
        modulesReferencieMap.put(module2.getAssetName(), module2);

        Set<ScriptModule> module3_deps = new HashSet();
        module3_deps.add(module1);
        ScriptModule module3 = new ScriptModule("module_3", module3_deps, 1, true);
        depGraph.addModule(module3);
        modulesReferencieMap.put(module3.getAssetName(), module3);

        Set<ScriptModule> module4_deps = new HashSet();
        module4_deps.add(module2);
        module4_deps.add(module3);
        ScriptModule module4 = new ScriptModule("module_4", module4_deps, 1, true);
        depGraph.addModule(module4);
        modulesReferencieMap.put(module4.getAssetName(), module4);

        Set<ScriptModule> module5_deps = new HashSet();
        module5_deps.add(module3);
        ScriptModule module5 = new ScriptModule("module_5", module5_deps, 1, true);
        depGraph.addModule(module5);
        modulesReferencieMap.put(module5.getAssetName(), module5);

        Set<ScriptModule> module9_deps = new HashSet();
        module9_deps.add(module5);
        ScriptModule module9 = new ScriptModule("module_9", module9_deps, 1, true);
        depGraph.addModule(module9);
        modulesReferencieMap.put(module9.getAssetName(), module9);

        Set<ScriptModule> module10_deps = new HashSet();
        module10_deps.add(module5);        
        ScriptModule module10 = new ScriptModule("module_10", module10_deps, 1, true);
        depGraph.addModule(module10);
        modulesReferencieMap.put(module10.getAssetName(), module10);

        Set<ScriptModule> module7_deps = new HashSet();
        module7_deps.add(module4);
        ScriptModule module7 = new ScriptModule("module_7", module7_deps, 1, true);            
        depGraph.addModule(module7);
        modulesReferencieMap.put(module7.getAssetName(), module7);

        Set<ScriptModule> module8_deps = new HashSet();
        module8_deps.add(module4);
        ScriptModule module8 = new ScriptModule("module_8", module8_deps, 1, true);
        depGraph.addModule(module8);
        modulesReferencieMap.put(module8.getAssetName(), module8);

        Set<ScriptModule> module6_deps = new HashSet();
        module6_deps.add(module4);
        ScriptModule module6 = new ScriptModule("module_6", module6_deps, 1, true);
        depGraph.addModule(module6);
        modulesReferencieMap.put(module6.getAssetName(), module6);

        Set<ScriptModule> module11_deps = new HashSet();
        module11_deps.add(module6);
        ScriptModule module11 = new ScriptModule("module_11", module11_deps, 1, true);
        depGraph.addModule(module11);
        modulesReferencieMap.put(module11.getAssetName(), module11);

        Set<ScriptModule> module12_deps = new HashSet();
        module12_deps.add(module6);
        ScriptModule module12 = new ScriptModule("module_12", module12_deps, 1, true);
        depGraph.addModule(module12);
        modulesReferencieMap.put(module12.getAssetName(), module12);

        Set<ScriptModule> module13_deps = new HashSet();
        module13_deps.add(module11);
        ScriptModule module13 = new ScriptModule("module_13", module13_deps, 1, true);
        depGraph.addModule(module13);
        modulesReferencieMap.put(module13.getAssetName(), module13);

        Set<ScriptModule> module14_deps = new HashSet();
        module14_deps.add(module12);
        module14_deps.add(module13);        
        ScriptModule module14 = new ScriptModule("module_14", module14_deps, 1, true);
        depGraph.addModule(module14);
        modulesReferencieMap.put(module14.getAssetName(), module14);

        Set<ScriptModule> module15_deps = new HashSet();
        module15_deps.add(module14);
        module15_deps.add(module8);
        module15_deps.add(module7);        
        ScriptModule module15 = new ScriptModule("module_15", module15_deps, 1, true);
        depGraph.addModule(module15);
        modulesReferencieMap.put(module15.getAssetName(), module15);

        Set<ScriptModule> notRegisteredModule_deps = new HashSet();
        notRegisteredModule_deps.add(module14);
        notRegisteredModule_deps.add(module8);
        ScriptModule notRegisteredModule =
                new ScriptModule("notRegisteredModule", notRegisteredModule_deps, 1, true);
        modulesReferencieMap.put(notRegisteredModule.getAssetName(), notRegisteredModule);

        return depGraph; 
    }

}


