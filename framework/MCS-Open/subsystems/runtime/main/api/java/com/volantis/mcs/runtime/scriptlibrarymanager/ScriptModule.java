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
import com.volantis.mcs.protocols.assets.ScriptAssetReference;

import java.util.Set;
import java.util.HashSet;

/**
 * Class representing javascript module.
 *
 * Note that instances of this class are used as 
 * the nodes in modules dependency graph so dependeny member have to be
 * immutable to protect from changing the graph structure after the node is added
 *
 */
public class ScriptModule {

    private Set<ScriptModule> dependencies;

    private String assetName;

    private int size;

    private boolean cacheable;

    ScriptAssetReference scriptAssetReference;

    public ScriptModule(String assetName, Set<ScriptModule> dependencies,
                        int size, boolean cacheable) {
        this.assetName = assetName;
        this.dependencies = new HashSet<ScriptModule>(dependencies);
        this.size = size;
        this.cacheable = cacheable;
    }

    public Set<ScriptModule> getDependencies() {
        return new HashSet<ScriptModule>(dependencies);
    }

    public String getAssetName() {
        return assetName;
    }

    public int getSize() {
        return size;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public ScriptAssetReference getScriptAssetReference() {
        return scriptAssetReference;
    }

    public void setScriptAssetReference(ScriptAssetReference scriptAssetReference) {
        this.scriptAssetReference = scriptAssetReference;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(); 
        sb.append(super.toString()).append("[name=" + assetName + ";dependencies=");       

        for (ScriptModule sm : dependencies) {
            sb.append(sm.getAssetName());
            sb.append(",");
        }   
        sb.append("]");
        return sb.toString();
    }
}
