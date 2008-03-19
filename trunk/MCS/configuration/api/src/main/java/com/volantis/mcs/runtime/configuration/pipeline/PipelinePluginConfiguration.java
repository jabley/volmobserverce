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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.pipeline;

/**
 * Base class for the configuration data holder for the
 * <pipeline-configuration>/<markup-extensions>/(rule|process) elements
 */
public abstract class PipelinePluginConfiguration {

    /**
     * The type of plugin
     */
    private PipelinePluginType type;

    /**
     * Fully qualified class name of the plugin.
     */
    private String className;

    /**
     * Initializes a <code>PipelinePluginConfiguration</code> instance
     * @param type the type of plugin
     */
    public PipelinePluginConfiguration(PipelinePluginType type) {
        this.type = type;
    }

    // javadoc unnecessary
    public String getClassName() {
        return className;
    }

    // javadoc unnecessary
    public void setClassName(String className) {
        this.className = className;
    }

    // javadoc unnecessary
    public PipelinePluginType getType() {
        return type;
    }

    /**
     * Type-Safe enum for plugin types.
     */
    public static final class PipelinePluginType {

        /**
         * Type for Rule plugins
         */
        public static final PipelinePluginType RULE = new PipelinePluginType();

        /**
         * Type for Process plugins
         */
        public static final PipelinePluginType PROCESS =
                    new PipelinePluginType();

        /**
         * Creates a <code>PipelinePluginType<code> instance
         */
        private PipelinePluginType() {
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	7418/1	doug	VBM:2005021505 Simplified pipeline initialization

 ===========================================================================
*/
